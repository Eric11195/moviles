package com.example.engine;

/**
 * Interface for platform social sharing integrations (messages, posts, images).
 */
public interface ISocial {

    /**
     * Shares a plain text message via platform share intent/dialog.
     *
     * @param text message text content
     */
    void sendMessage(String text);

    /**
     * Shares a message with an attached image and metadata.
     *
     * @param text message text content
     * @param img attached ImageEng instance
     * @param titleImg title for the attached image
     * @param imageDesc description for the attached image
     */
    void sendMessage(String text, ImageEng img, String titleImg, String imageDesc);

    /**
     * Creates a social post with text and an attached image.
     *
     * @param text post text content
     * @param img attached ImageEng instance
     */
    void createPost(String text, ImageEng img);
}
