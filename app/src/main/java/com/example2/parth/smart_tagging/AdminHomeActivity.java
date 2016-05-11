package com.example2.parth.smart_tagging;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class AdminHomeActivity extends AppCompatActivity{


    private Intent intent;
    private String username,role,env,sender,msg,access,db_url;
    private TextView ST1,ST2;
    private SharedPreferences sharedPreferences;
    Firebase receivedReference;
    Map<String,Object> receivedFetcher;
    Button createuser,read,write,createtag,listusers,viewlogs,updatepwd;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        Firebase.setAndroidContext(this);
        createuser=(Button)findViewById(R.id.AdminHomeActivityB1);
        read=(Button)findViewById(R.id.AdminHomeActivityB2);
        write=(Button)findViewById(R.id.AdminHomeActivityB3);
        createtag=(Button)findViewById(R.id.AdminHomeActivityBCreate);
        listusers=(Button)findViewById(R.id.AdminHomeActivityB4);
        viewlogs=(Button)findViewById(R.id.AdminHomeActivityB5);
        updatepwd=(Button)findViewById(R.id.AdminHomeActivityB6);


        sharedPreferences = getSharedPreferences("Smart_Tagging",Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username",null);
        env=sharedPreferences.getString("environment",null);
        role = sharedPreferences.getString("role",null);
        access=sharedPreferences.getString("Access",null);
        db_url=sharedPreferences.getString("firebasedbirl",null);

        if(access.equals("0x2")){
            read.setVisibility(View.INVISIBLE);
            write.setVisibility(View.INVISIBLE);
            createuser.setVisibility(View.INVISIBLE);
            listusers.setVisibility(View.INVISIBLE);
            viewlogs.setVisibility(View.INVISIBLE);
            updatepwd.setVisibility(View.INVISIBLE);
        }
        else if(access.equals("0x4")){
            createtag.setVisibility(View.INVISIBLE);
            write.setVisibility(View.INVISIBLE);
            createuser.setVisibility(View.INVISIBLE);
            listusers.setVisibility(View.INVISIBLE);
            viewlogs.setVisibility(View.INVISIBLE);
            updatepwd.setVisibility(View.INVISIBLE);
        }
        else if(access.equals("0x8")){
            createtag.setVisibility(View.INVISIBLE);
            createuser.setVisibility(View.INVISIBLE);

            viewlogs.setVisibility(View.INVISIBLE);
            updatepwd.setVisibility(View.INVISIBLE);
        }
        ST1 = (TextView)findViewById(R.id.AdminHomeActivityST1);
        ST2 = (TextView)findViewById(R.id.AdminHomeActivityST2);
        ST1.setText("Hi, " + username);
        ST2.setText("Role: " + role);
        receivedReference=new Firebase("https://amber-inferno-6557.firebaseio.com/Smart_Tagging/").child(env).child("Users_Chat").child(username).child("Received");
    }


    @Override
    protected void onResume(){
        super.onResume();
        receivedFetcher=new HashMap<String, Object>();
        receivedReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot resultDataSnapshot:dataSnapshot.getChildren()){
                    long ct=resultDataSnapshot.getChildrenCount();
                    if(ct==1 || resultDataSnapshot.getKey().equals("fetcher")){
                        //Toast.makeText(AdminHomeActivity.this,"No New Message Found",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        sender=resultDataSnapshot.getKey();
                        msg=resultDataSnapshot.getValue().toString();
                        Toast.makeText(AdminHomeActivity.this,"Message from :"+sender,Toast.LENGTH_SHORT).show();
                        Toast.makeText(AdminHomeActivity.this,"Message Content: "+msg,Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

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
        intent = new Intent(AdminHomeActivity.this,NfcClearTag.class);
        startActivity(intent);
    }


    public void onClickAdminHomeActivityLogout(View view){
        intent = new Intent(AdminHomeActivity.this,ApplicationLogoutActivity.class);
        startActivity(intent);
    }
    public void onClickAdminHomeActivityCreateTag(View view){
        intent =new Intent(AdminHomeActivity.this, NfcCreateTag.class);
        startActivity(intent);

    }

}
