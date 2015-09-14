package com.example.keor.businesscardscanner.Controller;

import android.content.Context;

import com.example.keor.businesscardscanner.DAL.DAOUser;
import com.example.keor.businesscardscanner.GUI.LoginActivity;
import com.example.keor.businesscardscanner.Model.BEUser;

import java.util.ArrayList;

/**
 * Created by keor on 31-08-2015.
 */
public class UserController {
    private static UserController instance = null;
    DAOUser _daoUser;
    static Context _context;

    private UserController(Context context) {
        _context = context;
       _daoUser = new DAOUser(_context);
    }

    public static UserController getInstance(Context context) {
        _context = context;
        if (instance == null)
            instance = new UserController(_context);
        return instance;
    }

    public BEUser getUserById(int id) {
        return _daoUser.getUserById(id);
    }
    public void login(String phoneNumber){
         _daoUser.login(phoneNumber);
    }

    public void createUser(String phoneNumber) {
        _daoUser.createUser(phoneNumber);
    }

}
