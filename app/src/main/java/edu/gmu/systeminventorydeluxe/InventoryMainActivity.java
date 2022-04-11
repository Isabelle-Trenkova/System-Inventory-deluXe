package edu.gmu.systeminventorydeluxe;

import android.app.LoaderManager;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import edu.gmu.systeminventorydeluxe.database.ItemInventoryContract.MainInventoryItem;

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
        LoaderManager.LoaderCallbacks<Cursor> {

    //FIXME: handle searching function, handle image stuff (Izzy)

    private ListView inventoryList; //the GUI list view itself
    AdaptorInventoryList listAdaptor; //the adaptor to be used to populate the GUI list
    private static final int INVENTORY_LOADER = 0;//loader is a param of the loader manager

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
    }

    /**
     * Instantiates and activates activity_inventory_main_view buttons
     */
    private void buttonHandler() {

        //floating action button
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
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
                MainInventoryItem.ITEM_QUANTITY

                //FIXME: add photo (Izzy)
                //MainInventoryItem.ITEM_PHOTO
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
