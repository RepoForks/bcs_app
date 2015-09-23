package com.example.keor.businesscardscanner.DAL;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.keor.businesscardscanner.GUI.CardDetailActivity;
import com.example.keor.businesscardscanner.GUI.GUIConstants;
import com.example.keor.businesscardscanner.GUI.LoginActivity;
import com.example.keor.businesscardscanner.GUI.OverviewActivity;
import com.example.keor.businesscardscanner.Model.BEBusinessCard;
import com.example.keor.businesscardscanner.Model.BEUser;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by keor on 07-09-2015.
 */
public class APICommunicator2 {
    ArrayList<BEBusinessCard> cards;
    BEUser user;
    Context _context;
    String phoneNumber;
    BEBusinessCard _selectedCard;
    //String domainUrl = "http://Dennis-Work.bws.dk:24334";
    Gson gson;
    String domainUrl = "http://pto-udv.bws.dk:24334";

    public APICommunicator2(Context context) {
        _context = context;
        gson = new Gson();
    }
    public void setContext(Context context){
        _context = context;
    }

    public void GetAllCards() {
        final OverviewActivity overviewActivity = (OverviewActivity) _context;
        cards = new ArrayList<>();
        Thread t = new Thread() {
            public void run() {
                String url = domainUrl + "/api/Card";
                String[] allTexts;
                final ArrayList<BEBusinessCard> cards = new ArrayList<>();

                overviewActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        overviewActivity.setCardList(cards);
                    }
                });
            }
        };
        t.start();
    }

    public void GetAllCardsByPhoneNumber(final String phoneNumber) {
        final OverviewActivity overviewActivity = (OverviewActivity) _context;
        cards = new ArrayList<>();

        Thread t = new Thread() {
            public void run() {
                String url2 = domainUrl + "/api/Card/GetAllCardsByPhonenumber/"+phoneNumber;

                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;
                String jsonString = "";

                try
                {
                    URL url = new URL(url2);

                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();
                    // Read the input stream into a String
                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();

                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while ((line = reader.readLine()) != null)
                    {
                        // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                        // But it does make debugging a *lot* easier if you print out the completed
                        // buffer for debugging.
                        buffer.append(line + "\n");
                    }

                    jsonString = buffer.toString();
                } catch (IOException e)
                {
                    Log.e("Login", "Error", e);
                } finally
                {
                    if (urlConnection != null)
                    {
                        urlConnection.disconnect();
                    }
                    if (reader != null)
                    {
                        try
                        {
                            reader.close();
                        } catch (final IOException e)
                        {
                            Log.e("Login", "Error closing stream", e);
                        }
                    }
                }
                try
                {
                    JSONArray secretJson = new JSONArray(jsonString);
                    cards = getCardsFromJson(secretJson);

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                overviewActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        overviewActivity.setCardList(cards);
                    }
                });
            }
        };
        t.start();
    }

    private ArrayList<BEBusinessCard> getCardsFromJson(JSONArray jsonString) throws JSONException {
        ArrayList cards = new ArrayList();

        for (int i = 0; i < jsonString.length(); i++){
            JSONObject obj = jsonString.getJSONObject(i);
            BEBusinessCard card = gson.fromJson(String.valueOf(obj), BEBusinessCard.class);
            cards.add(card);
        }

        return cards;

    }


    public int login(final String phoneNumber) {
        showLoginProgress();
        //final LoginActivity loginActivity = (LoginActivity) _context;
        this.phoneNumber = phoneNumber;
        final int[] result = {0};

        Thread t = new Thread() {
            public void run() {
                try {
                    Timer timer = new Timer();
                    int timeOut = 5000;
                    URL url = new URL(domainUrl + "/api/User/GetUserByPhoneNumber/" + phoneNumber);
                    final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    HttpURLConnection.setFollowRedirects(false);
                    connection.setConnectTimeout(timeOut);
                    connection.setReadTimeout(timeOut);
                    connection.setRequestMethod("GET");
                    timer.schedule(
                            new TimerTask() {
                                @Override
                                public void run() {
                                    Looper.prepare();
                                    connection.disconnect();
                                    result[0] = GUIConstants.RESULT_CONNECTION_TIMEOUT;
                                    return;
                                }
                            }, timeOut
                    );
                    connection.connect();

                    final int code = connection.getResponseCode();
                    timer.cancel();
                    if (code == 200) {
                        /*loginActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                GUIConstants.LOGGED_USER = GetUserByPhoneNumberJSON(phoneNumber);
                                if (GUIConstants.LOGGED_USER == null || GUIConstants.LOGGED_USER.getId() < 1)
                                    result[0] = GUIConstants.RESULT_USER_NOT_EXISTING;
                                else
                                    result[0] = GUIConstants.RESULT_LOGIN_SUCCESS;
                            }
                        });*/
                        GUIConstants.LOGGED_USER = GetUserByPhoneNumberJSON(phoneNumber);
                        if (GUIConstants.LOGGED_USER == null || GUIConstants.LOGGED_USER.getId() < 1)
                            result[0] = GUIConstants.RESULT_USER_NOT_EXISTING;
                        else
                            result[0] = GUIConstants.RESULT_LOGIN_SUCCESS;
                    } else {
                        /*loginActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loginFail();
                            }
                        });*/
                        result[0] = GUIConstants.RESULT_WRONG_CREDENTIALS;
                    }

                } catch (java.net.SocketTimeoutException e) {
                    result[0] = GUIConstants.RESULT_CONNECTION_TIMEOUT;
                } catch (MalformedURLException e) {
                    String s = "";
                } catch (IOException e) {
                    String b = "";
                } catch (Exception e) {
                    String b = "";
                }
            }
        };
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result[0];
    }

    private void loginSuccess() {
        LoginActivity loginActivity = (LoginActivity) _context;
        loginActivity.loginSuccess();
    }

    private void loginFail() {
        LoginActivity loginActivity = (LoginActivity) _context;
        loginActivity.loginFail();
    }

    private void timedOut() {
        LoginActivity loginActivity = (LoginActivity) _context;
        loginActivity.showConnectionFailedMessage();
    }

    private void showLoginProgress() {
        LoginActivity loginActivity = (LoginActivity) _context;
        loginActivity.showLoginProgress();
    }

    public void updateCard(final BEBusinessCard _card) {
        _selectedCard = _card;
        final CardDetailActivity cardDetailActivity = (CardDetailActivity) _context;
        Thread t = new Thread() {
            public void run() {
                try {
                    HttpClient client = new DefaultHttpClient();
                    HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
                    HttpResponse response = null;

                    HttpPut post = new HttpPut(domainUrl + "/api/Card");

                    JSONObject jsonObject = new JSONObject(gson.toJson(_card));


                    StringEntity se = new StringEntity(jsonObject.toString());
                    se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                    post.setEntity(se);

                    response = client.execute(post);

                        CardDetailActivity cardDetailActivity = (CardDetailActivity) _context;
                        cardDetailActivity.kill();

                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public void deleteCard(final BEBusinessCard card){
        _selectedCard = card;
        final OverviewActivity overviewActivity = (OverviewActivity) _context;
        Thread t = new Thread() {
            public void run() {
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    String baseUrl = domainUrl + "/api/Card/"+_selectedCard.getId();
                    HttpPut httpput = new HttpPut(baseUrl);
                    HttpResponse response = httpclient.execute(httpput);
                    overviewActivity.onSuccessCardDelete();
                } catch (Exception e) {
                    String s = e.getMessage();

                }
            }
        };
        t.start();
    }

    public void createUser(final String phoneNumber){
        final LoginActivity loginActivity = (LoginActivity) _context;
        Thread t = new Thread() {
            public void run() {
                try {
                    GetUserByPhoneNumberJSON(phoneNumber);
                    BEUser loggedUser = GUIConstants.LOGGED_USER;
                    if (loggedUser.getPhoneNumber().equals(phoneNumber))
                        loginActivity.userExistsPrompt();
                    else {
                        HttpClient httpclient = new DefaultHttpClient();
                        String baseUrl = domainUrl + "/api/User?";
                        String paramUrl = "phoneNumber=" + phoneNumber;
                        HttpPost httppost = new HttpPost(baseUrl + paramUrl);
                        HttpResponse response = httpclient.execute(httppost);
                        loginActivity.onRegisterSuccess();
                    }
                } catch (Exception e) {
                    String s = e.getMessage();
                }
            }
        };
        t.start();
    }

    public void sendJson(BEBusinessCard card) throws Exception
    {
        HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
        HttpResponse response = null;

        HttpPost post = new HttpPost(domainUrl+"/api/Card");

        JSONObject jsonObject = new JSONObject(gson.toJson(card));


        StringEntity se = new StringEntity(jsonObject.toString());
        se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        post.setEntity(se);

        response = client.execute(post);

            CardDetailActivity cardDetailActivity = (CardDetailActivity) _context;
            cardDetailActivity.kill();


    }

    public BEUser GetUserByPhoneNumberJSON(final String phoneNumber) {
        final LoginActivity loginActivity = (LoginActivity) _context;
        user = new BEUser();
        Thread t = new Thread() {
            public void run() {
                String url2 = domainUrl + "/api/User/GetUserByPhoneNumber/"+phoneNumber;
                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;
                String jsonString = null;

                try
                {
                    URL url = new URL(url2);

                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();
                    // Read the input stream into a String
                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();

                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while ((line = reader.readLine()) != null)
                    {
                        // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                        // But it does make debugging a *lot* easier if you print out the completed
                        // buffer for debugging.
                        buffer.append(line + "\n");
                    }

                    jsonString = buffer.toString();
                } catch (IOException e)
                {
                    Log.e("Login", "Error", e);
                } finally
                {
                    if (urlConnection != null)
                    {
                        urlConnection.disconnect();
                    }
                    if (reader != null)
                    {
                        try
                        {
                            reader.close();
                        } catch (final IOException e)
                        {
                            Log.e("Login", "Error closing stream", e);
                        }
                    }
                }
                try
                {
                    JSONObject secretJson = new JSONObject(jsonString);
                    user = gson.fromJson(String.valueOf(secretJson), BEUser.class);


                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        };
        t.start();
        try {
            t.join();
        } catch (Exception e) {}
        return user;
    }
}
