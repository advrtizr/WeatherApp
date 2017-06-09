package com.advrtizr.weatherservice.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.transition.TransitionManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.advrtizr.weatherservice.Constants;
import com.advrtizr.weatherservice.R;
import com.advrtizr.weatherservice.interfaces.ItemTouchHelperAdapter;
import com.advrtizr.weatherservice.interfaces.ItemTouchHelperViewHolder;
import com.advrtizr.weatherservice.interfaces.OnListChangeListener;
import com.advrtizr.weatherservice.interfaces.OnStartDragListener;
import com.advrtizr.weatherservice.model.json.weather.Forecast;
import com.advrtizr.weatherservice.model.json.weather.Image;
import com.advrtizr.weatherservice.model.json.weather.WeatherInfo;
import com.advrtizr.weatherservice.ui.DetailsActivity;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> implements ItemTouchHelperAdapter {

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
            String temperatureQuery = weatherInfo.getQuery().getResults().getChannel().getItem().getCondition().getTemp();
            String minTemperatureQuery = weatherInfo.getQuery().getResults().getChannel().getItem().getForecast().get(0).getLow() + Constants.DEGREE;
            String maxTemperatureQuery = weatherInfo.getQuery().getResults().getChannel().getItem().getForecast().get(0).getHigh() + Constants.DEGREE;
            String cityQuery = weatherInfo.getQuery().getResults().getChannel().getLocation().getCity();
            String conditionsQuery = weatherInfo.getQuery().getResults().getChannel().getItem().getCondition().getText();
            String codeQuery = weatherInfo.getQuery().getResults().getChannel().getItem().getCondition().getCode();
            String temperature = temperatureQuery + Constants.DEGREE;
            holder.temperature.setText(temperature);
            holder.minTemperature.setText(minTemperatureQuery);
            holder.maxTemperature.setText(maxTemperatureQuery);
            holder.location.setText(cityQuery);
            holder.conditions.setText(conditionsQuery);
            holder.forecastContainer.setVisibility(View.GONE);
            int resource = context.getResources().getIdentifier("@drawable/ic_" + codeQuery, null, context.getPackageName());
            holder.conditionImage.setImageResource(resource);
            int backgroundResource = context.getResources().getIdentifier("@drawable/bg_" + codeQuery, null, context.getPackageName());
            holder.itemCard.setBackgroundResource(backgroundResource);
            setAnimation(holder);
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
        holder.itemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailsIntent = new Intent(context, DetailsActivity.class);
                detailsIntent.putExtra(Constants.WEATHER_DATA, response.get(holder.getAdapterPosition()));
                String transitionName = context.getString(R.string.transition_temperature);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, holder.conditionImage, transitionName);
                ActivityCompat.startActivity(context, detailsIntent, options.toBundle());
            }
        });
    }

    private void setAnimation(WeatherViewHolder holder) {
        AnimationDrawable anim = (AnimationDrawable) holder.itemCard.getBackground();
        anim.setExitFadeDuration(3000);
        anim.start();
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
    public void onItemDrop(int mFrom, int mTo) {
        Collections.swap(response, mFrom, mTo);
        listChangeListener.onListChanged(response);
    }

    class WeatherViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        RelativeLayout forecastContainer;
        TextView location;
        TextView temperature;
        TextView minTemperature;
        TextView maxTemperature;
        TextView conditions;
        ImageView conditionImage;
        ImageView handleView;
        CardView itemCard;
        GridLayout gridLayout;
        TextView forecastDay;
        ImageView forecastImage;
        TextView forecastTemperature;

        WeatherViewHolder(View itemView) {
            super(itemView);
            forecastContainer = (RelativeLayout) itemView.findViewById(R.id.forecast_container);
            location = (TextView) itemView.findViewById(R.id.tv_location);
            temperature = (TextView) itemView.findViewById(R.id.tv_temperature);
            minTemperature = (TextView) itemView.findViewById(R.id.tv_min_temperature);
            maxTemperature = (TextView) itemView.findViewById(R.id.tv_max_temperature);
            conditions = (TextView) itemView.findViewById(R.id.tv_condition);
            conditionImage = (ImageView) itemView.findViewById(R.id.iv_conditions);
            handleView = (ImageView) itemView.findViewById(R.id.ib_drag);
            itemCard = (CardView) itemView.findViewById(R.id.weather_item_card);
            gridLayout = (GridLayout) itemView.findViewById(R.id.gl_forecast);
            forecastDay = (TextView) itemView.findViewById(R.id.tv_day);
            forecastImage = (ImageView) itemView.findViewById(R.id.iv_image);
            forecastTemperature= (TextView) itemView.findViewById(R.id.tv_forecast_temperature);
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
