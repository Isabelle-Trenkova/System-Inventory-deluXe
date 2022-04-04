package edu.gmu.systeminventorydeluxe;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * This will be the main place inventory can viewed, keeping
 * track of the inventory itself
 */
public class InventoryMainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{

    // Note to self:
    //  Handle searching function and
    //  and image stuff
    //  -Iz

    /**
     * The grid itself
     */
    private ListView inventoryList;
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

                startActivity(intent);
            }
        });


        //add any more bottons in the inventory activity class here if need
    }




    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

}
