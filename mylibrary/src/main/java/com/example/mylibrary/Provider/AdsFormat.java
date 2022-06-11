package com.example.mylibrary.Provider;

import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.mylibrary.BaseClass;

import org.json.JSONObject;

abstract class AdsFormat {

    private boolean isBannerLoaded = false;
    private boolean isNativeLoaded = false;
    private boolean isInterstitialLoaded = false;

    abstract void preloadAds(JSONObject jsonObject);

    abstract void setAdsId(JSONObject jsonObject);

    abstract void showInterstitialAds();

    abstract void showBannerAds(LinearLayout layout);

    abstract void showNative(LinearLayout layout, ImageView img);

    abstract void showRewardAds();

    public boolean isBannerLoaded() {
        return isBannerLoaded;
    }

    public boolean isInterstitialLoaded() {
        return isInterstitialLoaded;
    }

    public boolean isNativeLoaded() {
        return isNativeLoaded;
    }

    public boolean isRewardLoaded() {
        return isRewardLoaded();
    }
    public void setBannerLoaded(boolean bannerLoaded) {
        isBannerLoaded = bannerLoaded;
    }

    public void setNativeLoaded(boolean nativeLoaded) {
        isNativeLoaded = nativeLoaded;
    }

    public void setInterstitialLoaded(boolean interstitialLoaded) {
        isInterstitialLoaded = interstitialLoaded;
    }
    public void AfterDismissInterstitial(){
        BaseClass.interstitialCallBack();
        BaseClass.showFullAd = false;
        BaseClass.startAdHandler();
    }
}
