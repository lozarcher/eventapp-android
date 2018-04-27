package com.loz.iyaf.imagehelpers;

import android.content.Context;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class JsonCache {

    public static <T> void writeToCache(Context context, T object, String filename) {
        File f = new File(context.getExternalCacheDir(), filename);
        try {
            OutputStream os = new FileOutputStream(f);
            //OutputStream buffer = new BufferedOutputStream(os);
            ObjectOutput output = new ObjectOutputStream(os);
            output.writeObject(object);
            output.close();
        } catch (IOException e) {
            Log.e("cache", e.getMessage());
            Crashlytics.logException(e);
        }
    }

    public static ObjectInput readFromCache(Context context, String filename) {
        try {
            File f = new File(context.getExternalCacheDir(), filename);
            InputStream file = new FileInputStream(f);
            //InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(file);
            return input;
        } catch (Exception e) {
            Log.e("cache", "Cannot read file "+filename);
            e.printStackTrace();
            Crashlytics.logException(e);
        }
        return null;
    }
}