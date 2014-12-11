package pro.anton.averin.android.skeleton.data.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by AAverin on 9-12-2014.
 */
public abstract class DataSQLiteDB extends BaseSQLiteDB implements DatabaseHelper.DBI {

    private DatabaseHelper helper = null;

    public DataSQLiteDB(Context context, String name, int version) {
        super(context, name, version);
    }

    @Override
    public SQLiteOpenHelper getHelper() {
        if (helper == null) {
            helper = new DatabaseHelper(context, dbName, dbVersion, this);
            helper.getWritableDatabase(); //results in db onCreate being called
        }
        return helper;
    }

    @Override
    public void close() {
        helper.close();
    }

}
