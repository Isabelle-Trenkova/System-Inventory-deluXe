package edu.gmu.systeminventorydeluxe.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class DbHelper extends SQLiteOpenHelper {

    public final static String DATABASE_NAME = "SIXinventory.dp";

    public final static int DATABASE_VERSION = 1;


    public DbHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    //Will be called when accessing the database
    //creates new database
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {



    }

    //updates and upgrades
    //can be considered forward or backward compatability
    //this idealy shouldnt run, but just in case
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        
    }
}
