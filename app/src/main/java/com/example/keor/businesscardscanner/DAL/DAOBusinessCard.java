package com.example.keor.businesscardscanner.DAL;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.example.keor.businesscardscanner.Model.BEBusinessCard;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by keor on 27-08-2015.
 */
public class DAOBusinessCard {

    Context _context;
    SQLiteDatabase _db;
    SQLiteStatement _sql;
    String _INSERT = "INSERT INTO " + DAConstants.TABLE_CARD + "(Firstname, Lastname, Address, PhoneNumber, Country, City, Company, Title, Homepage, Fax, Postal, Email, Other, EncodedImage, CreatedDate, CreatedUserId, IsDeleted) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    String _UPDATE = "UPDATE " + DAConstants.TABLE_CARD + " SET Firstname=?, Lastname=?, Address=?, PhoneNumber=?, Country=?, City=?, Company=?, Title=?, Homepage=?, Fax=?, Postal=?, Email=?, Other=?, EncodedImage=? WHERE Id=?";
    String _DELETE = "UPDATE " + DAConstants.TABLE_CARD + " SET IsDeleted=? WHERE Id=?";

    SimpleDateFormat sdf;

    public DAOBusinessCard(Context context) {
        _context = context;
        OpenHelper openHelper = new OpenHelper(_context);
        _db = openHelper.getWritableDatabase();
        sdf = new SimpleDateFormat("dd MMM HH:mm");

        if (getAllCards().size() == 0){
            BEBusinessCard card = new BEBusinessCard("André","Thy", "Mølleparkvej 2 2.6", "+4522410745", "Denmark", "Esbjerg", "Blue Water Shipping", "Software developer", "www.bws.dk", "6715", "", "2809thy@gmail.com","","","",1,false);
            BEBusinessCard card2 = new BEBusinessCard("Jakob","Jensen", "Holmevej 41", "+45 75 41 21 36", "Denmark", "Vejen", "Red Light District SmbA", "HR Assistant", "www.rld.org", "6900", "", "jaje@rld.org","","","",2,false);
            BEBusinessCard card3 = new BEBusinessCard("Kevin","Anders", "Højvangshaven 37", "+45 42 95 91 21", "Denmark", "Esbjerg", "Promovo I/S", "CEO", "www.promovo.dk", "6700", "", "kevin.2703@hotmail.com","","","",3,false);
            insert(card); insert(card2); insert(card3);
        }
    }

    public long insert(BEBusinessCard card) {
        _sql = _db.compileStatement(_INSERT);
        _sql.bindString(1, card.getFirstname());
        _sql.bindString(2, card.getLastname());
        _sql.bindString(3, card.getAddress());
        _sql.bindString(4, card.getPhonenumber());
        _sql.bindString(5, card.getCountry());
        _sql.bindString(6, card.getCity());
        _sql.bindString(7, card.getCompany());
        _sql.bindString(8, card.getTitle());
        _sql.bindString(9, card.getHomepage());
        _sql.bindString(10, card.getPostal());
        _sql.bindString(11, card.getFax());
        _sql.bindString(12, card.getEmail());
        _sql.bindString(13, card.getOther());
        _sql.bindString(14, card.getEncodedImage());
        _sql.bindString(15, ""+sdf.format(new Date()).toString());
        _sql.bindString(16, ""+card.getCreatedUserId());
        int tmp = card.getIsDeleted() ? 1 : 0;
        _sql.bindString(17, ""+tmp);
        return _sql.executeInsert();
    }

    public long updateCard(BEBusinessCard card) {
        _sql = _db.compileStatement(_UPDATE);
        _sql.bindString(1, card.getFirstname());
        _sql.bindString(2, card.getLastname());
        _sql.bindString(3, card.getAddress());
        _sql.bindString(4, card.getPhonenumber());
        _sql.bindString(5, card.getCountry());
        _sql.bindString(6, card.getCity());
        _sql.bindString(7, card.getCompany());
        _sql.bindString(8, card.getTitle());
        _sql.bindString(9, card.getHomepage());
        _sql.bindString(10, card.getPostal());
        _sql.bindString(11, card.getFax());
        _sql.bindString(12, card.getEmail());
        _sql.bindString(13, card.getOther());
        _sql.bindString(14, card.getEncodedImage());
        _sql.bindString(15, ""+card.getId());
        return _sql.executeUpdateDelete();
    }

    public long deleteCard(BEBusinessCard card) {
        _sql = _db.compileStatement(_DELETE);
        _sql.bindString(1, ""+1);
        _sql.bindString(2, ""+card.getId());
        return _sql.executeUpdateDelete();
    }

    public ArrayList<BEBusinessCard> getAllCards() {
        ArrayList<BEBusinessCard> cards = new ArrayList<>();
        Cursor cursor = _db.query(DAConstants.TABLE_CARD, new String[]{"Id", "Firstname", "Lastname", "Address", "PhoneNumber", "Country", "City", "Company", "Title", "Homepage", "Fax", "Postal", "Email", "Other", "EncodedImage", "CreatedDate", "CreatedUserId", "IsDeleted"}, "IsDeleted=?", new String[]{"" + 0}, null, null, "Firstname desc");
        if (cursor.moveToFirst()) {
            do {
                cards.add(new BEBusinessCard(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),  cursor.getString(4), cursor.getString(5), cursor.getString(6),  cursor.getString(7),  cursor.getString(8),  cursor.getString(9),  cursor.getString(10), cursor.getString(11), cursor.getString(12), cursor.getString(13), cursor.getString(14), cursor.getString(15), cursor.getInt(16), (cursor.getInt(17)>0)));
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
                cursor.close();
        }
        return cards;
    }

}
