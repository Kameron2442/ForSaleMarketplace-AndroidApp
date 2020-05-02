package com.bourgeois.lister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ListingViewActivity extends AppCompatActivity {

    public static final String DOC_title = "title";
    public static final String DOC_price = "price";
    public static final String DOC_desc = "desc";
    public static final String DOC_posted = "posted";
    public static final String DOC_uid = "uid";
    public static final String DOC_email = "email";
    public static final String DOC_id = "did";

    private ConstraintLayout mine;
    private Button deletePostButton;
    private Button updatePostButton;
    private FirebaseAuth mAuth;
    private final FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private int delete_post_int = 0;
    private String listingId;

    private String titleText;
    private String priceText;
    private String descText;
    private String emailText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_view);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent ();
        titleText = intent.getStringExtra(DOC_title);
        priceText = intent.getStringExtra(DOC_price);
        descText = intent.getStringExtra(DOC_desc);
        emailText = intent.getStringExtra(DOC_email);
        listingId = intent.getStringExtra(DOC_id);
        String postedText = intent.getStringExtra(DOC_posted);
        String uidText = intent.getStringExtra(DOC_uid);


        TextView tb_title = (TextView)findViewById(R.id.listing_title);
        TextView tb_price = (TextView)findViewById(R.id.listing_price);
        TextView tb_desc = (TextView)findViewById(R.id.listing_desc);
        TextView tb_posted = (TextView)findViewById(R.id.listing_posted);
        TextView tb_email = (TextView)findViewById(R.id.listing_user);
        mine = findViewById(R.id.mine_group);
        updatePostButton = findViewById(R.id.update_button);
        deletePostButton = findViewById(R.id.delete_button);

        tb_title.setText(titleText);
        tb_price.setText("$" + priceText);
        tb_desc.setText(descText);
        tb_posted.setText(postedText);
        tb_email.setText("Contact: " + emailText);

        mine.setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.i("myApp", emailText);

        if(uidText.substring(11).equals(currentUser.getUid())){
            mine.setVisibility(View.VISIBLE);
        }


    }

    public void deletePost(View view){
        if(delete_post_int == 1){
            deletePostButton.setText("Deleting...");
            mDb.collection("listings").document(listingId).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Your post was deleted!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(ListingViewActivity.this, YourListingsActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Something broke, try again", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(ListingViewActivity.this, YourListingsActivity.class));
                        finish();
                    }
                });
            delete_post_int += 1;
            return;
        }
        delete_post_int += 1;
        deletePostButton.setText("Are you sure you want to delete?");

    }

    public void updatePost(View view){
        Intent listIntent = new Intent(ListingViewActivity.this, PostActivity.class);
        listIntent.putExtra(PostActivity.POST_ID, listingId);
        listIntent.putExtra(PostActivity.POST_TITLE, titleText);
        listIntent.putExtra(PostActivity.POST_PRICE, priceText);
        listIntent.putExtra(PostActivity.POST_EMAIL, emailText);
        listIntent.putExtra(PostActivity.POST_DESC, descText);

        startActivity(listIntent);
    }
}
