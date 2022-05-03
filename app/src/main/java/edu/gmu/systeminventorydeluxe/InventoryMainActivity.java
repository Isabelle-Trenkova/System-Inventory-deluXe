package edu.gmu.systeminventorydeluxe;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.content.Loader;
import android.content.CursorLoader;
import android.view.MenuItem;
import android.view.Menu;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import edu.gmu.systeminventorydeluxe.database.DatabaseContract.MainInventoryItem;

/*
 * Code in the class is partially based off of code for outside sources
 * Used for the loader methods imported from LoaderManager
 *
 * Code written by Michał Kołnierzak, code is licenced using a MIT licence
 * and free for commercial/private use and modifications
 *
 * https://github.com/kazdavegyms/Android-Inventory-Management-App-master
 *
 */

/**
 * InventoryMainActivity enables the user to dynamically view their inventory by:
 *      pulling existing data from database
 *      adapting that data to populate the activity_inventory_main_view layout
 *      tracking changes made by user to their inventory through EditInventoryActivity
 *
 * InventoryMainActivity is accessed through MainActivity
 */
public class InventoryMainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private ListView inventoryList; //the GUI list view itself
    private AdaptorInventoryList listAdaptor; //the adaptor to be used to populate the GUI list

    private static final int INVENTORY_LOADER = 0;//loader is a param of the loader manager

    private FloatingActionButton fab;

    /**
     * Runs upon each new instance of InventoryMainActivity
     *
     * @param savedInstanceState previous state of this activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_main_view);

        define(); //define local variables
        buttonHandler(); //instantiate & activate activity_inventory_main_view buttons
    }

    /**
     * Defines tools to populate inventory list:
     *      ListView from activity_inventory_main_view - displays inventory in GUI
     *      AdaptorInventoryList object - adapts data from database to fit GUI
     *
     * Sets ListView properties
     */
    private void define() {

        //define local variables
        inventoryList = (ListView) findViewById(R.id.inventory_item_view);
        listAdaptor = new AdaptorInventoryList(this, null, 1);

        //set ListView properties
        inventoryList.setAdapter(listAdaptor);
        inventoryList.setItemsCanFocus(true);

        fab = findViewById(R.id.floatingActionButton);
    }

    /**
     * Instantiates and activates activity_inventory_main_view buttons
     */
    private void buttonHandler() {

        //floating action button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //when clicked, floating action button starts EditInventoryActivity to add new item
                Intent intent = new Intent(InventoryMainActivity.this,
                        EditInventoryActivity.class);

                //start new activity (EditInventoryActivity)
                startActivity(intent);
            }
        });

        //clickable ListView
        inventoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //when item in ListView is clicked, starts EditInventoryActivity to edit that item
                Intent intent = new Intent(InventoryMainActivity.this, EditInventoryActivity.class);

                //populates item edit page (activity_edit_inventory_view) with existing item data
                Uri uri = ContentUris.withAppendedId(MainInventoryItem.CONTENT_URI, l);
                intent.setData(uri);

                //start new activity (EditInventoryActivity)
                startActivity(intent);
            }
        });

        //loader manager
        getLoaderManager().initLoader(INVENTORY_LOADER,null,this);

        //add any more buttons in the inventory activity class here if needed
    }

    /**
     * Returns a cursor from a database
     * @param string
     * @return
     */
    private Cursor cursorReturner(String string) {

        string = string.toLowerCase();

        ContentResolver contentResolver = getContentResolver();

        String[] tableColumns = {MainInventoryItem._ID,
                MainInventoryItem.ITEM_NAME,
                MainInventoryItem.ITEM_QUANTITY,
                MainInventoryItem.ITEM_IMAGE};

        //public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
        //                        @Nullable String selection, @Nullable String[] selectionArgs,
        //                        @Nullable String sortOrder) {
        Cursor likeItems = contentResolver.query(MainInventoryItem.CONTENT_URI, tableColumns,
                MainInventoryItem.ITEM_NAME + " Like ?",
                new String[] {"%"+string+"%"}, null);

        return likeItems;
    }

    /**
     * Inflates menu view and handles searching function
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_inventory_main, menu);

        MenuItem searchButton = menu.findItem(R.id.app_bar_search);
        SearchView searchview = (SearchView) MenuItemCompat.getActionView(searchButton);

        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            //search submitted from keyboard
            @Override
            public boolean onQueryTextSubmit(String s) {

                Cursor likeItems = cursorReturner(s);

                if (likeItems == null ) {

                    Toast.makeText(InventoryMainActivity.this, "Not found, try again!", Toast.LENGTH_LONG).show();
                }
                else {

                    AdaptorInventoryList searchAdapter = new AdaptorInventoryList(InventoryMainActivity.this, likeItems, 1);

                    inventoryList.setAdapter(searchAdapter);
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //FIXME: Only if you want search suggestions
                return false;
            }
        });

        //refreshes page when searching is ended
        searchview.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                finish();
                startActivity(getIntent());

                return true;
            }
        });

        return true;
    }

    /**
     * Operations running from menu bar
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            //will go to the add item page
            case R.id.add_an_item:

                Intent intent = new Intent(InventoryMainActivity.this, EditInventoryActivity.class);

                startActivity(intent);

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Populates ListView from database.
     *
     * @param id ID of item row in database
     * @param args //FIXME: what is this?
     * @return CursorLoader points to item with given ID
     */
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        String[] projection = {
                MainInventoryItem._ID,
                MainInventoryItem.ITEM_NAME,
                MainInventoryItem.ITEM_QUANTITY,
                MainInventoryItem.ITEM_IMAGE
        };

        return new CursorLoader(this,
                MainInventoryItem.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    /**
     * Updates CursorLoader to point to next item in database.
     *
     * @param loader current CursorLoader
     * @param data data from next cursor
     */
    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

        listAdaptor.swapCursor(data);
    }

    /**
     * Sets CursorLoader to null after ListView populated from database
     *
     * @param loader current CursorLoader
     */
    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        listAdaptor.swapCursor(null);
    }
}
