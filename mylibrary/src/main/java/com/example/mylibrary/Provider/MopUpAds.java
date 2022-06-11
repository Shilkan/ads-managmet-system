package com.example.mylibrary.Provider;

import static com.mopub.common.logging.MoPubLog.LogLevel.DEBUG;
import static com.mopub.common.logging.MoPubLog.LogLevel.INFO;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mylibrary.AdsConstant;
import com.example.mylibrary.BaseClass;
import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;
import com.mopub.mobileads.MoPubView;
import com.mopub.mobileads.base.BuildConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MopUpAds extends AppCompatActivity {

    //Defaults
    public static MopUpAds instance;
    public static Activity activity;

    //Mopub Ids
    public static String MOPUB_UNIT_ID = "";           // "24534e1901884e398f1253216226017e";
    public static String MOP_UP_INTERSTITIAL = "";     // "b195f8dd8ded45fe847ad89ed1d016da";
    public static String MOP_UP_BANNER = "";           // "252412d5e9364a05ab77d9396346d73d";
    public static String MOP_UP_NATIVE = "";           // "fb986119058e4f69ac2d5efa8410887a";

    //mop up loaded tags
    public static boolean isMoPubBannerLoaded;
    public static boolean isMoPubNativeLoaded;
    public static boolean isMoPubInterstitialLoaded;

    //mop up ad variables
    private static MoPubView moPubBannerView;
    private static MoPubView moPubNativeView;
    private final String TAG = "AMS@MopUp_Ads";
    public static MoPubInterstitial moPubInterstitial;

    public MopUpAds(Activity activity) {
        this.activity = activity;
    }

    public static MopUpAds getInstance(Activity activity1) {
        activity = activity1;
        if (instance == null) {
            instance = new MopUpAds(activity1);
        }
        return instance;
    }

    public void showMopUpBanner(LinearLayout adLayout) {
        if (adLayout != null) {
            adLayout.removeAllViews();
            if (moPubBannerView != null) {
                adLayout.addView(moPubBannerView);
                isMoPubBannerLoaded = false;
                preloadMoPubBannerAd();
            }
        }
    }

    public void showMopUpNative(LinearLayout layout, ImageView img) {
        if (layout != null) {
            layout.removeAllViews();
            layout.setGravity(Gravity.CENTER);
            layout.setPadding(12, 12, 12, 12);
            if (moPubNativeView != null) {
                if (moPubNativeView.getParent() != null) {
                    ((ViewGroup) moPubNativeView.getParent()).removeView(moPubNativeView);
                }
                layout.addView(moPubNativeView);
                if (img != null) {
                    img.setVisibility(View.GONE);
                }
                isMoPubNativeLoaded = false;
                preloadMoPubNativeAd();
            }
        }
    }


    private void preloadMoPubNativeAd() {
        if (isMoPubNativeLoaded)
            return;
        moPubNativeView = new MoPubView(activity);

        moPubNativeView.setAdUnitId(MOP_UP_NATIVE);
        moPubNativeView.setAdSize(MoPubView.MoPubAdSize.HEIGHT_250);
        moPubNativeView.loadAd();
        moPubNativeView.setBannerAdListener(new MoPubView.BannerAdListener() {
            @Override
            public void onBannerLoaded(@NonNull MoPubView moPubView) {
                Log.d(TAG, "onNativeLoaded: ");
                isMoPubNativeLoaded = true;
            }

            @Override
            public void onBannerFailed(MoPubView moPubView, MoPubErrorCode moPubErrorCode) {
                Log.d(TAG, "onNativeFailed: ");
            }

            @Override
            public void onBannerClicked(MoPubView moPubView) {

            }

            @Override
            public void onBannerExpanded(MoPubView moPubView) {

            }

            @Override
            public void onBannerCollapsed(MoPubView moPubView) {

            }
        });
    }

    private void preloadMoPubBannerAd() {
        Log.i(TAG, "preloadBannerAds Method Call: ");
        if (isMoPubBannerLoaded) {
            Log.i(TAG, "preloadBannerAds Already Preloaded :return");
            return;
        }
        moPubBannerView = new MoPubView(activity);

        moPubBannerView.setAdUnitId(MOP_UP_BANNER);
        moPubBannerView.setAdSize(MoPubView.MoPubAdSize.HEIGHT_50);
        moPubBannerView.loadAd();
        moPubBannerView.setBannerAdListener(new MoPubView.BannerAdListener() {
            @Override
            public void onBannerLoaded(@NonNull MoPubView moPubView) {
                Log.d(TAG, "onBannerLoaded: "+moPubView.getAdUnitId());
                isMoPubBannerLoaded = true;
            }

            @Override
            public void onBannerFailed(MoPubView moPubView, MoPubErrorCode moPubErrorCode) {
                Log.d(TAG, "onBannerFailed: Code : "+moPubErrorCode.getIntCode() +" Msg : "+moPubErrorCode.toString() );
            }

            @Override
            public void onBannerClicked(MoPubView moPubView) {

            }

            @Override
            public void onBannerExpanded(MoPubView moPubView) {

            }

            @Override
            public void onBannerCollapsed(MoPubView moPubView) {

            }
        });
    }

    public void showMopUpInterstitial() {
        MoPubInterstitial moPubInterstitial1 = moPubInterstitial;
        if (moPubInterstitial1 != null) {
            moPubInterstitial1.setInterstitialAdListener(new MoPubInterstitial.InterstitialAdListener() {
                @Override
                public void onInterstitialLoaded(MoPubInterstitial moPubInterstitial) {

                }

                @Override
                public void onInterstitialFailed(MoPubInterstitial moPubInterstitial, MoPubErrorCode moPubErrorCode) {

                }

                @Override
                public void onInterstitialShown(MoPubInterstitial moPubInterstitial) {

                }

                @Override
                public void onInterstitialClicked(MoPubInterstitial moPubInterstitial) {

                }

                @Override
                public void onInterstitialDismissed(MoPubInterstitial moPubInterstitial1) {
                    BaseClass.interstitialCallBack();
                    moPubInterstitial = null;
                    preloadMoPubInterstitial();
                }
            });
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                moPubInterstitial1.show();

                BaseClass.showFullAd = false;
                isMoPubInterstitialLoaded = false;
                Log.e("TEMP", "MoPub showMopUpInterstitial: " + isMoPubInterstitialLoaded);
                BaseClass.startAdHandler();
            }, 1000);

        }
    }

    public void preloadMopUpAds(JSONObject jsonObject) {
        Log.i(TAG, "preloadAds: Method Call");
        setAdsId(jsonObject);
        Map<String, String> mediatedNetworkConfiguration1 = new HashMap<>();
        mediatedNetworkConfiguration1.put("banner", MOP_UP_BANNER);
        Map<String, String> mediatedNetworkConfiguration2 = new HashMap<>();
        mediatedNetworkConfiguration2.put("native", MOP_UP_NATIVE);
        Map<String, String> mediatedNetworkConfiguration3 = new HashMap<>();
        mediatedNetworkConfiguration3.put("full", MOP_UP_INTERSTITIAL);

        final SdkConfiguration.Builder configBuilder1 = new SdkConfiguration.Builder(MOPUB_UNIT_ID);
        configBuilder1.withMediatedNetworkConfiguration(activity.toString(), mediatedNetworkConfiguration1);
        configBuilder1.withMediatedNetworkConfiguration(activity.toString(), mediatedNetworkConfiguration2);
        configBuilder1.withMediatedNetworkConfiguration(activity.toString(), mediatedNetworkConfiguration3);

        if (BuildConfig.DEBUG) {
            configBuilder1.withLogLevel(DEBUG);
        } else {
            configBuilder1.withLogLevel(INFO);
        }

        MoPub.initializeSdk(activity, configBuilder1.build(), () -> {
            Log.v(TAG, "onInitializationComplete : "+configBuilder1.toString());
            preloadMoPubBannerAd();
            preloadMoPubNativeAd();
            preloadMoPubInterstitial();
        });
    }

    public void preloadMoPubInterstitial() {
        Log.i(TAG, "preloadInterstitialAd Method Call: id"+MOP_UP_INTERSTITIAL);
        if (moPubInterstitial == null) {
            MoPubInterstitial mInterstitial = new MoPubInterstitial(activity, MOP_UP_INTERSTITIAL);
            mInterstitial.setInterstitialAdListener(new MoPubInterstitial.InterstitialAdListener() {
                @Override
                public void onInterstitialLoaded(MoPubInterstitial moPubInterstitial1) {
                    Log.d(TAG, "onInterstitialLoaded: ");
                    moPubInterstitial = moPubInterstitial1;
                    isMoPubInterstitialLoaded = true;
                    Log.e("TEMP", "MoPub showMopUpInterstitial: " + isMoPubInterstitialLoaded);
                }

                @Override
                public void onInterstitialFailed(MoPubInterstitial moPubInterstitial, MoPubErrorCode moPubErrorCode) {
                    Log.d(TAG, "onInterstitialFailed: "+moPubErrorCode);
                }

                @Override
                public void onInterstitialShown(MoPubInterstitial moPubInterstitial) {

                }

                @Override
                public void onInterstitialClicked(MoPubInterstitial moPubInterstitial) {

                }

                @Override
                public void onInterstitialDismissed(MoPubInterstitial moPubInterstitial) {

                }
            });
            mInterstitial.load();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        moPubNativeView.removeAllViews();
    }

    public void setAdsId(JSONObject jsonObject) {
        try {
            JSONObject googleJson = jsonObject.getJSONObject(AdsConstant.MopUpADS);
            MOPUB_UNIT_ID = googleJson.getString(AdsConstant.App_ID);
            MOP_UP_BANNER = googleJson.getString(AdsConstant.BannerAD_ID);
            MOP_UP_INTERSTITIAL = googleJson.getString(AdsConstant.FullScreen_ID);
            MOP_UP_NATIVE = googleJson.getString(AdsConstant.Native_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
