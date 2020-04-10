package com.bourgeois.lister;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ListingViewActivity extends AppCompatActivity {

    public static final String DOC_ID = "message"; //java convention

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_view);

    }
}
