package com.example2.parth.smart_tagging;


import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class UsersLoginService extends IntentService{


    private String environment,username,role,sector1,sector2,db_url;
    private Map<String,Object> map_uro,map_uac;
    private Firebase reference1,reference2;
    private Intent intentNew;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    public UsersLoginService(){
        super("UsersLoginService");
    }


    @Override
    protected void onHandleIntent(Intent intent){
        Firebase.setAndroidContext(this);
        sharedPreferences = getSharedPreferences("Smart_Tagging", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        environment = sharedPreferences.getString("environment",null);
        db_url = sharedPreferences.getString("firebasedburl",null);
        //Toast.makeText(UsersLoginService.this,environment,Toast.LENGTH_SHORT).show(); --> For Debugging.
        username = sharedPreferences.getString("username",null);
        //Toast.makeText(UsersLoginService.this,username,Toast.LENGTH_SHORT).show(); --> For Debugging.
        reference1 = new Firebase(db_url + environment + "/Users_Access/" + username + "/");
        map_uac = new HashMap<String,Object>();
        map_uac.put("fetcher", " ");
        reference1.updateChildren(map_uac);
        reference1.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                sector1 = dataSnapshot.child("sector-1").getValue(String.class);
                sector2 = dataSnapshot.child("sector-2").getValue(String.class);
                //Toast.makeText(UsersLoginService.this, sector1 + sector2, Toast.LENGTH_SHORT).show(); --> For Debugging.
                editor.putString("sector-1", sector1);
                editor.putString("sector-2", sector2);
                editor.apply();
            }
            @Override
            public void onCancelled(FirebaseError firebaseError){
                //Noting to do here . , ,
            }
        });
        reference2 = new Firebase(db_url + environment + "/Users_Role/" + username + "/");
        map_uro = new HashMap<String,Object>();
        map_uro.put("fetcher"," ");
        reference2.updateChildren(map_uro);
        reference2.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                role = dataSnapshot.child("role").getValue(String.class);
                //Toast.makeText(UsersLoginService.this, role, Toast.LENGTH_SHORT).show(); --> For Debugging.
                editor.putString("role", role);
                editor.apply();
                if(role.equals("GA")){
                    intentNew = new Intent(UsersLoginService.this,AdminHomeActivity.class);
                    intentNew.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentNew);
                }
                else if(role.equals("USER")){
                    intentNew = new Intent(UsersLoginService.this,AdminHomeActivity.class);
                    intentNew.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentNew);
                }
                else{
                    intentNew = new Intent(UsersLoginService.this,UsersHomeActivity.class);
                    intentNew.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentNew);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                //Noting to do here . , ,
            }
        });
    }


}
