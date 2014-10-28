package pro.anton.averin.android.skeleton.utils;

import android.app.LoaderManager;
import android.content.Loader;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

import pro.averin.anton.android.skeleton.R;


/**
 * Created by AAverin on 01.07.2014.
 */
public class Utils {

    public static void executAsyncTask(AsyncTask task, Object params) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        else
            task.execute(params);
    }

    public static void executAsyncTask(AsyncTask task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            task.execute();
    }

    public static void safeRemoveLayoutListener(ViewTreeObserver observer, ViewTreeObserver.OnGlobalLayoutListener listener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            observer.removeOnGlobalLayoutListener(listener);
        } else {
            observer.removeGlobalOnLayoutListener(listener);
        }
    }

    public static void safeSetBackground(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }

    }

    public static void setText(TextView target, Object text) {
        if (text instanceof Integer) {
            if ((Integer)text == -1) {
                target.setText(R.string.label_empty);
                return;
            }
        }

        if (text instanceof ArrayList) {
            StringBuilder builder = new StringBuilder();
            Iterator iterator = ((ArrayList)text).iterator();
            while (iterator.hasNext()) {
                Object e = iterator.next();
                builder.append(e.toString());
                if (iterator.hasNext()) {
                    builder.append(", ");
                }
            }
            target.setText(String.valueOf(builder.toString()));
            return;
        }

        if (text instanceof Boolean) {
            if ((Boolean)text) {
                target.setText(R.string.label_yes);
            } else {
                target.setText(R.string.label_no);
            }
            return;
        }
        target.setText(String.valueOf(text));

    }

    public static void initOrRestartLoader(LoaderManager loaderManager, int id, Bundle bundle, LoaderManager.LoaderCallbacks callbacks) {
        Loader loader = loaderManager.getLoader(id);
        if (loader == null) {
            loaderManager.initLoader(id, bundle, callbacks);
        } else {
            loaderManager.restartLoader(id, bundle, callbacks);
        }

    }

    public static BigDecimal round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
    public static int generateViewId() {
        for (;;) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }


    public interface OnGlobalLayoutCallback {
        public void onGlobalLayout(ViewGroup viewGroup);
    }

    public static void globalLayoutOnce(final ViewGroup viewGroup, final OnGlobalLayoutCallback callback) {
        viewGroup.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Utils.safeRemoveLayoutListener(viewGroup.getViewTreeObserver(), this);
                if (callback != null) {
                    callback.onGlobalLayout(viewGroup);
                }
            }
        });
    }
}
