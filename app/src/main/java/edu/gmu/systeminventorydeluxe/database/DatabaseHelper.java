package edu.gmu.systeminventorydeluxe.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import edu.gmu.systeminventorydeluxe.database.DatabaseContract.MainInventoryItem;
import edu.gmu.systeminventorydeluxe.database.DatabaseContract.ItemRecipes;

/*
 * Code in the class is based off of code for outside sources:
 *
 * ////////////////////////////////////////////////
 *
 * Database configuration was started without the use of this source, but proved
 * Laborious for the DatabaseProvider.java. citation is being added because of how close
 * this relates to DatabaseProvider.java
 *
 * Code written by Michał Kołnierzak, code is licenced using a MIT licence
 * and free for commercial/private use and modifications
 *
 * https://github.com/kazdavegyms/Android-Inventory-Management-App-master
 *
 * ///////////////////////////////////////////////////////
 *
 *  additionally
 *
 *
 *  The following source used is licenses under a  CC BY-SA 3.0 license and is
 *  free for use and adaption
 *
 *  https://riptutorial.com/android/example/9221/create-a-contract--helper-and-provider-for-sqlite-in-android
 *
 *
 */

//////////////////////////////////////////////////////////////////////////////////
//PLEASE DON'T EDIT ANY IMAGE STUFF/NTS comments, IZZY WILL HANDLE LATER,I promise
/////////////////////////////////////////////////////////////////////////////////

/**
 * THe database helper class.
 * Its main purpose is to create and update the database
 */
//FIXME: image stuff
public class DatabaseHelper extends SQLiteOpenHelper {


    /*
     * Some sources for understanding:
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

        //for reference: https://www.sqlite.org/datatype3.html
        String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + MainInventoryItem.TABLE_NAME + " ("
                        + MainInventoryItem._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + MainInventoryItem.ITEM_NAME + " TEXT NOT NULL, "
                        + MainInventoryItem.ITEM_QUANTITY + " REAL DEFAULT 0, "
                        + MainInventoryItem.ITEM_DESCRIPTION + " TEXT NOT NULL, "
                        + MainInventoryItem.ITEM_ISPRIORITY + " TEXT NOT NULL, "
                        + MainInventoryItem.ITEM_ISLOW + " TEXT NOT NULL, "
                        + MainInventoryItem.ITEM_IMAGE + " BLOB, "
                        + MainInventoryItem.ITEM_LOW_THRESHOLD + " REAL DEFAULT 0);";


        //DON'T ADD ANY EXTRA COLUMNS TO THE ABOVE UNLESS YOU CAN CHANGE EVERY OTHER BIT OF CODE
        //IN THE REST OF THE APP TO REFLECT THAT

        //There can be more tables, have them down here
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);

        String SQL_RECIPE_ENTRIES =
                "CREATE TABLE " + ItemRecipes.TABLE_NAME_RECIPE + " ("
                        + ItemRecipes._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + ItemRecipes.RECIPE_NAME + " TEXT NOT NULL, "
                        + ItemRecipes.ITEM_INGREDIENTS + " TEXT NOT NULL, "
                        + ItemRecipes.ITEM_STEPS + " TEXT NOT NULL);";

        sqLiteDatabase.execSQL(SQL_RECIPE_ENTRIES);

    }


    /**
     * Auto generated: update and upgrades. Can be considered
     * for security and forwards/backwards compatibility
     * this should run, but it may and it required
     * @param sqLiteDatabase the database
     * @param i the current/old version of the database
     * @param i1 the new version of the database
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {


      sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MainInventoryItem.TABLE_NAME + ";");
      onCreate(sqLiteDatabase);

      sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ItemRecipes.TABLE_NAME_RECIPE + ";");
      onCreate(sqLiteDatabase);
    }
}
