package com.example.keor.businesscardscanner.GUI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.keor.businesscardscanner.Controller.CardController;
import com.example.keor.businesscardscanner.Controller.UserController;
import com.example.keor.businesscardscanner.DAL.DAOBusinessCard;
import com.example.keor.businesscardscanner.Model.BEBusinessCard;
import com.example.keor.businesscardscanner.Model.BEUser;
import com.example.keor.businesscardscanner.R;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by keor on 27-08-2015.
 */
public class CardAdapter extends ArrayAdapter<BEBusinessCard> {
    private ArrayList<BEBusinessCard> cards;
    private CardController cc;
    private Context _context;
    private final int[] colours = {
            Color.parseColor("#B9ECFA"),
            Color.parseColor("#99DFF2")
    };


    public CardAdapter(Context context, int textViewResourceId,
                       ArrayList<BEBusinessCard> cards) {
        super(context, textViewResourceId, cards);
        _context = context;
        this.cards = cards;
        cc = CardController.getInstance(_context);
    }

    @Override
    public View getView(final int position, View v, ViewGroup parent) {
        //Log.d("LIST", "Position: " + position);
        if (v == null) {
            LayoutInflater li = (LayoutInflater) getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.cell, null);
        }
        ImageView imgDelete = (ImageView) v.findViewById(R.id.imgDelete);
        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder b = new AlertDialog.Builder(getContext());
                b.setTitle("Delete business card");
                b.setMessage("Are you sure you want to delete this business card?");
                b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // SHOULD NOW WORK
                        cc.deleteCard(cards.get(position));
                        cards.remove(position);
                        notifyDataSetChanged();
                    }
                });
                b.setNegativeButton("CANCEL", null);
                b.create().show();
            }
        });

        v.setBackgroundColor(colours[position % colours.length]);
        BEBusinessCard c = cards.get(position);
        try {
            ImageView cardImage = (ImageView) v.findViewById(R.id.imgCard);
            cardImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BEBusinessCard c = cards.get(position);
                    ((OverviewActivity)_context).showCardDialog(c);
                }
            });

        }
        catch (Exception e) {
        }
        finally {
            TextView name = (TextView) v.findViewById(R.id.name);
            TextView company = (TextView) v.findViewById(R.id.company);
            if (name != null) {
                name.setText(c.getFirstname() + " " + c.getLastname());
            }
            if (c.getCompany() != null) {
                company.setText(c.getCompany());
            }
        }
        return v;
    }


}
