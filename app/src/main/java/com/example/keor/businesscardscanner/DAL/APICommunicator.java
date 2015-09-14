package com.example.keor.businesscardscanner.DAL;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.example.keor.businesscardscanner.Model.BEBusinessCard;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestFactory;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by keor on 03-09-2015.
 */
public class APICommunicator extends AsyncTask<Void, Void, String> {
    BEBusinessCard _card;

    public APICommunicator(BEBusinessCard card) {
        _card = card;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            HttpClient httpclient = new DefaultHttpClient();
            String baseUrl = "http://pto-udv.bws.dk:24334/api/card?";
            String paramUrl = URLEncoder.encode("&firstName=" + _card.getFirstname() + "&lastName=" + _card.getLastname() + "&address=" + _card.getAddress() + "&phoneNumber=" + _card.getPhonenumber() + "&fax=" + _card.getFax() + "&country=" + _card.getCountry() + "&city=" + _card.getCity() + "&postal=" + _card.getPostal() + "&company=" + _card.getCompany() + "&title=" + _card.getTitle() + "&email=" + _card.getEmail() + "&homepage=" + _card.getHomepage() + "&other=" + _card.getOther() + "&createdDate=" + _card.getCreatedDate() + "&createdUserId=" + _card.getCreatedUserId() + "&isDeleted=" + _card.getIsDeleted(), "UTF-8");
            paramUrl = paramUrl.replace("%26","&");
            paramUrl = paramUrl.replace("%3D","=");
            HttpPost httppost = new HttpPost(baseUrl + paramUrl);
            HttpResponse response = httpclient.execute(httppost);

        } catch (Exception e) {
            // Catch Protocol Exception
            e.printStackTrace();
        }
        return "";
    }
}
