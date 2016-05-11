package com.example2.parth.smart_tagging;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ViewTagLogsActivity extends AppCompatActivity {
    TextView tagId,tagLog;
    SharedPreferences sharedPreferencesReadLog;
    String tagIdent,env;
    Firebase referenceTagLog;
    Map<String,Object> fetcherLog;
    String tagLogsData="",logValue,db_url;
    StringBuffer tagLogData=new StringBuffer("");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tag_logs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Firebase.setAndroidContext(this);
        tagId = (TextView) findViewById(R.id.textViewtagId);
        tagLog = (TextView) findViewById(R.id.textViewtagLog);
        tagLog.setMovementMethod(new ScrollingMovementMethod());
        sharedPreferencesReadLog = getSharedPreferences("Smart_Tagging", Context.MODE_PRIVATE);
        tagIdent = sharedPreferencesReadLog.getString("TagId", null);
        env = sharedPreferencesReadLog.getString("environment", null);
        db_url = sharedPreferencesReadLog.getString("firebasedburl", null);
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        tagId.setText("Logs of " + tagIdent);
        if (tagIdent != null) {


            referenceTagLog = new Firebase(db_url).child(env).child("Tags").child(tagIdent).child("Logs");
            fetcherLog = new HashMap<String, Object>();
            fetcherLog.put("fetcher", "");
            referenceTagLog.updateChildren(fetcherLog);
            referenceTagLog.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // tagLogsData= new String[(int) dataSnapshot.getChildrenCount()];
                    int i = 0;
                    for (DataSnapshot resultDataSnapShot : dataSnapshot.getChildren()) {

                        logValue = resultDataSnapShot.getValue().toString();
                        tagLogData = tagLogData.append(logValue);
                        tagLogData = tagLogData.append("\n\n");
                        // i++;
                    }
                    tagLog.setText(tagLogData);

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

        }
        else{
            Toast.makeText(this,"No Logs found",Toast.LENGTH_SHORT).show();
        }
    }
}
