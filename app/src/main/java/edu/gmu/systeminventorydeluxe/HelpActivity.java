package edu.gmu.systeminventorydeluxe;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.ms.square.android.expandabletextview.ExpandableTextView;

public class HelpActivity extends AppCompatActivity {

    private ExpandableTextView addingtext;
    private ExpandableTextView lowtext;
    private ExpandableTextView prioritytext;
    private ExpandableTextView imagetext;
    private ExpandableTextView searchtext;
    private ExpandableTextView recipetext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_view);

        define();
    }

    private void define() {

        addingtext = (ExpandableTextView) findViewById(R.id.expand_text_view);
        addingtext.setText(getString(R.string.addItem));

        //////////////////
        lowtext = (ExpandableTextView) findViewById(R.id.expand_text_view2);
        lowtext.setText(getString(R.string.lowItem));

        //////////////////
        prioritytext = (ExpandableTextView) findViewById(R.id.expand_text_view3);
        prioritytext.setText(getString(R.string.priorityItem));

        //////////////////
        recipetext = (ExpandableTextView) findViewById(R.id.expand_text_view4);
        recipetext.setText(getString(R.string.recipeItem));

        //////////////////
        imagetext = (ExpandableTextView) findViewById(R.id.expand_text_view5);
        imagetext.setText(getString(R.string.imageItem));

        //////////////////
        searchtext = (ExpandableTextView) findViewById(R.id.expand_text_view6);
        searchtext.setText(getString(R.string.searchItem));


    }

}
