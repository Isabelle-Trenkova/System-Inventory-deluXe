package edu.gmu.systeminventorydeluxe;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;

import android.view.MenuItem;
import android.view.Menu;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

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
    //To denote which permission to ask for
    private static final int PICK_FROM_GALLERY = 101;
    private static final int TAKE_PHOTO = 102;


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
    private ImageButton addImageButton;

    //check boxes
    private CheckBox isPriority;
    private CheckBox isLow;

    //local count of the quantity, used as a temp var
    private Double quantCount;
    private Bitmap imageBitmap;

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
        addImageButton = (ImageButton) findViewById(R.id.product_image);

        //checkboxes
        isPriority = (CheckBox) findViewById(R.id.is_priority);
        isLow = (CheckBox) findViewById(R.id.is_low_stock);

        //////////////////////////////
        //DON'T TOUCH THIS please, sets visibility to be invisible
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

        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //https://stackoverflow.com/questions/39866869/how-to-ask-permission-to-access-gallery-on-android-m

                if (ActivityCompat.checkSelfPermission(EditInventoryActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(EditInventoryActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
                }
                else {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, PICK_FROM_GALLERY);
                    }

            }
        });
    }

    //https://stackoverflow.com/questions/39866869/how-to-ask-permission-to-access-gallery-on-android-m
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PICK_FROM_GALLERY:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, PICK_FROM_GALLERY);
                } else {
                    permissionsDenied();
                }
                break;
        }
    }

    private void permissionsDenied() {
        //Alert dialog sequence
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(getString(R.string.deniedPer));

        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            /*case CAMERA_REQUEST:
                if (resultCode == RESULT_OK) {
                    imageBitmap = (Bitmap) data.getExtras().get("data");
                    productImageView.setImageBitmap(imageBitmap);
                }
                break;*/
            case PICK_FROM_GALLERY:
                if (resultCode == RESULT_OK && data != null) {
                    Uri imageUri = data.getData();
                    try {
                        imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    } catch (IOException ie) {
                        ie.printStackTrace();
                    }
                    addImageButton.setImageBitmap(imageBitmap);
                }
                break;
        }
    }


    /////////////////////////////////////
    //For imcrementing and decrementing
    private Double decrementQuantity(Double itemQuantity) {

        itemQuantity -= 1;

        return itemQuantity;
    }

    private Double incrementQuantity(Double itemQuantity) {

        itemQuantity += 1;

        return itemQuantity;
    }
    //////////////////////////////////////

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

        byte[] imageByte = getBytes(imageBitmap);

        //add item fields to single ContentValues variable
        ContentValues values = new ContentValues();

        if (imageByte != null) {
            values.put(MainInventoryItem.ITEM_IMAGE, imageByte);
        }
        values.put(MainInventoryItem.ITEM_NAME, stringProductName);
        values.put(MainInventoryItem.ITEM_QUANTITY, stringQuantity);
        values.put(MainInventoryItem.ITEM_DESCRIPTION, stringDescription);
        values.put(MainInventoryItem.ITEM_LOW_THRESHOLD, stringThreshold);
        values.put(MainInventoryItem.ITEM_ISPRIORITY, stringPriority);
        values.put(MainInventoryItem.ITEM_ISLOW, stringLow);


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

    public static byte[] getBytes(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            return stream.toByteArray();
        } else {
            return null;
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
                        MainInventoryItem.ITEM_ISLOW,
                        MainInventoryItem.ITEM_IMAGE
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
                int imageIndex = cursor.getColumnIndex(MainInventoryItem.ITEM_IMAGE);


                String productNameString = cursor.getString(productNameIndex);
                Double quantityDouble = cursor.getDouble(quantityIndex);
                Double thresholdDouble = cursor.getDouble(thresholdIndex);
                String descitptionString = cursor.getString(descriptionIndex);

                String isPriorityString = cursor.getString(priorityIndex);
                String isLowString = cursor.getString(lowIndex);

                Boolean priorityBool = Boolean.parseBoolean(isPriorityString);
                Boolean lowBool = Boolean.parseBoolean(isLowString);


                byte[] b = cursor.getBlob(imageIndex);

                if (b != null) {
                    Bitmap image = BitmapFactory.decodeByteArray(b, 0, b.length);
                    addImageButton.setImageBitmap(image);
                } else {

                    addImageButton.setImageResource(R.drawable.greyimage);
                }

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

        addImageButton.setImageResource(R.drawable.greyimage);
        itemName.setText("");
        itemQuantity.setText("");
        itemThreshold.setText("");
        itemDescription.setText("");

        isPriority.setChecked(false);
        isLow.setChecked(false);
    }
}
