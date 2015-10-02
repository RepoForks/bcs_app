package com.example.keor.businesscardscanner.GUI;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.keor.businesscardscanner.Controller.CardController;
import com.example.keor.businesscardscanner.Model.BEBusinessCard;
import com.example.keor.businesscardscanner.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CardDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private BEBusinessCard _card;
    private EditText txtFirstName;
    private EditText txtLastName;
    private EditText txtAddress;
    private EditText txtPhoneNumber;
    private EditText txtCountry;
    private EditText txtCity;
    private EditText txtCompany;
    private EditText txtTitle;
    private EditText txtHomepage;
    private EditText txtEmail;
    private EditText txtPostal;
    private EditText txtFax;
    private CardController _cardController;
    private Button btnSave;
    SimpleDateFormat sdf;

    private boolean saveState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail);
        Bundle b = getIntent().getExtras();
        sdf = new SimpleDateFormat("dd-MM-yyyy");
        _card = (BEBusinessCard) b.getSerializable(GUIConstants.CARD);
        saveState = b.getBoolean(GUIConstants.SAVE_STATE);
        _cardController = CardController.getInstance(this);
        _cardController.setContext(this);
        findViews();
        populateData();
        initToolbar();
        initListeners();
        initSettings();
    }

    private void initListeners() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCurrentCard();
                if (!saveState) {
                    _cardController.updateCard(_card);
                } else {
                    _card.setCreatedDate(sdf.format(new Date()));
                    _cardController.postCard(_card);
                }
            }
        });
    }

    private void initSettings() {
    }

    private void populateData() {
        txtFirstName.setText(_card.getFirstname());
        txtLastName.setText(_card.getLastname());
        txtAddress.setText(_card.getAddress());
        txtPhoneNumber.setText(_card.getPhonenumber());
        txtCountry.setText(_card.getCountry());
        txtCity.setText(_card.getCity());
        txtPostal.setText(_card.getPostal());
        txtCompany.setText(_card.getCompany());
        txtTitle.setText(_card.getTitle());
        txtHomepage.setText(_card.getHomepage());
        txtFax.setText(_card.getFax());
        txtEmail.setText(_card.getEmail());
    }

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        txtFirstName = (EditText) findViewById(R.id.txtFirstName);
        txtLastName = (EditText) findViewById(R.id.txtLastName);
        txtAddress = (EditText) findViewById(R.id.txtAddress);
        txtPhoneNumber = (EditText) findViewById(R.id.txtPhonenumber);
        txtCountry = (EditText) findViewById(R.id.txtCountry);
        txtCity = (EditText) findViewById(R.id.txtCity);
        txtCompany = (EditText) findViewById(R.id.txtCompany);
        txtTitle = (EditText) findViewById(R.id.txtTitle);
        txtHomepage = (EditText) findViewById(R.id.txtHomepage);
        txtPostal = (EditText) findViewById(R.id.txtPostal);
        txtFax = (EditText) findViewById(R.id.txtFax);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        btnSave = (Button) findViewById(R.id.btnSaveContact);
    }

    private void initToolbar() {
        toolbar.setTitle("Business Card details");
        toolbar.setTitleTextColor(Color.WHITE);
        //toolbar.setNavigationIcon(R.drawable.ic_details);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_card_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_save_card) {
            updateCurrentCard();
            if (!saveState) {
                _cardController.updateCard(_card);

            } else {
                _card.setCreatedDate(sdf.format(new Date()));
                _cardController.postCard(_card);
            }
        }
        return super.onOptionsItemSelected(item);
    }


    private void updateCurrentCard() {
        _card.setFirstname(txtFirstName.getText().toString());
        _card.setLastname(txtLastName.getText().toString());
        _card.setAddress(txtAddress.getText().toString());
        _card.setPhonenumber(txtPhoneNumber.getText().toString());
        _card.setCountry(txtCountry.getText().toString());
        _card.setCity(txtCity.getText().toString());
        _card.setCompany(txtCompany.getText().toString());
        _card.setTitle(txtTitle.getText().toString());
        _card.setHomepage(txtHomepage.getText().toString());
        _card.setPostal(txtPostal.getText().toString());
        _card.setFax(txtFax.getText().toString());
        _card.setEmail(txtEmail.getText().toString());
    }
    public void kill(){
        Intent overviewIntent = new Intent();
        overviewIntent.setClass(this, OverviewActivity.class);
        startActivity(overviewIntent);
        finish();
    }
}
