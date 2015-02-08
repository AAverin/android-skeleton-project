package pro.anton.averin.android.skeleton.utils;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    public static BigDecimal round(double d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
    public static int generateViewId() {
        for (; ; ) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

    public static String getRoundedSize(long value) {
        if (value == 0) {
            return "0b";
        }
        String sizePrefixes = " KMGTPEZYXWVU";
        int powShiftCount = 0;
        powShiftCount = (int) Math.min(Math.floor(Math.log(value)/Math.log(1024)), sizePrefixes.length() - 1);
        //make the actual rounding calculation
        double result = ((value * 100 / Math.pow(1024, powShiftCount)) / 100);
        int toSignificantNumber = 0;
        //if the resulting number is fixed integer - no further rounding needed
        if (result % 1 != 0) {
            //otherwise, round to the closes significant number after the decimal point + 2 points for precision
            toSignificantNumber = (int) Math.round((-Math.log(Math.abs(result)) / Math.log(10)) + 2);
        }

        //if significant number turned out to be < 0, eg. if we were not rounding down but making Megabytes from Gigabytes - don't round
        toSignificantNumber = toSignificantNumber < 0 ? 0 : toSignificantNumber;
        //return data in '10 GB' format
        return round(result, toSignificantNumber).toString() + ' ' + sizePrefixes.charAt(powShiftCount);
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


    public static String joinStrings(String... strings) {
        StringBuilder builder = new StringBuilder();
        for (String str : strings) {
            builder.append(str);
        }
        return builder.toString();
    }


    public static void copyStreamToPath(InputStream in, String path) throws IOException {
        FileOutputStream out = new FileOutputStream(path);
        byte[] buff = new byte[1024];
        int read = 0;
        try {
            while ((read = in.read(buff)) > 0) {
                out.write(buff, 0, read);
            }
        } finally {
            in.close();
            out.close();
        }
    }
    public static float dpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    public static float pixelToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }

    public static double min(double... values) {
        double lastValue = Double.MAX_VALUE;
        for (double a : values) {
            lastValue = Math.min(lastValue, a);
        }
        return lastValue;
    }
    public static double max(double... values) {
        double lastValue = Double.MIN_VALUE;
        for (double a : values) {
            lastValue = Math.max(lastValue, a);
        }
        return lastValue;
    }
    public static void setLocale(BaseContext baseContext, Locale locale) {
        Resources res = baseContext.getResources();
        Configuration configuration = new Configuration(res.getConfiguration());

        Locale.setDefault(locale);

        configuration.locale = locale;
        res.updateConfiguration(configuration, res.getDisplayMetrics());
    }

    public static void setLocale(BaseContext baseContext, String language, String country) {
        setLocale(baseContext, new Locale(language, country));
    }
}
