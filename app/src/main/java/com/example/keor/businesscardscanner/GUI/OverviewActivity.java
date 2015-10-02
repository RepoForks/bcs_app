package com.example.keor.businesscardscanner.GUI;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.support.v7.internal.widget.AdapterViewCompat.OnItemClickListener;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.keor.businesscardscanner.Controller.CardController;
import com.example.keor.businesscardscanner.DAL.APICommunicator2;
import com.example.keor.businesscardscanner.DAL.DAOBusinessCard;
import com.example.keor.businesscardscanner.Model.BEBusinessCard;
import com.example.keor.businesscardscanner.Model.BEUser;
import com.example.keor.businesscardscanner.R;

import java.io.IOException;
import java.util.ArrayList;

public class OverviewActivity extends AppCompatActivity {

    private Toolbar toolbar;
    CardAdapter adapter;
    ListView listCards;
    EditText txtSearch;
    BEUser _loggedUser;
    CardController _cardController;
    ArrayList<BEBusinessCard> cards;
    APICommunicator2 _apiCommunicator;
    boolean cameFromActivity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        _cardController = CardController.getInstance(this);
        _cardController.setContext(this);

        _apiCommunicator = new APICommunicator2(this);
        findViews();
        initSettings();
        initToolbar();
        _apiCommunicator.GetAllCardsByPhoneNumber(_loggedUser.getPhoneNumber());
        txtSearch.setVisibility(View.GONE);
    }

    private void setListeners() {
        listCards.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), CardDetailActivity.class);
                intent.putExtra(GUIConstants.CARD, cards.get(position));
                startActivityForResult(intent, 1);
                //makeShortToast("Full name: " + cards.get(position).getFullname());
            }
        });

        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                cards = _cardController.getCardsByInput(txtSearch.getText().toString().toLowerCase());
                if (!cameFromActivity)
                    setList(cards);
            }
        });

        txtSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (v.getId() == R.id.txtSearch && !hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        cameFromActivity = true;
        clearSearchField();
    }

    private void clearSearchField() {
        txtSearch.setText("");
        if (cameFromActivity == true)
            _apiCommunicator.GetAllCardsByPhoneNumber(_loggedUser.getPhoneNumber());
    }

    private void initSettings() {
        _loggedUser = GUIConstants.LOGGED_USER;
    }

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        listCards = (ListView) findViewById(R.id.lstCards);
        txtSearch = (EditText) findViewById(R.id.txtSearch);
    }

    private void initToolbar() {
        toolbar.setTitle("Overview");
        toolbar.setTitleTextColor(Color.WHITE);
        //toolbar.setNavigationIcon(R.drawable.ic_overview);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_overview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_take_picture) {
            Intent scanIntent = new Intent();
            scanIntent.setClass(this, ScanActivity.class);
            startActivity(scanIntent);
            return true;
        }
        if (id == R.id.action_search) {
            doSearchAnimation();
            return true;
        }
        if (id == R.id.action_select_export) {
            _cardController.CreateContacts(cards);
            Toast.makeText(this, "Contacts exported", Toast.LENGTH_SHORT).show();
            return true;
        }
        /*if (id == R.id.action_select_delete) {
            Toast.makeText(this, "select delete ", Toast.LENGTH_SHORT).show();
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    private void doSearchAnimation() {
        if (txtSearch.getVisibility() == View.GONE) {
            txtSearch.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left));
            txtSearch.setVisibility(View.VISIBLE);
            txtSearch.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(txtSearch, InputMethodManager.SHOW_IMPLICIT);
        } else {
            txtSearch.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right));
            clearSearchField();
            txtSearch.setVisibility(View.GONE);
        }
    }

    public void setCardList(ArrayList<BEBusinessCard> cards) {
        this.cards = cards;
        _cardController.setCards(cards);
        setList(cards);
        setListeners();
    }

    private void setList(ArrayList<BEBusinessCard> cards) {
        adapter = new CardAdapter(this, R.layout.cell, cards);
        listCards.setAdapter(adapter);
    }

    public void onSuccessCardDelete() {
        Toast.makeText(this,"Card successfully deleted",Toast.LENGTH_SHORT).show();
    }

    public void showCardDialog(BEBusinessCard card) {
        try {
            // Get screen size
            Display display = this.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int screenWidth = size.x;
            int screenHeight = size.y;
            boolean scaleDown = false;

            // Get target image size
            Bitmap bitmap = _cardController.fromBase64(card.getEncodedImage());
            double bitmapHeight = bitmap.getHeight();
            double bitmapWidth = bitmap.getWidth();

            // Scale the image down to fit perfectly into the screen
            // The value (250 in this case) must be adjusted for phone/tables displays
            while (bitmapHeight > (screenHeight - 100) || bitmapWidth > (screenWidth - 100)) {
                bitmapHeight = bitmapHeight / 1.25;
                bitmapWidth = bitmapWidth / 1.25;
                scaleDown = true;
            }
            if (!scaleDown) {
                while (bitmapHeight < (screenHeight - 250) && bitmapWidth < (screenWidth - 250)) {
                    bitmapHeight = bitmapHeight * 1.25;
                    bitmapWidth = bitmapWidth * 1.25;
                }
            }

            bitmapHeight = Math.floor(bitmapHeight);
            bitmapWidth = Math.floor(bitmapWidth);

            // Create resized bitmap image
            BitmapDrawable resizedBitmap = new BitmapDrawable(this.getResources(), Bitmap.createScaledBitmap(bitmap, (int)bitmapWidth, (int)bitmapHeight, false));

            Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.card_image_dialog);

            ImageView image = (ImageView) dialog.findViewById(R.id.imgCard);

            // !!! Do here setBackground() instead of setImageDrawable() !!! //
            image.setBackground(resizedBitmap);

            // Without this line there is a very small border around the image (1px)
            // In my opinion it looks much better without it, so the choice is up to you.
            dialog.getWindow().setBackgroundDrawable(null);

            // Show the dialog
            dialog.show();
        } catch (Exception e) {
            e.getStackTrace();
            Toast.makeText(this, "Could not generate image.", Toast.LENGTH_SHORT).show();
        }
    }

}
