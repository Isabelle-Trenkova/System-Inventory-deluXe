package edu.gmu.systeminventorydeluxe;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import edu.gmu.systeminventorydeluxe.database.ItemInventoryContract.MainInventoryItem;

/*
 * Code in the class is based off of code for outside sources
 *
 *  Used for the bindView();
 *
 * Code written by Michał Kołnierzak, code is licenced using a MIT licence
 * and free for commercial/private use and modifications
 *
 * https://github.com/kazdavegyms/Android-Inventory-Management-App-master
 */

/**
 * Adapts inventory data from database to fit ListView in GUI (activity_inventory_main_view)
 */
public class AdaptorInventoryList extends CursorAdapter {

    //////////////////////////////////////////////////////////////////////////////////
    //PLEASE DON'T EDIT ANY IMAGE STUFF/NTS comments, IZZY WILL HANDLE LATER,I promise
    /////////////////////////////////////////////////////////////////////////////////

    //Android documentation wanted this here
    public static final int FLAG_AUTO_REQUEREY = 0;

    private TextView productName; //GUI text view for name of item being adapted
    private TextView itemQuantity; //GUI text view for quantity

    //Izzy wants images, Izzy will handle said images.
    //FIXME: add photo (Izzy)
    //private ImageView itemImage;


    /**
     * Constructor
     *
     * @param context the context of the action
     * @param cursor the information of the item from the database
     *               Not exactly an object, but can be thought of as such
     *               This is a reference to a row in the database
     * @param FLAG_AUTO_REQUERY
     */
    public AdaptorInventoryList(Context context, Cursor cursor, int FLAG_AUTO_REQUERY) {

        super(context, cursor, FLAG_AUTO_REQUERY);
    }

    /**
     * Automatically generated methods from CursorAdaptor
     *
     * Creates a new view
     * @param context
     * @param cursor
     * @param viewGroup
     * @return
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

        //inflates the view with an icon design predefined in item_view_list.xml
        return LayoutInflater.from(context).inflate(R.layout.item_view_list, viewGroup, false);
    }

    /**
     * Updates an existing view
     *
     * @param view
     * @param context
     * @param cursor
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        //gets the id's of the fields/views in the item_view_list.xml
        productName = (TextView) view.findViewById(R.id.name_product);
        itemQuantity = (TextView) view.findViewById(R.id.quant_product);

        //FIXME: add photo (Izzy)
        //itemImage = (ImageView) view.findViewById(R.id.image_of_product);

        //gets the column index of the any information that will be added by this adaptor class
        int nameIndex = cursor.getColumnIndex(MainInventoryItem.ITEM_NAME);
        int quantityIndex = cursor.getColumnIndex(MainInventoryItem.ITEM_QUANTITY);

        //gets the string values of item at that index
        String nameString = cursor.getString(nameIndex);
        String quantString = cursor.getString(quantityIndex);

        ///FIXME: add photo (Izzy)


        //sets text views to string values of item
        productName.setText(nameString);
        itemQuantity.setText(String.valueOf(quantString));
    }
}
