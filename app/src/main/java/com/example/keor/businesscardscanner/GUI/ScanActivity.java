package com.example.keor.businesscardscanner.GUI;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.example.keor.businesscardscanner.R;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


public class ScanActivity extends AppCompatActivity {

    Button btnRecapture;
    Button btnNothing;
    Button btnOCR;
    private SliderLayout mDemoSlider;

    ArrayList<String> pictureLocation;
    ArrayList<Bitmap> pictures;
    private Uri imageUri;
    Bitmap selectedBitmap;
    String selectedBitmapPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        findViews();
        setListeners();
        initSettings();
        takePicture();
    }

    private void initSettings() {
        pictures = new ArrayList<>();
        pictureLocation = new ArrayList<>();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //  loadPictures();
    }

    private void loadPictures() {
        mDemoSlider.removeAllSliders();
        HashMap<String, Bitmap> url_maps = new HashMap<String, Bitmap>();
        for (int i = 0; i < pictures.size(); i++) {
            url_maps.put(pictureLocation.get(i), pictures.get(i));
        }


        for (int i = 0; i < url_maps.values().size(); i++) {
            File file = new File(pictureLocation.get(i));
            OutputStream os = null;
            try {
                os = new BufferedOutputStream(new FileOutputStream(file));
            } catch (FileNotFoundException e) {
                Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
                        .show();
            }
            pictures.get(i).compress(Bitmap.CompressFormat.JPEG, 100, os);
            try {
                os.close();
            } catch (IOException e) {
                Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
                        .show();
            }
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
//                    .description(name)
                    .image(file)
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(BaseSliderView baseSliderView) {

                        }
                    });

//            add your extra information
//            textSliderView.bundle(new Bundle());
//            textSliderView.getBundle()
//                    .putString("extra",name);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.stopAutoCycle();
        mDemoSlider.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                selectedBitmap = pictures.get(i);
                selectedBitmapPath = pictureLocation.get(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void takePicture() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File photo = new File(Environment.getExternalStorageDirectory(), java.util.UUID.randomUUID() + ".jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(photo));
        imageUri = Uri.fromFile(photo);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == GUIConstants.CAMERA_CAPTURE_CODE) {
            Uri selectedImage = imageUri;
            getContentResolver().notifyChange(selectedImage, null);

            try {
                Bitmap bitmap = loadImage(selectedImage.getPath());
                pictures.add(bitmap);
                pictureLocation.add(selectedImage.getPath());
                loadPictures();
//                imgView.setImageBitmap(bitmap);
                Toast.makeText(this, selectedImage.toString(),
                        Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, "Failed to load: " + e.getMessage(), Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private Bitmap loadImage(String imgPath) {
        BitmapFactory.Options options;
        try {
            options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Bitmap bitmap = BitmapFactory.decodeFile(imgPath, options);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setListeners() {
        btnNothing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickBtnNothing();
            }
        });
        btnRecapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickBtnRecapture();
            }
        });
        btnOCR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                onClickBtnOCR();

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
                String response = multipost(urlServer, reqEntity);
                ConvertResponseToCard(response);
            }
        });
    }

    private void ConvertResponseToCard(String response) {

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
            conn.addRequestProperty("Content-length", reqEntity.getContentLength()+"");
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

    private void onClickBtnOCR() {
        File file = new File(selectedBitmapPath);
        int file_sizeBytes = Integer.parseInt(String.valueOf(file.length()));
        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;
        String pathToOurFile = selectedBitmapPath;
        String urlServer = "http://bcr1.intsig.net/BCRService/BCR_VCF2?user=" + "keor@bws.dk" + "&pass=" + "PWELTERB6PBRYFJL" + "&lang=15&size=" + file_sizeBytes;
        String encodedUsernamePassword = getB64Auth("keor@bws.dk", "321keor654");

        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary =  "*****";

        String[] names2 = selectedBitmapPath.split("/");
        String name = names2[4].split("\\.")[0];


        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1*1024*1024;

        try
        {
            FileInputStream fileInputStream = new FileInputStream(new File(pathToOurFile) );

            URL url = new URL(urlServer);
            connection = (HttpURLConnection) url.openConnection();

            // Allow Inputs & Outputs.
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            // Set HTTP method to POST.
            connection.setRequestProperty("Authorization", "Basic " + encodedUsernamePassword);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "text/vcard");
            connection.setRequestProperty("Content-Type", "text/vcard");
            connection.setRequestProperty("Connection", "Keep-Alive");

            outputStream = new DataOutputStream( connection.getOutputStream() );
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"" + name + "\";filename=\"" + name + ".jpg" +"\"" + lineEnd);
            outputStream.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // Read file
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0)
            {
                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            outputStream.writeBytes(lineEnd);
            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);


            // Responses from the server (code and message)
            int serverResponseCode = connection.getResponseCode();
            String serverResponseMessage = connection.getResponseMessage();

            fileInputStream.close();
            outputStream.flush();
            outputStream.close();
        }
        catch (Exception ex)
        {
            Toast.makeText(this, "Failed to load, exception: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private String getB64Auth (String login, String pass) {
        String source=login+":"+pass;
        String ret="Basic "+Base64.encodeToString(source.getBytes(),Base64.URL_SAFE|Base64.NO_WRAP);
        return ret;
    }

    private void onClickBtnRecapture() {
        takePicture();
    }

    private void onClickBtnNothing() {

    }

    private void findViews() {
        btnNothing = (Button) findViewById(R.id.btnNothing);
        btnOCR = (Button) findViewById(R.id.btnOCR);
        btnRecapture = (Button) findViewById(R.id.btnRecapture);
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);
//        imgView = (ImageView) findViewById(R.id.imgView);
    }

}
