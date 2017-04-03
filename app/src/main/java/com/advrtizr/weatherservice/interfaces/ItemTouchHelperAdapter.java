package com.advrtizr.weatherservice.interfaces;

/**
 * Created by Камикадзе on 03.04.2017.
 */

public interface ItemTouchHelperAdapter {

    void onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}
