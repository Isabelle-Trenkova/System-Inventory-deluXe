package edu.gmu.systeminventorydeluxe;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import edu.gmu.systeminventorydeluxe.database.ItemInventoryContract.ItemRecipes;

/**
 * Code in the class is based off of code for outside sources
 *
 *  Used for the bindView();
 *
 * Code written by Michał Kołnierzak, code is licenced using a MIT licence
 * and free for commercial/private use and modifications
 *
 * https://github.com/kazdavegyms/Android-Inventory-Management-App-master
 *
 *
 */

/**
 * Adaptor for the inventory items for when they go into the list view
 */
public class AdaptorRecipeList extends CursorAdapter {

    //////////////////////////////////////////////////////////////////////////////////
    //PLEASE DON'T EDIT ANY IMAGE STUFF/NTS comments, IZZY WILL HANDLE LATER,I promise
    /////////////////////////////////////////////////////////////////////////////////

    //Android documentation wanted this here
    public static final int FLAG_AUTO_REQUEREY = 0;

    /**
     * Text views for the name and quantity of the
     * Item that is being adapted into the view
     */
    private TextView recipeName;

    /**
     * Constructor
     * @param context the context of the action
     * @param cursor the information of the item from the database
     *               Not exactly an object, but can be throught of as such
     *               This is a reference to a row in the database
     * @param FLAG_AUTO_REQUERY
     */
    public AdaptorRecipeList(Context context, Cursor cursor, int FLAG_AUTO_REQUERY) {

        super(context, cursor, FLAG_AUTO_REQUERY);
    }


    /**
     * Automatically generated methods from cursoradaptor
     *
     * Creates a new view
     * @param context
     * @param cursor
     * @param viewGroup
     * @return
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        /**
         * will inflate the view with an icon design predefined in item_view_list.xml
         *
         */
        return LayoutInflater.from(context).inflate(R.layout.recipe_view_list  , viewGroup, false);
    }

    /**
     * Updates an existing view
     * @param view
     * @param context
     * @param cursor
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        //These will get the id's of the fields/views in the item_view_list.xml
        recipeName = (TextView) view.findViewById(R.id.recipe_name);

        //will get the column index of the any information that will be
        //added by this adaptor class
        int nameIndex = cursor.getColumnIndex(ItemRecipes.RECIPE_NAME);


        //Will get the string item at that index
        String nameString = cursor.getString(nameIndex);


        recipeName.setText(nameString);

    }
}
