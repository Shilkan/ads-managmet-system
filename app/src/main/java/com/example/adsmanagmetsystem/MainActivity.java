package com.example.adsmanagmetsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mylibrary.BaseClass;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    EditText edt_adsJson;
    View.OnClickListener onClickListener;

    Button btnFullScreen, btnNativeBanner, btnUpdateJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edt_adsJson = findViewById(R.id.edt_adsJson);
        btnFullScreen = findViewById(R.id.btnFullScreen);
        btnNativeBanner = findViewById(R.id.btnNativeBanner);
        btnUpdateJson = findViewById(R.id.btnUpdateJson);
        edt_adsJson.setText(getJsonString());

        loadAsds();


        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {

                    case R.id.btnFullScreen:

                        BaseClass.getInstance(MainActivity.this).showInterstitialAds(() -> {
                            startActivity(new Intent(MainActivity.this, ActivityFullScreen.class));
                        });

                        break;
                    case R.id.btnNativeBanner:
                        startActivity(new Intent(MainActivity.this, ActivityNativeBanner.class));
                        break;
                    case R.id.btnUpdateJson:
                        loadAsds();
                        break;
                }
            }
        };

        btnFullScreen.setOnClickListener(onClickListener);
        btnNativeBanner.setOnClickListener(onClickListener);
        btnUpdateJson.setOnClickListener(onClickListener);


    }

    private void loadAsds() {
        JSONObject jsonObject = null;
        try {
            String jsonString = edt_adsJson.getText().toString();
            jsonObject = new JSONObject(jsonString);
            BaseClass.getInstance(MainActivity.this).initAds(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private String getJsonString() {
        return "{\n" +
                "  \"AdShow\": \"true\",\n" +
                "  \"ShowAd_After_Time_In_Sec\": \"1\",\n" +
                "  \"ShowAd_After_Taps\": \"1\",\n" +
                "  \"FirstAd\": \"Applovin\",\n" +
                "    \"SecondAd\":\"Facebook\",\n" +
                "\n" +
                "  \"Unity\":{\n" +
                "  \"AppId\": \"4388627\",\n" +
                "  \"FullScreen\": \"video\",\n" +
                "  \"Banner\": \"bannerads\",\n" +
                "    \"Native\": \"ca-app-pub-3940256099942544/1044960115\"\n" +
                "}, \n" +
                "  \"Applovin\":{\n" +
                "  \"AppId\": \"\",\n" +
                "  \"FullScreen\": \"YOUR_AD_UNIT_ID\",\n" +
                "  \"Banner\": \"241e9a23c064931c\",\n" +
                "    \"Native\": \"VID_HD_9_16_39S_APP_INSTALL#YOUR_PLACEMENT_ID\"\n" +
                "}\n" +
                "}";
    }
}