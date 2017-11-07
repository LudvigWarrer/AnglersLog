package com.example.benjamin.crudtest;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Benjamin on 03-11-2017.
 */

public class FishViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    View mView;
    private static final String TAG = "MyActivity";

    public FishViewHolder(View itemView) {
        super(itemView);
        mView = itemView;

        // trying to get it to work
        itemView.setOnClickListener(this);
    }

    public void setTitle(String title){
        TextView post_title = (TextView) mView.findViewById(R.id.post_title);
        post_title.setText(title);
    };

    public void setWeight(String weight){
        TextView post_weight = (TextView) mView.findViewById(R.id.post_weight);
        post_weight.setText(weight);
    }

    // On click go to id page
    @Override
    public void onClick(View view) {
        int itemPos =
                Log.e(TAG, "Clicking");

    }
}