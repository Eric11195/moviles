package com.example.engine;

/**
 * Platform-agnostic interface for managing banner and rewarded advertisements.
 */
public interface AdsInterface {

    /**
     * Checks if the ads service has completed initialization.
     *
     * @return true if initialized and ready, false otherwise
     */
    boolean isReady();

    /**
     * Initializes the advertising SDK.
     */
    void initialize();

    /**
     * Displays a banner advertisement on screen.
     */
    void showBanner();

    /**
     * Hides the banner advertisement from screen.
     */
    void hideBanner();

    /**
     * Displays a rewarded video advertisement.
     */
    void showRewarded();

    /**
     * Checks if a rewarded ad reward is available to consume.
     *
     * @return true if a reward is available, false otherwise
     */
    boolean hasReward();

    /**
     * Consumes the available ad reward.
     */
    void consumeReward();
}
