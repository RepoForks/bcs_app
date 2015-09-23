package com.example.keor.businesscardscanner.GUI;

import com.example.keor.businesscardscanner.Model.BEUser;

/**
 * Created by athy on 31-08-2015.
 */
public class GUIConstants {
    public static int CAMERA_CAPTURE_CODE = 0;
    public static String CARD = "CARD";
    public static String SAVE_STATE = "SAVE_STATE";
    public static boolean SAVE_STATE_VALUE;
    public static BEUser LOGGED_USER;

    public static final int RESULT_LOGIN_SUCCESS = 1;
    public static final int RESULT_WRONG_CREDENTIALS = 2;
    public static final int RESULT_USER_NOT_EXISTING = 3;
    public static final int RESULT_CONNECTION_TIMEOUT = 4;
    public static final int RESULT_UNKNOWN_ERROR = 5;
}
