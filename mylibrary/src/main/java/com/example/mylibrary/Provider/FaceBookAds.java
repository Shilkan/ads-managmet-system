package com.example.mylibrary.Provider;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mylibrary.AdsConstant;
import com.example.mylibrary.R;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FaceBookAds extends AdsFormat {

    //Defaults
    public static FaceBookAds instance;
    public static Activity activity;

    //FaceBook Ids
    private static String RewardAdId = "";
    private static String fullScreenAdId = "";//CAROUSEL_IMG_SQUARE_LINK
    private static String NativeId = "";//VID_HD_9_16_39S_APP_INSTALL
    private static String Banner_ID = "";//IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID

    //FaceBook ad variables
    String TAG = "AMS@FaceBookAds";
    com.facebook.ads.AdView bannerView;
    NativeAd nativeAd;
    InterstitialAd interstitialAd;


    public FaceBookAds(Activity activity) {
        this.activity = activity;
    }

    public static FaceBookAds getInstance(Activity activity) {
        if (instance == null) {
            instance = new FaceBookAds(activity);
        }
        return instance;
    }


    @Override
    public void preloadAds(JSONObject jsonObject) {
        Log.i(TAG, "preloadAds: Method Call");
        setAdsId(jsonObject);
        AudienceNetworkAds.initialize(activity);
        AdSettings.addTestDevice("9e70fc0d-9de9-4d87-b1d1-c78503a1037c");
        preloadBannerAds();
        preloadNativeAds();
        preloadInterstitialAd();

    }

    private void preloadInterstitialAd() {
        Log.i(TAG, "preloadInterstitialAd Method Call: ");
        if (isInterstitialLoaded()) {
            Log.i(TAG, "preloadInterstitialAd Already Preloaded :return");
            return;
        }
        interstitialAd = new InterstitialAd(activity, fullScreenAdId);
        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                Log.e(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                Log.e(TAG, "Interstitial ad dismissed.");
                AfterDismissInterstitial();
                preloadInterstitialAd();
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                setInterstitialLoaded(true);
            }

            @Override
            public void onAdClicked(Ad ad) {
                Log.d(TAG, "Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                Log.d(TAG, "Interstitial ad impression logged!");
            }
        };
        interstitialAd.loadAd(
                interstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());
    }

    private void preloadNativeAds() {
        Log.i(TAG, "preloadNativeAds Method Call: ");
        if (isNativeLoaded()){
            Log.i(TAG, "preloadNativeAds Already Preloaded :return");
            return;
        }
        nativeAd = new NativeAd(activity, NativeId);

        NativeAdListener nativeAdListener = new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {
                Log.e(TAG, "Native ad finished downloading all assets.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                Log.e(TAG, "Native ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                Log.d(TAG, "Native ad is loaded and ready to be displayed!");
                setNativeLoaded(true);
            }

            @Override
            public void onAdClicked(Ad ad) {
                Log.d(TAG, "Native ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                Log.d(TAG, "Native ad impression logged!");
            }
        };
        nativeAd.loadAd(
                nativeAd.buildLoadAdConfig()
                        .withAdListener(nativeAdListener)
                        .build());
    }

    private void preloadBannerAds() {
        Log.i(TAG, "preloadBannerAds Method Call: ");
        if (isBannerLoaded()) {
            Log.i(TAG, "preloadBannerAds Already Preloaded :return");
            return;
        }
        bannerView = new com.facebook.ads.AdView(activity, Banner_ID, com.facebook.ads.AdSize.BANNER_HEIGHT_50);
        com.facebook.ads.AdListener adListener = new com.facebook.ads.AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                setBannerLoaded(false);
                Log.e(TAG, "onError: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                setBannerLoaded(true);
                Log.e(TAG, "onAdLoaded: " + ad.getPlacementId());
            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {
            }
        };
        bannerView.loadAd(bannerView.buildLoadAdConfig().withAdListener(adListener).build());
    }

    @Override
    void setAdsId(JSONObject jsonObject) {
        try {
            JSONObject googleJson = jsonObject.getJSONObject(AdsConstant.FaceBookADS);
            Banner_ID = googleJson.getString(AdsConstant.BannerAD_ID);
            fullScreenAdId = googleJson.getString(AdsConstant.FullScreen_ID);
            NativeId = googleJson.getString(AdsConstant.Native_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showInterstitialAds() {
        Log.v(TAG, "showInterstitialAds");
        if (interstitialAd != null) {
            interstitialAd.show();
            setInterstitialLoaded(false);
        }
    }

    @Override
    public void showBannerAds(LinearLayout adLayout) {
        Log.v(TAG, "showBannerAds");
        if (adLayout != null) {
            adLayout.removeAllViews();
            if (bannerView != null) {
                adLayout.addView(bannerView);
                setBannerLoaded(false);
                preloadBannerAds();
            }
        }
    }

    private void inflateAd(NativeAd nativeAd, LinearLayout AdLayout) {

        nativeAd.unregisterView();
        LayoutInflater inflater = LayoutInflater.from(activity);
        View adView = inflater
                .inflate(R.layout.native_facebook, null);
        com.facebook.ads.NativeAdLayout nativeAdLayout = (com.facebook.ads.NativeAdLayout) adView.findViewById(R.id.native_ad_container);
//        LayoutInflater inflater = LayoutInflater.from(activity);
        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
//        adView = (LinearLayout) inflater.inflate(R.layout.native_ad_layout_1, nativeAdLayout, false);
        AdLayout.addView(adView);

        // Add the AdOptionsView
        LinearLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(activity, nativeAd, nativeAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        // Create native UI using the ad metadata.
        MediaView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

        // Create a list of clickable views
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);

        // Register the Title and CTA button to listen for clicks.
        nativeAd.registerViewForInteraction(
                adView, nativeAdMedia, nativeAdIcon, clickableViews);
    }

    @Override
    public void showNative(LinearLayout layout, ImageView img) {
        Log.v(TAG, "showNative");
        if (layout != null) {
            layout.removeAllViews();
            if (nativeAd != null) {
                inflateAd(nativeAd, layout);
                setNativeLoaded(false);
                if (img != null) {
                    img.setVisibility(View.GONE);
                }
                preloadNativeAds();
            }
        }
    }

    @Override
    void showRewardAds() {

    }
}
