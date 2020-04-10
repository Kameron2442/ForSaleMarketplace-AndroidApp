package com.bourgeois.lister;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

public class PostActivity extends AppCompatActivity {

    private EditText item_title;
    private EditText item_desc;
    private EditText item_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        item_title = findViewById(R.id.create_title_i);
        item_desc = findViewById(R.id.create_desc_i);
        item_price = findViewById(R.id.create_price_i);
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

        if (TextUtils.isEmpty(desc)) {
            item_price.setError("Required.");
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





    }
}
