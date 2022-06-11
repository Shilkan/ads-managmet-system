package com.example.mylibrary.Provider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.mylibrary.AdsConstant;
import com.example.mylibrary.BaseClass;
import com.example.mylibrary.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Objects;

public class GoogleAds {

    //Defaults
    public static GoogleAds instance;
    public static Activity activity;

    //Google Ids
    public static String AD_MOB_BANNER = "";            //"ca-app-pub-3940256099942544/6300978111";
    public static String AD_MOB_INTERSTITIAL = "";      //"ca-app-pub-3940256099942544/1033173712";
    public static String AD_MOB_NATIVE = "";            //"ca-app-pub-3940256099942544/1044960115";

    //google loaded tags
    public static boolean isGoogleBannerLoaded;
    public static boolean isGoogleNativeLoaded;
    public static boolean isGoogleInterstitialLoaded;

    //google ad variables
    private static AdView googleBannerAd;
    private static NativeAd googleNativeAd;
    public static com.google.android.gms.ads.interstitial.InterstitialAd googleInterstitialAd;
    private final String TAG = "AMS@Google_Ads";

    public GoogleAds(Activity activity1) {
        activity = activity1;
    }

    public static GoogleAds getInstance(Activity activity1) {
        if (instance == null) {
            instance = new GoogleAds(activity1);
        }
        return instance;

    }

