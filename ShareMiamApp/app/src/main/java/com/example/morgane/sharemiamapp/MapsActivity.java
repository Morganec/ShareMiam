package com.example.morgane.sharemiamapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Base64;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public ArrayList<Food> listFood = new ArrayList<Food>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
/*
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        listFood.addAll((ArrayList<Food>) extras.get("foodList"));
        placeAllMarker(listFood);
*/
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
                    placeAllMarker(listFood);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






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
        for (final Food food :listFood) {

            byte[] decodedBytes = Base64.decode(food.image, 0);
            Bitmap monImage = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
           monImage = Bitmap.createScaledBitmap(monImage, 300, 300, false);
          // monImage = GetBitmapClippedCircle(monImage);
           monImage = TransformBitmap(monImage);
            BitmapDrawable imageDraw = new BitmapDrawable(monImage);

            LatLng foodAdress = getLatAndLngFromAddress(food.street + " " + food.postalCode + " " + food.pays);
            if(foodAdress != null){
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(foodAdress)
                        .title(food.title + ": \n Cliquer pour +infos" )
                        .icon(BitmapDescriptorFactory.fromBitmap(((BitmapDrawable)imageDraw).getBitmap()))

                );

// .icon(BitmapDescriptorFactory.fromResource(R.drawable.noimage)
                marker.setTag(food);
                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        Food objectFood =  (Food)marker.getTag();
                        Intent intent = new Intent(MapsActivity.this, FoodDetailActivity.class);
                        intent.putExtra("title", objectFood.title );
                        intent.putExtra("descr", objectFood.description );
                        intent.putExtra("validDate", objectFood.validityDate );
                        startActivity(intent);

                    }
                });
            }





        }

    }

    public static Bitmap GetBitmapClippedCircle(Bitmap bitmap) {

        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();
        final Bitmap outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        final Path path = new Path();
        path.addCircle(
                (float)(width / 2)
                , (float)(height / 2)
                , (float) Math.min(width, (height / 2))
                , Path.Direction.CCW);

        final Canvas canvas = new Canvas(outputBitmap);
        canvas.clipPath(path);
        canvas.drawBitmap(bitmap, 0,0, null);
        return outputBitmap;
    }



    public static Bitmap TransformBitmap(Bitmap bitmap) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        int outerMargin = 20;
        int margin = 10;
        Canvas canvas = new Canvas(output);

        Paint paintBorder = new Paint();
        paintBorder.setColor(Color.RED);
        canvas.drawRoundRect(new RectF(outerMargin, outerMargin, bitmap.getWidth() - outerMargin, bitmap.getHeight() - outerMargin), 0, 0, paintBorder);

        Paint trianglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        trianglePaint.setStrokeWidth(2);
        trianglePaint.setColor(Color.RED);
        trianglePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        trianglePaint.setAntiAlias(true);

        Path triangle = new Path();
        triangle.setFillType(Path.FillType.EVEN_ODD);
        triangle.moveTo(outerMargin, bitmap.getHeight() / 2);
        triangle.lineTo(bitmap.getWidth()/2,bitmap.getHeight());
        triangle.lineTo(bitmap.getWidth()-outerMargin,bitmap.getHeight()/2);
        triangle.close();

        canvas.drawPath(triangle, trianglePaint);

        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        canvas.drawRoundRect(new RectF(margin+outerMargin, margin+outerMargin, bitmap.getWidth() - (margin + outerMargin), bitmap.getHeight() - (margin + outerMargin)), 0, 0, paint);

        if (bitmap != output) {
            bitmap.recycle();
        }

        return output;
    }




}
