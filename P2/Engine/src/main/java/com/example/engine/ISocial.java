package com.example.engine;

public interface ISocial {
    void sendMessage(String text);
    void sendMessage(String text, ImageEng img, String titleImg, String imageDesc);
    void createPost(String text, ImageEng img);
}
