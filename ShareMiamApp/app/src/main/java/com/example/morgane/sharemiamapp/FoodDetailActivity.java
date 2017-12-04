package com.example.morgane.sharemiamapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FoodDetailActivity extends AppCompatActivity {
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private Food selectedFood = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        String uid = ((String)extras.get("uid"));




     selectedFood = Constant.FOOD_ARRAY_LIST.get(uid);
        ArrayList<User> listInterm = new ArrayList<User>();
        listInterm.add(Constant.USERS_ARRAY_LIST.get(selectedFood.uidUser));
        remplirListView(listInterm);

        TextView twTitle = (TextView) findViewById(R.id.txtItemTitle);
        TextView twDescription = (TextView) findViewById(R.id.txtItemDescription);
        TextView twDateValid = (TextView) findViewById(R.id.txtValidityDate);
        ImageView imageView = (ImageView) findViewById(R.id.imageDetailledFood) ;


        twTitle.setText(selectedFood.title);
        twDescription.setText(selectedFood.description);
        twDateValid.setText(selectedFood.validityDate);

        Bitmap monImage = getBitMapImage(selectedFood.image);
        BitmapDrawable imageDraw = new BitmapDrawable(monImage);
        imageView.setBackground(imageDraw);

        final Button btnBuyFood = (Button) findViewById(R.id.btn_buyFood);
        btnBuyFood.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoodDetailActivity.this, ContactVendeur.class);
                intent.putExtra("uidVendeur", selectedFood.uidUser);
                intent.putExtra("uidFood",selectedFood.uid);
                startActivity(intent);
            }
        });


    }


    public Bitmap getBitMapImage(String image){
        byte[] decodedBytes = Base64.decode(image, 0);
        Bitmap monImage = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        return monImage;
    }

    private void remplirListView(ArrayList<User> listUser) {
        ListView profilApercu = (ListView) findViewById(R.id.viewProfilFoodDetail);
        UserAdapter adapter = new UserAdapter(FoodDetailActivity.this, listUser);
        profilApercu.setAdapter(adapter);
    }
}
