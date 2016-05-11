package com.example2.parth.smart_tagging;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.Firebase;


public class AdminUserDetailsUpdateActivity extends AppCompatActivity{


    private Firebase reference;
    private Bundle bundle;
    private String[] updateValues = new String[8];
    private TextView MT9;
    private EditText ET1,ET2,ET3,ET4,ET5,ET6,ET7;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_details_update);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        Firebase.setAndroidContext(this);
        bundle = getIntent().getExtras();
        updateValues = bundle.getStringArray("update");
        ET1 = (EditText)findViewById(R.id.AdminUserDetailsUpdateActivityET1);
        ET2 = (EditText)findViewById(R.id.AdminUserDetailsUpdateActivityET2);
        ET3 = (EditText)findViewById(R.id.AdminUserDetailsUpdateActivityET3);
        ET4 = (EditText)findViewById(R.id.AdminUserDetailsUpdateActivityET4);
        ET5 = (EditText)findViewById(R.id.AdminUserDetailsUpdateActivityET5);
        ET6 = (EditText)findViewById(R.id.AdminUserDetailsUpdateActivityET6);
        ET7 = (EditText)findViewById(R.id.AdminUserDetailsUpdateActivityET7);
        MT9 = (TextView)findViewById(R.id.AdminUserDetailsActivityMT9);
        ET1.setText(updateValues[0]);
        ET2.setText(updateValues[1]);
        ET3.setText(updateValues[2]);
        ET4.setText(updateValues[3]);
        ET5.setText(updateValues[4]);
        ET6.setText(updateValues[5]);
        ET7.setText(updateValues[6]);
        MT9.setText(updateValues[7]);
    }


    public void onClickAdminUserDetailsUpdateActivityGoBack(View view){
        finish();
        System.exit(0);
    }


    public void onClickAdminUserDetailsUpdateActivityUpdate(View view){
        //Nothing to do here . . .
    }


}
