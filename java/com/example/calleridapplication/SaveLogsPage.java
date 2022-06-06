package com.example.calleridapplication;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.ColorSpace;
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
import com.microsoft.identity.client.AuthenticationCallback;
import com.microsoft.identity.client.IAuthenticationResult;
import com.microsoft.identity.client.exception.MsalException;

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
    CallLogs cl2;
    CallLogs cl=null;
    String callDayTimes;
    String id;
    private final static String[] SCOPES = {"Files.Read","Mail.Read"};
    String useremail;
   // String secondPersonContactid = Window.contactid;
    String secondPersonContactid="858576c3-59db-ec11-bb3d-000d3a66d2a8";
    String number = Window.numberToFetch;
    TextView durationTxtView;
    String owner_id;
    String duration,time,date;
    DataBaseHelper dataBaseHelper;
    DataBaseHelper2 dataBaseHelper2;
    TextView logsStatus,contact;
    CardView logsCardView;
    String direction;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.savelog_layout);
        dataBaseHelper = new DataBaseHelper(SaveLogsPage.this);
        dataBaseHelper2 = new DataBaseHelper2(SaveLogsPage.this);




        // set onClickListener on the remove button, which removes
        // the view from the window

        logsCardView = findViewById(R.id.logscardView);
        ETdescription = findViewById(R.id.description);
        contact=findViewById(R.id.contact);
        durationTxtView =findViewById(R.id.duration);
        logsStatus = findViewById(R.id.logsStatus);
        //fetchLogs();
        if(CallLogsAdapter.openedfromfrag){
            CallLogsAdapter.openedfromfrag = false;
            Bundle bundle=getIntent().getExtras();
            id = bundle.getString("id").trim();
            Log.d("idsss",id);
            fetchCallLog(id);


        }

        durationTxtView.setText(callDuration);
      //  contact.setText("Call with : \n"+Window.contactFound.getContact_fname()+" " +Window.contactFound.getContact_lname()+"\n "+Window.contactFound.getContact_job()+"@"+Window.contactFound.getContact_company());

if(signin_fragment.is_signedin){
        contact.setText( signin_fragment.Email.replaceAll("\"","").trim());
        logsStatus.setText("");
}
        if(CallLogsAdapter.openedfromfrag){
            durationTxtView.setText(cl.getDuration());
            ETdescription.setText("");
            contact.setText(dataBaseHelper.getContactName(cl.getCallerid()));
        }
        findViewById(R.id.savephonecall).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {

                if(CallLogsAdapter.openedfromfrag){
                    if(signin_fragment.is_signedin){
                        useremail = signin_fragment.Email.replaceAll("\"","").trim();


                        desc = ETdescription.getText().toString().trim();
                       saveLogs(desc,Integer.parseInt(cl.getDuration()),cl.getPhoneNbre(),cl.getDate(), Boolean.valueOf(cl.getDirection()));
                      dataBaseHelper2.modifySaved(cl);

                    logsStatus.setTextColor(0xFFFF0000);
                    logsStatus.setText("you need to sign in before u save a log call!");
                    }else {

                        durationTxtView.setText("error");
                        ETdescription.setText("error");
                        logsCardView.setVisibility(View.GONE);
                        logsStatus.setTextColor(0xFFFF0000);
                        logsStatus.setText("you need to sign in before u save a log call!");
                        logsStatus.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                signIn();
                            }
                        });
                    }
                }else{
                if(signin_fragment.is_signedin){
                    //opening from call
                    // -> straight yaane contact found
                    //aw after creation of contact

                useremail = signin_fragment.Email.replaceAll("\"","").trim();

                    fetchLogs();
               desc = ETdescription.getText().toString().trim();

                }else {

                    durationTxtView.setText("error");
                    ETdescription.setText("error");
                    logsCardView.setVisibility(View.GONE);
                    logsStatus.setTextColor(0xFFFF0000);
                    logsStatus.setText("you need to sign in before u save a log call!");
                    logsStatus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            signIn();
                        }
                    });
                }

                

            }}
        });
