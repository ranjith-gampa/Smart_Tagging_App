package com.example2.parth.smart_tagging;


import android.app.Application;
import com.firebase.client.Firebase;


public class Smart_Tagging_Application extends Application{


    @Override
    public void onCreate(){
        super.onCreate();
        Firebase.setAndroidContext(this);
    }


}
