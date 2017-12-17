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
    double latitude;
    double longitude;
    String fileName;
    String titleText;
    String weightText;
    ImageView imageView;
    TextView title;
    TextView weight;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        title = (TextView)findViewById(R.id.textViewTitle);
        weight = (TextView)findViewById(R.id.textViewWeight);
        imageView = (ImageView)findViewById(R.id.imageViewDetail);

        Intent intentDetailn = getIntent();
        Bundle b = intentDetailn.getExtras();
        titleText = (String) b.get("title");
        weightText = (String) b.get("weight");
        latitude = (double) b.get("latitude");
        longitude = (double) b.get("longitude");
        fileName = (String) b.get("fileName");

        this.title.setText(titleText);
        this.weight.setText(weightText);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://crudtest-5e359.appspot.com");
        StorageReference pathRef = storageRef.child("images/" + fileName);

        Glide.with(this).using(new FirebaseImageLoader()).load(pathRef).into(imageView);
    }

    public void showMaps(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        startActivity(intent);
    }
}
