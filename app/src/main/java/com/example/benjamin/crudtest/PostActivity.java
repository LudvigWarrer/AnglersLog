package com.example.benjamin.crudtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class PostActivity extends AppCompatActivity {

    // Private static final String TAG = "PostActivity";
    // Private static final String REQUIRED = "required";

    // Start declare_database_reg
    private DatabaseReference mDatabase;
    // End

    // View objects
    EditText editTextFish;
    EditText editTextWeight;
    Button buttonAddFish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        // Start initialize_database_ref
        mDatabase = FirebaseDatabase.getInstance().getReference("Fish");
        // End

        // Getting views
        editTextFish = (EditText) findViewById(R.id.editTextFish);
        editTextWeight = (EditText) findViewById(R.id.editTextWeight);

        buttonAddFish = (Button) findViewById(R.id.buttonAddFish);

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
            Fish fish = new Fish(id, fishName, fishWeight);

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
}
