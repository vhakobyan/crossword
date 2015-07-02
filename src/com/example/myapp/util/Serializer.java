package com.example.myapp.util;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by Vahagn Hakobyan
 * on 7/2/15.
 */
public class Serializer {

    private static final String tagName = "Serializer";

    public Object loadObject(Context context, String fileName) {
        Object obj = null;
        ObjectInputStream in = null;
        try {
            File data = new File(context.getFilesDir(), fileName + ".ser");
            if (data.isFile()) {
                in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(data)));
                obj = in.readObject();
            }
        } catch (Exception e) {
            Log.e(tagName, e.getMessage());
        } finally {
            try {
                if (in != null) in.close();
            } catch (IOException ignored) {}
        }

        return obj;
    }

    public void storeObject(Context context, Serializable obj, String fileName) {
        ObjectOutputStream out = null;
        try {
            File data = new File(context.getFilesDir(), fileName + ".ser");
            out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(data)));
            out.writeObject(obj);
            out.close();
        } catch (Exception e) {
            Log.e(tagName, e.getMessage());
        } finally {
            try {
                if (out != null) out.close();
            } catch (IOException ignored) {}
        }
    }
}
