package com.example.keor.businesscardscanner.Controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.RemoteException;
import android.util.Base64;
import android.util.Log;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.RawContacts;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.OperationApplicationException;

import com.example.keor.businesscardscanner.DAL.DAOBusinessCard;
import com.example.keor.businesscardscanner.DAL.APICommunicator;
import com.example.keor.businesscardscanner.Model.BEBusinessCard;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by keor on 28-08-2015.
 */
public class CardController {

    private static CardController instance = null;
    DAOBusinessCard daoBusinessCard;
    ArrayList<BEBusinessCard> cards;
    Context _context;

    private CardController(Context context) {
        _context = context;
        daoBusinessCard = new DAOBusinessCard(_context);
    }

    public static CardController getInstance(Context context) {
        if (instance == null)
            instance = new CardController(context);
        return instance;
    }
    public void setContext(Context context){
        _context = context;
        daoBusinessCard.setContext(context);
    }

//    public ArrayList<BEBusinessCard> getCards() {
//        try {
//            cards = daoBusinessCard.getAllCards();
//            return cards;
//        } catch (Exception e) {
//            Log.d("Error", "test: " + e.getMessage());
//        }
//        return null;
//    }
    public void setCards(ArrayList<BEBusinessCard> localCards){
        cards = localCards;
    }

    public ArrayList<BEBusinessCard> getCardsByInput(String input) {
        ArrayList<BEBusinessCard> matchedCards = new ArrayList<>();
        for(BEBusinessCard c : cards){
            if (c.getFullname().toLowerCase().contains(input) || c.getCompany().toLowerCase().contains(input) || c.getCountry().toLowerCase().contains(input)) {
                matchedCards.add(c);
            }
        }
        return matchedCards;
    }

    public void updateCard(BEBusinessCard card) {
        daoBusinessCard.updateCard(card);
    }

    public void deleteCard(BEBusinessCard card) {
        daoBusinessCard.deleteCard(card);
    }

    public void postCard(BEBusinessCard card) {
        daoBusinessCard.postCard(card);
    }

    public String encodeToBase64(Bitmap image)
    {
        Bitmap immagex = scaleDownBitmap(image,100,_context);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }

    public Bitmap fromBase64(String input) throws Exception
    {
        input.replaceAll("=","~").replaceAll("/","_").replaceAll("\\+","-");
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context) {

        final float densityMultiplier = context.getResources().getDisplayMetrics().density;

        int h= (int) (newHeight*densityMultiplier);
        int w= (int) (h * photo.getWidth()/((double) photo.getHeight()));

        photo=Bitmap.createScaledBitmap(photo, w, h, true);

        return photo;
    }

    public void CreateContacts(ArrayList<BEBusinessCard> cards){
        for (BEBusinessCard card : cards){


        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        int rawContactInsertIndex = ops.size();

        ops.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
                .withValue(RawContacts.ACCOUNT_TYPE, null)
                .withValue(RawContacts.ACCOUNT_NAME, null).build());

        //Phone Number
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,
                        rawContactInsertIndex)
                .withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
                .withValue(Phone.NUMBER, card.getPhonenumber())
                .withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
                .withValue(Phone.TYPE, "2").build());

        //Display name/Contact name
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(Data.RAW_CONTACT_ID,
                        rawContactInsertIndex)
                .withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
                .withValue(StructuredName.DISPLAY_NAME, card.getFullname())
                .build());
        //Email details
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,
                        rawContactInsertIndex)
                .withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.DATA, card.getEmail())
                .withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.TYPE, "2").build());


        //Postal Address

        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,
                        rawContactInsertIndex)


                .withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE )
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.STREET, card.getAddress())

                .withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.CITY, card.getCity())


                .withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE, card.getPostal())

                .withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY, card.getCountry())

                .withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.TYPE, "2")


                .build());


        //Organization details
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(Data.RAW_CONTACT_ID,
                        rawContactInsertIndex)
                .withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, card.getCompany())
                .withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Organization.TITLE, card.getTitle())
                .withValue(Data.MIMETYPE, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, "2")

                .build());

        try {
            ContentProviderResult[] res = _context.getContentResolver().applyBatch(
                    ContactsContract.AUTHORITY, ops);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        }

    }
}
