package edu.gmu.systeminventorydeluxe;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.content.Loader;
import android.content.CursorLoader;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import edu.gmu.systeminventorydeluxe.database.ItemInventoryContract.MainInventoryItem;

/**
 * Code in the class is partially based off of code for outside sources
 * Used for the loader methods imported from LoaderManager
 *
 * Code written by Michał Kołnierzak, code is licenced using a MIT licence
 * and free for commercial/private use and modifications
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

    //////////////////////////////////////////////////////////////////////////////////
    //PLEASE DON'T EDIT ANY IMAGE STUFF/NTS comments, IZZY WILL HANDLE LATER,I promise
    /////////////////////////////////////////////////////////////////////////////////

    // Note to self:
    //  Handle searching function and
    //  and image stuff
    //  -Iz

    private ListView inventoryList; //The list itself
    AdaptorInventoryList listAdaptor; //The adaptor to be used here
    private static final int INVENTORY_LOADER = 0;//loader, its a param of the loader manager

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
     * defines the listview as well as setting the adaptor and focus
     * * */
    private void define() {

        inventoryList = (ListView) findViewById(R.id.inventory_item_view);
        listAdaptor = new AdaptorInventoryList(this, null, 1);
        inventoryList.setAdapter(listAdaptor);
        inventoryList.setItemsCanFocus(true);
    }

    /**
     * Handles the buttons
     */
    private void buttonhandler() {

        //Floating action button
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //when the floating action button is clicked this will run
                //the action to the add/edit page to add a new item

                Intent intent = new Intent(InventoryMainActivity.this,
                        EditInventoryActivity.class);

                startActivity(intent);
            }
        });



        /**
         * Inventory list adapted cursor is clicked
         */
        inventoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(InventoryMainActivity.this, EditInventoryActivity.class);

                //updates the fact that this activity in this instance still
                //technically exists, i.e. it doesn't create a new item page
                //it only revives the previous instance
                Uri uri = ContentUris.withAppendedId(MainInventoryItem.CONTENT_URI, l);
                intent.setData(uri);

                startActivity(intent);
            }
        });

        //loader manager
        getLoaderManager().initLoader(INVENTORY_LOADER,null,this);

        //add any more buttons in the inventory activity class here if need
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
