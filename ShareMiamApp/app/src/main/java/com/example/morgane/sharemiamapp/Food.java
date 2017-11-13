package com.example.morgane.sharemiamapp;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by morgane on 12/11/2017.
 */

public class Food {
    public String uid;
    public boolean deleted = false;
    public String title;
    public String description;
    public String street;
    public String postalCode;
    public String validityDate;
    public String pays;

    public Food(String uid, String title, String descr , String street, String postalCode, String validityDate, String pays ){
        this.uid = uid;
        this.title = title;
        this.description = descr;
        this.street = street;
        this.postalCode = postalCode;
        this.pays = pays;
        this.validityDate = validityDate;
    }
}
