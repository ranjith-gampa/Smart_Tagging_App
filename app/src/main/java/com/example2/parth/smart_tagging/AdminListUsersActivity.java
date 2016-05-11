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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AdminListUsersActivity extends AppCompatActivity{


    private ListView LV;
    private ArrayAdapter LA;
    private Firebase reference;
    private SharedPreferences sharedPreferences,sharedPreferences1;
    private String username,role,environment,new_username,new_role,user_string,selected_string,final_username,db_url;
    private String[] separator;
    private Map<String,Object> map_uro;
    private Intent intent;
    private ArrayList<String> users;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_list_users);
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
        Firebase.setAndroidContext(this);
        sharedPreferences = getSharedPreferences("Smart_Tagging",Context.MODE_PRIVATE);
        environment = sharedPreferences.getString("environment", null);
        username = sharedPreferences.getString("username", null);
        role = sharedPreferences.getString("role",null);
        db_url=sharedPreferences.getString("firebasedburl",null);
        reference = new Firebase(db_url + environment + "/" + "Users_Role/");
        map_uro = new HashMap<String,Object>();
        map_uro.put("fetcher"," ");
        reference.updateChildren(map_uro);
        users = new ArrayList<String>();
        users.add(username + ":" + role + "  [ME]  ");
        LA = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, users);
        LV = (ListView) findViewById(R.id.AdminListUsersActivityLV);
        LV.setAdapter(LA);
        editor=sharedPreferences.edit();
        reference.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                for (DataSnapshot resultDataSnapshot : dataSnapshot.getChildren()){
                    new_username = resultDataSnapshot.getKey();
                    new_role = resultDataSnapshot.child("role").getValue(String.class);
                    if (new_username.equals("fetcher")) {
                        continue;
                    }
                    if (username.equals(new_username) && role.equals(new_role)) {
                        continue;
                    }
                    user_string = new_username + ":" + new_role;

                    users.add(user_string);

                    LA.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                //Nothing to do here.
            }
        });
        LV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected_string = String.valueOf(parent.getItemAtPosition(position));
                separator = selected_string.split(":");
                final_username = separator[0];
                intent = new Intent(AdminListUsersActivity.this, AdminUserDetailsActivity.class);
                intent.putExtra("user", final_username);
                editor.putString("usersName",final_username);
                editor.apply();
                startActivity(intent);
            }
        });
    }



    public void onClickAdminListUsersActivityGoBack(View view){
        finish();
        System.exit(0);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        editor.clear();
    }


}
