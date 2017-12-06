package com.example.jeet.urbanpiper.models;

import java.util.HashMap;

/*
User dto for saving the user details
 */

public class User {

    private String fullName;
    private String email;
    private HashMap<String,Object> timestampJoined;

    public User() {
    }

    /**
     * Use this constructor to create new User.
     * Takes user name, email and timestampJoined as params
     *
     * @param timestampJoined
     */
    public User(String mFullName, String mEmail, HashMap<String, Object> timestampJoined) {
        this.fullName = mFullName;
        this.email = mEmail;
        this.timestampJoined = timestampJoined;
    }


    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public HashMap<String, Object> getTimestampJoined() {
        return timestampJoined;
    }
}
