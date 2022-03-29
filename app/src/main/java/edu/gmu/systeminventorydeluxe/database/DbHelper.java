package edu.gmu.systeminventorydeluxe.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * THe database helper class.
 * Its main purpose is to create and update the database
 */
public class DbHelper extends SQLiteOpenHelper {

    /**
     * Some sources:
     *     //https://developer.android.com/training/data-storage/sqlite
     *     //https://developer.android.com/reference/android/database/sqlite/SQLiteDatabase
     *     //https://riptutorial.com/android/example/9221/create-a-contract--helper-and-provider-for-sqlite-in-android
     *     //https://developer.android.com/reference/android/database/sqlite/SQLiteOpenHelper
     *     //https://www.youtube.com/watch?v=hDSVInZ2JCs&t=1628s
     */

    /**
     * The database name, this should not change
     */
    public final static String DATABASE_NAME = "SIXinventory.dp";

    /**
     * The database version, this always starts at 1
     */
    public final static int DATABASE_VERSION = 1;


    /**
     * Constructor of the helper
     * @param context a reference to the application itself
     */
    public DbHelper(Context context) {

        //context finds the path to the database,
        //DATABASE_NAME is the string name of the database
        //cursorfactory, to make cursors may need this later???
        //DATABASE_VERSION, which is just 1.

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    /**
     * Auto generated: will be called when accessing the database
     * will create a new database if needed
     * @param sqLiteDatabase the database
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        /*
        funcky code here
         */
    }


    /**
     * Auto generated: updata and upgrades. Can be considered
     * for security and forwards/backwards compataability
     * this should run, but it may and it required
     * @param sqLiteDatabase the database
     * @param i the current version of the database
     * @param i1 the new version of the database
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        
    }
}
