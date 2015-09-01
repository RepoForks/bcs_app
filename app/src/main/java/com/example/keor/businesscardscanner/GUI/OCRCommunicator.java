package com.example.keor.businesscardscanner.GUI;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by athy on 01-09-2015.
 */
public class OCRCommunicator extends AsyncTask<Void, Void, String> {

    String response;
    ScanActivity scanActivity;
    Bitmap selectedBitmap;
    String selectedBitmapPath;

    public OCRCommunicator(ScanActivity scanActivity, Bitmap selectedBitmap, String selectedBitmapPath) {
        this.scanActivity =  scanActivity;
        this.selectedBitmap = selectedBitmap;
        this.selectedBitmapPath = selectedBitmapPath;
    }

    @Override
    protected String doInBackground(Void... voids) {
        File file = new File(selectedBitmapPath);
        int file_sizeBytes = Integer.parseInt(String.valueOf(file.length()));
        String urlServer = "http://bcr1.intsig.net/BCRService/BCR_VCF2?user=" + "keor@bws.dk" + "&pass=" + "PWELTERB6PBRYFJL" + "&lang=15&size=" + file_sizeBytes;

        String[] names2 = selectedBitmapPath.split("/");
        String name = names2[4].split("\\.")[0];

        Bitmap bitmap = selectedBitmap;
        String filename = name + ".jpg";
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        ContentBody contentPart = new ByteArrayBody(bos.toByteArray(), filename);

        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        reqEntity.addPart("picture", contentPart);
        String multipost =
                response = multipost(urlServer, reqEntity);
        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        scanActivity.ConvertResponseToCard(response);
    }



    private static String multipost(String urlString, MultipartEntity reqEntity) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.addRequestProperty("Content-length", reqEntity.getContentLength() + "");
            conn.addRequestProperty(reqEntity.getContentType().getName(), reqEntity.getContentType().getValue());

            OutputStream os = conn.getOutputStream();
            reqEntity.writeTo(conn.getOutputStream());
            os.close();
            conn.connect();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return readStream(conn.getInputStream());
            }

        } catch (Exception e) {
//            Log.e(TAG, "multipart post error " + e + "(" + urlString + ")");
        }
        return null;
    }

    private static String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return builder.toString();
    }
}
