package com.example.morgane.sharemiamapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddItemActivity extends AppCompatActivity {
    private static final int RESULT_LOAD_IMG = 0;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String mCurrentPhotoPath;
    Button btnTakePicture;
    Button btnChoosePicture;
    Button btnSupprImage;
    ImageView imageViewFood;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        btnTakePicture = ( Button) findViewById(R.id.btnTakePicture);
        btnChoosePicture = (Button) findViewById(R.id.btnChoosePicture);
        btnSupprImage = (Button)findViewById(R.id.btnDeleteImg);
        imageViewFood = (ImageView) findViewById(R.id.imageFood);
        EditText edtDate = (EditText) findViewById(R.id.edtValidityDate);

        Date date = new Date();
        edtDate.setText(new SimpleDateFormat("dd-MM-yyyy").format(date));


        btnTakePicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

                    startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
                    takePictureIntent.getExtras();


                }
            }
        });

       btnChoosePicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        });

       btnSupprImage.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               imageViewFood.setVisibility(View.GONE);
               btnChoosePicture.setEnabled(true);
               btnTakePicture.setEnabled(true);
               btnSupprImage.setVisibility(View.GONE);
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


    private void  addItem(){

        EditText edtTitle = (EditText) findViewById(R.id.edtTitleItem);
        EditText edtDescr = (EditText) findViewById(R.id.edtDescriptionItem);
        EditText edtPostalCode = (EditText) findViewById(R.id.edtCodePostal);
        EditText edtStreet = (EditText) findViewById(R.id.edtRue);
        EditText edtCountry = (EditText) findViewById(R.id.edtCountry);
        EditText edtDate = (EditText) findViewById(R.id.edtValidityDate);


        String validityDate = edtDate.getText().toString();
        String title = edtTitle.getText().toString();
        String descr = edtDescr.getText().toString();
        String street = edtStreet.getText().toString();
        String country = edtCountry.getText().toString();
        String postalCode = edtPostalCode.getText().toString();


        String image = saveImage();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseReference = mFirebaseDatabase.getReference("Food");

        String id= mDatabaseReference.push().getKey();
        Food f = new Food(id,title,descr,street,postalCode,validityDate,country,image,auth.getCurrentUser().getUid());
        mDatabaseReference.child(id).setValue(f);
        Constant.FOOD_ARRAY_LIST.add(f);
    }

    private String saveImage() {
        imageViewFood.setDrawingCacheEnabled(true);
        imageViewFood.buildDrawingCache();
        Bitmap bitmap = imageViewFood.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        String imageB64 = Base64.encodeToString(data, Base64.DEFAULT);
        return imageB64;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            if(imageBitmap.getWidth() > imageBitmap.getHeight()){
                imageBitmap = Bitmap.createScaledBitmap(imageBitmap,600,400,true);
            }else{
                imageBitmap = Bitmap.createScaledBitmap(imageBitmap,400,600,true);
            }

            //BitmapDrawable imageDraw = new BitmapDrawable(imageBitmap);
           //imageViewFood.setBackground(imageDraw);
           imageViewFood.setImageBitmap(imageBitmap);

        }else if(requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK){
            Bitmap bm=null;
            if (data != null) {
                try {
                    bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                    if(bm.getWidth() > bm.getHeight()){
                        bm = Bitmap.createScaledBitmap(bm,600,400,true);
                    }else{
                        bm = Bitmap.createScaledBitmap(bm,400,600,true);
                    }
                    imageViewFood.setImageBitmap(bm);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
           // BitmapDrawable imageDraw = new BitmapDrawable(bm);
            //imageViewFood.setBackground(imageDraw);


        }
        imageViewFood.setVisibility(View.VISIBLE);
        btnSupprImage.setVisibility(View.VISIBLE);
        btnTakePicture.setEnabled(false);
        btnChoosePicture.setEnabled(false);

    }






}
