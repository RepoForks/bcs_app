package com.example.keor.businesscardscanner.GUI;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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
        adapter = new CardAdapter(this, R.layout.cell,cards);
        listCards.setAdapter(adapter);
        txtSearch.setVisibility(View.GONE);
    }

    private void setListeners() {
        listCards.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onCardClicked(parent, view, position, id);
            }
        });
    }

    private void onCardClicked(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "Clicked: " + cards.get(position).getFullname(), Toast.LENGTH_SHORT).show();
    }

    private void initSettings() {

    }

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        listCards = (ListView) findViewById(R.id.lstCards);
        txtSearch = (EditText) findViewById(R.id.txtxSearch);
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
           Intent scanIntent = new Intent();
            scanIntent.setClass(this, ScanActivity.class);
            startActivity(scanIntent);
            return true;
        }
        if (id == R.id.action_search) {
            //txtSearch.getVisibility() == 1 ? txtSearch.setVisibility(View.GONE) : txtSearch.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Search items ", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.action_select_delete) {
            Toast.makeText(this, "select delete ", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
