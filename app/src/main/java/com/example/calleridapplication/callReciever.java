package com.example.calleridapplication;

import android.animation.Keyframe;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class callReciever extends BroadcastReceiver {
    int count=0;
    public static final String CUSTOM_INTENT = "jason.wei.custom.intent.action.TEST";
   static String number="testing";

  // Dialog dialog;
    @Override
    public void onReceive(Context context, Intent intent) {


        if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {

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
                //   String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                //   System.out.println("The Caller Number is offhook: " + number);
                //   showToast(context, "Call started... The Number is: " + number);
                //showToast(context, "Call started...");

            } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                //  String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                System.out.println("The Caller Number is idle: " + number);

                Intent intent1 = new Intent(context, Callerinfoactivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent1.putExtra("number",number);
                context.startActivity(intent1);

                showToast(context, "Call ended... Number is: " + number);
                // showToast(context, "Call ended...");

            } else if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                System.out.println("The Caller Number is Ringing:  " + number);
                showToast(context, "Incoming call... Number is: " + number);
                // showToast(context, "Incoming call...");
            }


        }




       /* TelephonyManager telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);

        telephony.listen(new PhoneStateListener(){
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {

                super.onCallStateChanged(state, incomingNumber);
                String state1 = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
                // if(state1.equals(TelephonyManager.EXTRA_STATE_RINGING)){
               if(!incomingNumber.isEmpty() && !incomingNumber.equals("null")){
                number = incomingNumber;
                // loop(context,number);
                //if(count == 1){
                   showDialog(context,number);

                //    }

               }

                //  }
            }
        },PhoneStateListener.LISTEN_CALL_STATE);

*/
    }



//if (intent.getAction().equals(callReciever.CUSTOM_INTENT)) {
        /*
        System.out.println("GOT THE INTENT");
        Log.d("intent found","found");
        final String mobileNumber = intent.getExtras().getString("number");
        Thread thread = new Thread(){
            private int sleepTime = 40;

            @Override
            public void run() {
                super.run();
                try {
                    int wait_Time = 0;

                    while (wait_Time < sleepTime ) {
                        sleep(40);
                        wait_Time += 10 ;
                    }
                }catch (Exception e) {
                    Toast.makeText(context,
                            "Error Occured Because:" + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
                finally {

                }

                context.startActivity(new Intent(context, Callerinfoactivity.class).putExtra("number", mobileNumber)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        };
        thread.run();
  //  }
}*/
/* }


     //   String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
      //  if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
     //       Log.e("onmessagereceived","dsfa");
      //      number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
      //      showDialog(context);
     //   }


        /*
        if(intent.getAction().equals("android.intent.action.PHONE_STATE")) {

            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
             //Log.e(TAG, "Inside EXTRA_STATE_RINGING");
                number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                System.out.println("The Caller Number is: " + number);
                showToast(context, "The Caller Number is: " + number);
              //  Intent i = new Intent(context,Callerinfoactivity.class);
              //  i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              //  i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                // i.putExtra("number",number);
                Intent intent1 = context.getPackageManager().getLaunchIntentForPackage( "com.example.calleridapplication");
                if (intent1 != null) {
                    // We found the activity now start the activity
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent1);
                }*/

              //  context.startActivity(i);
            /*  new Timer().schedule(new TimerTask() {
                  @Override
                  public void run() {
                      if(!check_if_number_isEmpty(number)){
                          Intent i = new Intent(context,Callerinfoactivity.class);
                          i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                          //    i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                          if(context== null){  showToast(context,"context is null"); }
                         // i.putExtra("number",number);

                          context.startActivity(i);
                          return;
                      }

                  }
              },1);*/
              //Log.e(TAG, "incoming number : " + number);



//            }

     /*   telephony.listen(new PhoneStateListener(){
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);
                String state1 = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
                count++;
                showToast( context,"onRecieve called");
                    //if(count == 1){
                showToast( context,"lezim teftah anyminute now");
                  number = incomingNumber;

                  Intent i = new Intent(context,Callerinfoactivity.class);
                  i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              //    i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                  if(context== null){  showToast(context,"context is null"); }
                  i.putExtra("number",number);

                  context.startActivity(i);

               //    }


          }
        },PhoneStateListener.LISTEN_CALL_STATE);

/*


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
             //   String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
             //   System.out.println("The Caller Number is offhook: " + number);
             //   showToast(context, "Call started... The Number is: " + number);
                //showToast(context, "Call started...");

            } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
              //  String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                System.out.println("The Caller Number is idle: " + number);

                Intent intent1 = new Intent(context, Callerinfoactivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.putExtra("number",number);
                context.startActivity(intent1);

                showToast(context, "Call ended... Number is: " + number);
                // showToast(context, "Call ended...");

            } else if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                System.out.println("The Caller Number is Ringing:  " + number);
                showToast(context, "Incoming call... Number is: " + number);
                // showToast(context, "Incoming call...");
            }
        }*/


   public void showDialog(Context context,String nbre){

       Log.d("showDialog","outside if count is"+count );
       if(count!= 0){
       Log.d("showDialog","inside if block count is"+count );
       Intent i = new Intent(context,Callerinfoactivity.class);
       i.putExtra("number",nbre);
       i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
       i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
           i.setAction(CUSTOM_INTENT);
           context.sendBroadcast(i);
    //   context.startForegroundService(i);
       //    context.startActivity(i);
       }
       count++;
   }
public void loop(Context ctx,String number){
    while(number.isEmpty()){
        Log.d("looping","looping");
        loop(ctx,number);
        Log.d("looping","endded");
    }
    String savedNumber = number;
    showDialog(ctx,savedNumber);

    return;
}

public boolean check_if_number_isEmpty(String nbre){
        if(nbre.equals("null")){return true;}
        return false;
}
    void showToast(Context context,String message){
        Toast toast=Toast.makeText(context,message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

}

