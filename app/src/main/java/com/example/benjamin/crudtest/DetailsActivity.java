package com.example.benjamin.crudtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
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
    TextView title;
    TextView weight;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_placeholder);

        title = (TextView)findViewById(R.id.textViewTitle);
        weight = (TextView)findViewById(R.id.textViewWeight);
        imageView = (ImageView)findViewById(R.id.imageViewDetail);

        Intent intentDetailn = getIntent();
        Bundle b = intentDetailn.getExtras();
        String t = (String) b.get("title");
        String w = (String) b.get("weight");
        lat = (double) b.get("latitude");
        lon = (double) b.get("longitude");
        fileName = (String) b.get("fileName");

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
