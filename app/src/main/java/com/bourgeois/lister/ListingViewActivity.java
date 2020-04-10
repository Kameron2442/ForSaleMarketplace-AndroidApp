package com.bourgeois.lister;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ListingViewActivity extends AppCompatActivity {

    public static final String DOC_title = "title";
    public static final String DOC_price = "price";
    public static final String DOC_desc = "desc";
    public static final String DOC_posted = "posted";
    public static final String DOC_uid = "uid";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_view);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent ();
        String titleText = intent.getStringExtra(DOC_title);
        String priceText = intent.getStringExtra(DOC_price);
        String descText = intent.getStringExtra(DOC_desc);
        String postedText = intent.getStringExtra(DOC_posted);
        String uidText = intent.getStringExtra(DOC_uid);

        TextView tb_title = (TextView)findViewById(R.id.listing_title);
        TextView tb_price = (TextView)findViewById(R.id.listing_price);
        TextView tb_desc = (TextView)findViewById(R.id.listing_desc);
        TextView tb_posted = (TextView)findViewById(R.id.listing_posted);
        TextView tb_uid = (TextView)findViewById(R.id.listing_user);
        tb_title.setText(titleText);
        tb_price.setText(priceText);
        tb_desc.setText(descText);
        tb_posted.setText(postedText);
        tb_uid.setText(uidText);




    }
}
