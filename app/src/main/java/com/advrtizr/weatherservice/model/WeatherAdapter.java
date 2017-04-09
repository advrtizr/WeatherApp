package com.advrtizr.weatherservice.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.advrtizr.weatherservice.R;
import com.advrtizr.weatherservice.interfaces.ItemTouchHelperAdapter;
import com.advrtizr.weatherservice.interfaces.ItemTouchHelperViewHolder;
import com.advrtizr.weatherservice.interfaces.OnListChangeListener;
import com.advrtizr.weatherservice.interfaces.OnStartDragListener;
import com.advrtizr.weatherservice.model.json.weather.WeatherInfo;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> implements ItemTouchHelperAdapter{

    private Context context;
    private SharedPreferences preferences;
    private List<WeatherInfo> response;
    private OnListChangeListener listChangeListener;
    private OnStartDragListener startDragListener;

    public WeatherAdapter(Context context, SharedPreferences preferences, List<WeatherInfo> list, OnListChangeListener listChangeListener, OnStartDragListener startDragListener) {
        this.context = context;
        this.preferences = preferences;
        response = list;
        this.listChangeListener = listChangeListener;
        this.startDragListener = startDragListener;
        Log.i("adapter", " adapter list size " + String.valueOf(list.size()));
    }

    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.weather_item, parent, false);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final WeatherViewHolder holder, int position) {
        WeatherInfo weatherInfo = response.get(position);
        if (weatherInfo != null) {
            String unitsQuery = weatherInfo.getQuery().getResults().getChannel().getUnits().getTemperature();
            String temperatureQuery = weatherInfo.getQuery().getResults().getChannel().getItem().getCondition().getTemp();
            String cityQuery = weatherInfo.getQuery().getResults().getChannel().getLocation().getCity();
            String countryQuery = weatherInfo.getQuery().getResults().getChannel().getLocation().getCountry();
            String conditionsQuery = weatherInfo.getQuery().getResults().getChannel().getItem().getCondition().getText();
            String codeQuery = weatherInfo.getQuery().getResults().getChannel().getItem().getCondition().getCode();
            String temperature = temperatureQuery + "\u00B0" + unitsQuery;
            holder.temperature.setText(temperature);
            holder.location.setText(cityQuery + ", " + countryQuery);
            holder.conditions.setText(conditionsQuery);
            int resource = context.getResources().getIdentifier("@drawable/icon_" + codeQuery, null, context.getPackageName());
            holder.conditionImage.setImageResource(resource);
            int backgroundResource = context.getResources().getIdentifier("@drawable/test_background", null, context.getPackageName());
        }
        holder.handleView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    startDragListener.onStartDrag(holder);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return response.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        this.notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        String key = response.get(position).getKey();
        preferences.edit().remove(key).apply();
        response.remove(position);
        listChangeListener.onListChanged(response);
        this.notifyItemRemoved(position);
    }

    @Override
    public void onDrop(int mFrom, int mTo) {
        Collections.swap(response, mFrom, mTo);
        listChangeListener.onListChanged(response);
    }

    class WeatherViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        TextView location;
        TextView temperature;
        TextView conditions;
        ImageView conditionImage;
        ImageView handleView;
        CardView itemCard;

        WeatherViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(itemView);
            location = (TextView) itemView.findViewById(R.id.tv_location);
            temperature = (TextView) itemView.findViewById(R.id.tv_temperature);
            conditions = (TextView) itemView.findViewById(R.id.tv_conditions);
            conditionImage = (ImageView) itemView.findViewById(R.id.iv_conditions);
            handleView = (ImageView) itemView.findViewById(R.id.ib_drag);
            itemCard = (CardView) itemView.findViewById(R.id.weather_item_card);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }
}
