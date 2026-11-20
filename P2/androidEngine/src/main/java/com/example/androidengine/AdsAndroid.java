package com.example.androidengine;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

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

/**
 * Android implementation of {@link AdsInterface} using Google Mobile Ads SDK.
 */
public class AdsAndroid implements AdsInterface {

    /** Logcat tag for debugging. */
    private static final String TAG = "AdsAndroid";

    /** Google official test ad unit ID for banners. */
    private static final String BANNER_TEST_ID =
            "ca-app-pub-3940256099942544/6300978111";

    /** Google official test ad unit ID for rewarded ads. */
    private static final String REWARDED_TEST_ID =
            "ca-app-pub-3940256099942544/5224354917";

    /** Google Mobile Ads test application ID. */
    private static final String APP_ID =
            "ca-app-pub-3940256099942544~3347511713";

    /** Hosting activity context. */
    private final Activity activity;

    /** AdView component holding banner ads. */
    private AdView bannerView;

    /** Rewarded ad object instance. */
    private RewardedAd rewardedAd;

    /** Volatile flag indicating if MobileAds SDK initialization is complete. */
    private volatile boolean initialized = false;

    /** Volatile flag tracking whether a banner request is pending/active. */
    private volatile boolean isBannerRequested = false;

    /** Progress indicator displayed while loading banner ads. */
    private ProgressBar progressBar;

    /**
     * Constructs AdsAndroid for an Activity and AdView resource ID.
     *
     * @param activity hosting Activity instance
     * @param bannerViewId resource ID of the AdView in activity layout
     */
    public AdsAndroid(Activity activity, int bannerViewId) {
        this.activity = activity;
        this.bannerView = activity.findViewById(bannerViewId);
        Log.d(TAG, "AdsAndroid constructor called");
    }

    /**
     * Checks if MobileAds SDK initialization has completed.
     *
     * @return true if initialized, false otherwise
     */
    @Override
    public boolean isReady() {
        return initialized;
    }

    /**
     * Initializes Google Mobile Ads SDK on a background thread.
     */
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

                            if (isBannerRequested) {
                                loadBanner();
                            }
                            loadRewardedAd();
                        });
                    }
            );
        }).start();
    }

    /**
     * Displays the progress loading indicator for banner ads.
     */
    private void showLoadingSymbol() {
        ensureProgressBarCreated();
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Hides the progress loading indicator.
     */
    private void hideLoadingSymbol() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    /**
     * Dynamically creates and attaches a ProgressBar to the parent container of bannerView.
     */
    private void ensureProgressBarCreated() {
        if (progressBar == null && bannerView != null && bannerView.getParent() instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) bannerView.getParent();
            progressBar = new ProgressBar(activity);

            ViewGroup.LayoutParams bannerParams = bannerView.getLayoutParams();
            if (bannerParams instanceof ConstraintLayout.LayoutParams) {
                ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                ConstraintLayout.LayoutParams bLp = (ConstraintLayout.LayoutParams) bannerParams;
                lp.bottomToBottom = bLp.bottomToBottom;
                lp.topToTop = bLp.topToTop;
                lp.leftToLeft = bLp.leftToLeft;
                lp.rightToRight = bLp.rightToRight;
                lp.startToStart = bLp.startToStart;
                lp.endToEnd = bLp.endToEnd;
                progressBar.setLayoutParams(lp);
            } else if (bannerParams instanceof FrameLayout.LayoutParams) {
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                lp.gravity = ((FrameLayout.LayoutParams) bannerParams).gravity;
                progressBar.setLayoutParams(lp);
            } else {
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                progressBar.setLayoutParams(lp);
            }

            progressBar.setVisibility(View.GONE);
            progressBar.setElevation(bannerView.getElevation() + 1.0f);
            parent.addView(progressBar);
        }
    }

    /**
     * Loads a banner advertisement into the AdView component.
     */
    private void loadBanner() {
        if (bannerView == null) {
            Log.e(TAG, "AdView was not found in the Activity layout!");
            hideLoadingSymbol();
            return;
        }

        if (!isBannerRequested) {
            hideLoadingSymbol();
            bannerView.setVisibility(View.GONE);
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
                        activity.runOnUiThread(() -> {
                            hideLoadingSymbol();

                            if (isBannerRequested) {
                                bannerView.setVisibility(View.VISIBLE);
                                Log.d(TAG, "AdView dimensions: "
                                        + bannerView.getWidth() + "x"
                                        + bannerView.getHeight());
                            } else {
                                bannerView.setVisibility(View.GONE);
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError error) {
                        Log.e(TAG, "Banner failed to load: " + error);
                        activity.runOnUiThread(() -> {
                            hideLoadingSymbol();
                            if (bannerView != null) {
                                bannerView.setVisibility(View.GONE);
                            }
                        });
                    }
                }
        );
    }

    /**
     * Requests and displays a banner ad with loading indicator.
     */
    @Override
    public void showBanner() {
        activity.runOnUiThread(() -> {
            isBannerRequested = true;
            if (bannerView != null) {
                bannerView.setVisibility(View.GONE);
            }
            showLoadingSymbol();

            if (!initialized) {
                Log.d(TAG, "Banner requested; waiting for initialization.");
                return;
            }

            loadBanner();
        });
    }

    /**
     * Hides active banner ad and loading indicator.
     */
    @Override
    public void hideBanner() {
        activity.runOnUiThread(() -> {
            isBannerRequested = false;
            hideLoadingSymbol();
            if (bannerView != null) {
                bannerView.setVisibility(View.GONE);
            }
        });
    }

    /**
     * Displays a rewarded video ad if ready.
     */
    @Override
    public void showRewarded() {
        activity.runOnUiThread(() -> {
            if (!initialized) {
                Log.w(TAG, "Ads SDK has not been initialized.");
                return;
            }

            final RewardedAd ad = RewardedAdPreloader.pollAd(REWARDED_TEST_ID);

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

            ad.show(
                    activity,
                    rewardItem -> {
                        Log.d(TAG, "User earned reward: " + rewardItem.getAmount());
                        rewardAvailable = true;
                    }
            );
        });
    }

    /** Flag indicating if user earned a reward. */
    boolean rewardAvailable = false;

    /**
     * Checks if a reward is available.
     *
     * @return true if reward earned, false otherwise
     */
    @Override
    public boolean hasReward() {
        return rewardAvailable;
    }

    /**
     * Consumes the earned reward.
     */
    @Override
    public void consumeReward() {
        assert(rewardAvailable);
        rewardAvailable = false;
    }

    /**
     * Preloads rewarded ad instances.
     */
    private void loadRewardedAd() {
        if (!initialized) {
            return;
        }
        AdRequest adRequest = new AdRequest.Builder(REWARDED_TEST_ID).build();
        PreloadConfiguration preloadConfig = new PreloadConfiguration(adRequest);

        RewardedAdPreloader.start(REWARDED_TEST_ID, preloadConfig, new PreloadCallback() {
            @Override
            public void onAdFailedToPreload(@NonNull String preloadId, @NonNull LoadAdError error) {
                Log.e(TAG, "Preload failed for " + preloadId + ": " + error.getMessage());
            }
        });
    }
}
