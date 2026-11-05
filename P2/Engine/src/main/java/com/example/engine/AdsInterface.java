package com.example.engine;

public interface AdsInterface {
    boolean isReady();
    void initialize();

    void showBanner();

    void hideBanner();

    void showRewarded();
}