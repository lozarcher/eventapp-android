package com.loz.iyaf.gallery;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import com.loz.iyaf.feed.GalleryData;
import com.loz.iyaf.imagehelpers.JsonCache;
import com.loz.iyaf.R;
import com.loz.iyaf.feed.EventappService;
import com.loz.iyaf.feed.GalleryList;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import permission.auron.com.marshmallowpermissionhelper.ActivityManagePermission;
import permission.auron.com.marshmallowpermissionhelper.PermissionResult;
import permission.auron.com.marshmallowpermissionhelper.PermissionUtils;
import retrofit.Call;
import retrofit.Callback;
import retrofit.JacksonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class GalleryActivity extends ActivityManagePermission {


    private static final int REQUEST_CAMERA = 0;
    private static final int SELECT_FILE = 1;

    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://eventapp.eu-west-1.elasticbeanstalk.com")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        EventappService eventappService = retrofit.create(EventappService.class);
        Call<GalleryList> call = eventappService.getGallery();
        Log.d("LOZ", "Starting call to /gallery... Hope it works...");
        call.enqueue(new Callback<GalleryList>() {
            @Override
            public void onResponse(Response<GalleryList> response, Retrofit retrofit) {
                Log.d("LOZ", "Got response: " + response.body().toString());

                GalleryList galleryList = response.body();
                JsonCache.writeToCache(getApplicationContext(), galleryList, "gallery");
                processGalleryList(galleryList);
            }

            @Override
            public void onFailure(Throwable t) {
                // something went completely south (like no internet connection)
                Log.e("Error", t.getStackTrace().toString());
                GalleryList galleryList = null;
                ObjectInput oi = JsonCache.readFromCache(getApplicationContext(), "gallery");
                if (oi != null) {
                    try {
                        galleryList = (GalleryList) oi.readObject();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("cache", e.getMessage());
                    }
                    if (galleryList != null) {
                        processGalleryList(galleryList);
                    }
                }
            }
        });
    }

    private void processGalleryList(GalleryList galleryList) {
        ArrayList<GalleryData> photos = new ArrayList<>();
        for (GalleryData photo : galleryList.getData()) {
            photos.add(photo);
        }

        GridView grid = (GridView) findViewById(R.id.grid);
        grid.setAdapter(new GalleryAdapter(this, photos));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.gallery_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_camera:
                selectImage();
                return true;
        }
        return false;
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(GalleryActivity.this);
        builder.setTitle("Add a Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    askPermissions(new String[]{PermissionUtils.Manifest_CAMERA, PermissionUtils.Manifest_WRITE_EXTERNAL_STORAGE})
                            .setPermissionResult(new PermissionResult() {
                                @Override
                                public void permissionGranted() {
                                    //permission granted
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    try {
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                                Uri.fromFile(createImageFile()));
                                        startActivityForResult(intent, REQUEST_CAMERA);
                                    } catch (IOException e) {
                                        Toast.makeText(getApplicationContext(),
                                                "Error: Couldn't save your photo",
                                                Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void permissionNotGranted() {
                                    //permission denied
                                    Toast.makeText(getApplicationContext(),
                                            "You'll need to grant permission to take a photo",
                                            Toast.LENGTH_SHORT).show();
                                }
                            })
                            .requestPermission(PermissionUtils.KEY_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    askPermission(PermissionUtils.Manifest_READ_EXTERNAL_STORAGE)
                            .setPermissionResult(new PermissionResult() {
                                @Override
                                public void permissionGranted() {
                                    //permission granted
                                    Intent intent = new Intent(
                                            Intent.ACTION_PICK,
                                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    intent.setType("image/*");
                                    startActivityForResult(
                                            Intent.createChooser(intent, "Select File"),
                                            SELECT_FILE);
                                }

                                @Override
                                public void permissionNotGranted() {
                                    //permission denied
                                    Toast.makeText(getApplicationContext(),
                                            "You'll need to grant permission to choose a photo",
                                            Toast.LENGTH_SHORT).show();                             }
                            })
                            .requestPermission(PermissionUtils.KEY_READ_EXTERNAL_STORAGE);

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bitmap bitmap = null;
            String imagePath = "uploadedImage";
            if (requestCode == REQUEST_CAMERA) {
                bitmap = getPhoto(mCurrentPhotoPath);
            } else if (requestCode == SELECT_FILE) {
                try {
                    // When an Image is picked
                    if (resultCode == RESULT_OK
                            && null != data) {
                        // Get the Image from data
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = { MediaStore.Images.Media.DATA };
                        // Get the cursor
                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        // Move to first row
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        mCurrentPhotoPath = cursor.getString(columnIndex);
                        cursor.close();
                        bitmap = getPhoto(mCurrentPhotoPath);
                    } else {
                        Toast.makeText(this, "You haven't picked an image",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT)
                            .show();
                }
            }
            Intent intent = new Intent(GalleryActivity.this, GalleryUploadPhotoActivity.class);

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            FileOutputStream fo = null;
            try {
                fo = openFileOutput(imagePath, Context.MODE_PRIVATE);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            intent.putExtra("imagePath", imagePath);
            startActivity(intent);
            finish();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        String storageDir = Environment.getExternalStorageDirectory() + "/surbitonfood";
        File dir = new File(storageDir);
        if (!dir.exists())
            dir.mkdir();

        File image = new File(storageDir + "/" + imageFileName + ".jpg");

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private Bitmap getPhoto(String photoPath) {
        int targetW = 1024;
        int targetH = 1024;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        ExifInterface ei = null;
        try {
            ei = new ExifInterface(photoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bitmap bitmap = BitmapFactory.decodeFile(photoPath, bmOptions);

        if (ei != null) {
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    bitmap = rotateImage(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    bitmap = rotateImage(bitmap, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    bitmap = rotateImage(bitmap, 270);
                    break;
            }
        }

        return bitmap;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap retVal;

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

        return retVal;
    }
}
