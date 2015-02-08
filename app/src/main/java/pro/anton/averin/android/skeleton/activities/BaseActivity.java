package pro.anton.averin.android.skeleton.activities;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;

import com.splunk.mint.Mint;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import pro.anton.averin.android.skeleton.BaseContext;
import pro.anton.averin.android.skeleton.Config;
import pro.anton.averin.android.skeleton.fragments.BaseDialogFragment;
import pro.anton.averin.android.skeleton.fragments.BaseFragment;
import pro.anton.averin.android.skeleton.utils.Logger;
import pro.anton.averin.android.skeleton.views.LoadingOverlay;
import pro.averin.anton.android.skeleton.R;


public abstract class BaseActivity extends FragmentActivity {

    public final static String MAIN_FRAGMENT_TAG = "MAIN";

    public enum ActivityState {
        created,
        paused,
        stopped,
        resumed
    }
    public ActivityState activityState;

    protected BaseContext baseContext;
    public Handler uiHandler;

    protected LoadingOverlay loading = null;

    public MixpanelAPI mixpanelAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityState = ActivityState.created;

        uiHandler = new Handler();

        baseContext = (BaseContext) getApplicationContext();

        loading = LoadingOverlay.getInstance();

//        Mint.initAndStartSession(this, Config.BUGSENSE_API_KEY);
//        Mint.addExtraData("activity", this.getClass().getName());

        mixpanelAPI = MixpanelAPI.getInstance(this, Config.MIXPANEL_API_KEY);

//        ActionBar actionBar = getActionBar();
//        actionBar.setDisplayShowTitleEnabled(false);
//        actionBar.setDisplayShowCustomEnabled(true);
    }



    public void showLoading() {
        if (loading == null) {
            loading = LoadingOverlay.getInstance();
        }
        loading.showLoading();
    }

    public void hideLoading() {
        if (loading != null) {
            loading.hideLoading();
        }
    }

    public abstract String getScreenName();


    public boolean isActive() {
        return activityState != BaseActivity.ActivityState.paused && activityState != BaseActivity.ActivityState.stopped && !isFinishing();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Logger.log_e(this, "onStart");
        activityState = ActivityState.created;
        loading.setActivity(this);
        Mint.leaveBreadcrumb(getScreenName() + " onStart();");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Logger.log_e(this, "onPause");
        activityState = ActivityState.paused;
//        Mint.leaveBreadcrumb(getScreenName() + " onPause();");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.log_e(this, "onResume");
        activityState = ActivityState.resumed;
//        Mint.leaveBreadcrumb(getScreenName() + " onResume();");
//        Mint.flush();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Logger.log_e(this, "onStop");
        activityState = ActivityState.stopped;
//        Mint.leaveBreadcrumb(getScreenName() + " onStop();");
    }

    @Override
    protected void onDestroy() {
        mixpanelAPI.flush();
//        Mint.leaveBreadcrumb(getScreenName() + " onDestroy();");
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        try {
            BaseFragment fragment = (BaseFragment) getSupportFragmentManager().findFragmentByTag(MAIN_FRAGMENT_TAG);
            if (fragment != null && !fragment.handleBackPress()) {
                super.onBackPressed();
            }
        } catch (ClassCastException e) {
            BaseDialogFragment fragment = (BaseDialogFragment) getSupportFragmentManager().findFragmentByTag(MAIN_FRAGMENT_TAG);
            if (fragment != null && !fragment.handleBackPress()) {
                super.onBackPressed();
            }
        }
    }

    public static void restart(BaseContext baseContext) {
        restart(baseContext, 0);
    }
    public static void restart(BaseContext baseContext, int delay) {
        if (delay == 0) {
            delay = 1;
        }

        baseContext.permanentCache.save();
        Logger.log_e("", "restarting app");
        Intent restartIntent = baseContext.getPackageManager()
                .getLaunchIntentForPackage(baseContext.getPackageName());
        PendingIntent intent = PendingIntent.getActivity(
                baseContext, 0,
                restartIntent, Intent.FLAG_ACTIVITY_CLEAR_TOP);
        AlarmManager manager = (AlarmManager) baseContext.getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.RTC, System.currentTimeMillis() + delay, intent);
        System.exit(2);
    }

    public void handleGenericErrorRestart(int delay) {
        handleGenericError();
        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                restart(baseContext, 1);
            }
        }, delay);
    }

    public void handleGenericError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.label_critical_error)
                .setMessage(R.string.label_critical_error_message);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    boolean haveCached = false;
    boolean isTabletCache = false;
    public boolean isTablet() {
        if (!haveCached) {
            isTabletCache = getResources().getBoolean(R.bool.isTablet);
        }
        return isTabletCache;
    }

    public void shareLogcat() {
        String sdcardPath = null;
        String path = Environment.getExternalStorageDirectory() + File.separator + Config.APP_SHORT_NAME;
        File dir = new File(path);
        if (dir.mkdirs() || dir.isDirectory()) {
            sdcardPath = path + File.separator + "logcat_" + System.currentTimeMillis() + ".log";
        }

        if (sdcardPath == null) {
            return;
        }

        File logFile = null;
        try {
            logFile = new File(sdcardPath);
            String cmd = "logcat -d -f " + logFile.getAbsolutePath();
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///" + sdcardPath));
        sendIntent.setType("message/rfc822");
        sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { Config.APP_EMAIL });
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Logcat bugreport for " + Config.APP_NAME);
        startActivity(sendIntent);

        try {
            String cmd = "logcat -c";
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void debugLogEvent(String type, String eventName, HashMap<String, String> parameters) {
        StringBuilder debugLogMessage = new StringBuilder();

        debugLogMessage.append("__debugLogEvent__");
        debugLogMessage.append(type);
        debugLogMessage.append(" :: ");
        debugLogMessage.append(eventName);
        if (parameters != null) {
            debugLogMessage.append(" :: ");
            for (String key : parameters.keySet()) {
                String value = parameters.get(key);
                debugLogMessage.append("(");
                debugLogMessage.append(key);
                debugLogMessage.append(",");
                debugLogMessage.append(value);
                debugLogMessage.append("), ");
            }
        }

        Logger.log("BaseActivity", debugLogMessage.toString());
    }

    public interface EVENT_ENTRIES {
        //EVENTS
        public final static String EVENT_NAME_APP_OPENED = "AppOpened";

        //PARAMS
        public final static String EVENT_PARAM_SAMPLE = "Sample";

    }

    public void trackGlobalEvent(String eventName, HashMap<String, String> parameters) {
        if (mixpanelAPI != null) {
            if (Config.EVENT_TRACKING_ENABLED) {
                mixpanelAPI.track(eventName, parameters != null ? new JSONObject(parameters) : null);
            } else {
                debugLogEvent("MixpanelEvent", eventName, parameters);
            }

        }
    }
}
