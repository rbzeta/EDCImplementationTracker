package app.rbzeta.edcimplementationtracker.database;

import android.provider.BaseColumns;

/**
 * Created by Robyn on 12/30/2016.
 */

public class MyDBContract {
    public static final  int    DATABASE_VERSION   = 1;
    public static final  String DATABASE_NAME      = "edc.db";
    private static final String TEXT_TYPE          = " TEXT";
    private static final String COMMA_SEP          = ",";

    private MyDBContract(){

    }

    public static class Edc implements BaseColumns {


        private Edc(){}

        public static final String TABLE_NAME = "edc";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TID = "tid";
        public static final String COLUMN_MID = "mid";

        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY," +
                        COLUMN_ID + " INTEGER " + COMMA_SEP +
                        COLUMN_TID + TEXT_TYPE + COMMA_SEP +
                        COLUMN_MID + TEXT_TYPE + " )";

        public static final String DELETE_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;

    }
}
