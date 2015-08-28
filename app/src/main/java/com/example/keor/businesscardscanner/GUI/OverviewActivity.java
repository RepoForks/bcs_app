package com.example.keor.businesscardscanner.GUI;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.keor.businesscardscanner.Controller.CardController;
import com.example.keor.businesscardscanner.DAL.DAOBusinessCard;
import com.example.keor.businesscardscanner.Model.BEBusinessCard;
import com.example.keor.businesscardscanner.R;

import java.util.ArrayList;

public class OverviewActivity extends AppCompatActivity {

    private Toolbar toolbar;
    CardAdapter adapter;
    DAOBusinessCard _daoCard;
    ListView listCards;
    EditText txtSearch;
    CardController cc;
    ArrayList<BEBusinessCard> cards;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        findViews();
        setListeners();
        initToolbar();
        initSettings();
        _daoCard = new DAOBusinessCard(this);
        cards = _daoCard.getAllCards();
        populateList(cards);
        txtSearch.setVisibility(View.GONE);
        cc = CardController.getInstance(this);
    }

    private void setListeners() {
        listCards.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onCardClicked(parent, view, position, id);
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
                populateList(cc.getCardsByInput(txtSearch.getText().toString().toLowerCase()));
            }
        });

        txtSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(v.getId() == R.id.txtSearch && !hasFocus) {
                    InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });
    }

    private void populateList(ArrayList<BEBusinessCard> c) {
        adapter = new CardAdapter(this, R.layout.cell,c);
        listCards.setAdapter(adapter);
    }

    private void onCardClicked(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "Clicked: " + cards.get(position).getFullname(), Toast.LENGTH_SHORT).show();
    }

    private void initSettings() {

    }

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        listCards = (ListView) findViewById(R.id.lstCards);
        txtSearch = (EditText) findViewById(R.id.txtSearch);
    }

    private void initToolbar() {
        toolbar.setTitle("Overview");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_overview);
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
            Toast.makeText(this, "Take picture intent", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.action_search) {
            doSearchAnimation();
            return true;
        }
        if (id == R.id.action_select_delete) {
            Toast.makeText(this, "select delete ", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void doSearchAnimation() {
        if (txtSearch.getVisibility() == View.GONE) {
            txtSearch.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left));
            txtSearch.setVisibility(View.VISIBLE);
        }
        else {
            txtSearch.startAnimation(AnimationUtils.loadAnimation(this,android.R.anim.slide_out_right));
            txtSearch.setText("");
            txtSearch.setVisibility(View.GONE);
        }
    }
}
