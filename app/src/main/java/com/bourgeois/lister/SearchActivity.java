package com.bourgeois.lister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {

    private EditText title_ET;
    private EditText min_ET;
    private EditText max_ET;
    private TextView noresults;

    private FirebaseAuth mAuth;
    private final FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private ListingRecyclerAdapter mAdapter;
    private final SimpleDateFormat format = new SimpleDateFormat("MM-dd-yy", Locale.US);
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        title_ET = findViewById(R.id.search_title_i);
        min_ET = findViewById(R.id.search_min_i);
        max_ET = findViewById(R.id.search_max_i);
        noresults = findViewById(R.id.no_results);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        Query query = mDb.collection("listings").orderBy("posted", Query.Direction.DESCENDING);
        buildResults(query);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    public void searchDB(View view) {
        String search_title = title_ET.getText().toString();
        Integer search_min = 0;
        Integer search_max = 1000000;

        if(min_ET.getText().toString().length() != 0){
            search_min = Integer.parseInt(min_ET.getText().toString());
        }

        if(max_ET.getText().toString().length() != 0){
            search_max = Integer.parseInt(max_ET.getText().toString());
        }

        //different search queries are used depending on if the user inputs a title
        if(search_title.length() == 0){
            Query query = mDb.collection("listings")
                    .whereGreaterThan("price", search_min)
                    .whereLessThan("price", search_max)
                    .orderBy("price", Query.Direction.DESCENDING);
            buildResults(query);
        }else{
            Query query = mDb.collection("listings")
                    .whereEqualTo("title", search_title)
                    .whereGreaterThan("price", search_min)
                    .whereLessThan("price", search_max)
                    .orderBy("price", Query.Direction.DESCENDING);
            buildResults(query);
        }
    }

    public void buildResults(Query myquery){

        myquery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.getResult().size() == 0){
                    noresults.setVisibility(View.VISIBLE);
                }else{
                    noresults.setVisibility(View.GONE);
                }

            }
        });

        FirestoreRecyclerOptions<Listing> options = new FirestoreRecyclerOptions.Builder<Listing>().setQuery(myquery, Listing.class).build();

        mAdapter = new ListingRecyclerAdapter(options, new ListingRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Listing item = mAdapter.getSnapshots().getSnapshot(position).toObject(Listing.class);
                String id = mAdapter.getSnapshots().getSnapshot(position).getId();
                Intent listIntent = new Intent(SearchActivity.this, ListingViewActivity.class);
                listIntent.putExtra(ListingViewActivity.DOC_title, item.getTitle());
                listIntent.putExtra(ListingViewActivity.DOC_price, String.valueOf(item.getPrice()));
                listIntent.putExtra(ListingViewActivity.DOC_desc, item.getDesc());
                listIntent.putExtra(ListingViewActivity.DOC_posted, String.format(getResources().getString(R.string.created_on), format.format(item.getPosted())));
                listIntent.putExtra(ListingViewActivity.DOC_uid, String.format(getResources().getString(R.string.poster_uid), item.getUID()));
                listIntent.putExtra(ListingViewActivity.DOC_email, item.getEmail());
                listIntent.putExtra(ListingViewActivity.DOC_id, id);
                startActivity(listIntent);
            }
        });
        recyclerView.setAdapter(mAdapter);
        mAdapter.startListening();
    }
}