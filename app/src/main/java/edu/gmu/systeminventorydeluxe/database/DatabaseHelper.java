package edu.gmu.systeminventorydeluxe.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * THe database helper class.
 * Its main purpose is to create and update the database
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    /**
     * Some sources:
     *
     *      General SQLite:
     *     //https://developer.android.com/training/data-storage/sqlite
     *
     *     SQLiteDatabase:
     *     //https://developer.android.com/reference/android/database/sqlite/SQLiteDatabase
     *
     *     helpers, providers, contracts
     *     //https://riptutorial.com/android/example/9221/create-a-contract--helper-and-provider-for-sqlite-in-android
     *
     *     THIS IS ALSO A GOOD SOURCE for helpers, providers, and contracts**********
     *     ****************************************************************************
     *     https://google-developer-training.github.io/android-developer-fundamentals-course-practicals/en/Unit%204/111b_p_add_a_content_provider_to_wordlistsql.html
     *     ******************************************************************************
     *     **************************************************************************
     *
     *     SQLiteQpenHelper:
     *     //https://developer.android.com/reference/android/database/sqlite/SQLiteOpenHelper
     *
     *     General Database Tutorial:
     *     //https://www.youtube.com/watch?v=hDSVInZ2JCs&t=1628s
     *
     *     Base Columns:
     *     https://developer.android.com/reference/android/provider/BaseColumns
     *
     *     URIs (uniform resource identifier):
     *     https://developer.android.com/reference/android/net/Uri
     *
     *     Content Resolver/ Content Providers:
     *     To be used with the cursor loaders and adaptors
     *     https://developer.android.com/guide/topics/providers/content-providers
     */

    /**
     * The database name, this should not change
     */
    public final static String DB_NAME = "SIXinventory.dp";

    /**
     * The database version, this always starts at 1
     */
    public final static int DB_VERSION = 1;


    /**
     * Constructor of the helper
     * @param context a reference to the application itself
     */
    public DatabaseHelper(Context context) {

        //context finds the path to the database,
        //DATABASE_NAME is the string name of the database
        //cursorfactory, to make cursors may need this later???
        //DATABASE_VERSION, which is just 1.

        super(context, DB_NAME, null, DB_VERSION);
    }


    /**
     * Auto generated: will be called when accessing the database
     * will create a new database if needed
     * @param sqLiteDatabase the database
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String tableEntries = "";

        String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + ProductEntry.TABLE_NAME + " ("
                        + ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + ProductEntry.ITEM_NAME + "name"
                        + ProductEntry.ITEM_QUANTITY + "quantity"
                        + ProductEntry.ITEM_DESCRIPTION + "description"
                        + ProductEntry.ITEM_LOW_THRESHOLD + "threshold"
                        + ProductEntry.ITEM_IMAGE + "image)";

        sqLiteDatabase.execSQL(tableEntries);
    }


    /**
     * Auto generated: updata and upgrades. Can be considered
     * for security and forwards/backwards compataability
     * this should run, but it may and it required
     * @param sqLiteDatabase the database
     * @param i the current/old version of the database
     * @param i1 the new version of the database
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
      db.execSQL("DROP TABLE IF EXISTS " + ProductEntry.TABLE_NAME + ";");
    onCreate(db);
    }
}
