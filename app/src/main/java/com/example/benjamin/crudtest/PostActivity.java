package com.example.benjamin.crudtest;

import android.*;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostActivity extends AppCompatActivity {
    private static final int PLACE_PICKER_REQUEST = 1;

    // Private static final String TAG = "PostActivity";
    // Private static final String REQUIRED = "required";

    // Start declare_database_reg
    private DatabaseReference mDatabase;
    private StorageReference mStorage;

    public static final String TAG = PostActivity.class.getSimpleName();


    // View objects
    EditText editTextFish;
    EditText editTextWeight;
    Button buttonAddFish;
    Button buttonAddMaps;
    Button buttonAddImg;
    ImageView mImageView;

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

//        if (ContextCompat.checkSelfPermission(this,
//                android.Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(this,
//                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
//                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
//        }

        // Start initialize_database_ref
        mDatabase = FirebaseDatabase.getInstance().getReference("Fish");
        // End
        mStorage = FirebaseStorage.getInstance().getReference().child("Photos");

        // Getting views
        editTextFish = (EditText) findViewById(R.id.editTextFish);
        editTextWeight = (EditText) findViewById(R.id.editTextWeight);

        buttonAddMaps = (Button) findViewById(R.id.buttonAddMaps);
        buttonAddFish = (Button) findViewById(R.id.buttonAddFish);
        buttonAddImg = (Button) findViewById(R.id.buttonAddImg);
        mImageView = (ImageView) findViewById(R.id.mImageView);

        buttonAddFish.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                submitPost();
            }
        });

    }

    private void submitPost(){
        // Getting the values to save
        String fishName = editTextFish.getText().toString().trim();
        String fishWeight = editTextWeight.getText().toString().trim();

        // fishName and fishWeight is required
        if(!TextUtils.isEmpty(fishName)) {

            //Getting a unique id using push().getKey() method
            //It will create an unique id and use it for our fish
            String id = mDatabase.push().getKey();

            // Creating Post object
            Fish fish = new Fish(id, fishName, fishWeight, latitude, longitude);

            // Saving the Post
            mDatabase.child(id).setValue(fish);

            // Set fields to blank
            editTextFish.setText("");
            editTextWeight.setText("");

            Toast.makeText(this, "Fish added", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
        }

    }



    public void addMaps(View view){
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            Intent intent = builder.build(PostActivity.this);
            startActivityForResult(intent, PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    static final int REQUEST_IMAGE_CAPTURE = 2;
    public void dispatchTakePictureIntent(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    double latitude;
    double longitude;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // not using request and results code yet
        switch (requestCode){
            case 1:
                Place place = PlacePicker.getPlace(PostActivity.this, data);
                Log.i(TAG, place.getName().toString());
                latitude = place.getLatLng().latitude;
                longitude = place.getLatLng().longitude;
                Log.i(TAG, "Latitude is: " + latitude);
                Log.i(TAG, "Longitude is: " + longitude);
                break;
            case 2:
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                mImageView.setImageBitmap(imageBitmap);
                break;
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {
//            Place place = PlacePicker.getPlace(PostActivity.this, data);
//            Log.i(TAG, place.getName().toString());
//            latitude = place.getLatLng().latitude;
//            longitude = place.getLatLng().longitude;
//            Log.i(TAG, "Latitude is: " + latitude);
//            Log.i(TAG, "Longitude is: " + longitude);
//            Log.i(TAG, "Fuckuing qorks");
//        }
//    }





//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            mImageView.setImageBitmap(imageBitmap);
//        }
//    }








}
