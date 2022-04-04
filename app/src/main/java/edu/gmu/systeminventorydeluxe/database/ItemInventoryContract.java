package edu.gmu.systeminventorydeluxe.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;


/**
 * Code in the class is based off of code for outside sources
 *
 *
 * Code written by Michał Kołnierzak, code is licenced using a MIT licence
 * and free for commerical/private use and modifications
 *
 * https://github.com/kazdavegyms/Android-Inventory-Management-App-master
 *
 *
 */

public class ItemInventoryContract {

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
     * Sets the path for the content provider, i.e. the path in the app
     * where the database will be called
     */
    public static final String CONTENT_AUTHORITY = "edu.gmu.systeminventorydeluxe";

    /**
     * Actual point of contact with the app
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    //the path of the main inventory




    /**
     * Possible points of contact, this just prevents
     * improper paths invoking an inner class
     *
     * Maininventory is the path where all items show up
     */
    public static final String PATH_MAININVENTORY = "mainInventory";

    //default constructor
    public ItemInventoryContract() {

    }

    /**
     * This class will define all values to go into the table
     * for main inventory
     */
    public static class MainInventoryItem implements BaseColumns {

        /**
         * Content URI access this specific path, we want no where else to
         * to access this content
         */
        public final static Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MAININVENTORY);

        /**
         * This is the Android platform's base MIME type for a content: URI containing a Cursor of a single item.
         */
        public final static String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MAININVENTORY;

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MAININVENTORY;


        public final static String TABLE_NAME = "maininventory";

        //initalized all fields here as constants

        public final static String _ID = BaseColumns._ID;

        public final static String ITEM_NAME = "name";

        public final static String ITEM_QUANTITY = "quantity";

        public final static String ITEM_DESCRIPTION = "description";

        public final static String ITEM_LOW_THRESHOLD = "threshold";

        /*public final static String ITEM_IMAGE = "image"*/;

        //add more constants/information for the database here
        //remember to adjust the specific helper and provider classes as well
    }

}
