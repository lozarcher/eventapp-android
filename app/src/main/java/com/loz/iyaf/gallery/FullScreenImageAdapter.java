package com.loz.iyaf.gallery;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loz.iyaf.feed.GalleryData;
import com.loz.iyaf.imagehelpers.TouchImageView;
import com.loz.iyaf.imagehelpers.Utils;
import com.loz.iyaf.R;

public class FullScreenImageAdapter extends PagerAdapter {

    private Activity _activity;
    private ArrayList<GalleryData> _images;
    private LayoutInflater inflater;

    // constructor
    public FullScreenImageAdapter(Activity activity,
                                  ArrayList<GalleryData> images) {
        this._activity = activity;
        this._images = images;
    }

    @Override
    public int getCount() {
        return this._images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TouchImageView imgDisplay;
        Button btnClose;

        inflater = (LayoutInflater) _activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.activity_gallery_fullscreen_image, container,
                false);

        imgDisplay = (TouchImageView) viewLayout.findViewById(R.id.imgDisplay);
        btnClose = (Button) viewLayout.findViewById(R.id.btnClose);
        imgDisplay = (TouchImageView) viewLayout.findViewById(R.id.imgDisplay);
        TextView captionView = (TextView) viewLayout.findViewById(R.id.captionView);

        Utils.loadImage(_images.get(position).getPicture(), imgDisplay, null);

        String user = _images.get(position).getUser();
        String caption = _images.get(position).getCaption();
        if (caption.matches("")) {
            captionView.setText(user);
        } else {
            captionView.setText(user+"\r\n"+caption);
        }

        // close button click event
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _activity.finish();
            }
        });

        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);

    }
}