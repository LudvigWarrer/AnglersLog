package com.example.benjamin.crudtest;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Benjamin on 18-11-2017.
 */


public class DetailsActivity extends AppCompatActivity {
    public static final String TAG = PostActivity.class.getSimpleName();
    double lat;
    double lon;
    String fileName;
    ImageView imageView;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        TextView title = (TextView)findViewById(R.id.textViewTitle);
        TextView weight = (TextView)findViewById(R.id.textViewWeight);
        ImageView imageView = (ImageView)findViewById(R.id.imageViewDetail);
        Intent intentDetailn = getIntent();
        Bundle b = intentDetailn.getExtras();
        String t = (String) b.get("title");
        String w = (String) b.get("weight");
        lat = (double) b.get("latitude");
        lon = (double) b.get("longitude");
        fileName = (String) b.get("fileName");
        Log.d(TAG, "" + fileName);
        Log.d(TAG, "" + lat + " " + lon);
        Log.d(TAG, w + t);
        title.setText(t);
        weight.setText(w);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://crudtest-5e359.appspot.com");
        StorageReference pathRef = storageRef.child("images/" + fileName);

        Glide.with(this).using(new FirebaseImageLoader()).load(pathRef).into(imageView);

    }


    public void showMaps(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("latitude", lat);
        intent.putExtra("longitude", lon);
        startActivity(intent);
    }

}
