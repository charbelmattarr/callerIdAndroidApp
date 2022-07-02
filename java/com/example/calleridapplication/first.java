package com.example.calleridapplication;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

//import com.example.azurefirstapp.databinding.ActivityMainBinding;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.CallerIdApplication.R;
import com.example.CallerIdApplication.R.color;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.microsoft.graph.authentication.IAuthenticationProvider;
import com.microsoft.graph.concurrency.ICallback;
import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.http.IHttpRequest;
import com.microsoft.graph.models.extensions.Drive;
import com.microsoft.graph.models.extensions.IGraphServiceClient;
import com.microsoft.graph.requests.extensions.GraphServiceClient;
import com.microsoft.graph.requests.extensions.IContactCollectionPage;
import com.microsoft.identity.client.*;
import com.microsoft.identity.client.exception.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class first extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    String URL_GETALLCONTACTS = "https://getallcontacts20220530100450.azurewebsites.net/api/Function1?code=vQ39xN3XM4syuBk6ACNCDkUwpwAsS-EiiQi3Trc4028RAzFuOQbYKQ==";
    public static boolean isSignedIn = false;
    NavigationView navigationView;
    public static View mHeaderView;
    TextView signinStatus;
    TextView titles;
    Toolbar toolbar;
    private static final String TAG = callReciever.class.getSimpleName();
     DrawerLayout drawerLayout ;
     ImageView dehaze;
    public static ISingleAccountPublicClientApplication mSingleAccountApp;
    DataBaseHelper dataBaseHelper;
    DataBaseHelper3 dataBaseHelper3;
    ProgressBar progressBar;
    ImageView refresh;
    public static boolean firsttoCreate=false;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.browsertab_activity);
        dataBaseHelper = new DataBaseHelper(first.this);
        dataBaseHelper3 = new DataBaseHelper3(first.this);
        initializeUI();
        checkOverlayPermission();
       signinStatus =findViewById(R.id.signinstatus);
       progressBar = (ProgressBar)findViewById(R.id.progressBarDrawer);
       refresh = findViewById(R.id.refreshing);
       titles = findViewById(R.id.titles);
       titles.setText("");
       refresh.setVisibility(View.GONE);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CALL_LOG},1);
        }

        this.stopService(new Intent(this, ForegroundService.class));
         startService2();
        if (getApplicationContext().checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission has not been granted, therefore prompt the user to grant permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    1);
        }

        if (getApplicationContext().checkSelfPermission(Manifest.permission.PROCESS_OUTGOING_CALLS)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission has not been granted, therefore prompt the user to grant permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.PROCESS_OUTGOING_CALLS},
                    1);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},1);
        }
        // startService();


        if(CallLogsAdapter.openCreate){
            Bundle bundle=first.this.getIntent().getExtras();
            CallLogsAdapter.openCreate=false;
           String ids = bundle.getString("id").trim();
            Log.d("idinfirst",ids);

            openCreateContactsFragment2(ids);
            return;
        }
       if(relatedCallLogs.openCallLogs) {
             openCallLogsFragment();
               return;
              }

              if(ContactsAdapter.openContactFrag){

                openContactFragment();
              ContactsAdapter.openContactFrag=false;
      //  if(callReciever.openCreate){
      //      Log.d("using","openCreate");

     //         openCreateContactsFragment();
                      return;
              }
       // openCallLogsFragment();
        //  openSignInFragment();
        if(com.example.calleridapplication.Window.found){
            Log.d("using","window.found");
            Window.found=false;
            openCreateContactsFragment();
                 return;
        }

           if(callReciever.openedOnNotFound){
               Log.d("using","openedOnNotFound");
             firsttoCreate=true;
             openCreateContactsFragment();
          return;
   // callReciever.openedOnNotFound=false;

                 }
