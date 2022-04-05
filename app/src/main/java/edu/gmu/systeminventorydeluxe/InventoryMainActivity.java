package edu.gmu.systeminventorydeluxe;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.content.Loader;
import android.content.CursorLoader;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import edu.gmu.systeminventorydeluxe.database.ItemInventoryContract.MainInventoryItem;

/**
 * Code in the class is paritally based off of code for outside sources
 * Used for the loader methods imported from LoaderManager
 *
 * Code written by Michał Kołnierzak, code is licenced using a MIT licence
 * and free for commerical/private use and modifications
 *
 * https://github.com/kazdavegyms/Android-Inventory-Management-App-master
 *
 *
 */

/**
 * This will be the main place inventory can viewed, keeping
 * track of the inventory itself
 */
public class InventoryMainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    // Note to self:
    //  Handle searching function and
    //  and image stuff
    //  -Iz

    /**
     * The List itself
     */
    private ListView inventoryList;
    private static final int INVENTORY_LOADER = 0;
    private AdaptorInventoryList listAdaptor;

    /**
     * potential searching function **not ready yet
     * (change the javadoc when this is finished)
     */


    /**
     * Runs upon new instance of the app
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_view);

        define();
        buttonhandler();
    }

    /**
     * defines the good stuff (gridview)
     */
    private void define() {

        inventoryList = (ListView) findViewById(R.id.inventory_item_view);
        listAdaptor = new AdaptorInventoryList(this, null, 1);
        inventoryList.setAdapter(listAdaptor);
    }

    /**
     * Handles the buttons
     */
    private void buttonhandler() {

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //when the floating action button is clicked this will un

                Intent intent = new Intent(InventoryMainActivity.this,
                        EditInventoryActivity.class);
                startActivity(intent);

            }
        });


        /**
         * Inventory grid cursor is clicked
         *
         * ***configure
         */
        inventoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(InventoryMainActivity.this, EditInventoryActivity.class);

                Uri uri = ContentUris.withAppendedId(MainInventoryItem.CONTENT_URI, id);
                intent.setData(uri);
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(INVENTORY_LOADER,null,this);

        //add any more bottons in the inventory activity class here if need
    }




    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        String[] projection = {
                MainInventoryItem._ID,
                MainInventoryItem.ITEM_NAME,
                MainInventoryItem.ITEM_QUANTITY

                /*MainInventoryItem.ITEM_PHOTO*/
        };
        return new CursorLoader(this,
                MainInventoryItem.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

        listAdaptor.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        listAdaptor.swapCursor(null);
    }

}
