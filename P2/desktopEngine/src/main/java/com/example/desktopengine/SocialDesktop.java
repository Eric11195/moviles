package com.example.desktopengine;

import com.example.engine.ISocial;
import com.example.engine.ImageEng;

public class SocialDesktop implements ISocial {
    @Override
    public void sendMessage(String text) {
        System.out.println("Warning: Social::sendMessage does nothing on desktop");
    }

    @Override
    public void sendMessage(String text, ImageEng img, String titleImg, String imageDesc) {
        System.out.println("Warning: Social::sendMessage does nothing on desktop");
    }
}
