package com.example2.parth.smart_tagging;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import java.util.HashMap;
import java.util.Map;


public class AdminUserDetailsActivity extends AppCompatActivity{


    private Firebase reference;
    private Bundle bundle;
    private String environment,user,firstname,lastname,mail,phone,role,username,sector1,sector2;
    private SharedPreferences sharedPreferences;
    private Map<String,Object> map_udet;
    private TextView MT9,MT10,MT11,MT12,MT13,MT14,MT15,MT16;
    private Intent intent;
    private String[] updateValues = new String[8];


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Firebase.setAndroidContext(this);
        sharedPreferences = getSharedPreferences("Smart_Tagging", Context.MODE_PRIVATE);
        environment = sharedPreferences.getString("environment", null);
        bundle = getIntent().getExtras();
        user = (bundle.get("user")).toString();
        reference = new Firebase("https://amber-inferno-6557.firebaseio.com/Smart_Tagging/" + environment + "/Users_Detail/" + user + "/");
        map_udet = new HashMap<String,Object>();
        map_udet.put("fetcher", " ");
        reference.updateChildren(map_udet);
        reference.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                firstname = dataSnapshot.child("firstname").getValue(String.class);
                lastname = dataSnapshot.child("lastname").getValue(String.class);
                mail = dataSnapshot.child("mail").getValue(String.class);
                phone = dataSnapshot.child("phone").getValue(String.class);
                role = dataSnapshot.child("role").getValue(String.class);
                sector1 = dataSnapshot.child("sector-1").getValue(String.class);
                sector2 = dataSnapshot.child("sector-2").getValue(String.class);
                username = dataSnapshot.child("username").getValue(String.class);
            }
            @Override
            public void onCancelled(FirebaseError firebaseError){
                //Nothing to do here...
            }
        });
        MT9 = (TextView)findViewById(R.id.AdminUserDetailsActivityMT9);
        MT10 = (TextView)findViewById(R.id.AdminUserDetailsActivityMT10);
        MT11 = (TextView)findViewById(R.id.AdminUserDetailsActivityMT11);
        MT12 = (TextView)findViewById(R.id.AdminUserDetailsActivityMT12);
        MT13 = (TextView)findViewById(R.id.AdminUserDetailsActivityMT13);
        MT14 = (TextView)findViewById(R.id.AdminUserDetailsActivityMT14);
        MT15 = (TextView)findViewById(R.id.AdminUserDetailsActivityMT15);
        MT16 = (TextView)findViewById(R.id.AdminUserDetailsActivityMT16);
        MT9.setText(firstname);
        MT10.setText(lastname);
        MT11.setText(mail);
        MT12.setText(phone);
        MT13.setText(role);
        MT14.setText(sector1);
        MT15.setText(sector2);
        MT16.setText(username);
    }


    public void onClickAdminUserDetailsActivityGoBack(View view){
        finish();
        System.exit(0);
    }


    public void onClickAdminUserDetailsActivityRemove(View view){
        reference.removeValue();
        Toast.makeText(AdminUserDetailsActivity.this,"User Delected Successfully",Toast.LENGTH_LONG).show();
        intent = new Intent(AdminUserDetailsActivity.this,AdminListUsersActivity.class);
        startActivity(intent);
    }


    public void onClickAdminUserDetailsActivityUpdate(View view){
        updateValues[0] = firstname;
        updateValues[1] = lastname;
        updateValues[2] = mail;
        updateValues[3] = phone;
        updateValues[4] = role;
        updateValues[5] = sector1;
        updateValues[6] = sector2;
        updateValues[7] = username;
        intent = new Intent(AdminUserDetailsActivity.this,AdminUserDetailsUpdateActivity.class);
        intent.putExtra("update",updateValues);
        startActivity(intent);
    }


}
