package com.example.desktopengine;

import com.example.engine.AdsInterface;

public class AdsDesktop implements AdsInterface {

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void initialize() {}

    @Override
    public void showBanner() {}

    @Override
    public void hideBanner() {}


    @Override
    public void showRewarded() {}

    @Override
    public boolean hasReward() {
        return false;
    }

    @Override
    public void consumeReward() {}
}