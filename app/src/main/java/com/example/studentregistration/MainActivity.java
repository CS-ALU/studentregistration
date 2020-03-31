package com.example.studentregistration;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity<ParsedNdefRecord> extends AppCompatActivity {
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private TextView name;
    private TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name= findViewById(R.id.name);
        email=findViewById(R.id.email);

        nfcAdapter=NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter==null){
            Toast.makeText(this,"no NFC",Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        pendingIntent= new PendingIntent.getActivity(this,0,//This is not correct. You cannot use .getActivity on Main Activity java class
                new Intent(this,this.getClass())
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),0);

        //the spinner for the branches

        Spinner branch_spinner = findViewById(R.id.branch_spinner);

        ArrayAdapter<String> branch_adapter= new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.branch_array));
        branch_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        branch_spinner.setAdapter(branch_adapter);

            //the spinner for the Years
        Spinner years_spinner = findViewById(R.id.year_spinner);

        ArrayAdapter<String> years_adapter= new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.year_array));
        years_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        years_spinner.setAdapter(years_adapter);


        //the spinner for the Semesters
        Spinner semester_spinner = findViewById(R.id.semseter_spinner);

        ArrayAdapter<String> semester_adapter= new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.semester_array));
        semester_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semester_spinner.setAdapter(semester_adapter);


    }
    @Override
    protected void onResume(){
        super.onResume();
        if(nfcAdapter != null){
            if (!nfcAdapter.isEnabled())
                showWirelessSetting();
            nfcAdapter.enableForegroundDispatch(this,pendingIntent,null,null);

        }

    }
    @Override
    protected void  onPause(){
        super.onPause();
        if (nfcAdapter !=null){

            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent){
        setIntent(intent);
        resolveIntent(intent);

    }

    private void resolveIntent(Intent intent){
        String action= intent.getAction();

        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
            || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
            || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)){
           Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs;

            if (rawMsgs!= null){
                msgs= new  NdefMessage[rawMsgs.length];

                for (int i=0; i< rawMsgs.length;i++){
                    msgs[i]=(NdefMessage) rawMsgs[i];
                }
            }else{
                byte[]empty=new byte[0];
                byte[]id=intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
                Tag tag=(Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                byte[] payload = dumpTagData(tag).getBytes();
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN,empty,id,payload);
                NdefMessage msg = new NdefMessage(new NdefRecord[] {record});
                msgs= new NdefMessage[] {msg};


            }
            displayMsgs(msgs);
    }

}

    private void displayMsgs(NdefMessage[] msgs) {
        if (msgs== null || msgs.length==0)
            return;

        StringBuilder builder = new StringBuilder();
        List<ParsedNdefRecord> records = NdefMessageParser.parse(msgs[0]);
        final int size =records.size();

        for (int i = 0; i< size; i++){
            ParsedNdefRecord record = records.get(i);
            String str;
            str = record.toString();
            builder.append(str).append("\n");
        }

        name.setText(builder.toString());
        email.setText(builder.toString());

    }

    private String dumpTagData(Tag tag){
        StringBuilder sb= new StringBuilder();
        byte[] id= tag.getId();
    }
    private void showWirelessSetting(){
        Toast.makeText(this, "You need to enable NFC", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
    }
}
