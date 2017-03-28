package com.advrtizr.weatherservice.model;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.advrtizr.weatherservice.model.json.weather.WeatherInfo;

public class WeatherDatabase {

    private Context context;
    private WeatherDBHelper weatherDBHelper;
    private SQLiteDatabase database;
    private int size;

    WeatherDatabase(Context context, SharedPreferences preferences) {
        this.context = context;
        weatherDBHelper = WeatherDBHelper.getInstance(context);
        database = weatherDBHelper.getWritableDatabase();
        size = preferences.getAll().size();
    }

    public void saveEntry(WeatherInfo weatherInfo, int position) {
        if (weatherInfo != null) {
            String units = weatherInfo.getQuery().getResults().getChannel().getUnits().getTemperature();
            String temperature = weatherInfo.getQuery().getResults().getChannel().getItem().getCondition().getTemp();
            String city = weatherInfo.getQuery().getResults().getChannel().getLocation().getCity();
            String country = weatherInfo.getQuery().getResults().getChannel().getLocation().getCountry();
            String conditions = weatherInfo.getQuery().getResults().getChannel().getItem().getCondition().getText();
            String code = weatherInfo.getQuery().getResults().getChannel().getItem().getCondition().getCode();

            ContentValues values = new ContentValues();
            values.put(WeatherDBHelper.ENTRY_ID, position);
            values.put(WeatherDBHelper.CITY, city);
            values.put(WeatherDBHelper.COUNTRY, country);
            values.put(WeatherDBHelper.TEMPERATURE, temperature);
            values.put(WeatherDBHelper.UNITS, units);
            values.put(WeatherDBHelper.CONDITIONS, conditions);
            values.put(WeatherDBHelper.CODE, code);

            database.insert(WeatherDBHelper.TABLE_NAME, null, values);
        }
    }

    public void readEntry(WeatherAdapter.WeatherViewHolder holder, int position) {
        Cursor cursor = database.query(WeatherDBHelper.TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(WeatherDBHelper.ENTRY_ID);
            int cityIndex = cursor.getColumnIndex(WeatherDBHelper.CITY);
            int countryIndex = cursor.getColumnIndex(WeatherDBHelper.COUNTRY);
            int temperatureIndex = cursor.getColumnIndex(WeatherDBHelper.TEMPERATURE);
            int unitsIndex = cursor.getColumnIndex(WeatherDBHelper.UNITS);
            int conditionsIndex = cursor.getColumnIndex(WeatherDBHelper.CONDITIONS);
            int codeIndex = cursor.getColumnIndex(WeatherDBHelper.CODE);
            do {
                if (cursor.getInt(idIndex) == position) {
                    String temperature = cursor.getString(temperatureIndex) + "\u00B0" + cursor.getString(unitsIndex);
                    holder.temperature.setText(temperature);
                    String city = cursor.getString(cityIndex);
                    String country = cursor.getString(countryIndex);
                    holder.location.setText(city + ", " + country);
                    holder.conditions.setText(cursor.getString(conditionsIndex));
                    int resource = context.getResources().getIdentifier("@drawable/icon_" + cursor.getString(codeIndex), null, context.getPackageName());
                    holder.conditionImage.setImageResource(resource);
                }
                Log.i("data", String.valueOf(cursor.getInt(idIndex)) + " " + cursor.getString(cityIndex));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    public void deleteEntry(int position, boolean pressed) {
        String selection = WeatherDBHelper.ENTRY_ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(position)};
        database.delete(WeatherDBHelper.TABLE_NAME, selection, selectionArgs);
        if(pressed){
            for (int i = position; i < size + 1; i++) {
                ContentValues values = new ContentValues();
                values.put(WeatherDBHelper.ENTRY_ID, i);
                String sel = WeatherDBHelper.ENTRY_ID + " LIKE ?";
                String[] selArgs = {String.valueOf(i + 1)};
                database.update(WeatherDBHelper.TABLE_NAME, values, sel, selArgs);
            }
        }
    }

}

