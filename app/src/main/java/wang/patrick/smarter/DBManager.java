package wang.patrick.smarter;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class DBManager {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "usage.db";
    private final Context context;
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private MySQLiteOpenHelper myDBHelper;
    private SQLiteDatabase db;
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DBStructure.tableEntry.TABLE_NAME + " (" +
                    DBStructure.tableEntry._ID + " INTEGER PRIMARY KEY," +
                    DBStructure.tableEntry.COLUMN_ID + TEXT_TYPE + COMMA_SEP +
                    DBStructure.tableEntry.COLUMN_FRIDGE + TEXT_TYPE + COMMA_SEP
                    +
                    DBStructure.tableEntry.COLUMN_WASH + TEXT_TYPE + COMMA_SEP
                    +
                    DBStructure.tableEntry.COLUMN_AIR + TEXT_TYPE + COMMA_SEP
                    +
                    DBStructure.tableEntry.COLUMN_TEMP + TEXT_TYPE +
                    " );";
    private String[] columns = {
            DBStructure.tableEntry.COLUMN_ID,
            DBStructure.tableEntry.COLUMN_FRIDGE,
            DBStructure.tableEntry.COLUMN_WASH,
            DBStructure.tableEntry.COLUMN_AIR,
            DBStructure.tableEntry.COLUMN_TEMP
            };
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DBStructure.tableEntry.TABLE_NAME;

    private static class MySQLiteOpenHelper extends SQLiteOpenHelper {
        public MySQLiteOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }
    }



    public DBManager(Context ctx) {
        this.context = ctx;
        myDBHelper = new MySQLiteOpenHelper(context);
    }
    public DBManager open() throws SQLException {
        db = myDBHelper.getWritableDatabase();
        return this;
    }
    public void close() {
        myDBHelper.close();
    }




    public long insertUser(String hour, String fridge, String wash, String air, String temp) {
        ContentValues values = new ContentValues();
        values.put(DBStructure.tableEntry.COLUMN_ID, hour);
        values.put(DBStructure.tableEntry.COLUMN_FRIDGE, fridge);
        values.put(DBStructure.tableEntry.COLUMN_WASH, wash);
        values.put(DBStructure.tableEntry.COLUMN_AIR, air);
        values.put(DBStructure.tableEntry.COLUMN_TEMP, temp);
        return db.insert(DBStructure.tableEntry.TABLE_NAME, null, values);
    }

    public Cursor getAllUsers() {
        return db.query(DBStructure.tableEntry.TABLE_NAME, columns, null, null, null, null, null);
    }

    public int deleteUser(String rowId) {
        String[] selectionArgs = { String.valueOf(rowId) };
        String selection = DBStructure.tableEntry.COLUMN_ID + " LIKE ?";
        return db.delete(DBStructure.tableEntry.TABLE_NAME, selection,selectionArgs );
    }
    public void deleteAll(){
        db.execSQL("delete from "+ DBStructure.tableEntry.TABLE_NAME);
    }














}
