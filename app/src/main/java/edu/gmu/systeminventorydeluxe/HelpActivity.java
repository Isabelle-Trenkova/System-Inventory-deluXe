package edu.gmu.systeminventorydeluxe;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ms.square.android.expandabletextview.ExpandableTextView;

public class HelpActivity extends AppCompatActivity {

    private ExpandableTextView addingtext;
    private ExpandableTextView lowtext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_view);

        define();
    }

    private void define() {

        addingtext = (ExpandableTextView)
                findViewById(R.id.expand_text_view).findViewById(R.id.expand_text_view);

        addingtext.setText(getString(R.string.addItem));

        //////////////////
        //lowtext = (ExpandableTextView) findViewById(R.id.expand_text_view2).findViewById(R.id.expand_text_view);

        //lowtext.setText(getString(R.string.lowItem));
    }

}
