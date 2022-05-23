package com.example.callerid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class CallReceiver extends BroadcastReceiver {
    String incomingNumber1;

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals("android.intent.action.PHONE_STATE")) {

            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

            //if(state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
            //Log.d(TAG, "Inside Extra state off hook");
            // String number = intent.getStringExtra(TelephonyManager.EXTRA_PHONE_NUMBER);
            // Log.e(TAG, "outgoing number : " + number);
            // }



//  running          if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
//                //Log.e(TAG, "Inside EXTRA_STATE_RINGING");
//                String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
//                System.out.println("The Caller Number is: " + number);
//                showToast(context, "The Caller Number is: " + number);
//                //Log.e(TAG, "incoming number : " + number);
//            }


            // else if(state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
            // Log.d(TAG, "Inside EXTRA_STATE_IDLE");
            //}


            if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                System.out.println("The Caller Number is offhook: " + number);
                showToast(context, "Call started... The Number is: " + number);
                //showToast(context, "Call started...");

            } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                System.out.println("The Caller Number is idle: " + number);

                Intent intent1 = new Intent(context, CallerInfoActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.putExtra("number",number);
                context.startActivity(intent1);

                showToast(context, "Call ended... Number is: " + number);
               // showToast(context, "Call ended...");

            } else if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                System.out.println("The Caller Number is Ringing:  " + number);
                showToast(context, "Incoming call... Number is: " + number);
               // showToast(context, "Incoming call...");
            }
        }
    }

    void showToast(Context context,String message){
        Toast toast=Toast.makeText(context,message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

}

