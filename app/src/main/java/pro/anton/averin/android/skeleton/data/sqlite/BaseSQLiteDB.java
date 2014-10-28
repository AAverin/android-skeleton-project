package pro.anton.averin.android.skeleton.data.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;

import pro.anton.averin.android.skeleton.models.basicModel.BasicModel;

/**
 * Created by AAverin on 20.05.2014.
 */
public class BaseSQLiteDB {

    public static final String DATABASE_NAME = "aaverin_skeleton.db";
    private static final int DATABASE_VERSION = 1;
    protected final DatabaseHelper helper;

    public BaseSQLiteDB(Context context) {
        helper = HelperManager.getHelper(context);
    }

    public BasicModel addOrUpdate(BasicModel obj) throws SQLException {
        SQLiteDatabase db = helper.getWritableDatabase();
        if (exists(obj)) {
            return obj.update_byExternalId(db);
        } else {
            return obj.add(db);
        }
    }

    public <T extends BasicModel> boolean exists(T sample) {
        SQLiteDatabase db = helper.getReadableDatabase();
        try {
            return sample.exists(db);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void deleteTable(String tableName) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(tableName, null, null);
    }

    private static final class HelperManager {
        private static DatabaseHelper helper = null;
        private static int counter = 0;

        public static synchronized DatabaseHelper getHelper(Context context) {
            if (++counter == 1) {
                helper = new DatabaseHelper(context);
            }

            return helper;
        }

        public static synchronized void releaseHelper() {
            if (--counter == 0) {
                helper.close();
                helper = null;
            }

            if (counter < 0)
                throw new IllegalStateException();
        }
    }

    protected static final class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //db.execSQL(<Model>.SQLITE.table.getCreateSQL());

            //<Model>.SQLITE.table.createIndex(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w("SQLiteDB", "Upgrading database from version " + oldVersion + " to " + newVersion
                    + ", which will destroy all old data");
            //db.execSQL(<Model>.SQLITE.table.getDropSQL());

            onCreate(db);
        }

    }

    public void close() {
        HelperManager.releaseHelper();
    }
}


