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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.firebase.client.Firebase;
import java.util.HashMap;
import java.util.Map;


public class AdminCreateUserActivity extends AppCompatActivity{


    private EditText ET1,ET2,ET3,ET4,ET5,ET6,ET7;
    private RadioGroup RG1,RG2;
    private RadioButton RG1RB1,RG1RB2,RG1RB3,RG2RB1,RG2RB2,RG2RB3,RG1RB4;
    private String firstname,lastname,mail,password,username,phone,role,Access,sector2,environment;
    private int selectID1,selectID2;
    private Firebase reference,ref_uac,ref_ucr,ref_ude,ref_uli,ref_uro;
    private SharedPreferences sharedPreferences;
    private Map<String,Object> map_uac,map_ucr,map_ude,map_uli,map_uro;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_create_user);
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
        ET1 = (EditText)findViewById(R.id.AdminCreateUserActivityET1);
        ET2 = (EditText)findViewById(R.id.AdminCreateUserActivityET2);
        ET3 = (EditText)findViewById(R.id.AdminCreateUserActivityET3);
        ET4 = (EditText)findViewById(R.id.AdminCreateUserActivityET4);
        ET5 = (EditText)findViewById(R.id.AdminCreateUserActivityET5);
        ET6 = (EditText)findViewById(R.id.AdminCreateUserActivityET6);
        ET7 = (EditText)findViewById(R.id.AdminCreateUserActivityET7);
        RG1 = (RadioGroup)findViewById(R.id.AdminCreateUserActivityRG1);
        //RG2 = (RadioGroup)findViewById(R.id.AdminCreateUserActivityRG2);
        RG1RB1 = (RadioButton)findViewById(R.id.AdminCreateUserActivityRG1RB1);
        RG1RB2 = (RadioButton)findViewById(R.id.AdminCreateUserActivityRG1RB2);
        RG1RB3 = (RadioButton)findViewById(R.id.AdminCreateUserActivityRG1RB3);
       RG1RB4=(RadioButton)findViewById(R.id.AdminCreateUserActivityRG1RB4);
    }


    public void onClickAdminCreateUserActivityGoBack(View view){
        finish();
        System.exit(0);
    }


    public void onClickAdminCreateUserActivityCreate(View view){
        sharedPreferences = getSharedPreferences("Smart_Tagging",Context.MODE_PRIVATE);
        environment = sharedPreferences.getString("environment",null);
        if(environment != null){
            firstname = (ET1.getText()).toString();
            lastname = (ET2.getText()).toString();
            mail = (ET3.getText()).toString();
            phone = (ET4.getText()).toString();
            username = (ET5.getText()).toString();
            password = (ET6.getText()).toString();
            selectID1 = RG1.getCheckedRadioButtonId();
            if (selectID1 == RG1RB1.getId()){
                Access = "0x4";
            } else if (selectID1 == RG1RB2.getId()){
                Access = "0x8";
            } else if (selectID1 == RG1RB3.getId()){
                Access = "0x2";
            }
            else if (selectID1==RG1RB4.getId()){
                Access="0x1";
            }
            else {
                Access = null;
            }
            try{
                selectID2 = RG2.getCheckedRadioButtonId();
            if (selectID2 == RG2RB1.getId()){
                sector2 = "R";
            } else if (selectID2 == RG2RB2.getId()){
                sector2 = "W";
            } else if (selectID2 == RG2RB3.getId()){
                sector2 = "RW";
            } else {
                sector2 = null;
            }
            }
            catch (NullPointerException e){
                e.printStackTrace();
            }

            role = (ET7.getText()).toString();
            reference = new Firebase("https://amber-inferno-6557.firebaseio.com/Smart_Tagging/" + environment + "/");
            //Setting the value of Users_Access Node.
            ref_uac = reference.child("Users_Access/" + username);
            map_uac = new HashMap<String, Object>();
            map_uac.put("fetcher"," ");
            map_uac.put("Access",Access);
            //map_uac.put("sector-2",sector2);
            ref_uac.updateChildren(map_uac);
            //Setting the value of Users_Credential Node.
            ref_ucr = reference.child("Users_Credential/" + username);
            map_ucr = new HashMap<String, Object>();
            map_ucr.put("fetcher"," ");
            map_ucr.put("password",password);
            ref_ucr.updateChildren(map_ucr);
            //Setting the value of the Users_Detail Node.
            ref_ude = reference.child("Users_Detail/" + username);
            map_ude = new HashMap<String, Object>();
            map_ude.put("fetcher"," ");
            map_ude.put("firstname",firstname);
            map_ude.put("lastname",lastname);
            map_ude.put("mail",mail);
            map_ude.put("phone",phone);
            map_ude.put("role",role);
            map_ude.put("Access",Access);
            //map_ude.put("sector-2",sector2);
            map_ude.put("username",username);
            ref_ude.updateChildren(map_ude);
            //Setting the value of the Users_List Node.
            ref_uli = reference.child("Users_List");
            map_uli = new HashMap<String, Object>();
            map_uli.put("fetcher"," ");
            map_uli.put(username,username);
            ref_uli.updateChildren(map_uli);
            //Setting the value of the Users_Role Node.
            ref_uro = reference.child("Users_Role/" + username);
            map_uro = new HashMap<String, Object>();
            map_uro.put("fetcher"," ");
            map_uro.put("role",role);
            ref_uro.setValue(map_uro);
            Toast.makeText(AdminCreateUserActivity.this,"Created User Successfully",Toast.LENGTH_LONG).show();
            intent = new Intent(AdminCreateUserActivity.this,AdminListUsersActivity.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(AdminCreateUserActivity.this,"You are not Logged In",Toast.LENGTH_LONG).show();
            intent = new Intent(AdminCreateUserActivity.this,AdminLogInActivity.class);
            startActivity(intent);
        }
    }


}
