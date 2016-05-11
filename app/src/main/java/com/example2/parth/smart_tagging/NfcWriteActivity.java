package com.example2.parth.smart_tagging;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.speech.tts.TextToSpeech;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.firebase.client.Firebase;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

/**
 * Created by rnjth on 06-05-2016 successfully at 16:56
 */
public class NfcWriteActivity extends Activity {

    Locale myLocale;
    NfcAdapter nfcAdapter;
    byte access_control;
    String uid_string;
    int speak;
    int encryption;
    int nfcFormat = 0;
    int nfcWrite = 0;
    ToggleButton tglReadWrite;
    EditText textTagContent;
    TextView textView1;
    String readText = null;
    NdefMessage msg;
    private TextToSpeech t1;
    nfcSecurity nfcTagSecurity;
    String textDecrypt;
    int i = 0;
    Intent ii;
    Object obj,obj1;
    Bundle b;
    Long children;
    Long counters,count;
    String counter,db_url;
    StringTokenizer st,st1;
    String Id,env,LogKey,LogValue,uname;
    private Map<String,Object> fetcher2;
    private Firebase reference_logs;
    SimpleDateFormat s,s1;
    private SharedPreferences sharedPreferences1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_write);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        sharedPreferences1 = getSharedPreferences("Smart_Tagging", Context.MODE_PRIVATE);
        Firebase.setAndroidContext(this);
        env=sharedPreferences1.getString("environment",null);
        uname=sharedPreferences1.getString("username",null);
        db_url=sharedPreferences1.getString("firebasedburl",null);
        textTagContent = (EditText) findViewById(R.id.textTagContent);
        textView1 = (TextView) findViewById(R.id.textView1);
        setLocale("en");
        access_control = 0xf;
        //access_control=0xe;
        uid_string = "Patient ID";
        speak = 1;
        encryption = 0;
        b=getIntent().getExtras();

        //ii=getIntent();
        if(b!=null){
            count=b.getLong("len");
            counter=b.getString("Coounter");
        }
       // counters=  getIntent().getExtras().getLong("Length");

        Toast.makeText(NfcWriteActivity.this,counter+" :"+counters,Toast.LENGTH_SHORT).show();
        // Toast.makeText(NfcActivity.this,"Counters: "+counters,Toast.LENGTH_SHORT).show();
        nfcTagSecurity = new nfcSecurity();

        Log.d("NFCTAG", " start NfcActivity");

        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });
        byte[] encryptedValue = new byte[0];
        try {
            // encryptedValue = nfcSecurity.encrypt("plz try encrypt and decrypt me");
            encryptedValue = nfcSecurity.encrypt(" ");
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        Log.d("NFCTAG encryptedValue1 ", String.valueOf(encryptedValue));
        String decryptedValue = null;
        try {
            decryptedValue = nfcSecurity.decrypt(encryptedValue);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        Log.d("NFCTAG encryptdValue2::", String.valueOf(decryptedValue));

    }

    @Override
    protected void onResume() {
        super.onResume();

        enableForegroundDispatchSystem();
    }

    @Override
    protected void onPause() {
        super.onPause();

        disableForegroundDispatchSystem();
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("NFCTAG", " intent");
        String writeText;


        if (intent.hasExtra(NfcAdapter.EXTRA_TAG)) {
            Log.d("NFCTAG", "NfcIntent!");

            if (nfcFormat == 1) {
                nfcFormat = 0;
                if ((access_control & 0x2) == 0x2) {
                    writeText = "start_record";
                    if (encryption == 1) {

                        byte[] encryptedText = new byte[0];
                        try {
                            encryptedText = nfcSecurity.encrypt(writeText);
                        } catch (InvalidKeyException e) {
                            e.printStackTrace();
                        } catch (BadPaddingException e) {
                            e.printStackTrace();
                        } catch (IllegalBlockSizeException e) {
                            e.printStackTrace();
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                        writeText = String.valueOf(encryptedText);
                        Log.d("NFCTAG encrpyted string", writeText);
                    }

                    Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                    msg = createNdefMessage(writeText);

                    Log.d("NFCTAG nfcFormat", String.valueOf(msg));
                    writeNdefMessage(tag, msg);
                    // formatTag(tag,msg);

                }
            } else {
                if (nfcWrite == 1) {
                    nfcWrite = 0;
                    if ((access_control & 0x8) == 0x8) {


                        Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

                        try {
                            readText = readTextFromMessage((NdefMessage) parcelables[0]);
                        } catch (BadPaddingException e) {
                            e.printStackTrace();
                        } catch (InvalidKeyException e) {
                            e.printStackTrace();
                        } catch (IllegalBlockSizeException e) {
                            e.printStackTrace();
                        }
                        if (encryption == 1) {
                            Log.d("NFCTAG before decrypt", String.valueOf(textDecrypt));
                            try {
                                readText = nfcSecurity.decrypt(readText.getBytes());
                            } catch (InvalidKeyException e) {
                                e.printStackTrace();
                            } catch (BadPaddingException e) {
                                e.printStackTrace();
                            } catch (IllegalBlockSizeException e) {
                                e.printStackTrace();
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            }
                            Log.d("NFCTAG after decrypt", readText);
                        }

                        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

                        EditText et1 = (EditText) findViewById(R.id.textTagContent);

                        if (readText.equals(" ")) {
                            String textDisplay = "Enter " + uid_string + ": ";
                            Toast.makeText(this, textDisplay, Toast.LENGTH_SHORT).show();
                            // msg = createNdefMessage(uid_string + ": " + et1.getText().toString());
                            writeText = uid_string + ": " + et1.getText().toString();
                        } else {
                            writeText = readText + "\n" + et1.getText().toString();
                        }
                        if (encryption == 1) {
                            byte[] encryptedText = new byte[0];
                            try {
                                encryptedText = nfcSecurity.encrypt(writeText);
                            } catch (InvalidKeyException e) {
                                e.printStackTrace();
                            } catch (BadPaddingException e) {
                                e.printStackTrace();
                            } catch (IllegalBlockSizeException e) {
                                e.printStackTrace();
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            }
                            writeText = String.valueOf(encryptedText);
                            Log.d("NFCTAG encrpyted string", writeText);
                        }
                        msg = createNdefMessage(writeText);

                        //msg = createNdefMessage(writeText);
                        writeNdefMessage(tag, msg);

                        st=new StringTokenizer(readText,"\n");

                        st1=new StringTokenizer(st.nextToken(),":");

                        Log.d("InsideToken:",st1.nextToken());
                        Id=st1.nextToken();
                        Log.d("Id:",Id);
                        reference_logs=new Firebase(db_url).child(env).child("Tags").child(Id).child("Logs");
                        fetcher2=new HashMap<String, Object>();
                        s1=new SimpleDateFormat("dMyyhms");
                        s=new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                        LogKey="Log "+s1.format(new Date());
                        LogValue="Tag Written at: "+s.format(new Date())+" by "+uname+" Content: "+writeText;
                        s.format(new Date());
                        fetcher2.put(LogKey,LogValue);
                        reference_logs.updateChildren(fetcher2);

                        et1.setText(" ");
                    }

                }
            }

        }
    }

    private String readTextFromMessage(NdefMessage ndefMessage) throws BadPaddingException, InvalidKeyException, IllegalBlockSizeException {

        NdefRecord[] ndefRecords = ndefMessage.getRecords();

        if (ndefRecords != null && ndefRecords.length > 0) {

            NdefRecord ndefRecord = ndefRecords[0];
            //NdefRecord ndefRecord1=ndefRecord[1];

            String tagContent = getTextFromNdefRecord(ndefRecord);

            // Log.d("NFCTAG before decrypt", tagContent);
            // String tagContentDecrypt = nfcSecurity.decrypt(tagContent.getBytes());
            // Log.d("NFCTAG after decrypt", tagContentDecrypt);

            // return tagContentDecrypt;
            return tagContent;

        } else {
            Toast.makeText(this, "No NDEF records found!", Toast.LENGTH_SHORT).show();
            Log.d("NFCTAG", "No NDEF records found!");
            return null;
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin_log_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void enableForegroundDispatchSystem() {

        Intent intent = new Intent(this, NfcWriteActivity.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        IntentFilter[] intentFilters = new IntentFilter[]{};

        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);
    }

    private void disableForegroundDispatchSystem() {
        nfcAdapter.disableForegroundDispatch(this);
    }

    private void formatTag(Tag tag, NdefMessage ndefMessage) {
        try {

            NdefFormatable ndefFormatable = NdefFormatable.get(tag);

            if (ndefFormatable == null) {
                Toast.makeText(this, "Tag is not ndef formatable!", Toast.LENGTH_SHORT).show();
                Log.d("NFCTAG", "Tag is not ndef formatable!");
                return;
            }


            ndefFormatable.connect();
            ndefFormatable.format(ndefMessage);
            ndefFormatable.close();

            Toast.makeText(this, "Tag formatted!", Toast.LENGTH_SHORT).show();
            Log.d("NFCTAG", "Tag formatted!");
        } catch (Exception e) {
            Log.d("NFCTAG", e.getMessage());
        }

    }

    private void writeNdefMessage(Tag tag, NdefMessage ndefMessage) {
        Log.d("NDFTAG", "writing ndefmessage");
        try {

            if (tag == null) {
                Toast.makeText(this, "Tag object cannot be null", Toast.LENGTH_SHORT).show();
                Log.d("NDFTAG", "Tag object cannot be null");
                return;
            }

            Ndef ndef = Ndef.get(tag);

            if (ndef == null) {
                // format tag with the ndef format and writes the message.
                formatTag(tag, ndefMessage);
            } else {
                ndef.connect();

                if (!ndef.isWritable()) {
                    Toast.makeText(this, "Tag is not writable!", Toast.LENGTH_SHORT).show();
                    Log.d("NFCTAG", "Tag is not writable!");
                    ndef.close();
                    return;
                }

                ndef.writeNdefMessage(ndefMessage);
                ndef.close();

                Toast.makeText(this, "Tag written!", Toast.LENGTH_SHORT).show();
                Log.d("NFCTAG", "Tag written!");
            }

        } catch (Exception e) {
            Log.d("NFCTAG writeNdefMessage", e.getMessage());
        }

    }


    private NdefRecord createTextRecord(String content) {
        Log.d("NFCTAG", "Text Record Creating started");
        try {
            byte[] language;
            language = Locale.getDefault().getLanguage().getBytes("UTF-8");

            final byte[] text = content.getBytes("UTF-8");
            final int languageSize = language.length;
            byte[] applicationrec;
            final int textLength = text.length;
            final ByteArrayOutputStream payload = new ByteArrayOutputStream((1 + languageSize + textLength));
            //payload=new ByteArrayOutputStream()
            Log.d("NFCTAG", "Size used in tag: " + (1 + languageSize + textLength));
            payload.write((byte) (languageSize & 0x1F));
            // payload.write();
            payload.write(language, 0, languageSize);
            payload.write(text, 0, textLength);
            //payload.write(applicationrec,0,);
            //payload.write(text,0,textLength);


            return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload.toByteArray());

        } catch (UnsupportedEncodingException e) {
            Log.d("NFCTAG createTextRecord", e.getMessage());
        }
        return null;
    }


    private NdefMessage createNdefMessage(String content) {
        Log.d("NFCTAG", "Creating NDef message");

        NdefRecord[] nr = new NdefRecord[3];
        nr[0] = createTextRecord(content);
        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{nr[0]});
       /*defMessage msg = new NdefMessage(
                new NdefRecord[]{
                        NdefRecord.createMime(
                                "application/vnd.com.example.android.beam",
                                content.getBytes())
                });*/
        return ndefMessage;
    }



    public String getTextFromNdefRecord(NdefRecord ndefRecord) {

        Log.d("NFCTAG", "Reading Text From NDef Record");
        String tagContent = null;
        String textEncoding;
        String s = null;
        try {
            byte[] payload = ndefRecord.getPayload();
            //String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
            int languageSize = payload[0] & 0063;
            //tagContent=new String();

            if ((payload[0] & 128) == 0) textEncoding = "UTF-8";
            else textEncoding = "UTF-16";
            tagContent = new String(payload, languageSize + 1,
                    payload.length - languageSize - 1, textEncoding);
            //new String()
            Log.d("NFCTAG", "Creating string from ndefrecord");
        } catch (UnsupportedEncodingException e) {
            Log.d("NFCTAG msg", e.getMessage(), e);
        }
        Log.d("NFCTAG", tagContent);
        return tagContent;
    }



    public void nfcWrite(View view) {
        if ((access_control & 0x8) == 0x8) {
            nfcWrite = 1;
        }
    }

    public void setLocale(String lang) {
        myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        // Intent refresh = new Intent(this, MainActivity.class);
        // startActivity(refresh);
        // finish();
    }



}
