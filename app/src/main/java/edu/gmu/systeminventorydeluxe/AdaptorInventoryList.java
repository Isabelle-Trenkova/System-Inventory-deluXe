package edu.gmu.systeminventorydeluxe;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Adaptor for the inventory items for when they go into the list view
 */
public class AdaptorInventoryList extends CursorAdapter {

    public static final int FLAG_AUTO_REQUEREY = 1;

    private TextView productName;
    private TextView itemQuantity;
    //Items units will defult to having no units, functionallity
    //will be added later on
    private TextView itemUnits;

    //incase we want an image to show up on the grid view
    //for the item

    private ImageView itemImage;

    public AdaptorInventoryList(Context context, Cursor cursor, int FLAG_AUTO_REQUERY) {

        super(context, cursor);
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

        /**
         * Here there will maybe multiple items in the grid
         * for this the fields must be initialized here with the
         * **SPECIFIC** view that this is happening to
         *
         * Remember that items can be edited at any time without
         * having to create a new one
         */
        productName = (TextView) view.findViewById(R.id.name_product);
        itemQuantity = (TextView) view.findViewById(R.id.quant_product);
        itemImage = (ImageView) view.findViewById(R.id.image_of_product);

        /**
         * add more functionality
         */
        //productName.setText(//**something here); //do this for all fields


    }
}
