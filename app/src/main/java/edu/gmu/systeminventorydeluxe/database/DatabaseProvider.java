package edu.gmu.systeminventorydeluxe.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import edu.gmu.systeminventorydeluxe.database.DatabaseContract.MainInventoryItem;
import edu.gmu.systeminventorydeluxe.database.DatabaseContract.ItemRecipes;

/*
 * Code in the class is based off of code from outside sources
 *
 *  This class is almost entirely from this source. But adapted to
 *  be used with the ItemInventoryContract.java and DatabaseHelper.java
 *  which was created previously using the other sources in the file.
 *
 * Code written by Michał Kołnierzak, code is licenced using a MIT licence
 * and free for commercial/private use and modifications
 *
 * https://github.com/kazdavegyms/Android-Inventory-Management-App-master
 *
 *
 */

//////////////////////////////////////////////////////////////////////////////////
//PLEASE DON'T EDIT ANY IMAGE STUFF/NTS comments, IZZY WILL HANDLE LATER,I promise
/////////////////////////////////////////////////////////////////////////////////

    //Additionally, if you want more than 1 table, there will have to be
    //stark differences here are switch statement control

public class DatabaseProvider extends ContentProvider {


    public static final String LOG_TAG = DatabaseProvider.class.getSimpleName();

    //All ids must be unique otherwise a runtime exceptions will occur
    private static final int INVENTORY = 100;
    private static final int INVENTORY_ID = 101;

    private static final int RECIPE = 200;
    private static final int RECIPE_ID = 201;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY,
                DatabaseContract.PATH_MAININVENTORY, INVENTORY);

        sUriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY,
                DatabaseContract.PATH_MAININVENTORY + "/#", INVENTORY_ID);

        sUriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY,
                DatabaseContract.PATH_RECIPEITEM, RECIPE);

        sUriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY,
                DatabaseContract.PATH_RECIPEITEM + "/#", RECIPE_ID);
    }

    private DatabaseHelper databaseHelper;
    ///////////////////////////////////////////////////////////////////////

    /**
     * Will attempt to create a database
     * @return true if created, false if it fails for whatever reason
     */
    @Override
    public boolean onCreate() {

        try {

            databaseHelper = new DatabaseHelper(getContext());
        } catch (Exception e) {

            return false;
        }

        return true;
    }

    //////////////////////////////////////////////////////////////////////

    /**
     * Gets information from the database and returns that information as a cursor
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return cursor
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case INVENTORY:
                cursor = db.query(MainInventoryItem.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case INVENTORY_ID:
                selection = MainInventoryItem._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(MainInventoryItem.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case RECIPE:
                cursor = db.query(ItemRecipes.TABLE_NAME_RECIPE, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case RECIPE_ID:
                selection = ItemRecipes._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(ItemRecipes.TABLE_NAME_RECIPE, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot Resolve URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;

    }

    //////////////////////////////////////////////////////////////////////

    /**
     * Gets the type of the URI
     * @param uri
     * @return The types
     */
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case INVENTORY:
                return MainInventoryItem.CONTENT_LIST_TYPE;
            case INVENTORY_ID:
                return MainInventoryItem.CONTENT_ITEM_TYPE;
            case RECIPE:
                return ItemRecipes.CONTENT_LIST_TYPE;
            case RECIPE_ID:
                return ItemRecipes.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + "with match " + match);
        }
    }

    //////////////////////////////////////////////////////////////////

    /**
     * Inserts information into a new row
     * an exception will be thrown if it cannot be completed
     * @param uri
     * @param values
     * @return
     */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case INVENTORY:
                return insertProduct(uri, values);
            case RECIPE:
                return insertRecipe(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }


    //////////////////////////////////////////////////////////////////////////

    /**
     *  Inserting the product
     * @param uri
     * @param values
     * @return
     */
    private Uri insertProduct(Uri uri, ContentValues values) {

        /*FIGURE OUT IMAGE STUFF - NTS 4 IZZ*/

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long id = db.insert(MainInventoryItem.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    /**
     *  Inserting the recipekk
     * @param uri
     * @param values
     * @return
     */
    private Uri insertRecipe(Uri uri, ContentValues values) {


        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long id = db.insert(ItemRecipes.TABLE_NAME_RECIPE, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    ////////////////////////////////////////////////////////////

    /**
     * Deletes either a row or one piece of informaiton
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return
     */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int rowsDeleted;
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case INVENTORY:
                rowsDeleted = db.delete(MainInventoryItem.TABLE_NAME, selection, selectionArgs);
                break;
            case INVENTORY_ID:
                selection = MainInventoryItem._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = db.delete(MainInventoryItem.TABLE_NAME, selection, selectionArgs);
                break;
            case RECIPE:
                rowsDeleted = db.delete(ItemRecipes.TABLE_NAME_RECIPE, selection, selectionArgs);
                break;
            case RECIPE_ID:
                selection = ItemRecipes._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = db.delete(ItemRecipes.TABLE_NAME_RECIPE, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri " + uri + "with match " + match);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }


    ////////////////////////////////////////////////////////////

    /**
     * Updates either the entire thing or one piece of the information in the table
     * @param uri
     * @param values
     * @param selection
     * @param selectionArgs
     * @return
     */
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case INVENTORY:
                return updateInventory(uri, values, selection, selectionArgs);
            case INVENTORY_ID:
                selection = MainInventoryItem._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateInventory(uri, values, selection, selectionArgs);
            case RECIPE:
                return updateRecipes(uri, values, selection, selectionArgs);
            case RECIPE_ID:
                selection = ItemRecipes._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateRecipes(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    ///////////////////////////////////////////////////////////////

    /**
     * Updates individual lines in the item
     * @param uri
     * @param values
     * @param selection
     * @param selectionArgs
     * @return
     */
    private int updateInventory(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int rowsUpdated = db.update(MainInventoryItem.TABLE_NAME, values, selection, selectionArgs);
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }


    /**
     * Updates individual lines in the item
     * @param uri
     * @param values
     * @param selection
     * @param selectionArgs
     * @return
     */
    private int updateRecipes(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int rowsUpdated = db.update(ItemRecipes.TABLE_NAME_RECIPE, values, selection, selectionArgs);
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }
}
