package com.bourgeois.lister;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class YourListingsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private final FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private ListingRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_listings);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));


        Query query = mDb.collection("listings").whereEqualTo("uid", currentUser.getUid()).orderBy("posted", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Listing> options = new FirestoreRecyclerOptions.Builder<Listing>()
                .setQuery(query, Listing.class).build();

        mAdapter = new ListingRecyclerAdapter(options, new ListingRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
//                Listing item = mAdapter.getSnapshots().getSnapshot(position).toObject(Listing.class);
//                String id = mAdapter.getSnapshots().getSnapshot(position).getId();
//                mDb.collection("listings").document(id).delete();
//
//                Toast.makeText(getApplicationContext(),"Deleting " + item.getTitle(),Toast.LENGTH_SHORT).show();
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
