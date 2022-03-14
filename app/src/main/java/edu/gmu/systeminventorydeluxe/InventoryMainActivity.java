package edu.gmu.systeminventorydeluxe;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This will be the main place inventory can viewed, keeping
 * track of the inventory itself
 */
public class InventoryMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
    }
}
