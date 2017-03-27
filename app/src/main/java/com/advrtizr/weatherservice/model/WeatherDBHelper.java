package com.advrtizr.weatherservice.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WeatherDBHelper extends SQLiteOpenHelper {

    private static WeatherDBHelper mInstance = null;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "WeatherDatabase";

    public static final String TABLE_NAME = "weatherinfo";
    public static final String PRIMARY_ID = "_id";
    public static final String ENTRY_ID = "entry";
    public static final String CITY = "city";
    public static final String COUNTRY = "country";
    public static final String TEMPERATURE = "temperature";
    public static final String UNITS = "UNITS";
    public static final String CONDITIONS = "conditions";
    public static final String CODE = "code";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    PRIMARY_ID + " INTEGER PRIMARY KEY, " +
                    ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                    CITY + TEXT_TYPE + COMMA_SEP +
                    COUNTRY + TEXT_TYPE + COMMA_SEP +
                    TEMPERATURE + TEXT_TYPE + COMMA_SEP +
                    UNITS + TEXT_TYPE + COMMA_SEP +
                    CONDITIONS + TEXT_TYPE + COMMA_SEP +
                    CODE + TEXT_TYPE + " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    private WeatherDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    public static WeatherDBHelper getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new WeatherDBHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }


}