findViewById(R.id.buttonClose2).setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent i = new Intent(getApplicationContext(),SaveLogsPage.class);
        finishActivity(0);
    }
});



    }

    private void fetchCallLog(String id) {
        cl =dataBaseHelper2.getLoginfo(id);
        updateUI(cl.getDuration(),cl.getPhoneNbre(),cl.getDate(), Boolean.valueOf(cl.getDirection()));
    }

    private void signIn() {

      //  signin_fragment.mSingleAccountApp.signIn(SaveLogsPage.this, null, SCOPES, getAuthInteractiveCallback());
    }

    private void saveLogs(String desc, int duration, String phNumber, String dateFormat,Boolean direction) {
        logsCardView.setEnabled(false);
        String userContactid = null;
if(createContact.openFromCreate){
    createContact.openFromCreate = false;
     userContactid = createContact.contactid;
}else{
        userContactid = fetchContactid(signin_fragment.Email);
}
        // String userContactid ="cdcfa450-cb0c-ea11-a813-000d3a1b1223";
        String clientContactid = fetchContactidByphone(phNumber.trim());
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
            RequestBody body=null;
if(direction)
{
    //outgoing
    body = RequestBody.create(mediaType," {   \"subject\": \" "+desc+"\",\n" +
            "    \"phonenumber\": \""+phNumber+"\",\n" +
            "    \"description\": \"My description111\",\n" +
            "    \"directioncode\": "+direction+", //Direction : 0-->False/Incomming, 1-->True/Outgoing,\n" +
            "    \"scheduledstart\":\""+dateFormat+"\",\n" +
            "    \"actualdurationminutes\":\""+duration+"\",\n" +
            "   \"regardingobjectid_contact@odata.bind\": \"/contacts("+userContactid+")\", //Regarding is a contact\n" +
            "    \"phonecall_activity_parties\": [\n" +
            "       {\n" +
            "            \"partyid_contact@odata.bind\": \"/contacts("+userContactid+")\", // call started by a sustemuser\n" +
            "            \"participationtypemask\" : 1 // From\n" +
            "        },\n" +
            "        {\n" +
            "            \"partyid_contact@odata.bind\": \"/contacts("+clientContactid+")\", // call to by a contact\n" +
            "            \"participationtypemask\": 2 // To\n" +
            "        }\n" +
            "    ]}");

}else{
//incomming
    body = RequestBody.create(mediaType," {   \"subject\": \" "+desc+"\",\n" +
            "    \"phonenumber\": \""+phNumber+"\",\n" +
            "    \"description\": \"My description111\",\n" +
            "    \"directioncode\": "+direction+", //Direction : 0-->False/Incomming, 1-->True/Outgoing,\n" +
            "    \"scheduledstart\":\""+dateFormat+"\",\n" +
            "    \"actualdurationminutes\":\""+duration+"\",\n" +
            "   \"regardingobjectid_contact@odata.bind\": \"/contacts("+userContactid+")\", //Regarding is a contact\n" +
            "    \"phonecall_activity_parties\": [\n" +
            "       {\n" +
            "            \"partyid_contact@odata.bind\": \"/contacts("+clientContactid+")\", // call started by a sustemuser\n" +
            "            \"participationtypemask\" : 1 // From\n" +
            "        },\n" +
            "        {\n" +
            "            \"partyid_contact@odata.bind\": \"/contacts("+userContactid+")\", // call to by a contact\n" +
            "            \"participationtypemask\": 2 // To\n" +
            "        }\n" +
            "    ]}");

}
/*body =RequestBody.create(mediaType,"{    \"subject\": \" "+desc+"\",\n" +
        "    \"phonenumber\": \" "+phNumber+"\",\n" +
        "    \"description\": \"My description\",\n" +
        "    \"directioncode\": "+direction+", //Direction : 0-->False/Incomming, 1-->True/Outgoing,\n" +
        "    \"scheduledstart\":\""+dateFormat+"\",\n" +
        "    \"actualdurationminutes\":\"50\",\n" +
        "   \"regardingobjectid_contact@odata.bind\": \"/contacts(cdcfa450-cb0c-ea11-a813-000d3a1b1223)\", //Regarding is a contact\n" +
        "    \"phonecall_activity_parties\": [\n" +
        "       {\n" +
        "            \"partyid_contact@odata.bind\": \"/contacts(cdcfa450-cb0c-ea11-a813-000d3a1b1223)\", // call started by a sustemuser\n" +
        "            \"participationtypemask\" : 1 // From\n" +
        "        },\n" +
        "        {\n" +
        "            \"partyid_contact@odata.bind\": \"/contacts(9fd4a450-cb0c-ea11-a813-000d3a1b1223)\", // call to by a contact\n" +
        "            \"participationtypemask\": 2 // To\n" +
        "        }\n" +
        "    ]\n" +
        "    }");*/
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
                    logsStatus.setTextColor(0xFFFF0000);
                    logsStatus.setText("unsuccessfull!");
                 //   Toast.makeText(SaveLogsPage.this,"successfullt saved",Toast.LENGTH_LONG).show();
                    throw new IOException("Unexpected code " + response);
                }else{
                    //  Toast.makeText(getActivity(),"sucess",Toast.LENGTH_LONG).show();
                    dataBaseHelper2.modifySaved(cl2);
                    Log.e("success",response.toString());

                   // Toast.makeText(SaveLogsPage.this,"successfullt saved",Toast.LENGTH_LONG).show();
                    Log.d("create:","success");
                }

                // you code to handle response
            }

        });
    }
















    public void fetchLogs(){


        ContentResolver cr = SaveLogsPage.this.getContentResolver();
        Cursor c = cr.query(CallLog.Calls.CONTENT_URI, null, null, null, null);

        int totalCall = 10;

        if (c != null) {
            totalCall = 1; // intenger call log limit

            if (c.moveToLast()) { //starts pulling logs from last - you can use moveToFirst() for first logs
                for (int j = 0; j < totalCall; j++) {
                    Boolean directionBoolean = true;
                    phNumber = c.getString(c.getColumnIndexOrThrow(CallLog.Calls.NUMBER));
                    callDate = c.getString(c.getColumnIndexOrThrow(CallLog.Calls.DATE));
                    callDuration = c.getString(c.getColumnIndexOrThrow(CallLog.Calls.DURATION));





                    callDuration = String.valueOf((Integer.parseInt(String.valueOf(Integer.parseInt(callDuration)/60))));

                    dateFormat= new Date(Long.valueOf(callDate));
                    callDayTimes = String.valueOf(dateFormat);
                    //DateTimeFormatter dt = new DateTimeFormatterBuilder(dateFormat);
                    //Log.d('DATETIME:',dt.formatGmt('yyyy-MM-dd\'T\'HH:mm:ss.SSS\'Z\''));

                    SimpleDateFormat formatter = new SimpleDateFormat(
                            "MM/dd/yyyy HH:mm:ss");
                    String dateString = formatter.format(new Date(Long
                            .parseLong(callDate)));
                    String stringType;
                    try{
                    stringType = c.getString(c.getColumnIndexOrThrow(CallLog.Calls.TYPE));
                        Toast.makeText(SaveLogsPage.this,stringType,Toast.LENGTH_LONG).show();
                        switch (stringType) {
                            case "2":
                                direction = "OUTGOING";
                                directionBoolean = true;
                                Log.d(String.valueOf(SaveLogsPage.this),"durection boolean = true ->"+directionBoolean);
                                Log.d(String.valueOf(SaveLogsPage.this),"durection value = outgoing ->"+direction);
                                System.out.println("durection boolean = true ->"+directionBoolean);
                                break;
                            case "1":
                                direction = "INCOMING";
                                System.out.println("durection boolean = false ->"+directionBoolean);
                                Log.d(String.valueOf(SaveLogsPage.this),"durection boolean = false ->"+directionBoolean);
                                Log.d(String.valueOf(SaveLogsPage.this),"durection value = INCOMING ->"+direction);
                                break;

                            case "3":
                                direction = "MISSED";
                                directionBoolean = false;
                                Log.d(String.valueOf(SaveLogsPage.this),"durection value = INCOMING ->"+direction);
                                Log.d(String.valueOf(SaveLogsPage.this),"durection boolean = false ->"+directionBoolean);
                                break;

                            default:
                                direction = "DEFAULT";
                                directionBoolean = false;
                                Log.d(String.valueOf(SaveLogsPage.this),"durection value = INCOMING DEFAULT ->"+direction);
                                Log.d(String.valueOf(SaveLogsPage.this),"durection boolean = false DEFAULT->"+directionBoolean);
                                break;
                        }
                    }catch(Exception e){
                        Log.d("direction",e.toString());

                    }
                    @SuppressLint("Range") int dircode = Integer.parseInt(c.getString(c.getColumnIndex(CallLog.Calls.TYPE)));


                   /* switch (Integer.parseInt(c.getString(c.getColumnIndexOrThrow(CallLog.Calls.TYPE)))) {
                        case CallLog.Calls.OUTGOING_TYPE:
                            direction = "OUTGOING";
                            directionBoolean = true;
                            Log.d(String.valueOf(SaveLogsPage.this),"durection boolean = true ->"+directionBoolean);
                            System.out.println("durection boolean = true ->"+directionBoolean);
                            break;
                        case CallLog.Calls.INCOMING_TYPE:
                            direction = "INCOMING";
                            directionBoolean = false;
                            System.out.println("durection boolean = false ->"+directionBoolean);
                            Log.d(String.valueOf(SaveLogsPage.this),"durection boolean = false ->"+directionBoolean);
                            break;
                        case CallLog.Calls.MISSED_TYPE:
                            direction = "MISSED";
                            directionBoolean = false;

                            Log.d(String.valueOf(SaveLogsPage.this),"durection boolean = false ->"+directionBoolean);
                            break;
                        default:
                            break;
                    }*/
                     cl2 = dataBaseHelper2.fetchByDate(dateString);
                    dataBaseHelper2.modifySaved(cl2);

                    updateUI(callDuration,phNumber,dateString,directionBoolean);
                }
            }
            c.close();
        }
    }

    private void updateUI(String callDuration, String phNumber, String dateFormat, Boolean direction) {

//by default its outgoing
        durationTxtView.setText(callDuration);
        desc = ETdescription.getText().toString().trim();
        int durationInt = (int)Double.parseDouble(callDuration);
        if(!CallLogsAdapter.openedfromfrag){
        saveLogs(desc,durationInt,phNumber,dateFormat,direction);

        }

    }

public String fetchContactid(String email){
        //will return contactid of the person with the following email
Log.d("fetchContactid",dataBaseHelper.getContactId("clarita.hawat@javista.com"));
     return dataBaseHelper.getContactId(email);

    // return "f9c25f54-59db-ec11-bb3d-000d3a66d2a8";
}

    public String fetchContactidByphone(String phone){
        //will return contactid of the person with the following email
       // Log.d("fetchContactid",dataBaseHelper.getContactId("clarita.hawat@javista.com"));
        return dataBaseHelper.getContactIdByPhone(phone);

        // return "f9c25f54-59db-ec11-bb3d-000d3a66d2a8";
    }
    private AuthenticationCallback getAuthInteractiveCallback() {
        return new AuthenticationCallback() {
            @Override
            public void onSuccess(IAuthenticationResult authenticationResult) {
                /* Successfully got a token, use it to call a protected resource - MSGraph */
                Log.d("TAG", "Successfully authenticated");

            }

            @Override
            public void onError(MsalException exception) {
                /* Failed to acquireToken */
                Log.d("TAG", "Authentication failed: " + exception.toString());

            }
        @Override
            public void onCancel() {
                /* User canceled the authentication */
                Log.d("TAG", "User cancelled login.");
            }
        };
    }
}
