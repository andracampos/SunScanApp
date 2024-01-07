package com.example.myapplication.utils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {
    final static String USADARA_BASE_URL="https://api.openweathermap.org/data/3.0/onecall?lat=33.44&lon=-94.04&appid=701bca3259a8d2a3a105ad1c3cb9f096";

    public static URL buildUrl(){

        Uri urlStr = Uri.parse(USADARA_BASE_URL)
                .buildUpon().build();
        URL url= null;
        try {
            url = new URL(urlStr.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getDatafromHttpUrl(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        try {
            InputStream stream = connection.getInputStream();
            Scanner scanner = new Scanner(stream);
            scanner.useDelimiter("\\A");
            boolean hasnext = scanner.hasNext();
            if (hasnext) {
                return scanner.next();
            } else
                return null;
        }
        finally {
            connection.disconnect();
        }

    }




}
