package com.example.engine;

public interface ISocial {
    public void sendMessage(String text);
    public void sendMessage(String text, ImageEng img, String titleImg, String imageDesc);
    // public void publishOnApp(String text);
}
