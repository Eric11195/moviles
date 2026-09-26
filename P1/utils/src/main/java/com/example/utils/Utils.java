package com.example.utils;

import java.nio.file.FileSystems;

public class Utils {
    public static String getCurrentPath(){
        return FileSystems.getDefault().getPath("").toAbsolutePath().toString()+'\\';
    }
}