package com.advrtizr.weatherservice.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.advrtizr.weatherservice.interfaces.OnRequestFinishListener;
import com.advrtizr.weatherservice.presenter.WeatherPresenter;

import java.util.List;

public class WeatherDatabase implements WeatherModel{

    private WeatherDBHelper weatherDBHelper;
    private WeatherCompiler weatherCompiler;
    private SQLiteDatabase database;
    private Context context;
    private List<WeatherInfo> weatherInfoList;
    private List<WeatherPresenter> presenters;

    public WeatherDatabase(Context context) {
        this.context = context;
        weatherDBHelper = WeatherDBHelper.getInstance(context);
        database = weatherDBHelper.getWritableDatabase();
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
            values.put(WeatherDBHelper._ID, position);
            values.put(WeatherDBHelper.CITY, city);
            values.put(WeatherDBHelper.COUNTRY, country);
            values.put(WeatherDBHelper.TEMPERATURE, temperature);
            values.put(WeatherDBHelper.UNITS, units);
            values.put(WeatherDBHelper.CONDITIONS, conditions);
            values.put(WeatherDBHelper.CODE, code);

            database.insert(WeatherDBHelper.TABLE_NAME, null, values);
        }
    }

    public void updateEntry(int position) {

        String selection = WeatherDBHelper._ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(position)};

        database.update(WeatherDBHelper.TABLE_NAME, values, selection, selectionArgs);

    }

    public void getEntry(WeatherAdapter.WeatherViewHolder holder, int position) {
        Cursor cursor = database.query(WeatherDBHelper.TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(WeatherDBHelper._ID);
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
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    public void deleteEntry(int position) {
        String selection = WeatherDBHelper._ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(position)};
        database.delete(WeatherDBHelper.TABLE_NAME, selection, selectionArgs);
    }

    @Override
    public void getWeather(OnRequestFinishListener listener, String location) {

    }
}

