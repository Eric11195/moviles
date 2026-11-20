package com.example.engine;

import java.util.ArrayList;

/**
 * Abstract platform-independent interface for creating and scheduling local notifications.
 */
public abstract class NotificationInterface {

    /** List of created notification data definitions. */
    protected ArrayList<NotificationData> notifs = new ArrayList<>();

    /** Resource ID for small notification icon. */
    protected int smallIcon;

    /**
     * Sets the small icon resource ID for notifications.
     *
     * @param si icon resource ID
     */
    public abstract void setSmallIcon(int si);

    /**
     * Adds a notification data definition to the notifications list.
     *
     * @param data notification definition
     */
    public void createNotification(NotificationData data) {
        notifs.add(data);
    }

    /**
     * Schedules a notification to trigger after a delay or interval.
     *
     * @param i index of the notification data definition in the list
     * @param sec delay/period interval in seconds
     */
    public abstract void scheduleNotification(int i, int sec);
}
