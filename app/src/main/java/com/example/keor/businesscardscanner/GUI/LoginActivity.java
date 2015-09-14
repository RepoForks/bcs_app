package com.example.keor.businesscardscanner.GUI;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.StrictMode;
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

import com.example.keor.businesscardscanner.Controller.UserController;
import com.example.keor.businesscardscanner.DAL.DAOUser;
import com.example.keor.businesscardscanner.Model.BEBusinessCard;
import com.example.keor.businesscardscanner.Model.BEUser;
import com.example.keor.businesscardscanner.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button btnLogin;
    private Button btnRegister;
    private EditText txtPhoneNumber;
    private EditText txtPassword;
    ProgressDialog progress;
    private UserController _userController;
//    private DAOUser _daoUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViews();
        setListeners();
        initToolbar();
        _userController = UserController.getInstance(this);
//        _daoUser = new DAOUser(this);
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
        _userController.createUser(txtPhoneNumber.getText().toString());
    }

    public void onRegisterSuccess() {
        Toast.makeText(this,"User successfully registered!",Toast.LENGTH_SHORT).show();
    }

    private void onClickBtnLogin() {
        _userController.login(txtPhoneNumber.getText().toString());

        progress = new ProgressDialog(this);
        progress.setTitle("Login");
        progress.setMessage("Checking login information...");
        progress.setCancelable(false);
        progress.show();
    }

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        txtPhoneNumber = (EditText) findViewById(R.id.txtPhoneNumber);
        btnRegister = (Button) findViewById(R.id.btnRegister);
    }

    private void initToolbar() {
        toolbar.setTitle("Login");
        toolbar.setTitleTextColor(Color.WHITE);
        //toolbar.setNavigationIcon(R.drawable.ic_login);
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

    public void loginSuccess() {
        progress.dismiss();
        Intent overviewIntent = new Intent();
        overviewIntent.setClass(this, OverviewActivity.class);
        startActivity(overviewIntent);
        finish();
    }
    public void loginFail(){
        progress.dismiss();
        Toast.makeText(this, "Wrong credentials", Toast.LENGTH_SHORT).show();
    }

    public void setLoggedUser(BEUser user) {
        GUIConstants.LOGGED_USER = user;

    }

    public void userExistsPrompt() {
        Toast.makeText(this, "User already exists!", Toast.LENGTH_SHORT).show();
    }

    public void userNotExistPrompt() {
        progress.dismiss();
        Toast.makeText(this, "User does not exist!", Toast.LENGTH_SHORT).show();
    }
}