openCallLogsFragment();
/*
        PublicClientApplication.createSingleAccountPublicClientApplication(first.this, R.raw.auth_config_single_account,new IPublicClientApplication.ISingleAccountApplicationCreatedListener(){
            @Override
            public void onCreated(ISingleAccountPublicClientApplication application){

                if(first.this == null) Log.e("EMT","EMT");
                signinStatus.setVisibility(View.VISIBLE);
                mSingleAccountApp = application;
                signinStatus.setVisibility(View.GONE);
                openSignInFragment();
            }
            @Override
            public void onError(MsalException exception){
                signinStatus.setVisibility(View.VISIBLE);
                Toast.makeText(first.this,exception.toString(),Toast.LENGTH_LONG).show();

            }
        });
*/
        if(!dataBaseHelper3.getUser().getName().equals("")){
            TextView userName = first.mHeaderView.findViewById(R.id.userName);
            TextView userEmail = first.mHeaderView.findViewById(R.id.userEmail);
            userName.setText(dataBaseHelper3.getUser().getName());
            userEmail.setText(dataBaseHelper3.getUser().getEmail());
            Menu menu = navigationView.getMenu();
            menu.findItem(R.id.nav_signin).setTitle("Sign Out");
        }else {
            Menu menu = navigationView.getMenu();
            menu.findItem(R.id.nav_signin).setTitle("Sign In");
        }


        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(first.this,"refreshing...",Toast.LENGTH_SHORT).show();
                showProgressBar();
                openCallLogsFragment();
            }
        });

    }



    private void initializeUI(){
       drawerLayout = findViewById(R.id.drawerLayout);
       dehaze = findViewById(R.id.ImageMenu);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                (R.string.open_drawer), (R.string.Close_navigation_drawer));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.NavigationView);
        mHeaderView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);




      navigationView.setItemIconTintList(null);
