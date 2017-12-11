package com.example.benjamin.crudtest;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Benjamin on 03-11-2017.
 */

public class FishViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    View mView;
    private static final String TAG = "MyActivity";
    private String weight;
    private String title;
    private  double latitude;
    private double longitude;
    private String fileName;

    // This is the constructor
    public FishViewHolder(View itemView) {
        super(itemView);
        mView = itemView;

        itemView.setOnClickListener(this);
    }

    // Below are functions that are able to send data through the adapter and into the RecyclerView
    public void setTitle(String title){
        TextView post_title = (TextView) mView.findViewById(R.id.post_title);
        post_title.setText(title);
        this.title = title;
    };

    public void setWeight(String weight){
        TextView post_weight = (TextView) mView.findViewById(R.id.post_weight);
        post_weight.setText(weight);
        this.weight = weight;
    }

    public void setLatitude(double latitude){
        this.latitude = latitude;
    }

    public void setLongitude(double longitude){
        this.longitude = longitude;
    }

    public void setFileName(String fileName){
        this.fileName = fileName;
    }

    // On click opens DetailsActivity and sends the associated data through
    @Override
    public void onClick(View view) {
        Intent intentDetail = new Intent(view.getContext(), DetailsActivity.class);
        intentDetail.putExtra("title", title);
        intentDetail.putExtra("weight", weight);
        intentDetail.putExtra("latitude", latitude);
        intentDetail.putExtra("longitude", longitude);
        intentDetail.putExtra("fileName", fileName);
        view.getContext().startActivity(intentDetail);

    }

}