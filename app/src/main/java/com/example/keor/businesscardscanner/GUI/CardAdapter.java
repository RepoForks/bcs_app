package com.example.keor.businesscardscanner.GUI;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.keor.businesscardscanner.DAL.DAOBusinessCard;
import com.example.keor.businesscardscanner.Model.BEBusinessCard;
import com.example.keor.businesscardscanner.R;

import java.util.ArrayList;

/**
 * Created by keor on 27-08-2015.
 */
public class CardAdapter extends ArrayAdapter<BEBusinessCard> {
    private ArrayList<BEBusinessCard> cards;
    private final int[] colours = {
            Color.parseColor("#B9ECFA"),
            Color.parseColor("#99DFF2")
    };


    public CardAdapter(Context context, int textViewResourceId,
                         ArrayList<BEBusinessCard> cards) {
        super(context, textViewResourceId, cards);
        this.cards = cards;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        Log.d("LIST", "Position: " + position);
        if (v == null) {
            LayoutInflater li = (LayoutInflater) getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.cell, null);
        }

        v.setBackgroundColor(colours[position % colours.length]);


        BEBusinessCard c = cards.get(position);

        TextView name = (TextView) v.findViewById(R.id.name);
        TextView company = (TextView) v.findViewById(R.id.company);

        if (name != null) {
            name.setText(c.getFirstname() + " " + c.getLastname());
        }
        if (c.getCompany() != null) {
            company.setText(c.getCompany());
        }


//        if (f.getName().equals("Morten"))
//            v.setBackgroundColor(Color.YELLOW);


        return v;
    }
}
