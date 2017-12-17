package com.example.benjamin.crudtest;

import android.content.Intent;
import android.content.res.Resources;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mPostList;
    private LinearLayoutManager mLayoutManager;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLayoutManager = new LinearLayoutManager(MainActivity.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        mPostList = (RecyclerView) findViewById(R.id.post_list);
        mPostList.setLayoutManager(mLayoutManager);

        mDatabase = FirebaseDatabase.getInstance().getReference("Fish");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, PostActivity.class);
                    startActivity(intent);
                }
            });
    }

    @Override
    protected void onStart() {
        super.onStart();

        final Resources res = getResources();

        FirebaseRecyclerAdapter<Fish, FishViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Fish, FishViewHolder>(
                Fish.class,
                R.layout.post_row,
                FishViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(FishViewHolder viewHolder, Fish model, int position) {
                String title = res.getString(R.string.title_activity_main, model.getFishName());
                viewHolder.setTitle(title);
                String weight = res.getString(R.string.weigth_in_kg, model.getWeight());
                viewHolder.setWeight(weight);
                viewHolder.setLatitude(model.getLatitude());
                viewHolder.setLongitude(model.getLongitude());
                viewHolder.setFileName(model.getFileName());
            }
        };

        mPostList.setAdapter(firebaseRecyclerAdapter);
    }
}













