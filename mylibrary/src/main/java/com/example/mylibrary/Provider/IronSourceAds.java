package com.example.mylibrary.Provider;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.mylibrary.AdsConstant;

import org.json.JSONException;
import org.json.JSONObject;

public class IronSourceAds extends AdsFormat {

    //Defaults
    public static IronSourceAds instance;
    public static Activity activity;

    //Unity Ids
    private static String unityGameID = "4388627";
    private static String RewardAdId = "Rewarded_Android";
    private static String fullScreenAdId = "Interstitial_Android";
    private static String NativeId = "";
    private static String Banner_ID = "Banner_Android";//4590626(my)

    //Unity loaded tags


    //Unity ad variables
    private View bannerView;


    public IronSourceAds(Activity activity) {
        this.activity = activity;
    }

    public static IronSourceAds getInstance(Activity activity1) {
        activity = activity1;
        if (instance == null) {
            instance = new IronSourceAds(activity1);
        }
        return instance;
    }

    @Override
    public void preloadAds(JSONObject jsonObject) {
        setAdsId(jsonObject);
    }

    @Override
    public void setAdsId(JSONObject jsonObject) {
        try {
            JSONObject googleJson = jsonObject.getJSONObject(AdsConstant.IronSourceADS);
            Banner_ID = googleJson.getString(AdsConstant.BannerAD_ID);
            fullScreenAdId = googleJson.getString(AdsConstant.FullScreen_ID);
            NativeId = googleJson.getString(AdsConstant.Native_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showInterstitialAds() {

    }

    @Override
    public void showBannerAds(LinearLayout layout) {

    }

    @Override
    public void showNative(LinearLayout layout, ImageView img) {

    }

    @Override
    public void showRewardAds() {

    }
}
