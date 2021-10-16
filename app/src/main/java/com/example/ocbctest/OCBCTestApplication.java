package com.example.ocbctest;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class OCBCTestApplication extends Application {

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static SharedPreferences sharedpreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }

    public static void setToken(String token){
        setString("Token",token);
    }

    public static String getToken(){
        return sharedpreferences.getString("Token", null);
    }

    public static void setString(String key, String value){
        SharedPreferences.Editor myEdit = sharedpreferences.edit();
        myEdit.putString(key, value);
        myEdit.apply();
    }

}
