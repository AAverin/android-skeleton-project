package pro.anton.averin.android.skeleton.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.sql.SQLException;

import pro.anton.averin.android.skeleton.data.sqlite.SQLiteTable;
import pro.anton.averin.android.skeleton.models.basicModel.BasicModel;

/**
 * Created by AAverin on 11-12-2014.
 */
public class SampleModel extends BasicModel<SampleModel> {
    public String name;
    public double field1;
    public double field2;

    public SampleModel() {

    }

    public static class SQLITE {
        public final static String TABLE_NAME = "sampletable";

        public final static String COL_NAME = "name";
        public final static String COL_1 = "col1";
        public final static String COL_2 = "col2";

        public final static SQLiteTable table = new SQLiteTable.Builder(TABLE_NAME)
                .addIntegerColumn(ID, "PRIMARY KEY")
                .addTextColumn(COL_NAME)
                .addRealColumn(COL_1)
                .addRealColumn(COL_2)

//                .setIndexColumns(COL_1, COL_2)
                .build();
    }

    @Override
    public ContentValues asContentValues() {
        ContentValues values = new ContentValues();
        values.put(SQLITE.COL_NAME, name);
        values.put(SQLITE.COL_1, field1);
        values.put(SQLITE.COL_2, field2);
        return values;
    }

    @Override
    public SampleModel fromCursor(Cursor cursor) {
        SQLiteTable.TableCursor c = new SQLiteTable.TableCursor(SQLITE.table, cursor);

        SampleModel poi = new SampleModel();

        poi._id = c.getLong(ID);
        poi.name = c.getString(SQLITE.COL_NAME);
        poi.field1 = c.getDouble(SQLITE.COL_1);
        poi.field2 = c.getDouble(SQLITE.COL_2);

        return poi;
    }

    @Override
    public SampleModel add(SQLiteDatabase db) throws SQLException {
        ContentValues values = asContentValues();

        long id = db.insert(SQLITE.TABLE_NAME, null, values);
        if (id == -1) {
            throw new SQLException("Could not add " + getClass().getSimpleName() + " " + this);
        }
        _id = id;
        return this;
    }

    @Override
    public SampleModel update_byLocalId(SQLiteDatabase db) throws SQLException {
        ContentValues values = asContentValues();

        String whereClause = ID + "=?";
        String[] whereArgs = { String.valueOf(_id) };
        int rows = db.update(SQLITE.TABLE_NAME, values, whereClause, whereArgs);
        if (rows != 1)
            throw new SQLException("Could not update_byLocalId " + getClass().getSimpleName() + " " + this);

        return this;
    }

    @Override
    public SampleModel update_byExternalId(SQLiteDatabase db) throws SQLException {
        return null;
    }

    public static SampleModel getById(SQLiteDatabase db, long id) {
        String selection = ID + "=?";
        String[] selectionArgs = { String.valueOf(id) };
        Cursor c = db.query(SampleModel.SQLITE.TABLE_NAME, SampleModel.SQLITE.table.getColumns(), selection, selectionArgs, null, null, null);

        try {
            if (c.getCount() > 0) {
                c.moveToNext();
                return new SampleModel().fromCursor(c);
            } else
                return null;
        } finally {
            c.close();
        }
    }

    @Override
    public boolean exists(SQLiteDatabase db) throws SQLException {
        SampleModel obj = getById(db, _id);
        return obj != null;
    }

    @Override
    public void bindStatement(SQLiteStatement statement) throws SQLException {
        statement.bindLong(SQLITE.table.indexOf(ID), _id);
        statement.bindString(SQLITE.table.indexOf(SQLITE.COL_NAME), name);
        statement.bindDouble(SQLITE.table.indexOf(SQLITE.COL_1), field1);
        statement.bindDouble(SQLITE.table.indexOf(SQLITE.COL_2), field2);
    }

    @Override
    public boolean remove(SQLiteDatabase db) throws SQLException {
        return false;
    }
}
