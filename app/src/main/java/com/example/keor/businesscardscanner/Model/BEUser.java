package com.example.keor.businesscardscanner.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by keor on 27-08-2015.
 */
public class BEUser {
    @SerializedName("Id")
    private int m_id;
    @SerializedName("Username")
    private String m_username;
    @SerializedName("Password")
    private String m_password;
    @SerializedName("PhoneNumber")
    private String m_phoneNumber;

    public BEUser() {

    }

    public BEUser(String phoneNumber) {
        m_phoneNumber =  phoneNumber;
    }

    public BEUser(int id, String username, String password) {
        m_id = id;
        m_username = username;
        m_password = password;
    }

    public BEUser(String username, String password) {
        m_username = username;
        m_password = password;
    }

    public int getId() {return m_id;}

    public void setId(int id) {m_id = id;}

    public String getUsername() {
        return m_username;
    }

    public void setUsername(String username) {m_username = username;}

    public String getPassword() {
        return m_password;
    }

    public void setPassword(String password){m_password = password;}

    public String getPhoneNumber() {
        return m_phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber){m_phoneNumber = phoneNumber;}
}