/**
        NavController navController = Navigation.findNavController(this,R.id.navHostFragment);
        NavigationUI.setupWithNavController(navigationView,navController);
       navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
           @Override
           public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
               Toast.makeText(getBaseContext(),navDestination.getLabel(),Toast.LENGTH_LONG).show();
           }
       });**/
         }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
     switch(item.getItemId()){
      //   case R.id.mainFragment:
      ///       openMainFragment();
       //      break;

         case R.id.nav_signin:
             item.setChecked(true);
             if(signin_fragment.is_signedin){
                 item.setTitle("Sign out");
             }
             openSignInFragment();

             break;
     //    case R.id.log_calls:
     //        fetchLogs();
     //        break;
      case R.id.nav_contacts:
          item.setChecked(true);
          showProgressBar();
          fetchAllContacts();
            openContactFragment();
           break;

         case R.id.nav_updateDB:
             item.setChecked(true);
             titles.setText("Updating...");
             showProgressBar();
             fetchAllContacts();
             openContactFragment();
             break;

         case R.id.phonecalls:
             item.setChecked(true);

             openCallLogsFragment();
             break;
            }
       drawerLayout.closeDrawer(GravityCompat.START);

        return true;
     }


    private void openCallLogsFragment() {
        //toolbar.setTitle("Phone Calls");
       titles.setText("Phone calls");
        refresh.setVisibility(View.VISIBLE);
        navigationView.setCheckedItem(R.id.opensavelogs);

        CallLogsFrag frag = CallLogsFrag.newInstance();
        first.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideProgressBar();
            }
        });

     //   navigationView.setItemTextColor(ColorStateList.valueOf(first.this.getResources().getColor(R.color.blue)));
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,frag).commit();


    }


    private  void openCreateContactsFragment() {
        titles.setText("Create Contact");
        refresh.setVisibility(View.GONE);
        createContact frag = createContact.newInstance();

    //    navigationView.setItemTextColor(ColorStateList.valueOf(first.this.getResources().getColor(R.color.blue)));
     //   navigationView.setItemIconTintList(ColorStateList.valueOf(first.this.getResources().getColor(R.color.blue)));
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,frag).commit();

    }
    private  void openCreateContactsFragment2(String id) {
        titles.setText("Create Contact");
        refresh.setVisibility(View.GONE);
        createContact2 frag = createContact2.newInstance(id);

      //  navigationView.setItemTextColor(ColorStateList.valueOf(first.this.getResources().getColor(R.color.blue)));
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,frag).commit();

    }
    private void openContactFragment() {
        titles.setText("Contacts");
        refresh.setVisibility(View.GONE);
        navigationView.setCheckedItem(R.id.nav_contacts);
        Contacts frag = Contacts.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,frag).commit();
    //    navigationView.setItemTextColor(ColorStateList.valueOf(first.this.getResources().getColor(R.color.blue)));

    }

    private void openBottomFragment() {


      /*  Intent i = new Intent(first.this,Callerinfoactivity.class);
        this.startActivity(i);*/

        ///bottomSheetFrag frag = bottomSheetFrag.newInstance();
      ////  getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,frag).commit();
       // navigationView.setCheckedItem(R.id.nav_bottom);
    }

    private void openSignInFragment() {
        titles.setText("");
        refresh.setVisibility(View.GONE);
        navigationView.setCheckedItem(R.id.nav_signin);
        signin_fragment frag = signin_fragment.newInstance();
 //       navigationView.setItemTextColor(ColorStateList.valueOf(first.this.getResources().getColor(R.color.blue)));
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,frag).commit();

    }

    private void openMainFragment() {
        MainFragment frag = MainFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,frag).commit();
   //     navigationView.setCheckedItem(R.id.mainFragment);

    }

    public void checkOverlayPermission(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                // send user to the device settings
                Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                startActivity(myIntent);
            }
        }
    }
    public void startService(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // check if the user has already granted
            // the Draw over other apps permission
           if(Settings.canDrawOverlays(this)) {
                // start the service based on the android version
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Toast.makeText(first.this,"build version akbar mn .O",Toast.LENGTH_LONG).show();
                    startForegroundService(new Intent(this, ForegroundService.class));
                } else {
                    Toast.makeText(first.this,"build version azghar mn .O",Toast.LENGTH_LONG).show();
                    startService(new Intent(first.this, ForegroundService.class));
                }
            }
        }else{
            Toast.makeText(first.this,"build version azghar mn .M",Toast.LENGTH_LONG).show();
            startService(new Intent(first.this, ForegroundService.class));
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
    //    startService();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void fetchLogs(){


        ContentResolver cr = getBaseContext().getContentResolver();
        Cursor c = cr.query(CallLog.Calls.CONTENT_URI, null, null, null, null);

        int totalCall = 10;

        if (c != null) {
            totalCall = 1; // intenger call log limit

            if (c.moveToLast()) { //starts pulling logs from last - you can use moveToFirst() for first logs
                for (int j = 0; j < totalCall; j++) {


                    String phNumber = c.getString(c.getColumnIndexOrThrow(CallLog.Calls.NUMBER));
                    String callDate = c.getString(c.getColumnIndexOrThrow(CallLog.Calls.DATE));
                    String callDuration = c.getString(c.getColumnIndexOrThrow(CallLog.Calls.DURATION));
                    Date dateFormat= new Date(Long.valueOf(callDate));
                    String callDayTimes = String.valueOf(dateFormat);
                    SimpleDateFormat dt = new SimpleDateFormat("mm/dd/yyyy hh:mm:ss");


                    SimpleDateFormat formatter = new SimpleDateFormat(
                            "MM/dd/yyyy HH:mm:ss");
                    String dateString = formatter.format(new Date(Long
                            .parseLong(callDate)));
                    String date=null;

                      // date = String.valueOf(dt.format(dateString));

                    //String date=null;
                   // String oldstring = "2011-01-18 00:00:00.0";
                   // LocalDateTime datetime = LocalDateTime.parse(callDayTimes, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"));
                      //  date = dt.parse(callDate);
                  //  String newstring = datetime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                 //   System.out.println("new date string isss:"+newstring); // 2011
                    //  date =   dt.format(callDate);

                   // String dateString = String.valueOf(date);
                    String direction = null;
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

                    //  c.moveToPrevious(); // if you used moveToFirst() for first logs, you should this line to moveToNext

                    //  //Toast.makeText(getBaseContext(), phNumber + callDuration + callDate + direction, Toast.LENGTH_SHORT).show(); // you can use strings in this line
                    Toast.makeText(getBaseContext(),  dateString , Toast.LENGTH_SHORT).show(); // you can use strings in this line
                }
            }
            c.close();
        }
    }

    private void fetchAllContacts() {






       OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(50, TimeUnit.SECONDS)
                .writeTimeout(50, TimeUnit.SECONDS)
                .readTimeout(50, TimeUnit.SECONDS)
                .build();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "");
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("https://calleridcrmapi.azure-api.net/contacts")
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
               // .get()
                //.addHeader("Cookie", "ReqClientId=30c78179-6c3a-4708-8376-907a89493c54; last_commit_time=2022-05-31 12:54:18Z; orgId=8b7545a7-1d2b-48d7-be9d-832648fff0e3")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("okhttp1",e.toString());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                if (!response.isSuccessful()) {

                    Log.e("okhttp2",response.body().string());
                    Toast.makeText(first.this,"not successful",Toast.LENGTH_LONG).show();

                    throw new IOException("Unexpected code " + response);
                }else {
                    String responseBody = response.body().string();
                    Log.d("strr->>0",responseBody);
                    parseJSON(responseBody);

                }
            }


        });
    }

