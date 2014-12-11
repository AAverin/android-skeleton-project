package pro.anton.averin.android.skeleton.data.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;

import pro.anton.averin.android.skeleton.models.basicModel.BasicModel;

/**
 * Created by AAverin on 20.05.2014.
 */
public abstract class BaseSQLiteDB {

    protected String dbName = "";
    protected int dbVersion = 0;
    protected Context context;

    public BaseSQLiteDB(Context context, String name, int version) {
        dbName = name;
        dbVersion = version;
        this.context = context;
    }

    public BasicModel addOrUpdate(BasicModel obj) throws SQLException {
        SQLiteDatabase db = getHelper().getWritableDatabase();
        if (exists(obj)) {
            return obj.update_byExternalId(db);
        } else {
            return obj.add(db);
        }
    }

    public BasicModel update_ByLocalId(BasicModel obj) throws SQLException {
        SQLiteDatabase db = getHelper().getWritableDatabase();
        if (exists(obj)) {
            return obj.update_byLocalId(db);
        }
        return null;
    }

    public <T extends BasicModel> boolean exists(T sample) {
        SQLiteDatabase db = getHelper().getReadableDatabase();
        try {
            return sample.exists(db);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public abstract SQLiteOpenHelper getHelper();

    public void deleteTable(String tableName) {
        SQLiteDatabase db = getHelper().getWritableDatabase();
        db.delete(tableName, null, null);
    }

    public abstract void close();
}
