package pro.anton.averin.android.skeleton.data.prefs;

/**
 * Created by AAverin on 21.02.14.
 */

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

@SuppressWarnings("SameParameterValue")
public class AppSharedPreferences {

    private SharedPreferences sharedPreferences;
    private Editor preferencesEditor;

    public AppSharedPreferences(Context context, String scope) {
        this.sharedPreferences = context.getSharedPreferences(scope, Activity.MODE_PRIVATE);
        this.preferencesEditor = sharedPreferences.edit();
    }

    public String getStringSetting(String settingName) {
        return sharedPreferences.getString(settingName, null);
    }

    public void setStringSetting(String settingName, String settingValue) {
        preferencesEditor.putString(settingName, settingValue);
        preferencesEditor.commit();
    }

    public int getIntSetting(String settingName) {
        return sharedPreferences.getInt(settingName, -1);
    }

    public void setIntSetting(String settingName, int settingValue) {
        preferencesEditor.putInt(settingName, settingValue);
        preferencesEditor.commit();
    }

    public boolean getBooleanSetting(String settingName) {
        return sharedPreferences.getBoolean(settingName, false);
    }

    public void setBooleanSetting(String settingName, boolean settingValue) {
        preferencesEditor.putBoolean(settingName, settingValue);
        preferencesEditor.commit();
    }

    public long getLongSetting(String settingName) {
        return sharedPreferences.getLong(settingName, 0);
    }

    public void setLongSetting(String settingName, long settingValue) {
        preferencesEditor.putLong(settingName, settingValue);
        preferencesEditor.commit();
    }

    public void setFloatSetting(String settingName, float settingValue) {
        preferencesEditor.putFloat(settingName, settingValue);
        preferencesEditor.commit();
    }

    public float getFloatSetting(String settingName) {
        return sharedPreferences.getFloat(settingName, 0);
    }
}

