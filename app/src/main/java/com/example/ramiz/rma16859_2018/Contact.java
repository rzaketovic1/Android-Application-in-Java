package com.example.ramiz.rma16859_2018;

/**
 * Created by Ramiz on 27.5.2018.
 */

public class Contact {

    private  String contact_name;
    private String contact_email;


    public Contact(String contact_name, String contact_email) {
        this.contact_name = contact_name;
        this.contact_email = contact_email;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getContact_email() {
        return contact_email;
    }

    public void setContact_email(String contact_email) {
        this.contact_email = contact_email;
    }
}
