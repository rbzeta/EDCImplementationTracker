package app.rbzeta.edcimplementationtracker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Robyn on 12/30/2016.
 */

public class MyDBHandler extends SQLiteOpenHelper {

    public static MyDBHandler sInstance;

    public static synchronized MyDBHandler getInstance(Context context){

        if (sInstance == null) {
            sInstance = new MyDBHandler(context);
        }
        return sInstance;
    }

    private MyDBHandler(Context context) {
        super(context, MyDBContract.DATABASE_NAME, null, MyDBContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MyDBContract.Edc.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(MyDBContract.Edc.DELETE_TABLE);
        onCreate(db);

    }
}
