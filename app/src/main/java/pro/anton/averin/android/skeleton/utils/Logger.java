package pro.anton.averin.android.skeleton.utils;

import android.util.Log;

import pro.anton.averin.android.skeleton.Config;

/**
 * Created by AAverin on 20.05.2014.
 */
public class Logger {

    public static void log(String TAG, Object... message) {
        if (!Config.isLoggingEnabled) {
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (Object msg : message) {
            stringBuilder.append(String.valueOf(msg));
        }
        Log.d(TAG, stringBuilder.toString());
    }

    public static void log_e(String TAG, Object... message) {
        if (!Config.isLoggingEnabled) {
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (Object msg : message) {
            stringBuilder.append(String.valueOf(msg));
        }
        Log.e(TAG, stringBuilder.toString());
    }

    public static void logArray(Object o, String[] array) {
        String tag = o.getClass().getSimpleName();
        StringBuilder stringBuilder = new StringBuilder();
        for (String msg : array) {
            stringBuilder.append(String.valueOf(msg));
            stringBuilder.append(", ");
        }
        Log.d(tag, stringBuilder.toString());
    }

    public static void log(Object o, Throwable t, Object... message) {
        if (!Config.isLoggingEnabled) {
            return;
        }

        String tag = o.getClass().getSimpleName();
        StringBuilder stringBuilder = new StringBuilder();
        for (Object msg : message) {
            stringBuilder.append(String.valueOf(msg));
        }
        Log.d(tag, stringBuilder.toString(), t);
    }

    public static void log(Object o, Object... message) {
        log(o.getClass().getSimpleName(), message);
    }

    public static void log_e(Object o, Object... message) {
        log_e(o.getClass().getSimpleName(), message);
    }

}
