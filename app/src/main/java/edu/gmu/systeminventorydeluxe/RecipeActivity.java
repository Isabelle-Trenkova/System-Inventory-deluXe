package edu.gmu.systeminventorydeluxe;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import edu.gmu.systeminventorydeluxe.database.ItemInventoryContract.ItemRecipes;

public class RecipeActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>  {


    private static final int RECIPE_LOADER = 0;

    private ListView recipesList;
    private FloatingActionButton recipeAdd;

    private AdaptorRecipeList recipeAdaptor;

    //private Uri recipeItemStatus;


    /**
     * Runs upon each new instance of RecipeActivity
     *
     * @param savedInstanceState previous state of this activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_view);

        define(); //define local variables
        buttonHandler(); //instantiate & activate activity_inventory_main_view buttons
    }

    /**
     * Defines tools to populate recipe list:
     *      ListView from activity_inventory_main_view - displays inventory in GUI
     *      AdaptorInventoryList object - adapts data from database to fit GUI
     *      add button
     *      loaderManager - loads existing recipe data
     *
     * Sets ListView properties
     */
    private void define() {

        //instantiate add button
        recipeAdd = findViewById(R.id.floatingActionButtonRecipe);

        //set ListView properties
        recipesList = (ListView) findViewById(R.id.recipe_item_view);
        recipeAdaptor = new AdaptorRecipeList( this, null, 0);

        recipesList.setAdapter(recipeAdaptor);
        recipesList.setItemsCanFocus(true);

        //loaderManager
        getLoaderManager().initLoader(RECIPE_LOADER,null,this);
    }

    /**
     * Activates activity_recipe_view buttons
     */
    private void buttonHandler() {

        //add button
        recipeAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RecipeActivity.this, EditRecipeActivity.class);

                startActivity(intent);

            }
        });

        //clickable ListView
        recipesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //when item in ListView is clicked, starts EditInventoryActivity to edit that item
                Intent intent = new Intent(RecipeActivity.this, EditRecipeActivity.class);

                Uri uri = ContentUris.withAppendedId(ItemRecipes.CONTENT_URI, l);
                intent.setData(uri);

                startActivity(intent);
            }
        });
    }

    //must have following methods because implemented LoaderManager

    /**
     * Returns a cursor from a database
     * @param i
     * @return
     */
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection = {
                ItemRecipes._ID,
                ItemRecipes.RECIPE_NAME
        };

        return new CursorLoader(this,
                ItemRecipes.CONTENT_URI,
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
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        recipeAdaptor.swapCursor(cursor);
    }
    /**
     * Sets CursorLoader to null after ListView populated from database
     *
     * @param loader current CursorLoader
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        recipeAdaptor.swapCursor(null);
    }
}
