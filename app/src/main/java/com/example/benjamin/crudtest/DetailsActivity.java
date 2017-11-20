package com.example.benjamin.crudtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Benjamin on 18-11-2017.
 */


public class DetailsActivity extends AppCompatActivity {
    public static final String TAG = PostActivity.class.getSimpleName();
    double lat;
    double lon;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        TextView title = (TextView)findViewById(R.id.textViewTitle);
        TextView weight = (TextView)findViewById(R.id.textViewWeight);
        Intent intentDetailn = getIntent();
        Bundle b = intentDetailn.getExtras();
        String t = (String) b.get("title");
        String w = (String) b.get("weight");
        lat = (double) b.get("latitude");
        lon = (double) b.get("longitude");
        Log.d(TAG, "" + lat + " " + lon);
        Log.d(TAG, w + t);
        title.setText(t);
        weight.setText(w);
    }

    public void showMaps(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("latitude", lat);
        intent.putExtra("longitude", lon);
        startActivity(intent);
    }

}
