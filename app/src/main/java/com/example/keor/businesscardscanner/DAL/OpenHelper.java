package com.example.keor.businesscardscanner.DAL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by keor on 27-08-2015.
 */
public class OpenHelper extends SQLiteOpenHelper {

    public OpenHelper(Context context) {
        super(context, DAConstants.DATABASE_NAME, null, DAConstants.DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DAConstants.TABLE_USER + "(Username VARCHAR(100), Password VARCHAR(100))");
        db.execSQL("CREATE TABLE " + DAConstants.TABLE_CARD + "(Id INTEGER PRIMARY KEY, Firstname VARCHAR(100), Lastname VARCHAR(100), Address VARCHAR(100), PhoneNumber VARCHAR(100), Country VARCHAR(100), City VARCHAR(100), Company VARCHAR(100), Title VARCHAR(100), Homepage VARCHAR(100), Fax VARCHAR(100), Postal VARCHAR(100), Email VARCHAR(100), Other VARCHAR(100), EncodedImage TEXT, CreatedDate TEXT, CreatedUserId INTEGER, IsDeleted INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DAConstants.TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + DAConstants.TABLE_CARD);

        onCreate(db);

    }
}
