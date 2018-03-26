package com.loz.iyaf.gallery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.loz.iyaf.feed.GalleryData;
import com.loz.iyaf.imagehelpers.Utils;
import com.loz.R;

import java.util.ArrayList;

/**
 * Created by larcher on 04/03/2016.
 */
public class GalleryAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<GalleryData> data;
    private static LayoutInflater inflater=null;

    public GalleryAdapter(Activity a, ArrayList<GalleryData> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.gallery_image, null);

        ImageView imageView = (ImageView)vi.findViewById(R.id.image);
        final ProgressBar progressBar = (ProgressBar)vi.findViewById(R.id.progress);

        GalleryData photo = data.get(position);
        Utils.loadImage(photo.getThumb(), imageView, progressBar);
        imageView.setOnClickListener(new OnImageClickListener(position));
        return vi;
 }

    private class OnImageClickListener implements View.OnClickListener {
        int _position;
        public OnImageClickListener(int position) {
            this._position = position;
        }

        @Override
        public void onClick(View v) {
            // on selecting grid view image
            // launch full screen activity
            Intent i = new Intent(activity, GalleryFullscreenActivity.class);
            i.putExtra("position", _position);
            i.putExtra("gallery", data);
            activity.startActivity(i);
        }
    }
}
