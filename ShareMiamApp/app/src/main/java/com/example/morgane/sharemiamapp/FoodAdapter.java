package com.example.morgane.sharemiamapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by morgane on 19/11/2017.
 */

class FoodAdapter  extends ArrayAdapter<Food>{
    public Context context;
    public Food food;
    public FoodAdapter(Context context, List<Food> foodList) {
        super(context, 0, foodList);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_food,parent, false);
        }

        FoodViewHolder viewHolder = (FoodViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new FoodViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.foodTitle);
            viewHolder.text = (TextView) convertView.findViewById(R.id.text);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.foodImage);
            convertView.setTag(viewHolder);
        }

       this.food = getItem(position);

        //il ne reste plus qu'à remplir notre vue
        viewHolder.title.setText(food.title);
        viewHolder.text.setText(food.description);
        byte[] decodedBytes = Base64.decode(food.image, 0);
        Bitmap monImage = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        BitmapDrawable imageDraw = new BitmapDrawable(monImage);
        viewHolder.image.setBackground(imageDraw);


        return convertView;
    }




    private class FoodViewHolder{
        public TextView title;
        public TextView text;
        public ImageView image;
    }
}
