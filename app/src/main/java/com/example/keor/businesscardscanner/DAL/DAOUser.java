package com.example.keor.businesscardscanner.DAL;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

/**
 * Created by keor on 27-08-2015.
 */
public class DAOUser {
    Context _context;
    SQLiteDatabase _db;
    SQLiteStatement _sql;
    String _INSERT = "INSERT INTO " + DAConstants.TABLE_USER + "(Username, Password) VALUES (?, ?)";

    public DAOUser(Context context) {
        _context = context;
        OpenHelper openHelper = new OpenHelper(_context);
        _db = openHelper.getWritableDatabase();
    }

    public long insert(String username, String password) {
        _sql = _db.compileStatement(_INSERT);
        _sql.bindString(1, username);
        _sql.bindString(2, password);
        return _sql.executeInsert();
    }

    public boolean login(String username, String password) {
        Cursor cursor = _db.query(DAConstants.TABLE_USER, new String[]{ "Username", "Password"}, "Username=? and Password=?", new String[]{"" + username, "" + password}, null, null, null);
        if (cursor.moveToFirst()) {
            return true;
        }
        return false;
    }
}
