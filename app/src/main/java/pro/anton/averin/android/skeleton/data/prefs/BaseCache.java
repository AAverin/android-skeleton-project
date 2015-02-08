package pro.anton.averin.android.skeleton.data.prefs;

/**
 * Created by AAverin on 24.05.2014.
 */
public abstract class BaseCache {

    public abstract AppSharedPreferences getPreferences();

    public abstract void save();
    public abstract void restore();
    public abstract void reset();
}
