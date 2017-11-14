package com.example.morgane.sharemiamapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class FoodDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
        TextView twTitle = (TextView) findViewById(R.id.txtItemTitle);
        TextView twDescription = (TextView) findViewById(R.id.txtItemDescription);

        Intent intent = getIntent();

        Bundle titreAndDescr = intent.getExtras();

        twTitle.setText((String)titreAndDescr.get("title"));
        twDescription.setText((String)titreAndDescr.get("descr"));
    }
}
