package com.example.calleridapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import com.microsoft.aad.adal.AuthenticationContext;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.CallerIdApplication.R;
import com.google.android.gms.common.api.Api;
import com.microsoft.aad.adal.PromptBehavior;
import com.microsoft.identity.client.AuthenticationCallback;
import com.microsoft.identity.client.AuthenticationResult;
import com.microsoft.identity.client.IAuthenticationResult;
import com.microsoft.identity.client.exception.MsalException;
import com.microsoft.identity.common.internal.net.HttpClient;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Callerinfoactivity extends AppCompatActivity {
TextView txt;
LocalStorage storage;
TextView txt2;
Button btn;
    final static String AUTHORITY = "https://login.microsoftonline.com/common";
    final static String NEWAUTHORITY = "https://businesscentral.dynamics.com/companies/api/data/v9.0/contacts";
   // final static String NEWAUTHORITY2 = "http://sales.crm2.dynamics.com/api/data/v9.0/contacts";
 //  final static String NEWAUTHORITY2 = "https://api.businesscentral.dynamics.com/v2.0/production/api/v2.0";
   private AuthenticationContext context;
   //final static String NEWAUTHORITY2 = "https://org95c59dd9.crm2.dynamics.com/api/data/v9.0/contacts";
   final static String NEWAUTHORITY2 = "https://calleridwebapp.azurewebsites.net";
   private AuthenticationCallback callback = new AuthenticationCallback() {
       @Override
       public void onCancel() {

       }


       @Override
       public void onSuccess(IAuthenticationResult result) {
           if (result == null || result.getAccessToken() == null || result.getAccessToken().isEmpty()) {
               Toast.makeText(Callerinfoactivity.this, "Token is Empty", Toast.LENGTH_SHORT).show();

           } else {
               storage.SaveAuthenticationState(result.getAccessToken());
               ServiceTask task = new ServiceTask(Callerinfoactivity.this, ServiceTask.GET_USER_ID, new WebServiceCallBack<String>() {
                   @Override
                   public void OnSuccess(String result) {
                      txt2.setText(result);
                   }

                   @Override
                   public void OnError(String error) {

                   }
               });
               task.execute();
           }
       }

       @Override
       public void onError(MsalException exception) {

       }
   };



    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);

       // getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
      //  getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        win.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        setContentView(com.example.CallerIdApplication.R.layout.activity_callerinfoactivity);
        txt = (TextView)findViewById(R.id.testing);
        txt2 = (TextView)findViewById(R.id.json);
        btn = (Button)findViewById(R.id.button2);

        if(!callReciever.number.isEmpty()){
            txt.setText("number calling is "+ callReciever.number);

        }else{
            showToast(Callerinfoactivity.this,"number is empty basically");
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


           // new JSONAsyncTask().execute();
                /*
                context.acquireToken(Callerinfoactivity.this,
                        Constants.SERVICE_URL,
                        Constants.CLIENT_ID,
                        Constants.REDIRECT_URL,
                        "",
                        PromptBehavior.Auto,
                        "",
                        (com.microsoft.aad.adal.AuthenticationCallback<com.microsoft.aad.adal.AuthenticationResult>) callback);
*/

            }
        });
       // Bundle bundle = this.getIntent().getExtras();
       // if(bundle.getString("number")!=null){
        //    String number = bundle.getString("number");
       //     txt.setText(number);

       //     showToast(Callerinfoactivity.this,"FINAL NUMBER IS"+number);
     //   }




     //   public void callCRM(View v){


   // }
    }





    class JSONAsyncTask extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String result){

        }



        @Override
        protected String doInBackground(String... strings) {

            //txt.setText("trying");
            Log.d("trrrying","trrryyingg");
            String response = null;
            try {

                URL url = new URL(NEWAUTHORITY2);
              /*    OkHttpClient client = new OkHttpClient();
                client.m
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                Response responses = client.newCall(request).execute();
               if( responses.isSuccessful()){

                Log.d("response successfull",responses.body().string());

               }else{
                   Log.d("response unsuccessfull",responses.message());
               }
              //  return responses.body().string();

*/






                ////////////==========first wayy=====================///////
              HttpURLConnection http = (HttpURLConnection)url.openConnection();
                http.setRequestMethod("GET");
                http.setRequestProperty("Content-type","application/json");
                http.setRequestProperty("charset","utf-8");
                http.setRequestProperty("OData-MaxVersion", "4.0");
                http.setRequestProperty("OData-Version", "4.0");
                // http.setRequestProperty("Authorization","Bearer"+storage.getAccessToken());
             //    http.setRequestProperty("Authorization","Bearer"+MainActivity.token);
                http.connect();

                Log.d(" http.getResponseCode()",String.valueOf(http.getResponseCode()));
              //  displayjson(http.getResponseMessage());
                Log.d("TAG",http.getResponseMessage());
                Log.d("TAG","connecting");
                InputStreamReader read = new InputStreamReader(http.getInputStream());
                StringBuilder sb = new StringBuilder();
                int ch = read.read();
                while (ch != -1) {
                    sb.append((char) ch);
                    ch = read.read();
                }

                response = sb.toString();
                Log.d("TAGTAGTAGTAGTAG",response + MainActivity.token);

             //   read.close();
              //  http.disconnect();
                Log.d("json"+MainActivity.token,response);
                //displayjson(response);



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }
        }




    private void displayjson(String response) {
        txt2.setText(response);
    }

    void showToast(Context context, String message){
        Toast toast=Toast.makeText(context,message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }
}