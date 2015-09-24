package com.example.keor.businesscardscanner.DAL;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.example.keor.businesscardscanner.Model.BEUser;

import java.net.SocketTimeoutException;

/**
 * Created by keor on 27-08-2015.
 */
public class DAOUser {
    Context _context;
    APICommunicator2 _apiCommunicator;


    public DAOUser(Context context) {
        _context = context;
        _apiCommunicator = new APICommunicator2(_context);
    }

    public void createUser(String phoneNumber) {
        _apiCommunicator.createUser(phoneNumber);
    }

    public int login(String phoneNumber) {
         return _apiCommunicator.login(phoneNumber);
    }

    public BEUser getUserById(int id) {
        BEUser tmp;
        tmp = null;
        return tmp;
    }
}
