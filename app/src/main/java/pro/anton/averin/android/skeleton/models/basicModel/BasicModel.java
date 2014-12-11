package pro.anton.averin.android.skeleton.models.basicModel;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by AAverin on 20.05.2014.
 */
public abstract class BasicModel<T extends BasicModel> implements DBInterface<T> {

    public final static String ID = "id";

    public long _id = -1; //local database id

    public BasicModel() {

    }

    public abstract ContentValues asContentValues();
    public abstract BasicModel fromCursor(Cursor cursor);
    //static convention: implement this
    public static <T extends BasicModel> T getById(SQLiteDatabase db, long id) {
        return null;
    };
}
