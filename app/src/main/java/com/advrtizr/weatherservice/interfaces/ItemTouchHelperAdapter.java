package com.advrtizr.weatherservice.interfaces;

public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
    void onItemDrop(int mFrom, int mTo);
}
