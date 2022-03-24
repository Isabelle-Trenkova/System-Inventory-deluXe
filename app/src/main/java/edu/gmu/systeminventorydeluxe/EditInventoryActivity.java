package edu.gmu.systeminventorydeluxe;

import android.content.Intent;
import android.content.CursorLoader;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    private Button recipeButton;
    /**
     * item weight will be added in a little later
     */
    //private EditText itemWeight;
    private EditText itemDescription;
    private ImageButton productImage;

    //********************************
    /*********************************
     * Add this functionality to the activity_edit_inventory
     */
    private Integer lowItemTreshold = 0;  //show be zero unless the user says otherwise

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
        buttonHandler();
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
        recipeButton = (Button) findViewById(R.id.add_recipe_button);

        //Since activies are being used they get switched by using
        //intents, which can be nifty because he get get the data of
        //that intent and if it is null then the message the is shown
        //can change acordingly

        //"The data to operate on, such as a person record in the contacts
        // database, expressed as a Uri", from the android documentation page
        //
        //This can be expressed more simply, but for the sake of
        //readability it makes more sense this way
        inventoryItemStatus = (((Intent) getIntent()).getData());


        if (inventoryItemStatus != null) { //it is not newly made

            dynamicMessage.setText("Edit Item");
        } else {

            dynamicMessage.setText("Add Item");
        }

    }


    protected void buttonHandler() {


        recipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //preforms some action
                //show use intent to go to a new activity to get the recipe stuffs,
                //then show integrate the information back in the
                //database
            }
        });

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
