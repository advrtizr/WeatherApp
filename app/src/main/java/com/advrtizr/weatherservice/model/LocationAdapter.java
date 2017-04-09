package com.advrtizr.weatherservice.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.advrtizr.weatherservice.R;
import com.advrtizr.weatherservice.ui.LocationActivity;

import java.util.ArrayList;
import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationHolder> {

    private Context context;
    private List<String> locList = new ArrayList<>();
    private SharedPreferences locationPref;
    private final int MAX_CHECKED = 10;

    public LocationAdapter(Context context, List<String> locList) {
        this.context = context;
        this.locList = locList;
        locationPref = context.getSharedPreferences(LocationActivity.LOCATION_PREF, Context.MODE_PRIVATE);
    }

    @Override
    public LocationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.location_unit, parent, false);
        return new LocationHolder(view);
    }

    @Override
    public void onBindViewHolder(final LocationHolder holder, int position) {
        final String city = locList.get(position);
        holder.location.setText(city);

        final String key = generateKey(city);
        checkStatus(key, holder);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!holder.checkBox.isChecked()) {
                    holder.checkBox.setChecked(true);
                    locationPref.edit().putString(key, city).apply();
                } else {
                    holder.checkBox.setChecked(false);
                    locationPref.edit().remove(key).apply();
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return locList.size();
    }

    private String generateKey(String str){
        StringBuilder sb = new StringBuilder();
        String[] strings = str.split(", ");
        for(String string : strings){
            sb.append(string);
        }
        return sb.toString();
    }


    private void checkStatus(String key, LocationHolder holder) {
        boolean isContain = locationPref.contains(String.valueOf(key));
        holder.checkBox.setChecked(isContain);
    }

    class LocationHolder extends RecyclerView.ViewHolder {
        TextView location;
        CheckBox checkBox;

        LocationHolder(View itemView) {
            super(itemView);
            location = (TextView) itemView.findViewById(R.id.location_city);
            checkBox = (CheckBox) itemView.findViewById(R.id.location_checkbox);
            checkBox.setClickable(false);
        }
    }
}
