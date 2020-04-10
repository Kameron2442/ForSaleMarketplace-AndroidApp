package com.bourgeois.lister;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.os.Bundle;
import android.widget.Toast;


import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

public class RecentListingsActivity extends AppCompatActivity {

    private final FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private ListingRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_listings);


        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        Query query = mDb.collection("listings").orderBy("posted", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Listing> options = new FirestoreRecyclerOptions.Builder<Listing>()
                .setQuery(query, Listing.class).build();

        mAdapter = new ListingRecyclerAdapter(options, new ListingRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Listing item = mAdapter.getSnapshots().getSnapshot(position).toObject(Listing.class);
                String id = mAdapter.getSnapshots().getSnapshot(position).getId();
                mDb.collection("listings").document(id).delete();

                Toast.makeText(getApplicationContext(),"Deleting " + item.getTitle(),Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(mAdapter);

    }
}
