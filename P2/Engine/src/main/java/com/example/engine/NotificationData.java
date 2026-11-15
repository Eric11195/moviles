package com.example.engine;

public class NotificationData {
    public NotificationData(String _title, String _body){
        title = _title; body = _body;
    }
    public NotificationData(String _title, String _body, String _tag, String _value){
        title = _title; body = _body; tag = _tag; value = _value;
    }
    public NotificationData(String _title, String _body, String _tag){
        title = _title; body = _body; tag = _tag;
    }
    public String title = "";
    public String body = "";
    public String tag = "";
    public String value = "";
}
