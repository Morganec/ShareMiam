package com.example.morgane.sharemiamapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by morgane on 19/11/2017.
 */

class FoodAdapter  extends ArrayAdapter<Food>{
    //tweets est la liste des models à afficher
    public FoodAdapter(Context context, List<Food> foodList) {
        super(context, 0, foodList);
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

        //getItem(position) va récupérer l'item [position] de la List<Tweet> tweets
       Food food = getItem(position);

        //il ne reste plus qu'à remplir notre vue
        viewHolder.title.setText(food.title);
        viewHolder.text.setText(food.description);
        viewHolder.image.setImageResource(R.drawable.noimage);

        return convertView;
    }

    private class FoodViewHolder{
        public TextView title;
        public TextView text;
        public ImageView image;
    }
}
