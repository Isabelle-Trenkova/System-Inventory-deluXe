package edu.gmu.systeminventorydeluxe;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;

import android.view.MenuItem;
import android.view.Menu;

import edu.gmu.systeminventorydeluxe.database.DatabaseContract.MainInventoryItem;

/*
 * Code in the class is partially based off of code for outside sources
 * Only the adding and removing methods
 *
 * This code was also used as a resource for the loader methods imported
 * from LoaderManager
 *
 * Code written by Michał Kołnierzak, code is licenced using a MIT licence
 * and free for commercial/private use and modifications
 *
 * https://github.com/kazdavegyms/Android-Inventory-Management-App-master
 *
 */

/**
 * EditInventoryActivity enables the user to edit their inventory by:
 *      adding new items
 *      editing existing items
 *
 * Both capabilities are accessed through InventoryMainActivity
 */
//FIXME: image stuff
public class EditInventoryActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{

    //token to denote if item does not exist and is being added or does exist and is being edited
    private static final int PRODUCT_EXISTS = 1;

    //dynamic header for edit/add page (reads "Add Item" or "Edit Item)
    private TextView dynamicMessage;

    //used to populate item fields in activity_edit_inventory_view if item exists
    private Uri inventoryItemStatus;

    //item fields
    private EditText itemDescription;
    private EditText itemName;
    private EditText itemQuantity;
    private EditText itemThreshold;

    //activity_edit_inventory_view buttons
    private Button saveButton;
    private Button increment;
    private Button decrement;
    private Button deleteButton;
    private Button recipeButton;

    //check boxes
    private CheckBox isPriority;
    private CheckBox isLow;

    private Double quantCount;

    //FIXME: add image functionality (Izzy)
    //private ImageButton productImage;


    /**
     * Runs upon each new instance of EditInventoryActivity
     *
     * @param savedInstanceState previous state of this activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_inventory_view);

        define(); //define local variables and activity_edit_inventory_view buttons
        buttonHandler(); //activate activity_edit_inventory_view buttons
    }

    /**
     * Defines buttons and text fields in activity_edit_inventory_view
     */
    protected void define() {

        //dynamic header
        dynamicMessage = (TextView) findViewById(R.id.messageView);

        //item fields
        itemName = (EditText) findViewById(R.id.name_item);
        itemQuantity = (EditText) findViewById(R.id.quantity_item);
        itemDescription = (EditText) findViewById(R.id.decription_item);
        itemThreshold = (EditText) findViewById(R.id.threshold_item);

        //buttons
        saveButton = (Button) findViewById(R.id.save_button);
        deleteButton = (Button) findViewById(R.id.delete_button);
        increment = (Button) findViewById(R.id.increment_button);
        decrement = (Button) findViewById(R.id.decrement_button);
        recipeButton = (Button) findViewById(R.id.add_recipe_button);


        //FIXME: image button here (Izzy)
        //productImage = (ImageButton) findViewById(R.id.product_image);

        //checkboxes
        isPriority = (CheckBox) findViewById(R.id.is_priority);
        isLow = (CheckBox) findViewById(R.id.is_low_stock);

        //////////////////////////////
        //DON'T TOUCH THIS please
        isLow.setVisibility(View.GONE);
        //////////////////////////////

        //check if item has already been made and set header accordingly
        Intent intent = getIntent();
        inventoryItemStatus = (intent.getData());

        if (inventoryItemStatus != null) { //item is not newly made

            dynamicMessage.setText("Edit Item");

            //load previously recorded item data if item already exists
            getLoaderManager().initLoader(PRODUCT_EXISTS, null, this);

        } else {

            dynamicMessage.setText("Add Item");
        }
    }

