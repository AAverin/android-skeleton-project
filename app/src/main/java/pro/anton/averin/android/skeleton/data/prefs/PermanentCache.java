package pro.anton.averin.android.skeleton.data.prefs;

import pro.anton.averin.android.skeleton.BaseContext;

/**
 * Created by AAverin on 24.05.2014.
 */
public class PermanentCache extends BaseCache {

    public String sample = "";

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
    public void save() {
        preferencesCache.prefCache.setStringSetting(SharedPreferencesCache.SHARED_OPTIONS.SAMPLE, sample);
    }

    @Override
    public void restore() {
        sample = preferencesCache.prefCache.getStringSetting(SharedPreferencesCache.SHARED_OPTIONS.SAMPLE);
    }

    @Override
    public void reset() {
        sample = "";
        save();
    }
}
