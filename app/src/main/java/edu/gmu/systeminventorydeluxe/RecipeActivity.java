package edu.gmu.systeminventorydeluxe;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import edu.gmu.systeminventorydeluxe.database.DatabaseContract.ItemRecipes;

public class RecipeActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>  {

    private static final int RECIPE_LOADER = 0;

    private ListView recipesList;
    private FloatingActionButton recipeAdd;

    private AdaptorRecipeList recipeAdaptor;

    /**
     * Runs upon each new instance of InventoryMainActivity
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

    private void define() {

        recipeAdd = findViewById(R.id.floatingActionButtonRecipe);
        recipesList = (ListView) findViewById(R.id.recipe_item_view);

        recipeAdaptor = new AdaptorRecipeList( this, null, 0);

        recipesList.setAdapter(recipeAdaptor);
        recipesList.setItemsCanFocus(true);

        getLoaderManager().initLoader(RECIPE_LOADER,null,this);
    }
    private void buttonHandler() {

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

                Intent intent = new Intent(RecipeActivity.this, EditRecipeActivity.class);

                Uri uri = ContentUris.withAppendedId(ItemRecipes.CONTENT_URI, l);
                intent.setData(uri);

                startActivity(intent);
            }
        });
    }

    /**
     * Returns a cursor from a database
     * @param string
     * @return
     */
    private Cursor cursorReturner(String string) {

        string = string.toLowerCase();

        ContentResolver contentResolver = getContentResolver();

        String[] tableColumns = {ItemRecipes._ID,
                ItemRecipes.RECIPE_NAME};

        Cursor likeItems = contentResolver.query(ItemRecipes.CONTENT_URI, tableColumns,
                ItemRecipes.RECIPE_NAME + " Like ?",
                new String[] {"%"+string+"%"}, null);

        return likeItems;
    }

    /**
     * Inflates menu view and handles searching function
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_inventory_main, menu);

        MenuItem searchButton = menu.findItem(R.id.app_bar_search);
        SearchView searchview = (SearchView) MenuItemCompat.getActionView(searchButton);

        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {

                Cursor likeItems = cursorReturner(s);

                if (likeItems == null ) {

                    Toast.makeText(RecipeActivity.this, "Not found, try again!", Toast.LENGTH_LONG).show();
                }
                else {

                    AdaptorRecipeList searchAdapter = new AdaptorRecipeList(RecipeActivity.this, likeItems, 1);

                    recipesList.setAdapter(searchAdapter);
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //FIXME: Only if you want search suggestions
                return false;
            }
        });


        //refreshes page when searching is ended
        searchview.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                finish();
                startActivity(getIntent());

                return true;
            }
        });

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

            //will go to the add item page
            case R.id.add_an_item:

                Intent intent = new Intent(RecipeActivity.this, EditRecipeActivity.class);
                startActivity(intent);

                break;
        }

        return super.onOptionsItemSelected(item);
    }


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

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        recipeAdaptor.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        recipeAdaptor.swapCursor(null);
    }
}
