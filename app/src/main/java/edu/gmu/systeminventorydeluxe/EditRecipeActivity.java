package edu.gmu.systeminventorydeluxe;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import edu.gmu.systeminventorydeluxe.database.ItemInventoryContract.ItemRecipes;

public class EditRecipeActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private final static int RECIPE_EXISTS = 1;

    private Button saveButton;
    private Button deleteButton;

    private EditText ingredients;
    private EditText steps;
    private EditText nameRecipe;

    private TextView dynamicMessage;

    private Uri recipeStatus;


    /**
     * Upon new instnace;
     * @param savedInstanceState a new instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);

        define();
        buttonHandler();
    }

    private void define() {

        saveButton = (Button) findViewById(R.id.save_button_recp);
        deleteButton = (Button) findViewById(R.id.delete_button_recp);

        ingredients = (EditText) findViewById(R.id.ingridients_list);
        steps = (EditText) findViewById(R.id.stepsList);
        nameRecipe = (EditText) findViewById(R.id.name_of_recipe);


        dynamicMessage = (TextView) findViewById(R.id.greetingMessage);

        Intent intent = getIntent();
        recipeStatus = (intent.getData());

        if (recipeStatus != null) { //it is not newly made

            dynamicMessage.setText("Edit Recipe");

            //loads previous information of the item if the
            //item has existed
            getLoaderManager().initLoader(RECIPE_EXISTS, null, this);

        } else {

            dynamicMessage.setText("Add Recipe");

        }
    }

    private void buttonHandler() {

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean completed = check();

                if (completed) {

                    saveProduct();
                    finish();
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDeleteConfirmationDialog();
            }
        });

    }

    private void showDeleteConfirmationDialog() {

        //Alert dialog sequence
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(getString(R.string.remove_recipe));

        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //yes actually delete
                removeProduct();
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


    private void removeProduct() {

        if (recipeStatus != null) {

            int rowDeleted = getContentResolver().delete(recipeStatus, null, null);
            // Show a toast message depending on whether or not the removal was successful

            if (rowDeleted == 0) {
                // If no data was removed, then show the error message
                Toast.makeText(this, getString(R.string.fail_message), Toast.LENGTH_LONG).show();
            } else {
                // If data was successfully removed, display a toast.
                Toast.makeText(this, getString(R.string.remove_success),
                        Toast.LENGTH_SHORT).show();
            }

            finish();

        } else { //recipe does not exist in database

            //toast for empty recipe
            Toast.makeText(this, getString(R.string.empty_product), Toast.LENGTH_LONG).show();
        }
    }


    boolean check() {

        String ingredientsString = ingredients.getText().toString().trim();
        String stepsString = steps.getText().toString().trim();
        String nameString = nameRecipe.getText().toString().trim();

        if (TextUtils.isEmpty(ingredientsString) || TextUtils.isEmpty(stepsString) ||
                TextUtils.isEmpty(nameString)) {
            Toast.makeText(this, getString(R.string.empty_field_toast), Toast.LENGTH_LONG).show();

            return false;
        }

        return true;
    }

    private void saveProduct() {

        String ingredientsString = ingredients.getText().toString().trim();
        String stepsString = steps.getText().toString().trim();
        String nameString = nameRecipe.getText().toString().trim();

        if (recipeStatus == null &&
                TextUtils.isEmpty(ingredientsString)
                && TextUtils.isEmpty(stepsString)
                && TextUtils.isEmpty(nameString)) {

            return;
        }

        ContentValues values = new ContentValues();
        values.put(ItemRecipes.ITEM_INGREDIENTS, ingredientsString);
        values.put(ItemRecipes.ITEM_STEPS, stepsString);
        values.put(ItemRecipes.RECIPE_NAME, nameString);

        if (recipeStatus == null) {
            Uri newUri = getContentResolver().insert(ItemRecipes.CONTENT_URI, values);
            if (newUri == null) {
                Toast.makeText(this, getString(R.string.fail_message), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, getString(R.string.save_successful), Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowsAffected = getContentResolver().update(recipeStatus, values, null, null);
            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.fail_message), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, getString(R.string.save_successful), Toast.LENGTH_SHORT).show();
            }
        }

        return;
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
     * Populates ListView from database.
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
                        ItemRecipes._ID,
                        ItemRecipes.RECIPE_NAME,
                        ItemRecipes.ITEM_INGREDIENTS,
                        ItemRecipes.ITEM_STEPS


                };

        return new CursorLoader(this,
                recipeStatus,
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
                int recipeNameIndex = cursor.getColumnIndex(ItemRecipes.RECIPE_NAME);
                int stepsIndex = cursor.getColumnIndex(ItemRecipes.ITEM_STEPS);
                int ingredientsIndex = cursor.getColumnIndex(ItemRecipes.ITEM_INGREDIENTS);


                String productNameString = cursor.getString(recipeNameIndex);
                String stepsString = cursor.getString(stepsIndex);
                String ingredientsString = cursor.getString(ingredientsIndex);

                nameRecipe.setText(productNameString);
                ingredients.setText(ingredientsString);
                steps.setText(stepsString);
            }
            while (cursor.moveToNext());
        }
    }

    /**
     * Sets CursorLoader to null after ListView populated from database
     *
     * @param loader current CursorLoader
     */
    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        nameRecipe.setText("");
        ingredients.setText("");
        steps.setText("");;
    }
}
