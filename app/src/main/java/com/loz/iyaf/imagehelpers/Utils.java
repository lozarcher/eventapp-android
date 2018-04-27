package com.loz.iyaf.imagehelpers;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.crashlytics.android.Crashlytics;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

import java.io.InputStream;
import java.io.OutputStream;

public class Utils {
    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
                int count=is.read(bytes, 0, buffer_size);
                if(count==-1)
                    break;
                os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }

    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            Crashlytics.logException(nfe);
            return false;
        }
        return true;
    }



    public static void loadImage(String url, ImageView imageView, final ProgressBar progressBar, int roundedCorner) {
        DisplayImageOptions options;
        if (roundedCorner > 0) {
            options = new DisplayImageOptions.Builder()
                    .displayer(new RoundedBitmapDisplayer(roundedCorner))
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
        } else {
            options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
        }
        ImageLoader imageLoader = ImageLoader.getInstance();
        if (progressBar == null) {
            imageLoader.displayImage(url, imageView, options);
        } else {
            imageLoader.displayImage(url, imageView, options, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    if (progressBar != null) {
                        progressBar.setProgress(0);
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    Log.d("LOZ", "FAILED LOADING "+imageUri);
                    if (progressBar != null)
                        progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    if (progressBar != null)
                        progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    if (progressBar != null)
                        progressBar.setVisibility(View.GONE);
                }
            }, new ImageLoadingProgressListener() {
                @Override
                public void onProgressUpdate(String imageUri, View view, int current, int total) {
                    if (progressBar != null)
                        progressBar.setProgress(Math.round(100.0f * current / total));
                }
            });
        }
    }
}