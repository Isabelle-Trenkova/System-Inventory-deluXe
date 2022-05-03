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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import edu.gmu.systeminventorydeluxe.database.DatabaseContract.MainInventoryItem;

//FIXME: image stuff
public class PriorityActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private ListView priorityList;
    private AdaptorInventoryList listAdaptor;

    private static final int PRIORITY_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered_view);

        define(); //define local variables
        buttonHandler(); //instantiate buttons
        priorityLoader();
    }

    private void define() {

        priorityList = (ListView) findViewById(R.id.priority_item_view);

        listAdaptor = new AdaptorInventoryList(this, null, 1);

        priorityList.setAdapter(listAdaptor);
        priorityList.setItemsCanFocus(true);

        getLoaderManager().initLoader(PRIORITY_LOADER,null,this);
    }

    private void buttonHandler() {

        priorityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(PriorityActivity.this, EditInventoryActivity.class);

                Uri uri = ContentUris.withAppendedId(MainInventoryItem.CONTENT_URI, l);
                intent.setData(uri);

                startActivity(intent);
            }
        });
    }

    /**
     * Returns cursor from a database of like items
     * @param string
     * @return
     */
    private Cursor cursorReturner(String string) {

        string = string.toLowerCase();

        ContentResolver contentResolver = getContentResolver();

        String[] tableColumns = {MainInventoryItem._ID,
                MainInventoryItem.ITEM_NAME,
                MainInventoryItem.ITEM_QUANTITY,
                MainInventoryItem.ITEM_IMAGE};

        Cursor likeItems = contentResolver.query(MainInventoryItem.CONTENT_URI, tableColumns,
                MainInventoryItem.ITEM_ISPRIORITY + " Like ?",
                new String[] {"%"+string+"%"}, null);

        return likeItems;
    }

    private void priorityLoader() {

        Cursor likeItems = cursorReturner("true");

        if (likeItems == null ) {

            Toast.makeText(PriorityActivity.this, "Not found, try again!", Toast.LENGTH_LONG).show();
        }
        else {

            AdaptorInventoryList searchAdapter = new AdaptorInventoryList(PriorityActivity.this, likeItems, 1);

            priorityList.setAdapter(searchAdapter);
        }
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

        String[] projection = {
                MainInventoryItem._ID,
                MainInventoryItem.ITEM_NAME,
                MainInventoryItem.ITEM_QUANTITY,
                MainInventoryItem.ITEM_IMAGE
        };

        return new CursorLoader(this,
                MainInventoryItem.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    /**
     * Updates CursorLoader to point to next item in database.
     *
     * @param loader current CursorLoader
     * @param data data from next cursor
     */
    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

        listAdaptor.swapCursor(data);
    }

    /**
     * Sets CursorLoader to null after ListView populated from database
     *
     * @param loader current CursorLoader
     */
    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        listAdaptor.swapCursor(null);
    }
}
