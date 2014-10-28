package pro.anton.averin.android.skeleton.data.prefs;

import android.content.Context;

/**
 * Created by AAverin on 21.02.14.
 */
public class SharedPreferencesCache {
    public static interface SHARED_OPTIONS {
        public final static String SAMPLE = "sample";
        
    }



    private static final String PREFS = "pro.averin.anton.android.skeleton.premanent_cache";

    public static SharedPreferencesCache instance = null;

    public AppSharedPreferences prefCache;

    private Context context;

    public static SharedPreferencesCache getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferencesCache(context);
        }
        return instance;
    }

    private SharedPreferencesCache(Context context) {
        this.context = context;
        prefCache = getScope(PREFS);
    }

    private AppSharedPreferences getScope(String scope) {
        return new AppSharedPreferences(context, scope);
    }
}
