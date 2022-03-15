package edu.gmu.systeminventorydeluxe;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

/**
 * This will be the class where items are actually edited
 * From any action that will edit an item all activity should be redirected
 * here and to the Activity_edit_inventory to actually edit an item
 */
public class EditInventoryActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private TextView dynamicMessage;
    private EditText nameItem;
    private EditText itemQuantity;
    /**
     * item weight will be added in a little later
     */
    //private EditText itemWeight;
    private EditText itemDescription;

    /**
     * Upon new instnace;
     * @param savedInstanceState a new instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_inventoryView);

        define();
    }


    /**
     * Defines class fields
     */
    protected void define() {

        //have a dynamic way to set text, figure out

    }


    /**
     * Automatically generated loader methods
     * this will allow user defined objects to pop up
     * in the grid view
     */

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