/*
*
* OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, "{" +
                        "\r\n\"firstname\": \""+firstname.toUpperCase().trim().charAt(0)+""+firstname.substring(1).toLowerCase().trim()+"\"\r\n," +
                        "\r\n \"lastname\": \""+lastname.toUpperCase().trim()+"\"\r\n," +
                        "\r\n    \"cr051_companyname\": \""+company.trim()+"\"\r\n," +
                        "\r\n    \"emailaddress1\": \""+email.trim()+"\"\r\n," +
                        "\r\n    \"jobtitle\": \""+job.trim()+"\"\r\n," +
                        "\r\n    \"mobilephone\": \""+mobilephone.trim()+"\"\r\n}");

                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url("https://calleridcrmapi.azure-api.net/contacts")
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

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {


                                    ETfirstname.setText("");
                                    ETlastname.setText("");
                                    ETcompany.setText("");
                                    ETjob.setText("");
                                    ETemail.setText("");
                                    ETphonenumber.setText("");
                                    createStatus.setTextColor(Integer.parseInt("#eb4b4b"));
                                    createStatus.setText("unsuccessfull!");
                                    btnCreateContact.setEnabled(false);
                                }
                            });

                            throw new IOException("Unexpected code " + response);
                        }else{
                            //  Toast.makeText(getActivity(),"sucess",Toast.LENGTH_LONG).show();

                            Log.e("okhttp2",response.toString());
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    hideProgressBar();
                                    ETfirstname.setText("");
                                    ETlastname.setText("");
                                    ETcompany.setText("");
                                    ETjob.setText("");
                                    ETemail.setText("");
                                    ETphonenumber.setText("");
                                    btnCreateContact.setText("saved!");
                                    btnCreateContact.setEnabled(true);

                                    createStatus.setText("added successfully!");
                                    cancel.setText("back");
                                    addthisContactToDB(mobilephone);

                                }
                            });

                            Log.d("create:","success");
                        }

                        // you code to handle response
                    }

                });
            }
        });
*
* */
/*
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL_GETALLCONTACTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("strrrrr",">>"+response);
                        parseJSON(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        hideProgressBar();
                        Toast.makeText(first.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("errroor",">>"+error.toString());
                    }
                });


        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);
        }
*/


    private void parseJSON(String response) {

        String firstname,lastname,contactid,jobTitle,company,email,mobilephone;
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            int size = 100;
            //   if(jsonObject.getString("status").equals("true")){
            JSONArray callerid = jsonObject.getJSONArray("value");
            for (int i = 0; i < callerid.length(); i++) {
                Log.d("i->>", String.valueOf(i));
            //      for (int i = 0; i < size; i++) {
                //    String name,JobTitle,Company,etag,contactid;
                JSONObject dataobj = callerid.getJSONObject(i);
                firstname =dataobj.getString("firstname");
                lastname = dataobj.getString("lastname");
                jobTitle=dataobj.getString("jobtitle");
                company = dataobj.getString("cr051_companyname");
                contactid = dataobj.getString("contactid");
                //    etag = dataobj.getString("@odata.etag");
                email = dataobj.getString("emailaddress1");
                mobilephone = dataobj.getString("mobilephone");

                ContactModel c11 = new ContactModel(contactid,lastname,firstname,company,jobTitle,email,mobilephone);
                Log.d("contact model:",c11.toString());
                dataBaseHelper.addOne(c11);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            hideProgressBar();
        }
        Log.d("stopppped...","updating");
        hideProgressBar();
    }
    public void startService2(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // check if the user has already granted$^$
            // the Draw over other apps permission
            if(Settings.canDrawOverlays(this)) {
                // start the service based on the android version
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Toast.makeText(first.this,"build version akbar mn .O",Toast.LENGTH_LONG).show();
                    startForegroundService(new Intent(first.this, ForegroundService2.class));
                } else {
                    Toast.makeText(first.this,"build version azghar mn .O",Toast.LENGTH_LONG).show();
                    startService(new Intent(first.this, ForegroundService2.class));
                }
            }
        }else{
            Toast.makeText(first.this,"build version azghar mn .M",Toast.LENGTH_LONG).show();
            startService(new Intent(first.this, ForegroundService2.class));
        }
    }

    private void showProgressBar() {
        first.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                first.this.findViewById(R.id.progressBarDrawer)
                        .setVisibility(View.VISIBLE);
                first.this.findViewById(R.id.fragment_container)
                        .setEnabled(false);
                first.this.findViewById(R.id.fragment_container)
                        .setVisibility(View.INVISIBLE);
            }
        });


    }

    private void hideProgressBar() {
        first.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                first.this.findViewById(R.id.progressBarDrawer)
                        .setVisibility(View.GONE);
                first.this.findViewById(R.id.fragment_container)
                        .setEnabled(true);
                first.this.findViewById(R.id.fragment_container)
                        .setVisibility(View.VISIBLE);
            }
        });


    }
}



