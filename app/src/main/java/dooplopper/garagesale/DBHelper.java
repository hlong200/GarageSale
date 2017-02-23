package dooplopper.garagesale;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Hunter on 2/22/2017.
 */

public class DBHelper extends SQLiteOpenHelper implements BaseColumns {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "garagesale.db";

    public static final String TABLE_NAME = "items";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_NAME_PRICE = "price";

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY," + COLUMN_NAME + " TEXT," + COLUMN_NAME_PRICE + " DOUBLE)";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);

    }

}
