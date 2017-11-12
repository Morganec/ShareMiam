package com.example.morgane.sharemiamapp;

/**
 * Created by kwidz on 12/11/17.
 */

public class User {
    public String Username;
    public String Phonenumber;
    public String email;

    public User(String email){
        this.Username="Pas encore choisi";
        this.email=email;
        this.Phonenumber="pas encore choisi";
    }

}
