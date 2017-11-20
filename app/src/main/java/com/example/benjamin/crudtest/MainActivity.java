package com.example.benjamin.crudtest;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mPostList;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPostList = (RecyclerView) findViewById(R.id.post_list);
        mPostList.setHasFixedSize(true);
        mPostList.setLayoutManager(new LinearLayoutManager(this));

        mDatabase = FirebaseDatabase.getInstance().getReference("Fish");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Fish, FishViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Fish, FishViewHolder>(
                Fish.class,
                R.layout.post_row,
                FishViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(FishViewHolder viewHolder, Fish model, int position) {
                viewHolder.setTitle(model.getFishName());
                viewHolder.setWeight(model.getWeight());
                viewHolder.setLatitude(model.getLatitude());
                viewHolder.setLongitude(model.getLongitude());
            }
        };

        mPostList.setAdapter(firebaseRecyclerAdapter);
    }

    // Called when user taps button
    public void createPost(View view){
        Intent intent = new Intent(this, PostActivity.class);
        startActivity(intent);
    }
}













