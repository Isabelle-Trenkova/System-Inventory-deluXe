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

/**
 * Code in the class is based off of code for outside sources
 *
 *
 * Code written by Michał Kołnierzak, code is licenced using a MIT licence
 * and free for commerical/private use and modifications
 *
 * https://github.com/kazdavegyms/Android-Inventory-Management-App-master
 *
 *
 */


/**
 * Adaptor for the inventory items for when they go into the list view
 */
public class AdaptorInventoryList extends CursorAdapter {

    public static final int FLAG_AUTO_REQUEREY = 0;

    private TextView productName;
    private TextView itemQuantity;

    //private ImageView itemImage; //NTS 4 IZZ: handle the image stuff

    //////////////////////////////////////////////////////////////
    //Items units will defult to having no units, functionallity
    //will be added later on
    //private TextView itemUnits;

    //incase we want an image to show up on the list view
    //for the item


    ///////////////////////////////////////////////////////////////////////

    private InventoryMainActivity inventoryActivity = new InventoryMainActivity();

    public AdaptorInventoryList(Context context, Cursor cursor, int FLAG_AUTO_REQUERY) {

        super(context, cursor, FLAG_AUTO_REQUERY);
        this.inventoryActivity = (InventoryMainActivity) context;

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
        return LayoutInflater.from(context).inflate(R.layout.item_view_list, viewGroup, false);
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
        productName = (TextView) view.findViewById(R.id.name_product);
        itemQuantity = (TextView) view.findViewById(R.id.quant_product);

        //HANDLE IMAGE STUFF - NTS 4 IZZ
        //itemImage = (ImageView) view.findViewById(R.id.image_of_product);

        //will get the column index of the any information that will be
        //added by this adaptor class
        int nameIndex = cursor.getColumnIndex(MainInventoryItem.ITEM_NAME);
        int quantityIndex = cursor.getColumnIndex(MainInventoryItem.ITEM_QUANTITY);

        //Will get the string item at that index
        String nameString = cursor.getString(nameIndex);
        String quantString = cursor.getString(quantityIndex);

        /*HANDLE THE IMAGE STUFF*/


        //will be setting those views in item_view_list.xml
        productName.setText(nameString);
        itemQuantity.setText(String.valueOf(quantString));
    }
}
