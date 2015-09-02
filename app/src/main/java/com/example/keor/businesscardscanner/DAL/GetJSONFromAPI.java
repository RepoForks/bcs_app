package com.example.keor.businesscardscanner.DAL;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Kevin Ørskov.
 */
public class GetJSONFromAPI extends AsyncTask<String, Void, JSONArray> {

    JSONArray jsonArray;
    JSONObject jsnobject;
    String result;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected JSONArray doInBackground(String... params) {

        HttpURLConnection connection = null;
        BufferedReader br = null;
        URL url;
        try {
            url = new URL(params[0]);

            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream is = connection.getInputStream();
            StringBuilder sb = new StringBuilder();
            if (is == null) {
                return null;
            }
            br = new BufferedReader(new InputStreamReader(is, "iso-8859-1"));
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            if (sb.length() == 0) {
                return null;
            }
            is.close();
            result = sb.toString();

        } catch (IOException e) {
            Log.e("Get Clients", "Error ", e);
            return null;
        }
        if (connection != null) {
            connection.disconnect();
        }
        if (br != null) {
            try {
                br.close();
            } catch (final IOException e) {
                Log.e("GET Clients", "Error closing stream", e);
            }
        }

        try {
            jsnobject = new JSONObject(result);
            jsonArray = (JSONArray) jsnobject.get("viewentry");
        } catch (JSONException e) {
            Log.e("JSON", "Error creating JSON", e);
        }

        return jsonArray;

    }
}
