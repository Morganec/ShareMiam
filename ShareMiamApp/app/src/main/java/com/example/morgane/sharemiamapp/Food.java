package com.example.morgane.sharemiamapp;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by morgane on 12/11/2017.
 */

public class Food {
    public String uid;
    public String uidUser;
    public boolean deleted = false;
    public String title;
    public String description;
    public String street;
    public String postalCode;
    public String validityDate;
    public String addTime;
    public String pays;
    public String image;
    public int prix;

    public Food(String uid, String title, String descr , String street, String postalCode, String validityDate, String pays, String image,String uidUser, int prix){
        this.uid = uid;
        this.title = title;
        this.description = descr;
        this.street = street;
        this.postalCode = postalCode;
        this.pays = pays;
        this.validityDate = validityDate;
        this.uidUser = uidUser;
this.image = image;
        Date date = new Date();
        this.prix = prix;
        this.addTime=  new SimpleDateFormat("dd-MM-yyyy").format(date) + " " + new SimpleDateFormat("HH:mm").format(date);
    }

    public Food(){}

}
