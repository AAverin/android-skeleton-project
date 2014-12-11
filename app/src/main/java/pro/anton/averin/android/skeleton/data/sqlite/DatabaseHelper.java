package pro.anton.averin.android.skeleton.data.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by AAverin on 11-12-2014.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public interface DBI {
        public void onCreateDatabase(SQLiteDatabase db);
        public void onUpgradeDatabase(SQLiteDatabase db);
    }
    private DBI dbiCallback = null;

    DatabaseHelper(Context context, String dbName, int dbVersion, DBI dbiCallback) {
        super(context, dbName, null, dbVersion);
        this.dbiCallback = dbiCallback;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (dbiCallback != null) {
            dbiCallback.onCreateDatabase(db);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (dbiCallback != null) {
            dbiCallback.onUpgradeDatabase(db);
        }

        onCreate(db);
    }


}