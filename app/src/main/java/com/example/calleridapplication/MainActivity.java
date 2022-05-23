package com.example.calleridapplication;
import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

//import com.example.azurefirstapp.databinding.ActivityMainBinding;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.CallerIdApplication.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.microsoft.graph.authentication.IAuthenticationProvider;
import com.microsoft.graph.concurrency.ICallback;
import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.http.IHttpRequest;
import com.microsoft.graph.models.extensions.Drive;
import com.microsoft.graph.models.extensions.IGraphServiceClient;
import com.microsoft.graph.requests.extensions.GraphServiceClient;
import com.microsoft.identity.client.*;
import com.microsoft.identity.client.exception.*;
import com.microsoft.identity.common.internal.net.HttpClient;

import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    LocalStorage storage;
    private final static String[] SCOPES = {"Files.Read","Financials.ReadWrite.All"};

  //  final static String AUTHORITY = "https://login.microsoftonline.com/common";
    final static String AUTHORITY = "https://login.windows.net/common/oauth2/authorize?resource=https://api.businesscentral.dynamics.com";
    public  static String token;
    public static ISingleAccountPublicClientApplication mSingleAccountApp;
    private static final String TAG=MainActivity.class.getSimpleName();
    Button signInButton;
    Button signOutButton;
    Button callGraphApiInteractiveButton;
    Button callGraphApiSilentButton;
    TextView logTextView;
    TextView currentUserTextView;
    TextView hello;
    Dialog dialog;
    JsonObject DRIVEJSON;
    public static String displayName=null;
    Boolean getgraph=false;
    int signed=0;
    boolean newSign=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeUI();


        PublicClientApplication.createSingleAccountPublicClientApplication(getApplicationContext(),R.raw.auth_config_single_account,new IPublicClientApplication.ISingleAccountApplicationCreatedListener(){
            @Override
            public void onCreated(ISingleAccountPublicClientApplication application){
                mSingleAccountApp = application;
                loadAccount();
            }
            @Override
            public void onError(MsalException exception){
                displayError(exception);
            }
        });
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE} ,1);
        }
    }
    private void loadAccount(){
        if(mSingleAccountApp == null){
            return;
        }
        mSingleAccountApp.getCurrentAccountAsync(new ISingleAccountPublicClientApplication.CurrentAccountCallback(){
            @Override
            public void onAccountLoaded(@Nullable IAccount activeAccount){
                updateUI(activeAccount);
                newSign=true;
              //  openBrowserTabActivity();

            }

            @Override
            public void onAccountChanged(@Nullable IAccount priorAccount,@Nullable IAccount currentAccount){
                if(currentAccount == null){
                    performOperationOnSignOut();
              //      openBrowserTabActivity();
                }
            }
            @Override
            public void onError(@NonNull MsalException exception){
                displayError(exception);
            }
        });
    }




    private void initializeUI(){
        signInButton = findViewById(R.id.signIn);
        callGraphApiSilentButton = findViewById(R.id.callGraphSilent);
        callGraphApiInteractiveButton = findViewById(R.id.callGraphInteractive);
        signOutButton = findViewById(R.id.clearCache);
        logTextView = findViewById(R.id.txt_log);
        currentUserTextView = findViewById(R.id.current_user);

        //Sign in user
        signInButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if (mSingleAccountApp == null) {
                    return;
                }
                mSingleAccountApp.signIn(MainActivity.this, null, SCOPES, getAuthInteractiveCallback());
                newSign=true;

            }
        });

        //Sign out user
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSingleAccountApp == null){
                    return;
                }
                mSingleAccountApp.signOut(new ISingleAccountPublicClientApplication.SignOutCallback() {
                    @Override
                    public void onSignOut() {
                        updateUI(null);
                        performOperationOnSignOut();
                        newSign=false;
                    }
                    @Override
                    public void onError(@NonNull MsalException exception){
                        displayError(exception);
                    }
                });
            }
        });

        //Interactive
        callGraphApiInteractiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSingleAccountApp == null) {
                    return;
                }
                mSingleAccountApp.acquireToken(MainActivity.this, SCOPES, getAuthInteractiveCallback());
            }
        });

        //Silent
        callGraphApiSilentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSingleAccountApp == null){
                    return;
                }
                mSingleAccountApp.acquireTokenSilentAsync(SCOPES, AUTHORITY, getAuthSilentCallback());
            }
        });
    }


    private AuthenticationCallback getAuthInteractiveCallback() {
        return new AuthenticationCallback() {
            @Override
            public void onSuccess(IAuthenticationResult authenticationResult) {
                /* Successfully got a token, use it to call a protected resource - MSGraph */
                Log.d(TAG, "Successfully authenticated");

                /* Update UI */
                updateUI(authenticationResult.getAccount());
                /* call graph */
                callGraphAPI(authenticationResult);
            }

            @Override
            public void onError(MsalException exception) {
                /* Failed to acquireToken */
                Log.d(TAG, "Authentication failed: " + exception.toString());
                displayError(exception);
            }
            @Override
            public void onCancel() {
                /* User canceled the authentication */
                Log.d(TAG, "User cancelled login.");
            }
        };
    }
    private SilentAuthenticationCallback getAuthSilentCallback() {
        return new SilentAuthenticationCallback() {
            @Override
            public void onSuccess(IAuthenticationResult authenticationResult) {
                Log.d(TAG, "Successfully authenticated");
                callGraphAPI(authenticationResult);
            }
            @Override
            public void onError(MsalException exception) {
                Log.d(TAG, "Authentication failed: " + exception.toString());
                displayError(exception);
            }
        };
    }
    private void callGraphAPI(IAuthenticationResult authenticationResult) {

        final String accessToken = authenticationResult.getAccessToken();
       token=authenticationResult.getAccessToken();
      //  storage.SaveAuthenticationState(authenticationResult.getAccessToken());

        IGraphServiceClient graphClient =
                GraphServiceClient
                        .builder()
                        .authenticationProvider(new IAuthenticationProvider() {
                            @Override
                            public void authenticateRequest(IHttpRequest request) {
                                Log.d(TAG, "Authenticating request," + request.getRequestUrl());
                                request.addHeader("Authorization", "Bearer " + accessToken);
                                Log.d("token is", accessToken);
                            }
                        })
                        .buildClient();
        graphClient
                .me()
                .drive()
                .buildRequest()
                .get(new ICallback<Drive>() {
                    @Override
                    public void success(final Drive drive) {
                        Log.d(TAG, "Found Drive " + drive.id);
                        displayGraphResult(drive.getRawObject());
                        parsingJson(drive.getRawObject());



                       openBrowserTabActivity();
                        first.isSignedIn=true;
                    }

                    @Override
                    public void failure(ClientException ex) {
                        displayError(ex);
                    }
                });
    }private void updateUI(@Nullable final IAccount account) {
        if (account != null) {
            signInButton.setEnabled(false);
            signOutButton.setEnabled(true);
            callGraphApiInteractiveButton.setEnabled(true);
            callGraphApiSilentButton.setEnabled(true);
            currentUserTextView.setText(account.getUsername());
        } else {
            signInButton.setEnabled(true);
            signOutButton.setEnabled(false);
            callGraphApiInteractiveButton.setEnabled(false);
            callGraphApiSilentButton.setEnabled(false);
            currentUserTextView.setText("");
            logTextView.setText("");
        }
    }
    private void displayError(@NonNull final Exception exception) {
        logTextView.setText(exception.toString());
    }
    private  void displayGraphResult(@NonNull final JsonObject graphResponse) {
        logTextView.setText(graphResponse.toString());
        // String displayName = graphResponse.get("displayName").toString();
        // logTextView.setText("hello"+ displayName);

    }
    private  void parsingJson(@NonNull final JsonObject graphResponse){
        if(!graphResponse.equals(null)){
            JsonObject owner=graphResponse.getAsJsonObject("owner");
            JsonObject user = owner.getAsJsonObject("user");
            displayName = user.get("displayName").toString();
         //   Constants.USERNAME=displayName;
//            hello.setText(" salut!");
            // Intent intent = new Intent(MainActivity.this,BrowserTabActivity.class);
            // intent.putEx1tra("displayName",displayName);
            //  this.startActivity(intent);

        }else{
            Toast.makeText(MainActivity.this,"json empty",Toast.LENGTH_LONG).show();
        }


    }
    private void performOperationOnSignOut() {
        final String signOutText = "Signed Out.";
        currentUserTextView.setText("");

        logTextView.setText("");
        signed=0;
        getgraph=false;
        Toast.makeText(getApplicationContext(), signOutText, Toast.LENGTH_SHORT)
                .show();
    }

    private void callCRM(IAuthenticationResult authenticationResult) {

        final String accessToken = authenticationResult.getAccessToken();
        token=authenticationResult.getAccessToken();
        //  storage.SaveAuthenticationState(authenticationResult.getAccessToken());

        IGraphServiceClient graphClient =
                GraphServiceClient
                        .builder()
                        .authenticationProvider(new IAuthenticationProvider() {
                            @Override
                            public void authenticateRequest(IHttpRequest request) {
                                Log.d(TAG, "Authenticating request," + request.getRequestUrl());
                                request.addHeader("Authorization", "Bearer " + accessToken);
                                Log.d("token is", accessToken);
                            }
                        })
                        .buildClient();
        graphClient
                .me()
                .drive()
                .buildRequest()
                .get(new ICallback<Drive>() {
                    @Override
                    public void success(final Drive drive) {
                        Log.d(TAG, "Found Drive " + drive.id);
                        displayGraphResult(drive.getRawObject());
                        parsingJson(drive.getRawObject());



                        openBrowserTabActivity();
                        first.isSignedIn=true;
                    }

                    @Override
                    public void failure(ClientException ex) {
                        displayError(ex);
                    }
                });
    }


    public  void openBrowserTabActivity(){
        if(newSign){
            Intent intent = new Intent(MainActivity.this, com.example.calleridapplication.first.class);

          //  intent.putExtra("displayName",displayName);
            newSign=false;
            this.startActivity(intent);

        }
    }

}