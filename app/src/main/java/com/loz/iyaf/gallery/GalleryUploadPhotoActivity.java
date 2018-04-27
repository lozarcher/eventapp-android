package com.loz.iyaf.gallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.loz.R;
import com.loz.iyaf.feed.EventappService;
import com.loz.iyaf.feed.GalleryList;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import retrofit.Call;
import retrofit.Callback;
import retrofit.JacksonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class GalleryUploadPhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_upload_photo);
        final Button uploadButton = (Button)findViewById(R.id.uploadButton);
        String imagePath = getIntent().getStringExtra("imagePath");
        final Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeStream(GalleryUploadPhotoActivity.this
                    .openFileInput(imagePath));//here context can be anything like getActivity() for fragment, this or MainActivity.this
            UUID imageName = UUID.randomUUID();
            final String imageFilename = imageName+".jpg";

            uploadButton.setEnabled(true);
            final EditText nameText = (EditText)findViewById(R.id.nameText);
            final EditText captionText = (EditText)findViewById(R.id.captionText);

            uploadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Map<String, RequestBody> map = new HashMap<>();
                    String userName = nameText.getText().toString();
                    if (userName.matches("")) {
                        Toast.makeText(getApplicationContext(), "Please enter your name", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    uploadButton.setEnabled(false);
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://eventapp.lozarcher.co.uk")
                            .addConverterFactory(JacksonConverterFactory.create())
                            .build();

                    EventappService eventappService = retrofit.create(EventappService.class);
                    map.put("name", toRequestBody(userName));
                    map.put("caption", toRequestBody(captionText.getText().toString()));
                    map.put("filename", toRequestBody(imageFilename));

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);
                    byte[] byteArray = stream.toByteArray();
                    RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpeg"), byteArray);
                    map.put("photo\"; filename=\"" + imageFilename + "\"", fileBody);

                    Toast.makeText(GalleryUploadPhotoActivity.this, "Uploading photo - please wait...", Toast.LENGTH_LONG)
                            .show();

                    Call<GalleryList> call = eventappService.uploadGallery(map);

                    call.enqueue(new Callback<GalleryList>() {
                        @Override
                        public void onResponse(Response<GalleryList> response, Retrofit retrofit) {
                            Toast.makeText(getApplicationContext(), "Thank you for your photo!", Toast.LENGTH_SHORT).show();
                            Log.v("Upload", "success");
                            uploadButton.setEnabled(true);

                            finish();
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            Toast.makeText(getApplicationContext(), "Photo upload failed - please check your network connection or try again later", Toast.LENGTH_LONG).show();
                            Log.e("Upload", t.getMessage());
                            uploadButton.setEnabled(true);
                            Crashlytics.logException(t);
                        }
                    });


                }
            });
            Answers.getInstance().logContentView(new ContentViewEvent()
                    .putContentName("Gallery Upload")
                    .putContentType("Gallery")
                    .putContentId("gallery-upload"));
        } catch (FileNotFoundException e) {
            Toast.makeText(getApplicationContext(), "Uploaded image failed ", Toast.LENGTH_SHORT).show();
            Log.e("Upload", e.getMessage());
            uploadButton.setEnabled(true);
            e.printStackTrace();
            Crashlytics.logException(e);
            return;
        }
    }

    public static RequestBody toRequestBody (String value) {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body ;
    }

}
