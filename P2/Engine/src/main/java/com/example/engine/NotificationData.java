package com.example.engine;

/**
 * Data container holding title, body, tag, and intent values for a scheduled notification.
 */
public class NotificationData {

    /** Notification title string. */
    public String title = "";

    /** Notification body text string. */
    public String body = "";

    /** Notification tag identifier. */
    public String tag = "";

    /** Extra value string passed to the intent. */
    public String value = "";

    /**
     * Constructs NotificationData with title and body.
     *
     * @param _title notification title
     * @param _body notification body text
     */
    public NotificationData(String _title, String _body) {
        title = _title;
        body = _body;
    }

    /**
     * Constructs NotificationData with title, body, tag, and extra value.
     *
     * @param _title notification title
     * @param _body notification body text
     * @param _tag notification tag
     * @param _value extra intent value
     */
    public NotificationData(String _title, String _body, String _tag, String _value) {
        title = _title;
        body = _body;
        tag = _tag;
        value = _value;
    }

    /**
     * Constructs NotificationData with title, body, and tag.
     *
     * @param _title notification title
     * @param _body notification body text
     * @param _tag notification tag
     */
    public NotificationData(String _title, String _body, String _tag) {
        title = _title;
        body = _body;
        tag = _tag;
    }
}
