package edu.gmu.systeminventorydeluxe;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import edu.gmu.systeminventorydeluxe.database.DatabaseContract.MainInventoryItem;

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
public class AdaptorInventoryList extends CursorAdapter {

    //FIXME: image stuff
    //////////////////////////////////////////////////////////////////////////////////
    //PLEASE DON'T EDIT ANY IMAGE STUFF/NTS comments, IZZY WILL HANDLE LATER,I promise
    /////////////////////////////////////////////////////////////////////////////////

    //Android documentation wanted this here
    public static final int FLAG_AUTO_REQUEREY = 0;

    /**
     * Text views for the name and quantity of the
     * Item that is being adapted into the view
     */
    private TextView productName;
    private TextView itemQuantity;

    //Izzy wants images, Izzy will handle said images.
    //FIXME: add photo (Izzy)
     private ImageView itemImage;


    /**
     * Constructor
     * @param context the context of the action
     * @param cursor the information of the item from the database
     *               Not exactly an object, but can be throught of as such
     *               This is a reference to a row in the database
     * @param FLAG_AUTO_REQUERY
     */
    public AdaptorInventoryList(Context context, Cursor cursor, int FLAG_AUTO_REQUERY) {

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
        itemImage = (ImageView) view.findViewById(R.id.image_of_product);

        //will get the column index of the any information that will be
        //added by this adaptor class
        int nameIndex = cursor.getColumnIndex(MainInventoryItem.ITEM_NAME);
        int quantityIndex = cursor.getColumnIndex(MainInventoryItem.ITEM_QUANTITY);
        int imageIndex = cursor.getColumnIndex(MainInventoryItem.ITEM_IMAGE);

        //Will get the string item at that index
        String nameString = cursor.getString(nameIndex);
        String quantString = cursor.getString(quantityIndex);


        byte[] bitmap = cursor.getBlob(imageIndex);

        if (bitmap != null) {
            Bitmap image = BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length);
            itemImage.setImageBitmap(image);
        } else {

            itemImage.setImageResource(R.drawable.greyimage);
        }

        //will be setting those views in item_view_list.xml
        productName.setText(nameString);
        itemQuantity.setText(String.valueOf(quantString));
    }
}
