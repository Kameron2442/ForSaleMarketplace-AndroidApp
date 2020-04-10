package com.bourgeois.lister;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView mNameLabel;
    private final FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private ListingRecyclerAdapter mAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mNameLabel = findViewById(R.id.hello);
        mNameLabel.setText(String.format(getResources().getString(R.string.hello), currentUser.getEmail()));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        Query query = mDb.collection("listings").orderBy("posted", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Listing> options = new FirestoreRecyclerOptions.Builder<Listing>()
                .setQuery(query, Listing.class).build();

        mAdapter = new ListingRecyclerAdapter(options, new ListingRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Listing item = mAdapter.getSnapshots().getSnapshot(position).toObject(Listing.class);
                String id = mAdapter.getSnapshots().getSnapshot(position).getId();
                Intent listIntent = new Intent(HomeActivity.this, YourListingsActivity.class);
                listIntent.putExtra(ListingViewActivity.DOC_ID, id);
                startActivity(listIntent);
            }
        });
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    public void signOut(View view) {
        mAuth.signOut();
        startActivity(new Intent(this, MainActivity.class));
    }

    public void createPost(View view) {
        startActivity(new Intent(this, PostActivity.class));
    }

    public void yourListings(View view) {
        startActivity(new Intent(this, YourListingsActivity.class));
    }



}
