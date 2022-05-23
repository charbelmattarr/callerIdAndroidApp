package com.example.callerid;




import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



public class CallerInfoActivity extends AppCompatActivity {
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_caller_info);


        Bundle objet_Bundle = this.getIntent().getExtras();
        String number = objet_Bundle.getString("number");
        System.out.println("The Caller Number is: " + number);
        showToast(this, "Call ended... The Number is: " + number);
        textView = (TextView) findViewById(R.id.textView);

        textView.setText("" + number);
    }
    void showToast(Context context, String message){
        Toast toast=Toast.makeText(context,message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }
}