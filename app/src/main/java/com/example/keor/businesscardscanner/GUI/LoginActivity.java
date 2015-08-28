package com.example.keor.businesscardscanner.GUI;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.keor.businesscardscanner.DAL.DAOUser;
import com.example.keor.businesscardscanner.R;

public class LoginActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button btnLogin;
    private Button btnRegister;
    private EditText txtUsername;
    private EditText txtPassword;
    private DAOUser _daoUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViews();
        setListeners();
        initToolbar();
        _daoUser = new DAOUser(this);
    }

    private void setListeners() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBtnLogin();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBtnRegister();
            }
        });
    }

    private void onClickBtnRegister() {
        _daoUser.insert(txtUsername.getText().toString(), txtPassword.getText().toString());
        Toast.makeText(this, "User registered!", Toast.LENGTH_SHORT).show();
    }

    private void onClickBtnLogin() {
        if (_daoUser.login(txtUsername.getText().toString(), txtPassword.getText().toString())) {
            Intent overviewIntent = new Intent();
            overviewIntent.setClass(this, OverviewActivity.class);
            startActivity(overviewIntent);
            finish();
        } else {
            Toast.makeText(this, "Wrong username / password", Toast.LENGTH_SHORT).show();
        }
    }

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);
    }

    private void initToolbar() {
        toolbar.setTitle("Login");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_login);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
