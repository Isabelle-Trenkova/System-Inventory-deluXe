package edu.gmu.systeminventorydeluxe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * MainActivity runs the main menu for this app.
 */
public class MainActivity extends AppCompatActivity {

    Button fullInventoryButton; //accesses InventoryMainActivity
    Button priorityInventoryButton;
    Button lowInventoryButton;
    Button fullRecipesButton;

    /**
     * Runs upon each new instance of MainActivity.
     *
     * @param savedInstanceState previous state of this activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);

        define(); //define activity_main_view buttons
        buttonHandler(); //activate activity_main_view buttons
    }

    /**
     * Defines three activity_main_view buttons
     */
    private void define() {

        fullInventoryButton = (Button) findViewById(R.id.button_full_inventory);
        fullRecipesButton = (Button) findViewById(R.id.button_full_recipes);

        //two buttons are shells until InventoryMainActivity is finished
        priorityInventoryButton = (Button) findViewById(R.id.button_priority_inventory);
        lowInventoryButton = (Button) findViewById(R.id.button_low_inventory);
    }

    /**
     * Activates activity_main_view buttons
     */
    private void buttonHandler() {

        //button to view full inventory
        fullInventoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //when clicked, button starts InventoryMainActivity to view full inventory
                Intent intent = new Intent( MainActivity.this, InventoryMainActivity.class);

                //start new activity (InventoryMainActivity)
                startActivity(intent);

            }
        });

        //FIXME: this button causes system to crash (Carolyn to Izzy)
        //button to view all recipes
        fullRecipesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //when clicked, button starts RecipeActivity to view recipes
                Intent intent = new Intent( MainActivity.this, RecipeActivity.class);

                //start new activity (RecipeActivity)
                startActivity(intent);
            }
        });
    }
}