package com.example.calleridapplication;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.CallerIdApplication.R;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SaveLogsPage extends AppCompatActivity {
    EditText ETdescription;
    String desc;
    String phNumber;
    String callDate;
    String callDuration;
    Date dateFormat;
    String callDayTimes;
    String user_id="858576c3-59db-ec11-bb3d-000d3a66d2a8";
    String number = Window.numberToFetch;
    TextView durationTxtView;
    String owner_id;
    String duration,time,date;
    DataBaseHelper dataBaseHelper;
    TextView logsStatus,contact;
    CardView logsCardView;
    String direction;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.savelog_layout);
        dataBaseHelper = new DataBaseHelper(SaveLogsPage.this);
        // set onClickListener on the remove button, which removes
        // the view from the window

        logsCardView = findViewById(R.id.logscardView);
        ETdescription = findViewById(R.id.description);
        contact=findViewById(R.id.contact);
        durationTxtView =findViewById(R.id.duration);
        logsStatus = findViewById(R.id.logsStatus);
        fetchLogs();

        durationTxtView.setText(callDuration);
        contact.setText("Call with : \n"+Window.contactFound.getContact_fname()+" " +Window.contactFound.getContact_lname()+"\n "+Window.contactFound.getContact_job()+"@"+Window.contactFound.getContact_company());
        findViewById(R.id.savephonecall).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {


                desc = ETdescription.getText().toString().trim();
                

            }
        });
findViewById(R.id.buttonClose2).setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent i = new Intent(getApplicationContext(),SaveLogsPage.class);
       // SaveLogsPage.this.finishActivity(i);
    }
});
    }

        private void saveLogs(String desc, String duration, String phNumber, String dateFormat,String direction) {
        logsCardView.setEnabled(false);
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
            RequestBody body=null;

            body = RequestBody.create(mediaType, "{\r\n    \"subject\": \" "+desc+" \"\r\n," +
                   "\r\n    \"phonenumber\": \" "+phNumber+" \"\r\n," +
               //     "\r\n    \"scheduledstart\": \" "+dateFormat+" \"\r\n," +
               //     "\r\n    \"_createdby_value\": \" "+user_id+" \"\r\n," +
                  //  "\r\n    \"from\": \" "+Window.contactid+" \"\r\n," +
                    //   "\r\n    \"actualdurationminutes\": \" "+ duration+" \"\r\n" +
                    "}");

   //     }

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("https://calleridcrmapi.azure-api.net/phonecalls")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                //.addHeader("Cookie", "ReqClientId=30c78179-6c3a-4708-8376-907a89493c54; last_commit_time=2022-05-31 12:54:18Z; orgId=8b7545a7-1d2b-48d7-be9d-832648fff0e3")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("okhttp1",e.toString());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {

                    Log.e("okhttp2",response.toString());

                     logsStatus.setText("successfully added!");
                 //   Toast.makeText(SaveLogsPage.this,"successfullt saved",Toast.LENGTH_LONG).show();
                    throw new IOException("Unexpected code " + response);
                }else{
                    //  Toast.makeText(getActivity(),"sucess",Toast.LENGTH_LONG).show();

                    Log.e("success",response.toString());

                   // Toast.makeText(SaveLogsPage.this,"successfullt saved",Toast.LENGTH_LONG).show();
                    Log.d("create:","success");
                }

                // you code to handle response
            }

        });
    }
















    @RequiresApi(api = Build.VERSION_CODES.N)
    public void fetchLogs(){


        ContentResolver cr = SaveLogsPage.this.getContentResolver();
        Cursor c = cr.query(CallLog.Calls.CONTENT_URI, null, null, null, null);

        int totalCall = 10;

        if (c != null) {
            totalCall = 1; // intenger call log limit

            if (c.moveToLast()) { //starts pulling logs from last - you can use moveToFirst() for first logs
                for (int j = 0; j < totalCall; j++) {


                    phNumber = c.getString(c.getColumnIndexOrThrow(CallLog.Calls.NUMBER));
                    callDate = c.getString(c.getColumnIndexOrThrow(CallLog.Calls.DATE));
                    callDuration = c.getString(c.getColumnIndexOrThrow(CallLog.Calls.DURATION));
                    dateFormat= new Date(Long.valueOf(callDate));
                    callDayTimes = String.valueOf(dateFormat);
                    //DateTimeFormatter dt = new DateTimeFormatterBuilder(dateFormat);
                    //Log.d('DATETIME:',dt.formatGmt('yyyy-MM-dd\'T\'HH:mm:ss.SSS\'Z\''));

                    switch (Integer.parseInt(c.getString(c.getColumnIndexOrThrow(CallLog.Calls.TYPE)))) {
                        case CallLog.Calls.OUTGOING_TYPE:
                            direction = "OUTGOING";
                            break;
                        case CallLog.Calls.INCOMING_TYPE:
                            direction = "INCOMING";
                            break;
                        case CallLog.Calls.MISSED_TYPE:
                            direction = "MISSED";
                            break;
                        default:
                            break;
                    }

//Wed jun 01 00:14:43 GMT +03:00 2022
                    String inputPattern = "DDD MMM dd HH:mm:ss ZZZ YYYY";
                    String outputPattern = "YYYY-MM-DDTHH:mm:ssZ";
                    SimpleDateFormat inputFormat = null;
                    String str = null;
                /*    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        inputFormat = new SimpleDateFormat(inputPattern);
                        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
                        Date date = dateFormat;


                        try {
                            date = inputFormat.parse(time);
                            str = outputFormat.format(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

*/

                    //  c.moveToPrevious(); // if you used moveToFirst() for first logs, you should this line to moveToNext
                                                                          //0Wed jun 01 00:14:43 GMT +03:00 2022null    dd mm day hh:mm:ss tzone hrs yyy
                    //Toast.makeText(SaveLogsPage.this, phNumber + callDuration + callDayTimes + direction, Toast.LENGTH_SHORT).show(); // you can use strings in this line
                   // saveLogs(desc,callDuration,phNumber,dateFormat,direction);
                    updateUI(callDuration,phNumber,dateFormat.toString(),direction);
                }
            }
            c.close();
        }
    }

    private void updateUI(String callDuration, String phNumber, String dateFormat, String direction) {

        durationTxtView.setText(callDuration);
        desc = ETdescription.getText().toString().trim();
        saveLogs(desc,duration,phNumber,dateFormat,direction);



    }


}
