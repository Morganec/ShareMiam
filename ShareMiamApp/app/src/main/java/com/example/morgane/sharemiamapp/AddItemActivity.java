package com.example.morgane.sharemiamapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddItemActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String mCurrentPhotoPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);


        final Button btnTakePict = (Button) findViewById(R.id.btnTakePicture);
        btnTakePict.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               dispatchTakePictureIntent();
            }
        });

        final Button btnAdd= (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText edtTitre = (EditText) findViewById(R.id.edtTitleItem);
                TextView twDescr = (TextView) findViewById(R.id.twDescrAdress);
                EditText edtCodePostal = (EditText) findViewById(R.id.edtCodePostal);
                EditText edtRue = (EditText) findViewById(R.id.edtRue);

                String titre = edtTitre.getText().toString();
                String descr = twDescr.getText().toString();
                String adresse = edtRue.getText().toString() + " " + edtCodePostal.getText().toString();

                Context context = getApplicationContext();
                CharSequence text = "Element ajouté";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                Intent intent = new Intent(AddItemActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }








}
