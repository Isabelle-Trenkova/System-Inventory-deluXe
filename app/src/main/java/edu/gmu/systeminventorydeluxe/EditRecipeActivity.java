package edu.gmu.systeminventorydeluxe;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EditRecipeActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private final static int RECIPE_EXISTS = 1;

    private Button saveButton;
    private Button deleteButton;

    private EditText ingredients;
    private EditText steps;

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

            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
