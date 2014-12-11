package pro.anton.averin.android.skeleton.data.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by AAverin on 20.05.2014.
 */
public class SQLiteDB extends DataSQLiteDB {

    public SQLiteDB(Context context) {
        super(context, "skeleton_db.sqlite", 1);
    }

    @Override
    public void onCreateDatabase(SQLiteDatabase db) {

    }

    @Override
    public void onUpgradeDatabase(SQLiteDatabase db) {

    }
}
