package com.example.creditmanagement.Main.Users;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.creditmanagement.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    MyAdapter adapter;
    private ProgressBar progressBar;
    private ArrayList<Model> users = new ArrayList<>();
    TextView dynamic_tv;
    boolean stack_empty = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        progressBar.setVisibility(View.VISIBLE);

        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        FirebaseApp.initializeApp(this);
        FirebaseDatabase.getInstance().getReference("users")
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot child: dataSnapshot.getChildren()){
                        Model model = new Model();
                        model.setName(child.getKey());
                        model.setCredit(child.getValue(String.class));
                        users.add(model);

                        }

                        progressBar.setVisibility(View.INVISIBLE);
                        Collections.sort(users);
                        adapter.notifyDataSetChanged();

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void initViews(){

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new MyAdapter(users , this);
        progressBar = findViewById(R.id.progressBar);
        dynamic_tv = findViewById(R.id.dynamic_tv);

    }

    @Override
    public void onBackPressed() {
        if(stack_empty)
            super.onBackPressed();

        else
            users.add(adapter.sender.pop());

        stack_empty = true;
        Collections.sort(users);
        adapter.notifyDataSetChanged();
        dynamic_tv.setVisibility(View.GONE);

    }
}
