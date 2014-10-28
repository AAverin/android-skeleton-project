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
    }

}
