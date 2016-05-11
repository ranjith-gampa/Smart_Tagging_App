package com.example2.parth.smart_tagging;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import java.util.HashMap;
import java.util.Map;


public class AdminLogInActivity extends AppCompatActivity{


    private Intent intent;
    private EditText ET1,ET2,ET3;
    private String username,password,environment,new_username,new_password,new_environment,Language, String_ui;
    private  int encryption,voice;
    private Firebase reference,reference_environment,reference_username,reference_password,reference_template,reference_access;
    private Map<String,Object> fetcher1,fetcher2,fetcher3,fetchertp,fetcherAC;
    private long counter1,counter2,counter3,counter4 = 0;
    private SharedPreferences sharedPreferences,sharedPreferences1,sharedPreferences2,sharedPreferencesurl;
    private SharedPreferences.Editor editor,editor1,editor2,editorurl;
    String db_url;
    //static ConnectivityManager cm;
    ConnectivityManager cm;
    NetworkInfo activeNetwork;
    boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_log_in);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db_url=""+"Smart_Tagging/";
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
/*        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
       cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if(!isConnected){
            Toast.makeText(this,"Check your Internet Connection",Toast.LENGTH_SHORT).show();
        }


        Firebase.setAndroidContext(this);
        ET1 = (EditText)findViewById(R.id.AdminLogInActivityET1);
        ET2 = (EditText)findViewById(R.id.AdminLogInActivityET2);
        ET3 = (EditText)findViewById(R.id.AdminLogInActivityET3);
        sharedPreferencesurl = getSharedPreferences("Smart_Tagging",Context.MODE_PRIVATE);
        editorurl = sharedPreferencesurl.edit();
        editorurl.putString("firebasedburl",db_url);

    }


    public void onClickAdminSignUpActivity(View view){
        intent = new Intent(AdminLogInActivity.this,AdminSignUpActivity.class);
        startActivity(intent);
    }


    public void onClickAdminHomeActivity(View view){
        environment=ET1.getText().toString();
        //environment = "Mobile_235";
        username = (ET2.getText()).toString();
        password = (ET3.getText()).toString();
        reference = new Firebase(db_url);
        reference_environment = reference.child("Applications_List");

        fetcher1 = new HashMap<String,Object>();

        fetcher1.put("fetcher","fetch");

        reference_environment.updateChildren(fetcher1);
        reference_environment.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot adataSnapshot){
                counter2 = adataSnapshot.getChildrenCount();
                for(DataSnapshot resultDataSnapshot: adataSnapshot.getChildren()){
                    new_environment = resultDataSnapshot.getKey();
                    if(new_environment.equals(environment)){
                        String latest_path = environment + "/Users_List/";
                        String template_path=environment+"/Template/";
                        reference_username = reference.child(latest_path);
                        reference_template=reference.child(template_path);
                        fetcher2 = new HashMap<String,Object>();
                        fetchertp=new HashMap<String, Object>();
                        fetchertp.put("fetcher","");
                        fetcher2.put("fetcher"," ");
                        reference_template.updateChildren(fetchertp);
                        reference_username.updateChildren(fetcher2);
                        reference_username.addValueEventListener(new ValueEventListener(){
                            @Override
                            public void onDataChange(DataSnapshot bdataSnapshot){
                                counter3 = bdataSnapshot.getChildrenCount();
                                for (DataSnapshot resultDataSnapshot : bdataSnapshot.getChildren()){
                                    new_username = resultDataSnapshot.getKey();
                                    if(new_username.equals(username)){
                                        String new_latest_path = environment + "/Users_Credential/" + username + "/";
                                        reference_password = reference.child(new_latest_path);
                                        fetcher3 = new HashMap<String, Object>();
                                        fetcher3.put("fetcher"," ");
                                        reference_password.updateChildren(fetcher3);
                                        reference_password.addValueEventListener(new ValueEventListener(){
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot){
                                                new_password = dataSnapshot.child("password").getValue(String.class);
                                                if(new_password.equals(password)){
                                                    Toast.makeText(AdminLogInActivity.this,"Login Successful",Toast.LENGTH_LONG).show();
                                                    sharedPreferences = getSharedPreferences("Smart_Tagging",Context.MODE_PRIVATE);
                                                    editor = sharedPreferences.edit();
                                                    editor.putString("environment",environment);
                                                    editor.putString("username",username);
                                                    editor.apply();
                                                    reference_access=reference.child(environment).child("Users_Access").child(username);
                                                    intent = new Intent(AdminLogInActivity.this,UsersLoginService.class);
                                                    fetcherAC=new HashMap<String, Object>();
                                                    fetcherAC.put("fetcher","");
                                                    reference_access.updateChildren(fetcherAC);
                                                    reference_access.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            //DataSnapshot resultDataSnapShot=dataSnapshot.getChildren();
                                                            //if((dataSnapshot.getKey()).equals("Access")){
                                                                sharedPreferences2 = getSharedPreferences("Smart_Tagging",Context.MODE_PRIVATE);
                                                                editor2 = sharedPreferences2.edit();
                                                                editor2.putString("Access",dataSnapshot.child("Access").getValue().toString());
                                                                editor2.apply();
                                                            //}

                                                        }

                                                        @Override
                                                        public void onCancelled(FirebaseError firebaseError) {

                                                        }
                                                    });
                                                    startService(intent);
                                                }
                                                else{
                                                    Toast.makeText(AdminLogInActivity.this,"Login Failed...Incorrect Password",Toast.LENGTH_LONG).show();
                                                }
                                            }
                                            @Override
                                            public void onCancelled(FirebaseError firebaseError){
                                                //nothing to do here . . .
                                            }
                                        });
                                    }
                                    else{
                                        counter4++;
                                        if(counter4 == counter3){
                                            Toast.makeText(AdminLogInActivity.this,"Login Failed...Incorrect Username",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(FirebaseError firebaseError){
                                //nothing to do here . . .
                            }
                        });
                        reference_template.addValueEventListener(new ValueEventListener(){

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                encryption=dataSnapshot.child("Encryption").getValue(Integer.class);
                                voice=dataSnapshot.child("Voice").getValue(Integer.class);
                                String_ui=dataSnapshot.child("String ui").getValue(String.class);
                                Language=dataSnapshot.child("Language").getValue(String.class);
                                sharedPreferences1 = getSharedPreferences("Smart_Tagging",Context.MODE_PRIVATE);
                                editor1 = sharedPreferences1.edit();
                                editor1.putString("String ui",String_ui);
                                editor1.putString("Language",Language);
                                editor1.putInt("Voice",voice);
                                editor1.putInt("Encryption",encryption);
                                editor1.apply();
                                Toast.makeText(AdminLogInActivity.this,"Voice: "+voice,Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                    }
                    else{
                        counter1++;
                        if(counter1 == counter2){
                            Toast.makeText(AdminLogInActivity.this,"Login Failed...Incorrect Environment",Toast.LENGTH_LONG).show();
                        }

                    }
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError){
                //nothing to do here . . .
            }
        });
    }


    public void onClickAdminLogInExitActivity(View view){
        finish();
        System.exit(0);
    }


    public void onClickAdminForgotCredentialsActivity(View view){
        intent = new Intent(AdminLogInActivity.this,AdminForgotCredentialsActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin_log_in, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        editor.clear();
        editor1.clear();
        editor2.clear();

    }


}
