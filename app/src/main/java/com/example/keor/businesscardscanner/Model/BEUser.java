package com.example.keor.businesscardscanner.Model;

/**
 * Created by keor on 27-08-2015.
 */
public class BEUser {
    private int m_id;
    private String m_username;
    private String m_password;

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

    public String getUsername() {
        return m_username;
    }

    public void setUsername(String username) {m_username = username;}

    public String getPassword() {
        return m_password;
    }

    public void setPassword(String password){m_password = password;}
}
