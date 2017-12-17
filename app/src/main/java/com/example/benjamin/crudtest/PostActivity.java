package com.example.benjamin.crudtest;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
import java.util.Date;

public class PostActivity extends AppCompatActivity {

    private static final int PLACE_PICKER_REQUEST = 1;
    private static final int TAKE_PHOTO_REQUEST = 2;

    // Declare database
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

    // Create ImageFile
    String mCurrentPhotoPath;

    // Gallery AddPic
    String downloadURL;
    String fileName;
    ProgressBar bar;

    // Add maps
    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        // Start initialize_database_ref
        mDatabase = FirebaseDatabase.getInstance().getReference("Fish");
        mStorage = FirebaseStorage.getInstance().getReference();

        // Getting views
        editTextFish = (EditText) findViewById(R.id.editTextFish);
        editTextWeight = (EditText) findViewById(R.id.editTextWeight);

        buttonAddMaps = (Button) findViewById(R.id.buttonAddMaps);
        buttonAddFish = (Button) findViewById(R.id.buttonAddFish);
        buttonAddImg = (Button) findViewById(R.id.buttonAddImg);
        mImageView = (ImageView) findViewById(R.id.mImageView);

        // Set listeners
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
        buttonAddMaps.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                addMaps();
            }
        });

    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File
                image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.benjamin.crudtest.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, TAKE_PHOTO_REQUEST);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case TAKE_PHOTO_REQUEST:
                if(resultCode == RESULT_OK){
                    File f = new File(mCurrentPhotoPath);
                    Uri contentUri = Uri.fromFile(f);
                    mImageView.setImageURI(contentUri);
                    break;
                }
            case PLACE_PICKER_REQUEST:
                if(resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(PostActivity.this, data);
                    latitude = place.getLatLng().latitude;
                    longitude = place.getLatLng().longitude;
                    break;
                }
        }
    }

    private void galleryAddPic() {
        bar = (ProgressBar)findViewById(R.id.indeterminateBar);
        File f;
        try {
            f = new File(mCurrentPhotoPath);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(PostActivity.this, "Please fill out everything and try again", Toast.LENGTH_LONG).show();
            return;
        }
        Uri contentUri = Uri.fromFile(f);
        mImageView.setImageURI(contentUri);
        StorageReference storage = mStorage.child("images/"+contentUri.getLastPathSegment());
        UploadTask uploadTask = storage.putFile(contentUri);
        bar.setVisibility(View.VISIBLE);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                downloadURL = String.valueOf(taskSnapshot.getDownloadUrl());
                fileName = taskSnapshot.getMetadata().getName();
                String fishName = editTextFish.getText().toString().trim();
                String fishWeight = editTextWeight.getText().toString().trim();

                if(!TextUtils.isEmpty(fishName) &&
                        !TextUtils.isEmpty(fishName) &&
                        (fileName != null) &&
                        (latitude != 0) &&
                        (longitude != 0)){

                    String id = mDatabase.push().getKey();
                    Fish fish = new Fish(id, fishName, fishWeight, latitude, longitude, fileName);
                    mDatabase.child(id).setValue(fish);

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
    }

    public void addMaps(){
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            Intent intent = builder.build(PostActivity.this);
            startActivityForResult(intent, PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }
}