    /**
     * Activates all activity_edit_inventory_view buttons.
     */
    protected void buttonHandler() {

        recipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(EditInventoryActivity.this, EditRecipeActivity.class);

                startActivity(intent);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //when clicked, check if all fields filled out, then save
                boolean completed;

                //check if all fields are filled out
                completed = checkEmptyFields();

                if (completed) {

                    switchLowCheckbox();
                    saveProduct();

                    finish();
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //when clicked, confirm user really wants to delete, then delete
                showDeleteConfirmationDialog();
            }
        });

        increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String quantString = itemQuantity.getText().toString().trim();

                if (TextUtils.isEmpty(quantString)) {

                    quantCount = 1.0;

                    itemQuantity.setText(Double.toString(quantCount));
                }
                else {

                    quantCount = Double.parseDouble(itemQuantity.getText().toString());
                    quantCount = incrementQuantity(quantCount);

                    itemQuantity.setText(Double.toString(quantCount));
                }

            }
        });

        decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String quantString = itemQuantity.getText().toString().trim();

                if (TextUtils.isEmpty(quantString)) {

                    Toast.makeText(EditInventoryActivity.this, getString(R.string.neg_string),
                            Toast.LENGTH_LONG).show();
                }
                else {

                    quantCount = Double.parseDouble(itemQuantity.getText().toString());

                    if (quantCount > 0.0) {

                        quantCount = decrementQuantity(quantCount);
                        itemQuantity.setText(Double.toString(quantCount));
                    }
                    else {

                        Toast.makeText(EditInventoryActivity.this, getString(R.string.neg_string),
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }


    //FIXME: THIS FEELS SO DIRTY AND WRONG (Izzy to Carolyn)
    private Double decrementQuantity(Double itemQuantity) {

        itemQuantity -= 1;

        return itemQuantity;
    }

    //FIXME: THIS FEELS SO DIRTY AND WRONG (Izzy to Carolyn)
    private Double incrementQuantity(Double itemQuantity) {

        itemQuantity += 1;

        return itemQuantity;
    }

    /**
     * Method confirms user really wants to delete item.
     * If so, method calls removeProduct() to delete item.
     */
    private void showDeleteConfirmationDialog() {

        //alert dialog sequence
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //set dialog message
        builder.setMessage(getString(R.string.remove_product));

        //"yes" option
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //yes, actually delete
                removeProduct();
            }
        });

        //"no" option
        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                //no, don't delete
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        //create and show the alert dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Method removes item from database if item is in database.
     */
    private void removeProduct() {

        if (inventoryItemStatus != null) { //item exists in database

            //delete
            int rowDeleted = getContentResolver().delete(inventoryItemStatus,
                    null, null);

            if (rowDeleted == 0) { //toast for failed deletion

                Toast.makeText(this, getString(R.string.fail_message),
                        Toast.LENGTH_LONG).show();

            } else { //toast for successful deletion

                Toast.makeText(this, getString(R.string.remove_success),
                        Toast.LENGTH_SHORT).show();
            }

            finish();

        } else { //item does not exist in database

            //toast for empty item
            Toast.makeText(this, getString(R.string.empty_product), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Method checks if there are any empty item fields.
     *
     * @return boolean True if empty fields, false if no empty fields.
     */
    boolean checkEmptyFields() {

        String productNameString = itemName.getText().toString().trim();
        String quantityString = itemQuantity.getText().toString().trim();
        String descriptionString = itemDescription.getText().toString().trim();
        String thresholdString = itemThreshold.getText().toString().trim();

        //FIXME: DO THE IMAGE STUFFS (Izzy)
        //getBytes(imageBitmap);

        if (TextUtils.isEmpty(thresholdString)){

            itemThreshold.setText(String.valueOf(0.0));
        }
        if (TextUtils.isEmpty(productNameString) || TextUtils.isEmpty(descriptionString) ||
                TextUtils.isEmpty(quantityString)) {

            Toast.makeText(this, getString(R.string.empty_field_toast), Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    /**
     * Will programmatically set the low threshold box to be checked if
     * the quantity is less than or equal to the threshold
     */
    private void switchLowCheckbox() {

        String quantityString = String.valueOf(itemQuantity.getText());
        String thresholdString = String.valueOf(itemThreshold.getText());

        Double intQuantity = Double.parseDouble(quantityString);
        Double intThreshold = Double.parseDouble(thresholdString);

        if (intQuantity <= intThreshold) {

            isLow.setChecked(true);
        }
        else {

            //This may seem pointless but this will uncheck an item automatically
            //Once the quantity is no longer less than the threshold
            //SO LEAVE IT <3
            isLow.setChecked(false);
        }

    }

    /**
     * Saves new item to database.
     * If item is new, method saves to new location. If not, method updates old location.
     */
    private void saveProduct() {

        //FIXME: CHECK IS QUANTITY IS LESS THAN THRESHOLD
        //FIXME: SWITCH CHECKED BOX ACCORDINGLY

        // collect item fields
        String stringProductName = itemName.getText().toString().trim();
        String stringQuantity = itemQuantity.getText().toString().trim();
        String stringDescription = itemDescription.getText().toString().trim();
        String stringThreshold = itemThreshold.getText().toString().trim();

        String stringPriority = new Boolean(isPriority.isChecked()).toString();
        String stringLow = new Boolean(isLow.isChecked()).toString();


        //FIXME: DO IMAGE STUFF (Izzy)
        //byte[] imageByte = getBytes(imageBitmap);

        //add item fields to single ContentValues variable
        ContentValues values = new ContentValues();
        values.put(MainInventoryItem.ITEM_NAME, stringProductName);
        values.put(MainInventoryItem.ITEM_QUANTITY, stringQuantity);
        values.put(MainInventoryItem.ITEM_DESCRIPTION, stringDescription);
        values.put(MainInventoryItem.ITEM_LOW_THRESHOLD, stringThreshold);
        values.put(MainInventoryItem.ITEM_ISPRIORITY, stringPriority);
        values.put(MainInventoryItem.ITEM_ISLOW, stringLow);

        //FIXME: add image stuff
        /*
        if (imageByte != null) {
            values.put(ProductEntry.IMAGE, imageByte);
        } */


        //checks if item is new or edited
        if (inventoryItemStatus == null) { //new item

            //create new URI for item
            Uri newUri = getContentResolver().insert(MainInventoryItem.CONTENT_URI, values);

            //check if save was successful
            if (newUri == null) {
                Toast.makeText(this, getString(R.string.fail_message), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, getString(R.string.save_successful), Toast.LENGTH_SHORT).show();
            }

        } else { //old item

            //find row index of item
            int rowsAffected = getContentResolver().update(inventoryItemStatus, values,
                    null, null);

            //check if save was successful
            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.fail_message), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, getString(R.string.save_successful), Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onBackPressed() {

        //Alert dialog sequence
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(getString(R.string.backpress));

        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //yes actually delete
                finish();
                dialog.dismiss();

            }
        });
        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                //Doesn't actually want to delete
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the Alert Dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }

    /**
     * Inflates menu view and handles searching function
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_edit_header, menu);

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

            case (R.id.delete_item):

                showDeleteConfirmationDialog();
                break;

            case (R.id.return_button):

                finish();
                break;
            case (R.id.save_item):

                //when clicked, check if all fields filled out, then save
                boolean completed;

                //check if all fields are filled out
                completed = checkEmptyFields();

                if (completed) {
                    saveProduct();
                    finish();
                }

                break;

        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Populates edit/add page from database.
     *
     * @param id ID of item row in database
     * @param args //FIXME: what is this?
     * @return CursorLoader points to item with given ID
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
                        MainInventoryItem.ITEM_ISPRIORITY,
                        MainInventoryItem.ITEM_ISLOW

                        //FIXME: add image (Izzy)
                        //MainInventoryItem.ITEM_IMAGE
                };

        return new CursorLoader(this,
                inventoryItemStatus,
                projection,
                null,
                null,
                null);
    }

    /**
     * Updates CursorLoader to point to next item in database.
     *
     * @param loader current CursorLoader
     * @param cursor data from next cursor
     */
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
                int priorityIndex = cursor.getColumnIndex(MainInventoryItem.ITEM_ISPRIORITY);
                int lowIndex = cursor.getColumnIndex(MainInventoryItem.ITEM_ISLOW);

                //FIXME: add image (Izzy)
                //int imageIndex = cursor.getColumnIndex(ProductEntry.IMAGE);


                String productNameString = cursor.getString(productNameIndex);
                Double quantityDouble = cursor.getDouble(quantityIndex);
                Double thresholdDouble = cursor.getDouble(thresholdIndex);
                String descitptionString = cursor.getString(descriptionIndex);

                String isPriorityString = cursor.getString(priorityIndex);
                String isLowString = cursor.getString(lowIndex);

                Boolean priorityBool = Boolean.parseBoolean(isPriorityString);
                Boolean lowBool = Boolean.parseBoolean(isLowString);

                //FIXME: add image (Izzy)
                /*byte[] b = cursor.getBlob(imageIndex);

                if (b == null) {
                    productImageView.setImageResource(R.drawable.no_image);
                } else {
                    Bitmap image = BitmapFactory.decodeByteArray(b, 0, b.length);
                    productImageView.setImageBitmap(image);
                }*/

                itemName.setText(productNameString);
                itemQuantity.setText(String.valueOf(quantityDouble));
                itemThreshold.setText(String.valueOf(thresholdDouble));
                itemDescription.setText(descitptionString);

                isPriority.setChecked(priorityBool);
                isLow.setChecked(lowBool);
            }
            while (cursor.moveToNext());
        }
    }

    /**
     * Sets CursorLoader to null after edit/add page populated from database
     *
     * @param loader current CursorLoader
     */
    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        //FIXME: add image (Izzy)
        //productImageView.setImageResource(R.drawable.no_image);
        itemName.setText("");
        itemQuantity.setText("");
        itemThreshold.setText("");
        itemDescription.setText("");

        isPriority.setChecked(false);
        isLow.setChecked(false);
    }
}
