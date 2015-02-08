package pro.anton.averin.android.skeleton.data.prefs;

import pro.anton.averin.android.skeleton.BaseContext;

/**
 * Created by AAverin on 24.05.2014.
 */
public class PermanentCache extends BaseCache {

    public String sample = "";
    public String locale_lang = "ru";
    public String locale_country = "RUS";
    public String locale_variant = "";

    private SharedPreferencesCache preferencesCache;

    public static PermanentCache instance = null;

    public static PermanentCache getInstance(BaseContext context) {
        if (instance == null) {
            instance = new PermanentCache(context);
        }
        return instance;
    }

    private PermanentCache(BaseContext baseContext) {
        preferencesCache = SharedPreferencesCache.getInstance(baseContext);
    }

    @Override
    public AppSharedPreferences getPreferences() {
        return preferencesCache.prefCache;
    }

    @Override
    public void save() {
        preferencesCache.prefCache.setStringSetting(SharedPreferencesCache.SHARED_OPTIONS.SAMPLE, sample);
        preferencesCache.prefCache.setStringSetting(SharedPreferencesCache.SHARED_OPTIONS.LOCALE_LANG, locale_lang);
        preferencesCache.prefCache.setStringSetting(SharedPreferencesCache.SHARED_OPTIONS.LOCALE_COUNTRY, locale_country);
        preferencesCache.prefCache.setStringSetting(SharedPreferencesCache.SHARED_OPTIONS.LOCALE_VARIANT, locale_variant);
    }

    @Override
    public void restore() {
        sample = preferencesCache.prefCache.getStringSetting(SharedPreferencesCache.SHARED_OPTIONS.SAMPLE);
        locale_lang = preferencesCache.prefCache.getStringSetting(SharedPreferencesCache.SHARED_OPTIONS.LOCALE_LANG);
        locale_country = preferencesCache.prefCache.getStringSetting(SharedPreferencesCache.SHARED_OPTIONS.LOCALE_COUNTRY);
        locale_variant = preferencesCache.prefCache.getStringSetting(SharedPreferencesCache.SHARED_OPTIONS.LOCALE_VARIANT);
    }

    @Override
    public void reset() {
        sample = "";
        locale_lang = "";
        locale_country = "";
        locale_variant = "";
        save();
    }
}
