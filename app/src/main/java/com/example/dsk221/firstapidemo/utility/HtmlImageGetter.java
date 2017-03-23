package com.example.dsk221.firstapidemo.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.TextUtils;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

/**
 * Created by dsk-221 on 23/3/17.
 */

public abstract class HtmlImageGetter implements Html.ImageGetter {
    private WeakReference<Context> mContext;
    private int placeHolder;

    public HtmlImageGetter(@NonNull Context context, @DrawableRes int placeHolder) {
        this.mContext = new WeakReference<>(context);
        this.placeHolder = placeHolder;
    }

    @Override
    public Drawable getDrawable(String source) {
        if (TextUtils.isEmpty(source)) {
            return null;
        }
        Context context = mContext.get();
        if (context == null) {
            return null;
        }
        LevelListDrawable d = new LevelListDrawable();
        Drawable empty;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            empty = context.getResources().getDrawable(placeHolder, null);
        } else {
            empty = context.getResources().getDrawable(placeHolder);
        }
        d.addLevel(0, 0, empty);
        d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());

        new LoadImage().execute(source, d);
        return d;
    }

    private class LoadImage extends AsyncTask<Object, Void, Bitmap> {

        private LevelListDrawable mDrawable;

        @Override
        protected Bitmap doInBackground(Object... params) {
            String source = (String) params[0];
            mDrawable = (LevelListDrawable) params[1];
            try {
                InputStream is = new URL(source).openStream();
                return BitmapFactory.decodeStream(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap == null) {
                return;
            }
            Context context = mContext.get();
            if (context == null) {
                return;
            }
            BitmapDrawable d = new BitmapDrawable(context.getResources(), bitmap);
            mDrawable.addLevel(1, 1, d);
            mDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
            mDrawable.setLevel(1);
            onTextUpdate();
        }
    }

    public abstract void onTextUpdate();
}
