package edu.gmu.systeminventorydeluxe;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

/**
 * This will be the class where items are actually edited AND ADDED
 *
 * There will be a dynamic message stating that if the item is new
 * as in the user is about to add it, then an add message will be displayed
 * otherwise an edit message will be displayed.
 *
 * From any action that will edit an item all activity should be redirected
 * here and to the Activity_edit_inventory to actually edit an item
 */
public class EditInventoryActivity extends AppCompatActivity /*implements
        LoaderManager.LoaderCallbacks<Cursor>*/{

    private TextView dynamicMessage;
    private EditText nameItem;
    private EditText itemQuantity;
    /**
     * item weight will be added in a little later
     */
    //private EditText itemWeight;
    private EditText itemDescription;
    private ImageButton productImage;

    //add recipe field right here and don't forget to include it in the
    //define() method
    //add functionality later on

    //using intent class you can see if there had been any modifications
    //more specifically getIntent(); if it is null then it is a new product

    //NTS: see if this can be private later
    public Uri inventoryItemStatus;


    /**
     * Upon new instnace;
     * @param savedInstanceState a new instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_inventory_view);

        define();
    }

    /**
     * Defines class fields
     */
    protected void define() {

        dynamicMessage = (TextView) findViewById(R.id.messageView);
        nameItem = (EditText) findViewById(R.id.name_item);
        itemQuantity = (EditText) findViewById(R.id.quantity_item);
        itemDescription = (EditText) findViewById(R.id.decription_item);
        productImage = (ImageButton) findViewById(R.id.product_image);

        inventoryItemStatus = ((getIntent()).getData());

        if (inventoryItemStatus != null) {

            dynamicMessage.setText("Edit Item");
        } else {

            dynamicMessage.setText("Add Item");
        }

    }


    /**
     * Automatically generated loader methods
     * this will allow user defined objects to pop up
     * in the grid view
     */

    /*
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
    */
}
