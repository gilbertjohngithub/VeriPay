package com.berstek.veripay.models;

import com.google.firebase.database.Exclude;

public class User {

    @Exclude
    private String key;

    private String lastname, firstname, middlename;
    private String bio;
    private String email;
    private String photo_url;
    private long date_created;
    private String pay_id;
    private String phone_number;
    private boolean account_setup_finished;

    private String address_street_brgy, address_city, address_state;
    private int postal_code;

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getDate_created() {
        return date_created;
    }

    public void setDate_created(long date_created) {
        this.date_created = date_created;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public String getPay_id() {
        return pay_id;
    }

    public void setPay_id(String pay_id) {
        this.pay_id = pay_id;
    }

    @Exclude
    public String getFullName() {
        return firstname + " " + lastname;
    }

    @Exclude
    public String getKey() {
        return key;
    }

    @Exclude
    public void setKey(String key) {
        this.key = key;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public boolean isAccount_setup_finished() {
        return account_setup_finished;
    }

    public void setAccount_setup_finished(boolean account_setup_finished) {
        this.account_setup_finished = account_setup_finished;
    }

    public String getAddress_street_brgy() {
        return address_street_brgy;
    }

    public void setAddress_street_brgy(String address_street_brgy) {
        this.address_street_brgy = address_street_brgy;
    }

    public String getAddress_city() {
        return address_city;
    }

    public void setAddress_city(String address_city) {
        this.address_city = address_city;
    }

    public String getAddress_state() {
        return address_state;
    }

    public void setAddress_state(String address_state) {
        this.address_state = address_state;
    }

    public int getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(int postal_code) {
        this.postal_code = postal_code;
    }
}
