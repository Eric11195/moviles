package com.example.engine;

import java.util.ArrayList;

public abstract class NotificationInterface {
    protected ArrayList<NotificationData> notifs = new ArrayList<>();
    protected int smallIcon;
    public abstract void setSmallIcon(int si);
    public abstract void createNotification(String _title, String _body);
    public abstract void scheduleNotification(int i, int sec);
}
