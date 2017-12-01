package com.example.morgane.sharemiamapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class FoodDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
        TextView twTitle = (TextView) findViewById(R.id.txtItemTitle);
        TextView twDescription = (TextView) findViewById(R.id.txtItemDescription);
        TextView twDateValid = (TextView) findViewById(R.id.txtValidityDate);
       ImageView imageView = (ImageView) findViewById(R.id.imageDetailledFood) ;

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        twTitle.setText((String)extras.get("title"));
        twDescription.setText((String)extras.get("descr"));
        twDateValid.setText((String)extras.get("validDate"));
        //String image = (String)extras.get("imag");
       /* byte[] decodedBytes = Base64.decode(image, 0);
        Bitmap monImage = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        monImage = Bitmap.createScaledBitmap(monImage, 300, 300, false);
        BitmapDrawable imageDraw = new BitmapDrawable(monImage);*/
      // imageView.setBackground(imageDraw);

    }
}
