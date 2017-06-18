package com.example.admin.googleapi;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Admin on 6/4/2017.
 */

public class userinfo implements Serializable {

   @SerializedName("token_id")
    String token_id;

    @SerializedName("personName")
    String personName;

    @SerializedName("personEmail")
    String personEmail;

    @SerializedName("photoURL")
    String photoURL;

   /*  @SerializedName("name")
    private String Name;

    @SerializedName("address")
    private String Address;

    @SerializedName("mobile")
    private String phone_no;

    @SerializedName("email")
    private String Email;

    public String getName() {
        return Name;
    }*/

     /*public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }*/



   public String getToken_id() {
        return token_id;
    }

    public void setToken_id(String token_id) {
        this.token_id = token_id;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonEmail() {
        return personEmail;
    }

    public void setPersonEmail(String persoEemail) {
        this.personEmail = persoEemail;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }




}
