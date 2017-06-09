package com.advrtizr.weatherservice.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.advrtizr.weatherservice.Constants;
import com.advrtizr.weatherservice.R;
import com.advrtizr.weatherservice.model.json.weather.Forecast;
import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {

    private Context context;
    private List<Forecast> forecastList;

    public ForecastAdapter(Context context, List<Forecast> forecastList) {
        this.context = context;
        this.forecastList = forecastList;
    }

    @Override
    public ForecastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.forecast_item, parent, false);
        return new ForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ForecastViewHolder holder, int position) {
        Forecast forecast = forecastList.get(position);
        String day = forecast.getDay();
        String low = forecast.getLow() + Constants.DEGREE;
        String high = forecast.getHigh()+ Constants.DEGREE;
        String code = forecast.getCode();
        holder.forecastDay.setText(day);
        int resource = context.getResources().getIdentifier("@drawable/ic_" + code, null, context.getPackageName());
        holder.forecastImage.setImageResource(resource);
        holder.forecastTemperature.setText(low + "..." + high);
    }

    @Override
    public int getItemCount() {
        return forecastList.size();
    }

    class ForecastViewHolder extends RecyclerView.ViewHolder {

        TextView forecastDay;
        ImageView forecastImage;
        TextView forecastTemperature;
        public ForecastViewHolder(View itemView) {
            super(itemView);
            forecastDay = (TextView) itemView.findViewById(R.id.tv_day);
            forecastImage = (ImageView) itemView.findViewById(R.id.iv_image);
            forecastTemperature = (TextView) itemView.findViewById(R.id.tv_forecast_temperature);
        }
    }
}