    private AdSize getGoogleBannerAdSize() {
        //info support only in android studio 4.2
            /*Display display = context.getWindowManager().getDefaultDisplay();
            DisplayMetrics outMetrics = new DisplayMetrics();
            display.getRealMetrics(outMetrics);

            float density = context.getResources().getDisplayMetrics().density;
            float widthPixels = outMetrics.widthPixels;

            int adWidth = (int) (widthPixels / density);

            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth);*/

        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth);
    }

    public void showGoogleBanner(LinearLayout adLayout) {
        if (adLayout != null) {
            adLayout.removeAllViews();
            if (googleBannerAd != null) {
                adLayout.addView(googleBannerAd);
                isGoogleBannerLoaded = false;
                preloadGoogleBannerAd();
            }
        }
    }


    public void showGoogleInterstitial() {
        com.google.android.gms.ads.interstitial.InterstitialAd googleInterstitial = googleInterstitialAd;

        if (googleInterstitial != null) {
            googleInterstitial.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    BaseClass.interstitialCallBack();
                    googleInterstitialAd = null;
                    preloadGoogleInterstitialAd();
                }

                @Override
                public void onAdShowedFullScreenContent() {
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                }
            });

            googleInterstitial.show(activity);

            BaseClass.showFullAd = false;
            isGoogleInterstitialLoaded = false;
            Log.d("TEMP", "google onAdLoaded: " + isGoogleInterstitialLoaded);
            BaseClass.startAdHandler();
        }

    }


    public void preloadGoogleAds(JSONObject jsonObject) {
        Log.i(TAG, "preloadAds: Method Call");
        RequestConfiguration configuration = new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("9BEB8867DBD88CBE438401DF85350419")).build();
        MobileAds.setRequestConfiguration(configuration);
        setAdsId(jsonObject);
        preloadGoogleBannerAd();
        preloadGoogleNativeAd();
        preloadGoogleInterstitialAd();
    }

    public void preloadGoogleInterstitialAd() {
        Log.i(TAG, "preloadInterstitialAd Method Call: id");
        if (googleInterstitialAd == null) {
            AdRequest adRequest = new AdRequest.Builder().build();
            com.google.android.gms.ads.interstitial.InterstitialAd.load(activity, AD_MOB_INTERSTITIAL, adRequest, new InterstitialAdLoadCallback() {
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    Log.d(TAG, "googleInterstitialPreLoad: " + loadAdError.getMessage());
                }

                @Override
                public void onAdLoaded(@NonNull com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {
                    super.onAdLoaded(interstitialAd);
                    Log.d(TAG, "googleInterstitialPreLoad onAdLoaded: " + interstitialAd);
                    googleInterstitialAd = interstitialAd;
                    isGoogleInterstitialLoaded = true;
                    Log.d("TEMP", "google onAdLoaded: " + isGoogleInterstitialLoaded);
                }
            });
        }
    }


    @SuppressLint("MissingPermission")
    private void preloadGoogleNativeAd() {
        if (isGoogleNativeLoaded) {
            return;
        }

        AdLoader.Builder builder = new AdLoader.Builder(activity, AD_MOB_NATIVE);

        builder.forNativeAd(nativeAd -> {
            Log.d(TAG, "preloadGoogleNativeAd onNativeAdLoaded: ");
            googleNativeAd = nativeAd;
            isGoogleNativeLoaded = true;
        }).withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(LoadAdError errorCode) {
                Log.d(TAG, "preloadGoogleNativeAd onAdFailedToLoad: " + errorCode);
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();

            }
        });

        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(true)
                .build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();

        builder.withNativeAdOptions(adOptions);
        builder.build().loadAd(new AdRequest.Builder().build());
    }

    @SuppressLint("MissingPermission")
    private void preloadGoogleBannerAd() {
        Log.i(TAG, "preloadBannerAds Method Call: ");
        if (isGoogleBannerLoaded) {
            Log.i(TAG, "preloadBannerAds Already Preloaded :return");
            return;
        }
        googleBannerAd = new AdView(activity);
        googleBannerAd.setAdSize(getGoogleBannerAdSize());
        googleBannerAd.setAdUnitId(AD_MOB_BANNER);

        AdRequest adRequest = new AdRequest.Builder().build();
        googleBannerAd.loadAd(adRequest);
        googleBannerAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.d(TAG, "preloadGoogleBannerAd onAdLoaded");
                isGoogleBannerLoaded = true;
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError errorCode) {
                Log.d(TAG, "preloadGoogleBannerAd onAdFailedToLoad: " + errorCode);
            }
        });
    }

    public void showGoogleNative(LinearLayout layout, ImageView img) {
        if (layout != null) {
            layout.removeAllViews();
            if (googleNativeAd != null) {
                inflateGoogleNative(googleNativeAd, layout);
                isGoogleNativeLoaded = false;
                if (img != null) {
                    img.setVisibility(View.GONE);
                }
                preloadGoogleNativeAd();
            }
        }
    }

    @SuppressLint("InflateParams")
    private void inflateGoogleNative(NativeAd nativeAd, LinearLayout ad_layout) {
        if (!activity.isFinishing()) {
            try {
                ad_layout.setVisibility(View.VISIBLE);
                LayoutInflater inflater = LayoutInflater.from(activity);
                View view = inflater
                        .inflate(R.layout.native_admobe, null);
                ad_layout.addView(view);

                NativeAdView adView = view.findViewById(R.id.uadview);
                adView.setMediaView(adView.findViewById(R.id.ad_media));
                adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
                adView.setBodyView(adView.findViewById(R.id.ad_body));
                adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
                adView.setIconView(adView.findViewById(R.id.ad_app_icon));
                adView.setPriceView(adView.findViewById(R.id.ad_price));
                adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
                adView.setStoreView(adView.findViewById(R.id.ad_store));
                adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

                ((TextView) Objects.requireNonNull(adView.getHeadlineView())).setText(nativeAd.getHeadline());
                Objects.requireNonNull(adView.getMediaView()).setMediaContent(Objects.requireNonNull(nativeAd.getMediaContent()));

                if (nativeAd.getBody() == null) {
                    Objects.requireNonNull(adView.getBodyView()).setVisibility(View.INVISIBLE);
                } else {
                    Objects.requireNonNull(adView.getBodyView()).setVisibility(View.VISIBLE);
                    ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
                }

                if (nativeAd.getCallToAction() == null) {
                    Objects.requireNonNull(adView.getCallToActionView()).setVisibility(View.INVISIBLE);
                } else {
                    Objects.requireNonNull(adView.getCallToActionView()).setVisibility(View.VISIBLE);
                    ((TextView) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
                }

                if (nativeAd.getIcon() == null) {
                    Objects.requireNonNull(adView.getIconView()).setVisibility(View.GONE);
                } else {
                    ((ImageView) Objects.requireNonNull(adView.getIconView())).setImageDrawable(
                            nativeAd.getIcon().getDrawable());
                    adView.getIconView().setVisibility(View.VISIBLE);
                }


                if (nativeAd.getStarRating() == null) {
                    Objects.requireNonNull(adView.getStarRatingView()).setVisibility(View.INVISIBLE);
                } else {
                    ((RatingBar) Objects.requireNonNull(adView.getStarRatingView()))
                            .setRating(nativeAd.getStarRating().floatValue());
                    adView.getStarRatingView().setVisibility(View.VISIBLE);
                }

                if (nativeAd.getAdvertiser() == null) {
                    Objects.requireNonNull(adView.getAdvertiserView()).setVisibility(View.INVISIBLE);
                } else {
                    ((TextView) Objects.requireNonNull(adView.getAdvertiserView())).setText(nativeAd.getAdvertiser());
                    adView.getAdvertiserView().setVisibility(View.VISIBLE);
                }

                adView.setNativeAd(nativeAd);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setAdsId(JSONObject jsonObject) {
        try {
            JSONObject googleJson = jsonObject.getJSONObject(AdsConstant.GoogleADS);
            AD_MOB_BANNER = googleJson.getString(AdsConstant.BannerAD_ID);
            AD_MOB_INTERSTITIAL = googleJson.getString(AdsConstant.FullScreen_ID);
            AD_MOB_NATIVE = googleJson.getString(AdsConstant.Native_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
