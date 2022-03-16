package edu.gmu.systeminventorydeluxe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button inventoryButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        define();

        buttonHandler();
    }

    private void define() {

        inventoryButton = (Button) findViewById(R.id.button_invnetory);

    }

    private void buttonHandler() {


        inventoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent( MainActivity.this, InventoryMainActivity.class);

                startActivity(intent);

            }
        });
    }
}