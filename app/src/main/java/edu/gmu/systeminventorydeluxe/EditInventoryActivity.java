package edu.gmu.systeminventorydeluxe;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import edu.gmu.systeminventorydeluxe.database.ItemInventoryContract.MainInventoryItem;

/**
 * Code in the class is paritally based off of code for outside sources
 * Only the adding and removing methods
 * This code was also used as a resource for the loader methods imported
 * from LoaderManager
 *
 * Code written by Michał Kołnierzak, code is licenced using a MIT licence
 * and free for commerical/private use and modifications
 *
 * https://github.com/kazdavegyms/Android-Inventory-Management-App-master
 *
 *
 */

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
public class EditInventoryActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private TextView dynamicMessage;
    private EditText nameItem;
    private EditText itemQuantity;
    private EditText lowItemEditText;

    //private Button recipeButton;

    private Button saveButton;
    private Button increment;
    private Button decrement;
    private Button deleteButton;
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
        lowItemEditText = (EditText) findViewById(R.id.threshold_item);


        //productImage = (ImageButton) findViewById(R.id.product_image);

        //recipeButton = (Button) findViewById(R.id.add_recipe_button);

        saveButton = (Button) findViewById(R.id.save_button);
        deleteButton = (Button) findViewById(R.id.delete_button);
        increment = (Button) findViewById(R.id.increment_button);
        decrement = (Button) findViewById(R.id.decrement_button);


        //Since activies are being used they get switched by using
        //intents, which can be nifty because he get get the data of
        //that intent and if it is null then the message the is shown
        //can change acordingly

        //"The data to operate on, such as a person record in the contacts
        // database, expressed as a Uri", from the android documentation page
        //
        //This can be expressed more simply, but for the sake of
        //readability it makes more sense this way


        Intent intent = getIntent();
        inventoryItemStatus = (intent.getData());


        if (inventoryItemStatus != null) { //it is not newly made

            dynamicMessage.setText("Edit Item");
        } else {

            dynamicMessage.setText("Add Item");
        }

    }


    protected void buttonHandler() {

    /*
        recipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //preforms some action
                //show use intent to go to a new activity to get the recipe stuffs,
                //then show integrate the information back in the
                //database
            }
        });

     */
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean completed;

                completed = checkandsave();

                if (completed != true) {

                    //probably throw an exception here
                }

            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDeleteConfirmationDialog();
            }
        });


        increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    private void showDeleteConfirmationDialog() {
        // Create an Alert Dialog set the messages and click listeners
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.remove_product));
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User confirmed removing the product, so remove the product.
                removeProduct();
            }
        });
        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // // User canceled removing the product, so dismiss the dialog
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        // Create and show the Alert Dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void removeProduct() {
        if (inventoryItemStatus != null) {
            int rowDeleted = getContentResolver().delete(inventoryItemStatus, null, null);
            // Show a toast message depending on whether or not the removal was successful
            if (rowDeleted == 0) {
                // If no data was removed, then show the error message
                Toast.makeText(this, getString(R.string.remove_failed), Toast.LENGTH_LONG).show();
            } else {
                // If data was successfully removed, display a toast.
                Toast.makeText(this, getString(R.string.remove_success),
                        Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }


    boolean checkandsave() {

        String productNameString = nameItem.getText().toString().trim();
        String quantityString = itemQuantity.getText().toString().trim();
        String descriptionString = itemDescription.getText().toString().trim();
        String thresholdString = lowItemEditText.getText().toString().trim();

        //DO THE IMAGE STUFFS
        //getBytes(imageBitmap);

        if (TextUtils.isEmpty(productNameString) || TextUtils.isEmpty(descriptionString) || TextUtils.isEmpty(quantityString) || TextUtils.isEmpty(thresholdString)) {
            Toast.makeText(this, getString(R.string.empty_field_toast), Toast.LENGTH_LONG).show();
        }
         else {
            saveProduct();
            finish();

        }
        return true;
    }

    private void saveProduct() {
        String stringProductName = nameItem.getText().toString().trim();
        String stringQuantity = itemQuantity.getText().toString().trim();
        String stringDescription = itemDescription.getText().toString().trim();
        String stringThreshold = lowItemEditText.getText().toString().trim();

        //DO IMAGE STUFF
        //byte[] imageByte = getBytes(imageBitmap);
        if (inventoryItemStatus == null &&
                TextUtils.isEmpty(stringProductName)
                && TextUtils.isEmpty(stringDescription)
                && TextUtils.isEmpty(stringQuantity)
                && TextUtils.isEmpty(stringThreshold)) {

            return;
        }

        ContentValues values = new ContentValues();
        values.put(MainInventoryItem.ITEM_NAME, stringProductName);
        values.put(MainInventoryItem.ITEM_QUANTITY, stringQuantity);
        values.put(MainInventoryItem.ITEM_DESCRIPTION, stringDescription);
        values.put(MainInventoryItem.ITEM_LOW_THRESHOLD, stringThreshold);

        /*
        if (imageByte != null) {
            values.put(ProductEntry.IMAGE, imageByte);
        } */

        if (inventoryItemStatus == null) {
            Uri newUri = getContentResolver().insert(MainInventoryItem.CONTENT_URI, values);
            if (newUri == null) {
                Toast.makeText(this, getString(R.string.insert_failed), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, getString(R.string.insert_success), Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowsAffected = getContentResolver().update(inventoryItemStatus, values, null, null);
            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.update_failed), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, getString(R.string.update_success), Toast.LENGTH_SHORT).show();
            }
        }


        return;
    }



    /**
     * Automatically generated loader methods
     * this will allow user defined objects to pop up
     * in the grid view
     */


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        String[] projection =
                {
                        MainInventoryItem._ID,
                        MainInventoryItem.ITEM_NAME,
                        MainInventoryItem.ITEM_QUANTITY,
                        MainInventoryItem.ITEM_DESCRIPTION,
                        MainInventoryItem.ITEM_LOW_THRESHOLD,
                        //MainInventoryItem.ITEM_IMAGE
                };


        return new CursorLoader(this,
                inventoryItemStatus,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {

        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        if (cursor.moveToFirst()) {
            do {
                int productNameIndex = cursor.getColumnIndex(MainInventoryItem.ITEM_NAME);
                int quantityIndex = cursor.getColumnIndex(MainInventoryItem.ITEM_QUANTITY);
                int descriptionIndex = cursor.getColumnIndex(MainInventoryItem.ITEM_DESCRIPTION);
                int thresholdIndex = cursor.getColumnIndex(MainInventoryItem.ITEM_LOW_THRESHOLD);

                /*int imageIndex = cursor.getColumnIndex(ProductEntry.IMAGE);*/


                String productNameString = cursor.getString(productNameIndex);
                int quantityInteger = cursor.getInt(quantityIndex);
                int thresholdInteger = cursor.getInt(thresholdIndex);
                String descitptionString = cursor.getString(descriptionIndex);

                /*byte[] b = cursor.getBlob(imageIndex);

                if (b == null) {
                    productImageView.setImageResource(R.drawable.no_image);
                } else {
                    Bitmap image = BitmapFactory.decodeByteArray(b, 0, b.length);
                    productImageView.setImageBitmap(image);
                }*/

                nameItem.setText(productNameString);
                itemQuantity.setText(String.valueOf(quantityInteger));
                lowItemEditText.setText(String.valueOf(thresholdInteger));
                itemDescription.setText(descitptionString);
            }
            while (cursor.moveToNext());
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {


        /*productImageView.setImageResource(R.drawable.no_image);*/
        nameItem.setText("");
        itemQuantity.setText("");
        lowItemEditText.setText("");
        itemDescription.setText("");
    }


}
