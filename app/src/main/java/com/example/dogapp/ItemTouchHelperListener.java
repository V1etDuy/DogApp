package com.example.dogapp;

import androidx.recyclerview.widget.RecyclerView;

public interface ItemTouchHelperListener {
    void onSwiped(RecyclerView.ViewHolder viewHolder);
}
