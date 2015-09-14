package com.example.keor.businesscardscanner.DAL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.StrictMode;

import com.example.keor.businesscardscanner.Model.BEBusinessCard;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by keor on 27-08-2015.
 */
public class DAOBusinessCard {

    Context _context;
    APICommunicator2 _apiCommunicator;
    ArrayList<BEBusinessCard> cards;
    SimpleDateFormat sdf;

    public DAOBusinessCard(Context context) {
        _context = context;
        _apiCommunicator = new APICommunicator2(context);
        sdf = new SimpleDateFormat("dd MMM HH:mm");
    }

    public void setContext(Context context){
        _context = context;
        _apiCommunicator.setContext(context);
    }

    public void updateCard(BEBusinessCard card) {
        _apiCommunicator.updateCard(card);
    }

    public void deleteCard(BEBusinessCard card) {
        _apiCommunicator.deleteCard(card);
    }

    private String getTextContent(NodeList items, int y, int itemNumber) {
        if (items == null || items.item(y) == null || items.item(y).getChildNodes() == null || items.item(y).getChildNodes().item(itemNumber) == null || items.item(y).getChildNodes().item(itemNumber).getChildNodes() == null || items.item(y).getChildNodes().item(itemNumber).getChildNodes().item(0) == null)
            return "";
        return items.item(y).getChildNodes().item(itemNumber).getChildNodes().item(0).getTextContent();
    }

    public void postCard(BEBusinessCard card) {
        try {
            _apiCommunicator.sendJson(card);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
