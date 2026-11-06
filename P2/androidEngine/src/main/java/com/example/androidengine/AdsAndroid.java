package com.example.androidengine;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.example.engine.AdsInterface;

import com.google.android.libraries.ads.mobile.sdk.MobileAds;
import com.google.android.libraries.ads.mobile.sdk.common.PreloadCallback;
import com.google.android.libraries.ads.mobile.sdk.common.PreloadConfiguration;
import com.google.android.libraries.ads.mobile.sdk.initialization.InitializationConfig;

import com.google.android.libraries.ads.mobile.sdk.banner.AdSize;
import com.google.android.libraries.ads.mobile.sdk.banner.AdView;
import com.google.android.libraries.ads.mobile.sdk.banner.BannerAd;
import com.google.android.libraries.ads.mobile.sdk.banner.BannerAdRequest;

import com.google.android.libraries.ads.mobile.sdk.common.AdLoadCallback;
import com.google.android.libraries.ads.mobile.sdk.common.LoadAdError;

import com.google.android.libraries.ads.mobile.sdk.rewarded.RewardedAd;
import com.google.android.libraries.ads.mobile.sdk.common.AdRequest;
import com.google.android.libraries.ads.mobile.sdk.rewarded.RewardedAdEventCallback;
import com.google.android.libraries.ads.mobile.sdk.rewarded.RewardedAdPreloader;

public class AdsAndroid implements AdsInterface {

    private static final String TAG = "AdsAndroid";

    // Google's official test ad unit IDs.
    private static final String BANNER_TEST_ID =
            "ca-app-pub-3940256099942544/6300978111"; // AdMob Adaptive Banner
    private static final String REWARDED_TEST_ID =
            "ca-app-pub-3940256099942544/5224354917"; // AdMob Rewarded Ad

    // Replace with your AdMob application ID.
    private static final String APP_ID =
            "ca-app-pub-3940256099942544~3347511713";

    private final Activity activity;

    private AdView bannerView;
    private RewardedAd rewardedAd;

    private volatile boolean initialized = false;

    public AdsAndroid(Activity activity, int bannerViewId) {
        this.activity = activity;
        this.bannerView = activity.findViewById(bannerViewId);
        Log.d(TAG, "AdsAndroid constructor called");
    }

    @Override
    public boolean isReady() {
        return initialized;
    }

    @Override
    public void initialize() {
        new Thread(() -> {
            MobileAds.initialize(
                    activity.getApplicationContext(),
                    new InitializationConfig.Builder(APP_ID).build(),
                    initializationStatus -> {
                        activity.runOnUiThread(() -> {
                            initialized = true;
                            Log.d(TAG, "Google Mobile Ads initialized.");

                            loadBanner();
                            loadRewardedAd();

                        });
                    }
            );
        }).start();
    }
    private void loadBanner() {
        if (bannerView == null) {
            Log.e(TAG, "AdView was not found in the Activity layout!");
            return;
        }

        int widthDp = activity.getResources()
                .getConfiguration().screenWidthDp;

        AdSize adSize =
                AdSize.getLargeAnchoredAdaptiveBannerAdSize(
                        activity,
                        widthDp
                );

        BannerAdRequest request =
                new BannerAdRequest.Builder(BANNER_TEST_ID, adSize)
                        .build();

        bannerView.loadAd(
                request,
                new AdLoadCallback<BannerAd>() {

                    @Override
                    public void onAdLoaded(@NonNull BannerAd ad) {
                        Log.d(TAG, "Banner successfully loaded!");

                        bannerView.setVisibility(View.VISIBLE);

                        Log.d(TAG, "AdView dimensions: "
                                + bannerView.getWidth() + "x"
                                + bannerView.getHeight());
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError error) {
                        Log.e(TAG, "Banner failed to load: " + error);
                    }
                }
        );
    }

    @Override
    public void showBanner() {

        activity.runOnUiThread(() -> {
            if (!initialized) {
                Log.d(TAG, "Banner requested; waiting for initialization.");
                return;
            }

            bannerView.setVisibility(View.VISIBLE);

            loadBanner();
        });
    }

    @Override
    public void hideBanner() {
        activity.runOnUiThread(() -> {
            if (bannerView != null) {
                bannerView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void showRewarded() {
        activity.runOnUiThread(() -> {
            if (!initialized) {
                Log.w(TAG, "Ads SDK has not been initialized.");
                return;
            }

            // Polling returns the next available ad and loads another ad in the background.
            final RewardedAd ad = RewardedAdPreloader.pollAd(REWARDED_TEST_ID);

            // If this is null, the ad hasn't finished downloading yet (or failed to load)
            if (ad == null) {
                Log.e(TAG, "Rewarded ad is not available yet.");
                return;
            }

            Log.d(TAG, "Rewarded ad response info: " + ad.getResponseInfo());
            ad.setAdEventCallback(
                    new RewardedAdEventCallback() {
                        @Override
                        public void onAdImpression() {
                            Log.d(TAG, "Rewarded ad recorded an impression.");
                        }
                    });

            // Show the ad.
            ad.show(
                    activity,
                    rewardItem -> {
                        Log.d(TAG, "User earned reward: " + rewardItem.getAmount());
                        rewardAvailable = true;
                    }
            );
        });
    }
    boolean rewardAvailable=false;

    @Override
    public boolean hasReward() {
        return rewardAvailable;
    }

    @Override
    public void consumeReward() {
        assert(rewardAvailable);
        rewardAvailable=false;
    }

    private void loadRewardedAd() {
        if (!initialized) {
            return;
        }
        AdRequest adRequest = new AdRequest.Builder(REWARDED_TEST_ID).build();
        PreloadConfiguration preloadConfig = new PreloadConfiguration(adRequest);

        // Add the callback to log errors if preloading fails
        RewardedAdPreloader.start(REWARDED_TEST_ID, preloadConfig, new PreloadCallback() {
            @Override
            public void onAdFailedToPreload(@NonNull String preloadId, @NonNull LoadAdError error) {
                Log.e(TAG, "Preload failed for " + preloadId + ": " + error.getMessage());
            }
        });
    }
}