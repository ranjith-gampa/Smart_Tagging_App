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


public class AdminHomeActivity extends AppCompatActivity{


    private Intent intent;
    private String username,role;
    private TextView ST1,ST2;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        sharedPreferences = getSharedPreferences("Smart_Tagging",Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username",null);
        role = sharedPreferences.getString("role",null);
        ST1 = (TextView)findViewById(R.id.AdminHomeActivityST1);
        ST2 = (TextView)findViewById(R.id.AdminHomeActivityST2);
        ST1.setText("Hi, " + username);
        ST2.setText("Role: " + role);
    }


    public void onClickAdminHomeActivityCreateUser(View view){
        intent = new Intent(AdminHomeActivity.this,AdminCreateUserActivity.class);
        startActivity(intent);
    }


    public void onClickAdminHomeActivityReadTag(View view){
        //To be integrated with Ranjith's Code.
        intent = new Intent(AdminHomeActivity.this,NfcReadActivity.class);
        startActivity(intent);
    }


    public void onClickAdminHomeActivityWriteTag(View view){
        //To be integrated with Ranjith's Code.
        intent = new Intent(AdminHomeActivity.this,NfcWriteActivity.class);
        startActivity(intent);
    }


    public void onClickAdminHomeActivityListUsers(View view){
        intent = new Intent(AdminHomeActivity.this,AdminListUsersActivity.class);
        startActivity(intent);
    }


    public void onClickAdminHomeActivityViewTagLogs(View view){
        intent = new Intent(AdminHomeActivity.this,ViewTagLogsActivity.class);
        startActivity(intent);
    }


    public void onClickAdminHomeActivityUpdatePassword(View view){
        intent = new Intent(AdminHomeActivity.this,UpdatePasswordActivity.class);
        startActivity(intent);
    }


    public void onClickAdminHomeActivityLogout(View view){
        intent = new Intent(AdminHomeActivity.this,ApplicationLogoutActivity.class);
        startActivity(intent);
    }


}
