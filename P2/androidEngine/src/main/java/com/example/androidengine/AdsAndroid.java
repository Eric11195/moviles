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
import com.google.android.libraries.ads.mobile.sdk.initialization.InitializationConfig;

import com.google.android.libraries.ads.mobile.sdk.banner.AdSize;
import com.google.android.libraries.ads.mobile.sdk.banner.AdView;
import com.google.android.libraries.ads.mobile.sdk.banner.BannerAd;
import com.google.android.libraries.ads.mobile.sdk.banner.BannerAdRequest;

import com.google.android.libraries.ads.mobile.sdk.common.AdLoadCallback;
import com.google.android.libraries.ads.mobile.sdk.common.LoadAdError;

import com.google.android.libraries.ads.mobile.sdk.rewarded.RewardedAd;
import com.google.android.libraries.ads.mobile.sdk.common.AdRequest;

public class AdsAndroid implements AdsInterface {

    private static final String TAG = "AdsAndroid";

    // Google's official test ad unit IDs.
    private static final String BANNER_TEST_ID =
            "/21775744923/example/adaptive-banner";
    private static final String REWARDED_TEST_ID =
            "/21775744923/example/rewarded";

    // Replace with your AdMob application ID.
    private static final String APP_ID =
            "ca-app-pub-3940256099942544~3347511713";

    private final Activity activity;

    private AdView bannerView;
    private RewardedAd rewardedAd;

    private volatile boolean initialized = false;
    private volatile boolean bannerRequested = false;
    private boolean bannerLoadStarted = false;

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

                            loadRewardedAd();

                            if (bannerRequested) {
                                bannerView.setVisibility(View.VISIBLE);
                                loadBanner();
                            }
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

        bannerLoadStarted = true;

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
                        bannerLoadStarted = false;
                    }
                }
        );
    }

    @Override
    public void showBanner() {
        bannerRequested = true;

        activity.runOnUiThread(() -> {
            if (!initialized) {
                Log.d(TAG, "Banner requested; waiting for initialization.");
                return;
            }

            bannerView.setVisibility(View.VISIBLE);

            if (!bannerLoadStarted) {
                loadBanner();
            }
        });
    }

    @Override
    public void hideBanner() {
        bannerRequested = false;

        activity.runOnUiThread(() -> {
            if (bannerView != null) {
                bannerView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void showRewarded() {
        if (!initialized) {
            Log.w(TAG, "Ads SDK has not been initialized.");
            return;
        }

        if (rewardedAd == null) {
            Log.w(TAG, "Rewarded ad is not ready.");
            loadRewardedAd();
            return;
        }

        RewardedAd ad = rewardedAd;
        rewardedAd = null;

        ad.show(activity, rewardItem -> {
            Log.d(TAG, "User earned reward: " + rewardItem.getAmount());

            // TODO: Notify the game that the reward was earned.
        });

        loadRewardedAd();
    }

    private void loadRewardedAd() {
        if (!initialized) {
            return;
        }

        RewardedAd.load(
                new AdRequest.Builder(REWARDED_TEST_ID).build(),
                new AdLoadCallback<RewardedAd>() {

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        rewardedAd = ad;
                        Log.d(TAG, "Rewarded ad loaded.");
                    }

                    @Override
                    public void onAdFailedToLoad(
                            @NonNull LoadAdError error) {
                        rewardedAd = null;
                        Log.e(TAG, "Rewarded ad failed: " + error);
                    }
                }
        );
    }

    public void destroy() {
//        activity.runOnUiThread(() -> {
//            if (bannerView != null) {
//                bannerView.destroy();
//                bannerView = null;
//            }
//        });
    }
}