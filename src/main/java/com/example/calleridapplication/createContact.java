package com.example.calleridapplication;

import static com.example.CallerIdApplication.R.*;
import static com.example.CallerIdApplication.R.color.*;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.CallerIdApplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link createContact#newInstance} factory method to
 * create an instance of this fragment.
 */
public class createContact extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public String APIURL ="https://calleridcrmapi.azure-api.net/test/contacts/";
    EditText ETfirstname,ETlastname,ETcompany,ETjob,ETemail,ETphonenumber;
    TextView createStatus;
    String firstname,lastname,company,job,email,mobilephone;
    Button btnCreateContact;
    ProgressBar progressbar;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public createContact() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *  //@param param1 Parameter 1.
     * //@param param2 Parameter 2.
     * //@return A new instance of fragment createContact.
     */
    // TODO: Rename and change types and number of parameters
    public static createContact newInstance() {
        createContact fragment = new createContact();
     //   Bundle args = new Bundle();
    //   args.putString(ARG_PARAM1, param1);
    //    args.putString(ARG_PARAM2, param2);
     //   fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   if (getArguments() != null) {
         //   mParam1 = getArguments().getString(ARG_PARAM1);
         //   mParam2 = getArguments().getString(ARG_PARAM2);
     //   }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(layout.createcontact, container, false);
        //EditText ETfirstname,ETlastname,ETcompany,ETjob,ETemail,ETphonenumber;
        ETfirstname = view.findViewById(id.cr_firstname);
        ETlastname = view.findViewById(id.cr_lastname);
        ETcompany = view.findViewById(id.cr_company);
        ETjob = view.findViewById(id.cr_job);
        ETemail = view.findViewById(id.cr_email);
        ETphonenumber= view.findViewById(id.cr_phonenbre);
        progressbar=view.findViewById(id.progressBar1);
        btnCreateContact = view.findViewById(id.createContact);
        createStatus = view.findViewById(id.createStatus);
        createStatus.setText("");
        ETfirstname.setText("");
        ETlastname.setText("");
        ETcompany.setText("");
        ETjob.setText("");
        ETemail.setText("");
        ETphonenumber.setText("");
        btnCreateContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstname=ETfirstname.getText().toString().trim();
               lastname=ETlastname.getText().toString().trim();
                company=ETcompany.getText().toString().trim();
                job=ETjob.getText().toString().trim();
                email=ETemail.getText().toString().trim();
                if(!callReciever.number.isEmpty()){
                    ETphonenumber.setText(callReciever.number);
                    ETphonenumber.setEnabled(false);
                    ETphonenumber.setClickable(false);
                }
                mobilephone=ETphonenumber.getText().toString().trim();
if(firstname.isEmpty() || lastname.isEmpty() || mobilephone.isEmpty()){

  //  Toast.makeText(getActivity(),"make sure that all required fields are there!",Toast.LENGTH_LONG).show();
 //   return;
}
                   //this one is using volley
                //   createContactinCRM1(firstname,lastname,company,job,email,mobilephone);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressbar.setActivated(true);
                        progressbar.setVisibility(View.VISIBLE);
                    /*    for(int i=0;i<1000;i++){
                             createContactinCRM3("firstname","lastname","company","job","email","mobilephone");
                        }*/
                        createContactinCRM2(firstname,lastname,company,job,email,mobilephone);
                    }
                });




            }
        });






        return view;
    }

    private void createContactinCRM3(String firstname, String lastname, String company, String job, String email, String mobilephone) {


        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {


                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, "{\r\n    \"firstname\": \" "+firstname+" \"\r\n," +
                        "\r\n    \"lastname\": \" "+lastname+"    \"\r\n," +
                        "\r\n    \"cr051_companyname\": \" "+company+"    \"\r\n," +
                        "\r\n    \"emailaddress1\": \" "+email+"    \"\r\n," +
                        "\r\n    \"jobtitle\": \" "+job+"    \"\r\n," +
                        "\r\n    \"mobilephone\": \" "+mobilephone+"    \"\r\n}");

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


                            throw new IOException("Unexpected code " + response);
                        }else{
                            //  Toast.makeText(getActivity(),"sucess",Toast.LENGTH_LONG).show();

                            Log.e("okhttp2",response.toString());


                            Log.d("create:","success");
                        }

                        // you code to handle response
                    }

                });
            }
        });



    }









    private void createContactinCRM2(String firstname, String lastname, String company, String job, String email, String mobilephone) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {


                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, "{\r\n    \"firstname\": \" "+firstname+" \"\r\n," +
                                                                         "\r\n    \"lastname\": \" "+lastname+"    \"\r\n," +
                                                                          "\r\n    \"cr051_companyname\": \" "+company+"    \"\r\n," +
                        "\r\n    \"emailaddress1\": \" "+email+"    \"\r\n," +
                        "\r\n    \"jobtitle\": \" "+job+"    \"\r\n," +
                        "\r\n    \"mobilephone\": \" "+mobilephone+"    \"\r\n}");

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
                                    progressbar.setVisibility(View.INVISIBLE);
                                    ETfirstname.setText("");
                                    ETlastname.setText("");
                                    ETcompany.setText("");
                                    ETjob.setText("");
                                    ETemail.setText("");
                                    ETphonenumber.setText("");
                                    createStatus.setTextColor(Integer.parseInt("#eb4b4b"));
                                    createStatus.setText("unsuccessfull!");

                                }
                            });

                            throw new IOException("Unexpected code " + response);
                        }else{
                          //  Toast.makeText(getActivity(),"sucess",Toast.LENGTH_LONG).show();

                            Log.e("okhttp2",response.toString());
getActivity().runOnUiThread(new Runnable() {
    @Override
    public void run() {
        progressbar.setVisibility(View.INVISIBLE);
        ETfirstname.setText("");
        ETlastname.setText("");
        ETcompany.setText("");
        ETjob.setText("");
        ETemail.setText("");
        ETphonenumber.setText("");
        createStatus.setText("added success!");
    }
});

                        Log.d("create:","success");
                        }

                        // you code to handle response
                    }

                });
            }
        });



    }


































    private void createContactinCRM1(String firstname, String lastname, String company, String job, String email, String mobilephone) {

      getActivity().runOnUiThread(new Runnable() {
          @Override
          public void run() {


        RequestQueue queue = Volley.newRequestQueue(getActivity());

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.POST, APIURL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // inside on response method we are
                // hiding our progress bar
                // and setting data to edit text as empty
                ETfirstname.setText("");
                ETlastname.setText("");
                ETcompany.setText("");
                ETjob.setText("");
                ETemail.setText("");
                ETphonenumber.setText("");
                // on below line we are displaying a success toast message.
                Toast.makeText(getContext(), "Data added to API", Toast.LENGTH_SHORT).show();
                // on below line we are parsing the response
                // to json object to extract data from it.

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(getContext(), "Fail to get response = " + error, Toast.LENGTH_LONG).show();
                Log.e("creating error",error.getStackTrace().toString());
            }
        }) {

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("OData-MaxVersion", "4.0");
                headers.put("OData-Version", "4.0");
                //  return headers;
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                 //super.getParams();
                Map<String, String> params = new HashMap<>();

                // on below line we are passing our key
                // and value pair to our parameters.
                params.put("firstname", firstname);
                params.put("lastname", lastname);
                params.put("cr051_companyname", company);
                params.put("jobtitle", job);
                params.put("emailaddress1", email);
                params.put("mobilephone", mobilephone);
                  return params;

                // at last we are
                // returning our params.
                //   return params;
            }

        };
        // below line is to make
        // a json object request.
        queue.add(request);

          }
      });
    }
    }
/*
* {
            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<>();

                // on below line we are passing our key
                // and value pair to our parameters.
                params.put("firstname", firstname);
                params.put("lastname", lastname);
                params.put("cr051_companyname", company);
                params.put("jobtitle", job);
                params.put("emailaddress1", email);
                params.put("mobilephone", mobilephone);


                // at last we are
                // returning our params.
             //   return params;
            }
* */
