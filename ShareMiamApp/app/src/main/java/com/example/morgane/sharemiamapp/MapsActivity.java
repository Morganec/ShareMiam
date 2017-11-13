package com.example.morgane.sharemiamapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

      // Food foodTest = new Food("a","Pomme","descr","8 rue saint prudent ","21110","10-10-2018","FRANCE");
       final ArrayList<Food> listFood = new ArrayList<Food>();
     //  listFood.add(foodTest);


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("food");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        ArrayList<String> foodString = new ArrayList<String>();
                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                            foodString.add(String.valueOf(dataSnapshot.getValue())); //add result into array list

                        }
                        System.out.println(foodString);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });


       placeAllMarker(listFood);

       /* LatLng monAdress = getLatAndLngFromAddress(" 8 rue saint prudent 21110 Izier FRANCE ");
        mMap.addMarker(new MarkerOptions().position(monAdress).title("Marker in My adress"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(monAdress)); */




        int permissionCheck = ContextCompat.checkSelfPermission(MapsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            // ask permissions here using below code
            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            mMap.setMyLocationEnabled(true);
        } else {
            mMap.setMyLocationEnabled(true);
        }


    }


    public LatLng getLatAndLngFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this);
        double longitude =0;
        double latitude=0;
        LatLng latLng = null;

        try {
            ArrayList<Address> adresses = (ArrayList<Address>) coder.getFromLocationName(strAddress, 1);
            longitude = adresses.get(0).getLongitude();
            latitude = adresses.get(0).getLatitude();
            latLng = new LatLng(latitude,longitude);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return latLng;

    }

    public void placeAllMarker(ArrayList<Food> listFood){
        for (Food food :listFood) {
            LatLng foodAdress = getLatAndLngFromAddress(food.street + " " + food.postalCode + " " + food.pays);
            if(foodAdress != null){
                mMap.addMarker(new MarkerOptions().position(foodAdress).title(food.title));
            }

        }

    }

    private ArrayList<Food> collectFoodList(Map<String,Object> foodMap) {

        ArrayList<Food> foodList = new ArrayList<>();

        for(Map.Entry<String, Object> entry : foodMap.entrySet()){
            Map singleFood = (Map) entry.getValue();
            Food food = new Food(singleFood.get("uid").toString(),
                    singleFood.get("title").toString(),
                    singleFood.get("description").toString(),
                    singleFood.get("street").toString(),
                    singleFood.get("postalCode").toString(),
                    singleFood.get("validityDate").toString(),
                    singleFood.get("pays").toString());
            foodList.add(food);
        }

        return foodList;
    }
}
