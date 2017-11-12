package com.example.morgane.sharemiamapp;

/**
 * Created by kwidz on 12/11/17.
 */

public class User {
    public String Username;
    public String Phonenumber;
    public String email;
    public String uid;

    public User(String uid, String email,String phone, String pseudo){
        this.Username=pseudo;
        this.email=email;
        this.Phonenumber=phone;
        this.uid=uid;
    }

}
