package com.example.calleridapplication;
import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;

//import com.example.azurefirstapp.databinding.ActivityMainBinding;

import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.CallerIdApplication.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
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

import org.json.JSONObject;

public class first extends AppCompatActivity {

        public String displayName;
        TextView welcome;
        TextView logs;
        Button btnSearch;
    Dialog dialog;
        public static boolean isSignedIn = false;
    private String[] SCOPES = { "User.Read","Contacts.Read","Contacts.ReadWrite" };
        String AUTHORITY = "https://login.microsoftonline.com/v1.0/me";
        // private AuthenticationHelper mAuthHelper = null;
    //    private ISingleAccountPublicClientApplication mSingleAccountApp;
        // and get whatever type user account id is




    private static final String TAG = callReciever.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.browsertab_activity);

        initializeUI();
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED  ){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},1);
        }
     if( ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS)
        != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(this,
            new String[]{Manifest.permission.READ_PHONE_NUMBERS},1);
    }


         if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
              != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,
            new String[]{Manifest.permission.ACCESS_NETWORK_STATE},1);
}

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CALL_LOG},1);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},1);
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS)
                != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CALL_LOG},1);
        }

    }



    private void initializeUI(){
        Bundle extras = getIntent().getExtras();
        if(isSignedIn){
         if(MainActivity.displayName != null || !Constants.USERNAME.equals("username")){
        displayName =MainActivity.displayName;
        welcome = (TextView)findViewById(R.id.welcome);
        logs = (TextView)findViewById(R.id.logs);
      //  btnSearch = (Button)findViewById(R.id.searchNumber);
        String firstName = displayName.replaceAll("\"","");
        String[] name= firstName.split(" ");
        welcome.setText("welcome " + name[0]);
      //  btnSearch.setVisibility(View.VISIBLE);
     /*   btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 if (MainActivity.mSingleAccountApp == null){
                     Log.d(TAG,"single account null");
                    return;
                   }
                //getAuthSilentCallback();
               MainActivity.mSingleAccountApp.acquireTokenSilentAsync(SCOPES,AUTHORITY, getAuthSilentCallback());

                Log.d("button clicked","button clicked bs nothing is going to happen");
            }
        });*/

         }
        }

        }



public void gotoLogin(View view){
        Intent i = new Intent(first.this,MainActivity.class);
        this.startActivity(i);
}






















/*
*
*
* graphHelper
        .getCalendarView(startOfWeek, endOfWeek, mTimeZone)
        .thenAccept(eventList -> {
            mEventList = eventList;

            addEventsToList();
            hideProgressBar();
        })
        .exceptionally(exception -> {
            hideProgressBar();
            Log.e("GRAPH", "Error getting events", exception);
            Snackbar.make(getView(),
                exception.getMessage(),
                BaseTransientBottomBar.LENGTH_LONG).show();
            return null;
        });

*
*
* */












//silent calls to acquireToken calls
    public SilentAuthenticationCallback getAuthSilentCallback(){
        return new SilentAuthenticationCallback() {
            @Override
            public void onSuccess(IAuthenticationResult authenticationResult) {

                        Log.d(TAG,"Successfully authenticated");
                        logs.setText(" silent auth ");
                Log.d(TAG,"success");
                Log.d(TAG,AUTHORITY);
                Log.d("new access token",authenticationResult.getAccessToken());
                Log.d(TAG,authenticationResult.getAccount().toString());
                        callGraphAPI(authenticationResult);

            }

            @Override
            public void onError(MsalException exception) {
                Log.d(TAG,"Authentication failed :" + exception.toString());
                if(exception instanceof MsalClientException){
                    /* Exception inside MSAL, more info inside MsalError.java */
              }
                else if(exception instanceof MsalServiceException){
                    /* Exception when communicating with the STS, likely config issue */
                }
               else if(exception instanceof MsalUiRequiredException){
                    /* Tokens expired or no session, retry with interactive */
              }
            }
        };




    }
public void gotoNextPage(View v){
        Intent i = new Intent(first.this,Callerinfoactivity.class);
        startActivity(i);
}

    // making an HTTP request using volley to obtain MSGraph data

    private void callGraphAPI(final IAuthenticationResult authenticationResult){

    Log.d("first2","entered callgraphAPi");

        final String accessToken = authenticationResult.getAccessToken();
         Log.d("access token:" ,accessToken);
        IGraphServiceClient graphClient =
                GraphServiceClient
                        .builder()
                        .authenticationProvider(new IAuthenticationProvider() {
                            @Override
                            public void authenticateRequest(IHttpRequest request) {
                                Log.d(TAG, "Authenticating request2," + request.getRequestUrl());
                                request.addHeader("Authorization2", "Bearer " + accessToken);
                            }
                        })
                        .buildClient();
        graphClient
                .me()
                .contacts()
                .buildRequest()
                .get(new ICallback<IContactCollectionPage>() {
                    @Override
                    public void success(IContactCollectionPage iContactCollectionPage) {
                        Log.d(TAG, "displayName2 " + iContactCollectionPage.getRawObject().toString());
//ContactsContract.Contacts.DISPLAY_NAME
                        logs.setText(iContactCollectionPage.getRawObject().toString());
                    }

                    @Override
                    public void failure(ClientException ex) {

                        Log.d(TAG,ex.toString());
                        logs.setText(ex.toString());
                    }
                });


        Log.d("first","left callgraphAPi");


      /*  final String GraphNumberResourceURL = "https://graph.microsoft.com/v1.0/me/contacts?$search='mobilePhone:+96170753661'";
        MSGraphRequestWrapper.callGraphAPIUsingVolley(getBaseContext(), GraphNumberResourceURL, authenticationResult.getAccessToken(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Log.d(TAG,"response"+response.toString());
                logs.setText(response.toString());

            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Log.d(TAG,"error"+ error.toString());

            }
        });*/

    }
    private void displayError(@NonNull final Exception exception) {
        logs.setText(exception.toString());
    }

}


