package edu.gmu.systeminventorydeluxe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button fullInventoryButton;
    Button priorityInventoryButton;
    Button lowInventoryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);

        define();

        buttonHandler();
    }

    private void define() {

        fullInventoryButton = (Button) findViewById(R.id.button_full_inventory);

        //two buttons are shells until InventoryMainActivity is finished
        priorityInventoryButton = (Button) findViewById(R.id.button_priority_inventory);
        lowInventoryButton = (Button) findViewById(R.id.button_low_inventory);
    }

    private void buttonHandler() {

        fullInventoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent( MainActivity.this, InventoryMainActivity.class);

                startActivity(intent);

            }
        });
    }
}