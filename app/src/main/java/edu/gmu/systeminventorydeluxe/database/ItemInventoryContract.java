package edu.gmu.systeminventorydeluxe.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class ItemInventoryContract {

    /**
     * Some sources:
     *     //https://developer.android.com/training/data-storage/sqlite
     *     //https://developer.android.com/reference/android/database/sqlite/SQLiteDatabase
     *     //https://riptutorial.com/android/example/9221/create-a-contract--helper-and-provider-for-sqlite-in-android
     *     //https://developer.android.com/reference/android/database/sqlite/SQLiteOpenHelper
     *     //https://www.youtube.com/watch?v=hDSVInZ2JCs&t=1628s
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
     */
    public static final String PATH_MAININVENTORY = "maininventory";

    //default constructor
    public ItemInventoryContract() {

    }

    //For the main InventoryItems
    //This class will define all values to go into the table

    /**
     * This class will define all values to go into the table
     * for main inventory
     */
    public static class InventoryItem implements BaseColumns {


        /**
         *
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MAININVENTORY;

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MAININVENTORY;

        /**
         * Content URI access
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MAININVENTORY);

        public static final String TABLE_NAME = "maininventory";

        //initalized all fields here as constants
    }

}
