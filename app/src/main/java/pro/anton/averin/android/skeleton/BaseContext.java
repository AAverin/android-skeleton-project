package pro.anton.averin.android.skeleton;

import android.app.Application;

import pro.anton.averin.android.skeleton.data.net.NetworkManager;
import pro.anton.averin.android.skeleton.data.net.RestService;
import pro.anton.averin.android.skeleton.data.prefs.PermanentCache;
import pro.anton.averin.android.skeleton.data.sqlite.SQLiteDB;
import pro.anton.averin.android.skeleton.utils.TimeUtils;

/**
 * Created by AAverin on 28-10-2014.
 */
public class BaseContext extends Application {

    public NetworkManager restManager;
    public RestService restService;
    public PermanentCache permanentCache;
    public SQLiteDB db;
    public TimeUtils timeUtils;

    @Override
    public void onCreate() {
        super.onCreate();

        timeUtils = new TimeUtils(this);

        db = new SQLiteDB(this);

        restManager = NetworkManager.getInstance(this);
        restService = restManager.getRestService();
        permanentCache = PermanentCache.getInstance(this);
        permanentCache.restore();
        if (permanentCache.locale_lang == null) {
            changeLocale("ru", "RUS");
        } else {
            changeLocale(permanentCache.locale_lang, permanentCache.locale_country, permanentCache.locale_variant);
        }
    }

    public boolean isRULocale() {
        return getResources().getConfiguration().locale.getISO3Country().equals("RUS");
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (locale != null) {
            Utils.setLocale(this, locale);
        }
    }
    public void changeLocale(String language, String country) {
        changeLocale(language, country, "");
    }
    public void changeLocale(String language, String country, String variant) {
        Configuration config = getBaseContext().getResources().getConfiguration();
        if (!"".equals(language) && !config.locale.getLanguage().equals(language)) {

            permanentCache.locale_lang = language;
            permanentCache.locale_country = country;
            permanentCache.locale_variant = variant;
            permanentCache.save();

            locale = new Locale(language, country, variant);
            Utils.setLocale(this, locale);
        }
    }
}
