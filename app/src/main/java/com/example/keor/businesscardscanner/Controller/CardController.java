package com.example.keor.businesscardscanner.Controller;

import android.content.Context;
import android.util.Log;

import com.example.keor.businesscardscanner.DAL.DAOBusinessCard;
import com.example.keor.businesscardscanner.GUI.APICommunicator;
import com.example.keor.businesscardscanner.Model.BEBusinessCard;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by keor on 28-08-2015.
 */
public class CardController {

    private static CardController instance = null;
    DAOBusinessCard daoBusinessCard;
    ArrayList<BEBusinessCard> cards;

    private CardController(Context context) {
        daoBusinessCard = new DAOBusinessCard(context);
    }

    public static CardController getInstance(Context context) {
        if (instance == null)
            instance = new CardController(context);
        return instance;
    }

    public ArrayList<BEBusinessCard> getCards() {
        try {
            cards = daoBusinessCard.getAllCards();
            return cards;
        } catch (Exception e) {
            Log.d("Error", "test: " + e.getMessage());
        }
        return null;
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

    public void saveCard(BEBusinessCard card) {
        daoBusinessCard.updateCard(card);
    }

    public void deleteCard(BEBusinessCard card) {
        daoBusinessCard.deleteCard(card);
    }

    public void postCard(BEBusinessCard card) {
        APICommunicator api = new APICommunicator(card);
        api.execute();
    }
}
