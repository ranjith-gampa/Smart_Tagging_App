package com.example2.parth.smart_tagging;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.firebase.client.Firebase;
import java.util.HashMap;
import java.util.Map;


public class AdminSignUpActivity extends AppCompatActivity{


    private Intent intent;
    private EditText ET1,ET2,ET3,ET4,ET5,ET6,ET7;
    private RadioGroup radioGroup;
    private RadioButton RB1,RB2,RB3;
    private String firstname,lastname,mail,phone,username,password,name,type,db_url,String_ui;
    private int selectedID;
    private Firebase reference,ref,ref_app,ref_temp,ref_ucred,ref_udet,ref_uli,ref_uro,ref_acc;
    private Map<String,Object> map_appli,map_app,map_temp,map_ucred,map_udet,map_uli,map_uro,map_acc;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sign_up);
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
        sharedPreferences = getSharedPreferences("Smart_Tagging", Context.MODE_PRIVATE);
        db_url = ""+"Smart_Tagging/";
        ET1 = (EditText)findViewById(R.id.AdminSignUpActivityET1);
        ET2 = (EditText)findViewById(R.id.AdminSignUpActivityET2);
        ET3 = (EditText)findViewById(R.id.AdminSignUpActivityET3);
        ET4 = (EditText)findViewById(R.id.AdminSignUpActivityET4);
        ET5 = (EditText)findViewById(R.id.AdminSignUpActivityET5);
        ET6 = (EditText)findViewById(R.id.AdminSignUpActivityET6);
        radioGroup = (RadioGroup)findViewById(R.id.AdminSignUpActivityRadioGroup);
        RB1 = (RadioButton)findViewById(R.id.AdminSignUpActivityRB1);
        RB2 = (RadioButton)findViewById(R.id.AdminSignUpActivityRB2);
        RB3 = (RadioButton)findViewById(R.id.AdminSignUpActivityRB3);
        ET7 = (EditText)findViewById(R.id.AdminSignUpActivityET7);
    }


    public void onClickAdminSignUpExitActivity(View view){
        finish();
        System.exit(0);
    }


    public void onClickGoToAdminLogInActivity(View view){
        firstname = (ET1.getText()).toString();
        lastname = (ET2.getText()).toString();
        mail = (ET3.getText()).toString();
        phone = (ET4.getText()).toString();
        username = (ET5.getText()).toString();
        password = (ET6.getText()).toString();
        selectedID = radioGroup.getCheckedRadioButtonId();
        if(selectedID == RB1.getId()){
            type = "HO";
            String_ui="Patient ID";
        }
        else if(selectedID == RB2.getId()){
            type = "DC";
            String_ui="Asset ID";
        }
        else if(selectedID == RB3.getId()){
            type = "MU";
            String_ui="Exhibit ID";
        }
        else{
            type = null;
        }
        name = (ET7.getText()).toString();
        //Adding the Environment to Applications List.
        Log.d("FBs",db_url);
        reference = new Firebase(db_url +"/Applications_List/");
        map_appli = new HashMap<String,Object>();
        map_appli.put(name,name);
        reference.updateChildren(map_appli);
        //Adding the Environment to Application.
        ref = new Firebase(db_url + name + "/");
        //Setting the value of Application Node.
        ref_app = ref.child("Application");
        map_app = new HashMap<String,Object>();
        map_app.put("fetcher", " ");
        map_app.put("name", name);
        ref_app.setValue(map_app);
        //Setting the value of Template Node.
        ref_temp = ref.child("Template");
        map_temp = new HashMap<String,Object>();
        map_temp.put("fetcher", " ");
        map_temp.put("type", type);
        map_temp.put("Voice",1);
        map_temp.put("Encryption",0);
        map_temp.put("String ui",String_ui);
        map_temp.put("Language","en");
        ref_temp.setValue(map_temp);
        //Setting the value of Users_Credential Node.
        ref_ucred = ref.child("Users_Credential/" + username);
        map_ucred = new HashMap<String,Object>();
        map_ucred.put("fetcher", " ");
        map_ucred.put("password", password);
        ref_ucred.setValue(map_ucred);
        //Setting the value of Users_Detail Node.
        ref_udet = ref.child("Users_Detail/" + username);
        map_udet = new HashMap<String,Object>();
        map_udet.put("fetcher"," ");
        map_udet.put("firstname",firstname);
        map_udet.put("lastname",lastname);
        map_udet.put("mail",mail);
        map_udet.put("phone",phone);
        map_udet.put("role","GA");
        map_udet.put("Access","0xf");
        //map_udet.put("sector-1","RW");
        //map_udet.put("sector-2","RW");
        map_udet.put("username",username);
        ref_udet.setValue(map_udet);
        //Setting the value of Users_List Node.
        ref_uli = ref.child("Users_List");
        map_uli = new HashMap<String,Object>();
        map_uli.put("fetcher"," ");
        map_uli.put(username,username);
        ref_uli.setValue(map_uli);
        //Setting the value of Users_Role Node.
        ref_uro = ref.child("Users_Role/" + username);
        map_uro = new HashMap<String,Object>();
        map_uro.put("fetcher"," ");
        map_uro.put("role","GA");
        ref_uro.setValue(map_uro);
        //Setting the value of Users_Access Node.
        ref_acc = ref.child("Users_Access/" + username);
        map_acc = new HashMap<String,Object>();
        map_acc.put("fetcher"," ");
        map_acc.put("Access","0xf");
        ref_acc.setValue(map_acc);
        Toast.makeText(AdminSignUpActivity.this,"Signed Up Successfully",Toast.LENGTH_LONG).show();
        intent = new Intent(AdminSignUpActivity.this,AdminLogInActivity.class);
        startActivity(intent);
    }


}
