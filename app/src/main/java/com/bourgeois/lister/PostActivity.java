package com.bourgeois.lister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class PostActivity extends AppCompatActivity {

    private static final String TAG = "PostActivity";
    private final FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private EditText item_title;
    private EditText item_desc;
    private EditText item_price;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        item_title = findViewById(R.id.create_title_i);
        item_desc = findViewById(R.id.create_desc_i);
        item_price = findViewById(R.id.create_price_i);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
    }

    private boolean validateForm(String title, String desc, String price) {
        boolean valid = true;

        if (TextUtils.isEmpty(title)) {
            item_title.setError("Required.");
            valid = false;
        } else {
            item_title.setError(null);
        }

        if (TextUtils.isEmpty(desc)) {
            item_desc.setError("Required.");
            valid = false;
        } else {
            item_desc.setError(null);
        }

        if (TextUtils.isEmpty(price)){
            item_price.setError("Required.");
            valid = false;
        }else if (Integer.parseInt(price) < 0) {
            item_price.setError("Can't be negative.");
            valid = false;
        } else {
            item_price.setError(null);
        }

        return valid;
    }

    public void postItem(View view) {

        String title = item_title.getText().toString();
        String desc = item_desc.getText().toString();
        String price = item_price.getText().toString();

        if (!validateForm(title, desc, price)) {
            return;
        }
        Integer priceInt = Integer.parseInt(price);
        Listing newListing = new Listing(title, priceInt, desc, currentUser.getUid(),new Date());

        mDb.collection("listings").add(newListing)
            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(getApplicationContext(), "Your post is up!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(PostActivity.this, YourListingsActivity.class));
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Something broke! Try again", Toast.LENGTH_LONG).show();
                }
            });




    }


}
