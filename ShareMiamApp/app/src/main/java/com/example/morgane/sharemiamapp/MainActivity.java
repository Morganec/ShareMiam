package com.example.morgane.sharemiamapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    public ArrayList<Food> listFood = new ArrayList<Food>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final Button btnSeeMap = (Button) findViewById(R.id.btnSeeMap);
        btnSeeMap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                //intent.putExtra("foodList",listFood);
                startActivity(intent);
            }
        });

        final Button btnAddItem = (Button) findViewById(R.id.btnAddItem);
        btnAddItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                startActivity(intent);
            }
        });

        final Button btnProfile = (Button) findViewById(R.id.btnProfile);
        btnProfile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });







        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        final Query query = reference.child("Food");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot singleFood : dataSnapshot.getChildren()) {


                        //singleFood.getValue(Food.class);

                        Food f = new Food(singleFood.getValue(Food.class).uid,
                                singleFood.getValue(Food.class).title,
                                singleFood.getValue(Food.class).description,
                                singleFood.getValue(Food.class).street,
                                singleFood.getValue(Food.class).postalCode,
                                singleFood.getValue(Food.class).validityDate,
                                singleFood.getValue(Food.class).pays,
                                singleFood.getValue(Food.class).image);

                        listFood.add(f);
                    }
                    remplirListView(listFood);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    private void remplirListView(ArrayList<Food> listFood) {
        ListView lvPlat = (ListView) findViewById(R.id.listViewFood);


        FoodAdapter adapter = new FoodAdapter(MainActivity.this, listFood);
        lvPlat.setAdapter(adapter);
    }
}
