package com.example.keor.businesscardscanner.GUI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.example.keor.businesscardscanner.Model.BEBusinessCard;
import com.example.keor.businesscardscanner.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;


public class ScanActivity extends AppCompatActivity {

    private Toolbar toolbar;
    Button btnNext;
    Button btnDelete;
    Button btnOCR;
    private SliderLayout mDemoSlider;
    ProgressDialog progress;

    ArrayList<String> pictureLocation;
    ArrayList<Bitmap> pictures;
    private Uri imageUri;
    Bitmap selectedBitmap;
    String selectedBitmapPath;
    BEBusinessCard createdCard = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        findViews();
        setListeners();
        initSettings();
        initToolbar();
        takePicture();
    }

    private void initToolbar() {
        toolbar.setTitle("Scan a card");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_camera);
        setSupportActionBar(toolbar);
    }

    private void initSettings() {
        pictures = new ArrayList<>();
        pictureLocation = new ArrayList<>();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //  loadPictures();
    }

    private void setStates() {
        btnOCR.setEnabled(pictures.size() != 0);
        btnDelete.setEnabled(pictures.size() != 0);
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
                if (pictures.size() == 1) {
                    selectedBitmap = pictures.get(0);
                    selectedBitmapPath = pictureLocation.get(0);
                }
                setStates();
                loadPictures();
//                Toast.makeText(this, selectedImage.toString(),
//                        Toast.LENGTH_LONG).show();
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
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickBtnDelete();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickBtnNextPicture();
            }
        });
        btnOCR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickBtnOCR();

            }
        });
    }

    private void onClickBtnOCR() {
        btnOCR.setEnabled(false);
        OCRCommunicator ocrCommunicator = new OCRCommunicator(this, selectedBitmap, selectedBitmapPath);
        ocrCommunicator.execute();

        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Scanning...");
        progress.setCancelable(false);
        progress.show();
// To dismiss the dialog
//        progress.dismiss();

//        ConvertResponseToCard(response);
    }

    public void ConvertResponseToCard(String response) {
        BEBusinessCard card = new BEBusinessCard();

        try {
            //Firstname
            if (response.contains("FN;CHARSET=utf-8:")) {
                String name = response.split("FN;CHARSET=utf-8:")[1];
                String firstName = name.split(" ")[0];
                card.setFirstname(firstName);

                //Lastname
                String lastName = name.split(" ")[1];
                lastName = lastName.split("X-IS-INFO")[0];
                card.setLastname(lastName);
            }
            //City
            if (response.contains("CITY???")) {
                String city = response.split("TITLE:")[1];
                city = city.split("X-IS-INFO")[0];
                card.setCity(city);
            }

            //Postal
            if (response.contains("POSTAL???")) {
                String postal = response.split("TITLE:")[1];
                postal = postal.split("X-IS-INFO")[0];
                card.setPostal(postal);
            }

            //Country
            if (response.contains("COUNTRY???")) {
                String country = response.split("TITLE:")[1];
                country = country.split("X-IS-INFO")[0];
                card.setCountry(country);
            }

            //Address
            if (response.contains("LABEL;WORK;PREF;CHARSET=utf-8:")) {
                String address = response.split("LABEL;WORK;PREF;CHARSET=utf-8:")[1];
                address = address.split("X-IS-INFO")[0];
                card.setAddress(address);
            }

            //Company
            if (response.contains("ORG;CHARSET=utf-8:;")) {
                String company = response.split("ORG;CHARSET=utf-8:;")[1]; // LABEL;WORK;PREF;CHARSET=utf-8: // RG;CHARSET=utf-8:;
                company = company.split("X-IS-INFO")[0];
                card.setCompany(company);
            }

            //Phonenumber
            if (response.contains("TEL;CELL;VOICE:")) {
                String phoneNumber = response.split("TEL;CELL;VOICE:")[1];
                phoneNumber = phoneNumber.split("X-IS-INFO")[0];
                card.setPhonenumber(phoneNumber);
            }

            //Title
            if (response.contains("ORG;CHARSET=utf-8:;")) {
                String title = response.split("ORG;CHARSET=utf-8:;")[1];
                title = title.split("X-IS-INFO")[0];
                card.setTitle(title);
            }

            //Homepage
            if (response.contains("URL;WORK;CHARSET=utf-8:")) {
                String homepage = response.split("URL;WORK;CHARSET=utf-8:")[1];
                homepage = homepage.split("X-IS-INFO")[0];
                card.setHomepage(homepage);
            }

            //Fax
            if (response.contains("TEL;WORK;FAX:")) {
                String fax = response.split("TEL;WORK;FAX:")[1];
                fax = fax.split("X-IS-INFO")[0];
                card.setFax(fax);
            }

            //Email
            if (response.contains("EMAIL;PREF;INTERNET:")) {
                String email = response.split("EMAIL;PREF;INTERNET:")[1];
                email = email.split("X-IS-INFO")[0];
                card.setEmail(email);
            }

            //Other /note
            if (response.contains("NOTE;CHARSET=utf-8:")) {
                String other = response.split("NOTE;CHARSET=utf-8:")[1];
                other = other.split("X-IS-INFO")[0];
                card.setOther(other);
            }

            //EncodedImage
            if (response.contains("URL;WORK;CHARSET=utf-8:")) {

            }

            //isDeleted = false
            if (response.contains("URL;WORK;CHARSET=utf-8:")) {

            }

            //createdUserId = ??
            if (response.contains("URL;WORK;CHARSET=utf-8:")) {

            }
            createdCard = card;
            btnOCR.setEnabled(true);
            progress.dismiss();
            Toast.makeText(this, "Finished scanning", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            Toast.makeText(this, "fejl: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }






    private void onClickBtnDelete() {
        pictures.remove(mDemoSlider.getCurrentPosition());
        pictureLocation.remove(mDemoSlider.getCurrentPosition());
        mDemoSlider.removeSliderAt(mDemoSlider.getCurrentPosition());
        if (pictures.size() == 0) {
//            mDemoSlider.removeAllViews();
//            mDemoSlider = (SliderLayout) findViewById(R.id.slider);
        }
        loadPictures();
        setStates();
    }

    private void onClickBtnNextPicture() {
        takePicture();
    }

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnOCR = (Button) findViewById(R.id.btnOCR);
        btnNext = (Button) findViewById(R.id.btnNextPicture);
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_continue) {
            if (pictures.size() != 0) {
                if (createdCard == null){
                    Toast.makeText(this, "Please use OCR on a picture", Toast.LENGTH_SHORT).show();
                }
                else{
                Intent saveIntent = new Intent();
                    Bundle b = new Bundle();
                    b.putSerializable(GUIConstants.CARD, createdCard);
                    saveIntent.putExtras(b);
                    GUIConstants.SAVE_STATE_VALUE = true;
                    saveIntent.putExtra(GUIConstants.SAVE_STATE, GUIConstants.SAVE_STATE_VALUE);
                saveIntent.setClass(this, CardDetailActivity.class);
                startActivity(saveIntent);
                }
            } else {
                Toast.makeText(this, "Please take atleast 1 picture", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
