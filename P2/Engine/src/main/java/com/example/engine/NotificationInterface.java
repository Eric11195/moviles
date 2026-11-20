package com.example.engine;

import java.util.ArrayList;

public abstract class NotificationInterface {
    protected ArrayList<NotificationData> notifs = new ArrayList<>();
    protected int smallIcon;
    public abstract void setSmallIcon(int si);
    public abstract void createNotification(NotificationData data, int idx);
    public abstract void scheduleNotification(int idx, int sec);
}
