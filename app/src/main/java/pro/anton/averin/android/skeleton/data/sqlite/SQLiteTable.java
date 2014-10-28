package pro.anton.averin.android.skeleton.data.sqlite;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.BaseColumns;

import java.util.*;

/**
 * Created by AAverin on 12.11.13.
 */
@SuppressWarnings({"StringBufferMayBeStringBuilder", "DefaultFileTemplate"})
public final class SQLiteTable {

    ArrayList<String> tableIndex = new ArrayList<String>();
    HashMap<String, String> tableStructure = new HashMap<String, String>();
    private final String tableName;
    protected String indexSQL = "";

    protected SQLiteTable(String tableName) {
        this.tableName = tableName;
    }

    public String getCreateSQL() {
        StringBuffer createTable = new StringBuffer();
        createTable.append("CREATE TABLE ");
        createTable.append(tableName);
        createTable.append("(");
        Iterator<Map.Entry<String, String>> iterator = tableStructure.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            createTable.append(entry.getKey());
            createTable.append(" ");
            createTable.append(entry.getValue());
            if (iterator.hasNext()) {
                createTable.append(", ");
            }
        }
        createTable.append(");");

        return createTable.toString();
    }

    public String getIndexSQL() {
        return indexSQL;
    }

    public void createIndex(SQLiteDatabase db) {
        if (indexSQL != null && indexSQL.length() > 0) {
            db.execSQL(indexSQL);
        }
    }

    public String getInsertSQL() {
        StringBuffer createTable = new StringBuffer();
        createTable.append("INSERT OR REPLACE INTO ");
        createTable.append(tableName);

        StringBuffer keysBuffer = new StringBuffer();
        keysBuffer.append(" (");
        StringBuffer valuesBuffer = new StringBuffer();
        valuesBuffer.append("(");

        Iterator<String> iterator = tableIndex.iterator();
        while(iterator.hasNext()) {
            String entry = iterator.next();
            if (!entry.equals(BaseColumns._ID)) {
                keysBuffer.append(entry);
                valuesBuffer.append("?");
                if (iterator.hasNext()) {
                    keysBuffer.append(", ");
                    valuesBuffer.append(", ");
                }
            }
        }

        keysBuffer.append(")");
        valuesBuffer.append(")");

        //keys
        createTable.append(keysBuffer.toString());
        createTable.append(" VALUES ");
        createTable.append(valuesBuffer.toString());
        //values

        return createTable.toString();
    }

    public String getDropSQL() {
        return "DROP TABLE IF EXISTS " + tableName;
    }

    public String[] getColumns() {
        String[] columns = new String[tableIndex.size()];
        return tableIndex.toArray(columns);
    }

    public int indexOf(String column_key) {
        return tableIndex.indexOf(column_key);
    }

    private void addEntry(String key, String value) {
        tableIndex.add(key);
        tableStructure.put(key, value);
    }

    @SuppressWarnings("SameParameterValue")
    public static class Builder {
        static SQLiteTable sqLiteTable;

        public Builder(final String tableName) {
            sqLiteTable = new SQLiteTable(tableName);
        }

        public Builder addIntegerColumn(String key) {
            return addIntegerColumn(key, "");
        }
        public Builder addIntegerColumn(String key, String additionalOptions) {
            sqLiteTable.addEntry(key, "INTEGER " + additionalOptions);
            return this;
        }

        public Builder addRealColumn(String key) {
            return addRealColumn(key, "");
        }
        public Builder addRealColumn(String key, String additionalOptions) {
            sqLiteTable.addEntry(key, "REAL " + additionalOptions);
            return this;
        }

        public Builder addTextColumn(String key) {
            return addTextColumn(key, "");
        }
        public Builder addTextColumn(String key, String additionalOptions) {
            sqLiteTable.addEntry(key, "TEXT " + additionalOptions);
            return this;
        }

        private String generateIndexSQL(String... columns) {
            StringBuilder indexSql = new StringBuilder();
            indexSql.append("CREATE INDEX ");
            indexSql.append(sqLiteTable.tableName);
            indexSql.append("_index ON ");
            indexSql.append(sqLiteTable.tableName);
            indexSql.append(" (");
            Iterator<String> iterator = Arrays.asList(columns).iterator();
            while (iterator.hasNext()) {
                String column = iterator.next();
                indexSql.append(column);
                if (iterator.hasNext()) {
                    indexSql.append(",");
                }
            }
            indexSql.append(");");
            return indexSql.toString();
        }

        public Builder setIndexColumns(String... columns) {
            sqLiteTable.indexSQL = generateIndexSQL(columns);
            return this;
        }

        public SQLiteTable build() {
            return sqLiteTable;
        }
    }

    public static class TableCursor implements Cursor {
        private Cursor baseCursor;
        private SQLiteTable table;
        public TableCursor(SQLiteTable table, Cursor cursor) {
            this.table = table;
            baseCursor = cursor;
        }

        @Override
        public int getCount() {
            return baseCursor.getCount();
        }

        @Override
        public int getPosition() {
            return baseCursor.getPosition();
        }

        @Override
        public boolean move(int i) {
            return baseCursor.move(i);
        }
        public boolean move(String key) {
            return move(table.indexOf(key));
        }

        @Override
        public boolean moveToPosition(int i) {
            return baseCursor.moveToPosition(i);
        }
        public boolean moveToPosition(String key) {
            return moveToPosition(table.indexOf(key));
        }

        @Override
        public boolean moveToFirst() {
            return baseCursor.moveToFirst();
        }

        @Override
        public boolean moveToLast() {
            return baseCursor.moveToLast();
        }

        @Override
        public boolean moveToNext() {
            return baseCursor.moveToNext();
        }

        @Override
        public boolean moveToPrevious() {
            return baseCursor.moveToPrevious();
        }

        @Override
        public boolean isFirst() {
            return baseCursor.isFirst();
        }

        @Override
        public boolean isLast() {
            return baseCursor.isLast();
        }

        @Override
        public boolean isBeforeFirst() {
            return baseCursor.isBeforeFirst();
        }

        @Override
        public boolean isAfterLast() {
            return baseCursor.isAfterLast();
        }

        @Override
        public int getColumnIndex(String s) {
            return baseCursor.getColumnIndex(s);
        }

        @Override
        public int getColumnIndexOrThrow(String s) throws IllegalArgumentException {
            return baseCursor.getColumnIndexOrThrow(s);
        }

        @Override
        public String getColumnName(int i) {
            return baseCursor.getColumnName(i);
        }
        public String getColumnName(String key) {
            return getColumnName(table.indexOf(key));
        }

        @Override
        public String[] getColumnNames() {
            return baseCursor.getColumnNames();
        }

        @Override
        public int getColumnCount() {
            return baseCursor.getColumnCount();
        }

        @Override
        public byte[] getBlob(int i) {
            return baseCursor.getBlob(i);
        }
        public byte[] getBlob(String key) {
            return getBlob(table.indexOf(key));
        }

        @Override
        public String getString(int i) {
            return baseCursor.getString(i);
        }
        public String getString(String key) {
            return getString(table.indexOf(key));
        }

        @Override
        public void copyStringToBuffer(int i, CharArrayBuffer charArrayBuffer) {
            baseCursor.copyStringToBuffer(i, charArrayBuffer);
        }

        @Override
        public short getShort(int i) {
            return baseCursor.getShort(i);
        }
        public short getShort(String key) {
            return getShort(table.indexOf(key));
        }

        @Override
        public int getInt(int i) {
            return baseCursor.getInt(i);
        }
        public int getInt(String key) {
            return getInt(table.indexOf(key));
        }

        @Override
        public long getLong(int i) {
            return baseCursor.getLong(i);
        }
        public long getLong(String key) {
            return getLong(table.indexOf(key));
        }

        @Override
        public float getFloat(int i) {
            return baseCursor.getFloat(i);
        }
        public float getFloat(String key) {
            return getFloat(table.indexOf(key));
        }

        @Override
        public double getDouble(int i) {
            return baseCursor.getDouble(i);
        }
        public double getDouble(String key) {
            return getDouble(table.indexOf(key));
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public int getType(int i) {
            return baseCursor.getType(i);
        }
        public int getType(String key) {
            return getType(table.indexOf(key));
        }

        @Override
        public boolean isNull(int i) {
            return baseCursor.isNull(i);
        }
        public boolean isNull(String key) {
            return isNull(table.indexOf(key));
        }

        @SuppressWarnings("deprecation")
        @Override
        public void deactivate() {
            baseCursor.deactivate();
        }

        @SuppressWarnings("deprecation")
        @Override
        public boolean requery() {
            return baseCursor.requery();
        }

        @Override
        public void close() {
            baseCursor.close();
        }

        @Override
        public boolean isClosed() {
            return baseCursor.isClosed();
        }

        @Override
        public void registerContentObserver(ContentObserver contentObserver) {
            baseCursor.registerContentObserver(contentObserver);
        }

        @Override
        public void unregisterContentObserver(ContentObserver contentObserver) {
            baseCursor.unregisterContentObserver(contentObserver);
        }

        @Override
        public void registerDataSetObserver(DataSetObserver dataSetObserver) {
            baseCursor.registerDataSetObserver(dataSetObserver);
        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
            baseCursor.unregisterDataSetObserver(dataSetObserver);
        }

        @Override
        public void setNotificationUri(ContentResolver contentResolver, Uri uri) {
            baseCursor.setNotificationUri(contentResolver, uri);
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        public Uri getNotificationUri() {
            return baseCursor.getNotificationUri();
        }

        @Override
        public boolean getWantsAllOnMoveCalls() {
            return baseCursor.getWantsAllOnMoveCalls();
        }

        @Override
        public Bundle getExtras() {
            return baseCursor.getExtras();
        }

        @Override
        public Bundle respond(Bundle bundle) {
            return baseCursor.respond(bundle);
        }
    }
}
