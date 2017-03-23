package com.advrtizr.weatherservice.model;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.advrtizr.weatherservice.R;
import com.advrtizr.weatherservice.presenter.WeatherPresenter;

import java.util.List;

import butterknife.ButterKnife;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    private Context context;
    private List<String> keys;
    private List<WeatherPresenter> presenters;
    private SharedPreferences locationPreferences;
    private WeatherDatabase weatherDatabase;

    public WeatherAdapter(Context context, List<String> keys, List<WeatherPresenter> presenters, SharedPreferences preferences) {
        this.context = context;
        this.keys = keys;
        this.presenters = presenters;
        this.locationPreferences = preferences;
        weatherDatabase = new WeatherDatabase(context);
        makeFirstRequest();
    }

    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.weather_item, parent, false);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WeatherViewHolder holder, int position) {
        WeatherInfo weatherInfo = presenters.get(position).getWeatherInfo();
        weatherDatabase.saveToDatabase(weatherInfo, position);
        weatherDatabase.initFromSaved(holder, position);
    }


    @Override
    public int getItemCount() {
        return presenters.size();
    }

    private void makeFirstRequest() {
    }

    class WeatherViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView location;
        TextView temperature;
        TextView conditions;
        ImageView refresh;
        ImageView delete;
        ImageView conditionImage;

        WeatherViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(itemView);
            location = (TextView) itemView.findViewById(R.id.tv_location);
            temperature = (TextView) itemView.findViewById(R.id.tv_temperature);
            conditions = (TextView) itemView.findViewById(R.id.tv_conditions);
            conditionImage = (ImageView) itemView.findViewById(R.id.iv_conditions);
            refresh = (ImageView) itemView.findViewById(R.id.ib_refresh);
            delete = (ImageView) itemView.findViewById(R.id.ib_delete);
            refresh.setOnClickListener(this);
            delete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ib_refresh:
                    presenters.get(getAdapterPosition()).loadWeather();
                    break;
                case R.id.ib_delete:
                    String key = keys.get(getAdapterPosition());
                    if (key.equals("")) {
                        break;
                    }
                    locationPreferences.edit().remove(key).apply();
                    presenters.remove(getAdapterPosition());
                    String selection = WeatherDBHelper._ID + " LIKE ?";
                    String[] selectionArgs = { String.valueOf(getAdapterPosition()) };
                    database.delete(WeatherDBHelper.TABLE_NAME, selection, selectionArgs);
                    notifyItemRemoved(getAdapterPosition());
                    break;
            }
        }
    }
}
