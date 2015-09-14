package com.example.keor.businesscardscanner.Controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

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
        String imageEncoded = Base64.encodeToString(b, Base64.URL_SAFE);
        return imageEncoded;
    }

    public Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context) {

        final float densityMultiplier = context.getResources().getDisplayMetrics().density;

        int h= (int) (newHeight*densityMultiplier);
        int w= (int) (h * photo.getWidth()/((double) photo.getHeight()));

        photo=Bitmap.createScaledBitmap(photo, w, h, true);

        return photo;
    }
}
