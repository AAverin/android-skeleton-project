package pro.anton.averin.android.skeleton.models.basicModel;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.sql.SQLException;

/**
 * Created by AAverin on 24.05.2014.
 */
public interface DBInterface<T extends BasicModel> {
    public abstract T add(SQLiteDatabase db) throws SQLException;
    public abstract T update_byLocalId(SQLiteDatabase db) throws SQLException;
    public abstract T update_byExternalId(SQLiteDatabase db) throws SQLException;
    public abstract boolean exists(SQLiteDatabase db) throws SQLException;
    public abstract void bindStatement(SQLiteStatement statement) throws SQLException;
    public abstract boolean remove(SQLiteDatabase db) throws SQLException;
}
