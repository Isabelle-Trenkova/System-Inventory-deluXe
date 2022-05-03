package edu.gmu.systeminventorydeluxe.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/*
 * Code in the class is based off of code for outside sources:
 *
 * ///////////////////////////////////////////////////////////////////////////
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
 * additionally
 *
 *
 * The following source used is licenses under a  CC BY-SA 3.0 license and is
 * free for use and adaption
 *
 * https://riptutorial.com/android/example/9221/create-a-contract--helper-and-provider-for-sqlite-in-android
 *
 *
 */

//////////////////////////////////////////////////////////////////////////////////
//PLEASE DON'T EDIT ANY IMAGE STUFF/NTS comments, IZZY WILL HANDLE LATER,I promise
/////////////////////////////////////////////////////////////////////////////////

//FIXME: image stuff
public class DatabaseContract {

    /*
     * Some resources:
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


    /**
     * Possible points of contact, this just prevents
     * improper paths invoking an inner class
     *
     * Maininventory is the path where all items show up
     */
    public static final String PATH_MAININVENTORY = "mainInventory";
    public static final String PATH_RECIPEITEM = "recipeItem";


    //default constructor
    public DatabaseContract() {

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

        //initialized all fields here as constants

        public final static String _ID = BaseColumns._ID;

        public final static String ITEM_NAME = "name";

        public final static String ITEM_QUANTITY = "quantity";

        public final static String ITEM_DESCRIPTION = "description";

        public final static String ITEM_IMAGE = "imageuri";

        public final static String ITEM_LOW_THRESHOLD = "threshold";

        public final static String ITEM_ISPRIORITY = "isPriority";

        public final static String ITEM_ISLOW = "isLow";

        /*public final static String ITEM_IMAGE = "image"*/;

        //add more constants/information for the database here
        //remember to adjust the specific helper and provider classes as well
        //and all other code to reflect the change
    }

    //DON'T ADD ANY EXTRA COLUMNS TO THE ABOVE UNLESS YOU CAN CHANGE EVERY OTHER BIT OF CODE
    //IN THE REST OF THE APP TO REFLECT THAT

    //There can be more inner classes, have them down here

    public static class ItemRecipes implements BaseColumns{



        public final static Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_RECIPEITEM);

        /**
         * This is the Android platform's base MIME type for a content: URI containing a Cursor of a single item.
         */
        public final static String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RECIPEITEM;

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RECIPEITEM;


        public final static String TABLE_NAME_RECIPE = "itemRecipe";

        //initialized all fields here as constants

        public final static String _ID = BaseColumns._ID;

        public final static String RECIPE_NAME = "recipe";

        public final static String ITEM_INGREDIENTS = "ingredients";

        public final static String ITEM_STEPS = "steps";

    }
}
