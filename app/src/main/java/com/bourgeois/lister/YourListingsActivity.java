package com.bourgeois.lister;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class YourListingsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private final FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private ListingRecyclerAdapter mAdapter;
    private final SimpleDateFormat format = new SimpleDateFormat("MM-dd-yy", Locale.US);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_listings);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));


        Query query = mDb.collection("listings").whereEqualTo("uid", currentUser.getUid()).orderBy("posted", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Listing> options = new FirestoreRecyclerOptions.Builder<Listing>()
                .setQuery(query, Listing.class).build();

        mAdapter = new ListingRecyclerAdapter(options, new ListingRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Listing item = mAdapter.getSnapshots().getSnapshot(position).toObject(Listing.class);
                String id = mAdapter.getSnapshots().getSnapshot(position).getId();
                Intent listIntent = new Intent(YourListingsActivity.this, ListingViewActivity.class);
                listIntent.putExtra(ListingViewActivity.DOC_title, item.getTitle());
                listIntent.putExtra(ListingViewActivity.DOC_price, String.format(getResources().getString(R.string.price_dollar), String.valueOf(item.getPrice())));
                listIntent.putExtra(ListingViewActivity.DOC_desc, item.getDesc());
                listIntent.putExtra(ListingViewActivity.DOC_posted, String.format(getResources().getString(R.string.created_on), format.format(item.getPosted())));
                listIntent.putExtra(ListingViewActivity.DOC_uid, String.format(getResources().getString(R.string.poster_uid), item.getUID()));
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
}
