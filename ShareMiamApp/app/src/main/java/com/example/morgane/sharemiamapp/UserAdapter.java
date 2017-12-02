package com.example.morgane.sharemiamapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by morgane on 01/12/2017.
 */

public class UserAdapter extends ArrayAdapter<User> {
    public UserAdapter(Context context, List<User> userList) {
        super(context, 0, userList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_user,parent, false);
        }

        UserViewHolder viewHolder = (UserViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new UserViewHolder();
            viewHolder.pseudo = (TextView) convertView.findViewById(R.id.profilPseudo);
            viewHolder.note = (TextView) convertView.findViewById(R.id.profilNote);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.profilImage);
            convertView.setTag(viewHolder);
        }

        User user = getItem(position);

        //il ne reste plus qu'à remplir notre vue
       viewHolder.pseudo.setText(user.Username);
        viewHolder.note.setText(user.note + "/5");
        byte[] decodedBytes = Base64.decode(user.imageProfil, 0);
        Bitmap monImage = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        BitmapDrawable imageDraw = new BitmapDrawable(monImage);
        viewHolder.image.setBackground(imageDraw);

        return convertView;
    }

    private class UserViewHolder{
        public TextView pseudo;
        public TextView note;
        public ImageView image;
    }
}
