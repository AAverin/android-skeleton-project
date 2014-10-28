package pro.anton.averin.android.skeleton.models.basicModel;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by AAverin on 20.05.2014.
 */
public abstract class BasicModel<T extends BasicModel> implements DBInterface<T> {

    public long _id; //local database id

    public BasicModel() {

    }

    public abstract ContentValues asContentValues();
    public abstract BasicModel fromCursor(Cursor cursor);
}
