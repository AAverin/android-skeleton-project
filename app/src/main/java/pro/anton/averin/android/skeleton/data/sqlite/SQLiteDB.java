package pro.anton.averin.android.skeleton.data.sqlite;

import pro.anton.averin.android.skeleton.BaseContext;

/**
 * Created by AAverin on 20.05.2014.
 */
public class SQLiteDB extends BaseSQLiteDB {

    private BaseContext baseContext;

    public SQLiteDB(BaseContext context) {
        super(context);
        this.baseContext = context;
    }

    //actual db logic

}
