package com.example.benjamin.crudtest;

import android.*;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
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
import android.widget.ProgressBar;
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

        // Start initialize_database_ref
        mDatabase = FirebaseDatabase.getInstance().getReference("Fish");
        // End
        mStorage = FirebaseStorage.getInstance().getReference();

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

        buttonAddImg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                dispatchTakePictureIntent();
            }
        });

    }

    String mCurrentPhotoPath;


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File
                image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        Log.d(TAG, "CreateImageFile is here " + storageDir);
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    static final int REQUEST_TAKE_PHOTO = 2;



    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
                Log.d(TAG, "photoFile was created");
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.benjamin.crudtest.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_TAKE_PHOTO:
                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    File f = new File(mCurrentPhotoPath);
                    Uri contentUri = Uri.fromFile(f);
                    mediaScanIntent.setData(contentUri);
                    mImageView.setImageURI(contentUri);

                break;
            case PLACE_PICKER_REQUEST:
                Place place = PlacePicker.getPlace(PostActivity.this, data);
                Log.i(TAG, place.getName().toString());
                latitude = place.getLatLng().latitude;
                longitude = place.getLatLng().longitude;
                Log.i(TAG, "Latitude is: " + latitude);
                Log.i(TAG, "Longitude is: " + longitude);
                Log.i(TAG, "Fuckuing qorks");
                break;
        }
    }

    String downloadURL;
    String fileName;

    ProgressBar bar;

    private void galleryAddPic() {
        bar = (ProgressBar)findViewById(R.id.indeterminateBar);
        bar.setVisibility(View.VISIBLE);
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
        mImageView.setImageURI(contentUri);
        StorageReference storage = mStorage.child("images/"+contentUri.getLastPathSegment());
        UploadTask uploadTask = storage.putFile(contentUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                downloadURL = String.valueOf(taskSnapshot.getDownloadUrl());
                fileName = taskSnapshot.getMetadata().getName();
                Log.d(TAG, "fileName: " + fileName + " Is done uploading");

                String fishName = editTextFish.getText().toString().trim();
                String fishWeight = editTextWeight.getText().toString().trim();
                // fishName and fishWeight is required
                if(!TextUtils.isEmpty(fishName) &&
                        !TextUtils.isEmpty(fishName) &&
                        (fileName != null) &&
                        (latitude != 0) &&
                        (longitude != 0)){

                    //Getting a unique id using push().getKey() method
                    //It will create an unique id and use it for our fish
                    String id = mDatabase.push().getKey();

                    // Creating Post object
                    Fish fish = new Fish(id, fishName, fishWeight, latitude, longitude, fileName);

                    // Saving the Post
                    mDatabase.child(id).setValue(fish);
                    Log.d(TAG, "Is there no filename?" + fileName);

                    // Set fields to blank
                    editTextFish.setText("");
                    editTextWeight.setText("");

                    Toast.makeText(PostActivity.this, getString(R.string.fishAdded), Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(PostActivity.this, "Please fill out everything and try again", Toast.LENGTH_LONG).show();
                    bar.setVisibility(View.GONE);
                }

            }
        });
    }


    private void submitPost(){
        galleryAddPic();
        // Getting the values to save
//        String fishName = editTextFish.getText().toString().trim();
//        String fishWeight = editTextWeight.getText().toString().trim();
//        // fishName and fishWeight is required
//        if(!TextUtils.isEmpty(fishName) && fileName != null){
//
//            //Getting a unique id using push().getKey() method
//            //It will create an unique id and use it for our fish
//            String id = mDatabase.push().getKey();
//
//            // Creating Post object
//            Fish fish = new Fish(id, fishName, fishWeight, latitude, longitude, fileName);
//
//            // Saving the Post
//            mDatabase.child(id).setValue(fish);
//            Log.d(TAG, "Is there no filename?" + fileName);
//
//            // Set fields to blank
//            editTextFish.setText("");
//            editTextWeight.setText("");
//
//            Toast.makeText(this, "Fish added", Toast.LENGTH_LONG).show();
//            finish();
//        } else {
//            Toast.makeText(this, "Please fill out everything!", Toast.LENGTH_LONG).show();
//        }

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

    double latitude;
    double longitude;

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


}

