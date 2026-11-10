package com.example.engine;

import java.util.ArrayList;

public abstract class NotificationInterface {
    ArrayList<NotificationData> notifs = new ArrayList<>();
    public abstract void createNotification (String _title, String _body);
}
