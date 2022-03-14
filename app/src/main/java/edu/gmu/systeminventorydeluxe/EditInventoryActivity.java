package edu.gmu.systeminventorydeluxe;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This will be the class where items are actually edited
 * From any action that will edit an item all activity should be redirected
 * here and to the Activity_edit_inventory to actually edit an item
 */
public class EditInventoryActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_inventory);

    }
}
