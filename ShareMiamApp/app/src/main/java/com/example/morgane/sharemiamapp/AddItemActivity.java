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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
                CheckBox chbxConfirm = (CheckBox) findViewById(R.id.chbxConfirm);

                if(chbxConfirm.isChecked()){

                    addItem();
                    Context context = getApplicationContext();
                    CharSequence text = "Element ajouté";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                    Intent intent = new Intent(AddItemActivity.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    Context context = getApplicationContext();
                    CharSequence text = "Veuillez confirmer que vous avez lu les conditions d'utilisations";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }

            }
        });

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }


    private void  addItem(){

        EditText edtTitle = (EditText) findViewById(R.id.edtTitleItem);
        EditText edtDescr = (EditText) findViewById(R.id.edtDescriptionItem);
        EditText edtPostalCode = (EditText) findViewById(R.id.edtCodePostal);
        EditText edtStreet = (EditText) findViewById(R.id.edtRue);
        EditText edtDate = (EditText) findViewById(R.id.edtValidityDate);


        String validityDate = edtDate.getText().toString();
        String title = edtTitle.getText().toString();
        String descr = edtDescr.getText().toString();
        String street = edtStreet.getText().toString();
        String postalCode = edtPostalCode.getText().toString();


        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseReference = mFirebaseDatabase.getReference("food");
        String uid = auth.getCurrentUser().getUid();
        String id= mDatabaseReference.push().getKey();
        Food f = new Food(uid,title,descr,street,postalCode,validityDate);
        mDatabaseReference.child(id).setValue(f);
    }







}
