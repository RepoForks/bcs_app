package com.example.keor.businesscardscanner.Controller;

import android.content.Context;

import com.example.keor.businesscardscanner.DAL.DAOUser;
import com.example.keor.businesscardscanner.Model.BEUser;

import java.util.ArrayList;

/**
 * Created by keor on 31-08-2015.
 */
public class UserController {
    private static UserController instance = null;
    DAOUser daoUser;

    private UserController(Context context) {
        daoUser = new DAOUser(context);
    }

    public static UserController getInstance(Context context) {
        if (instance == null)
            instance = new UserController(context);
        return instance;
    }

    public BEUser getUserById(int id) {
        return daoUser.getUserById(id);
    }

}
