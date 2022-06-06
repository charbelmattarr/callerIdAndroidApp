package com.example.calleridapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper2 extends SQLiteOpenHelper {


    public static final String CALLLOGS_TABLE = "CALLLOGS_TABLE";
    public static final String CALLLOGS_ID = "CALLLOGS_ID";
    public static final String CALLLOGS_DURATION = "CALLLOGS_DURATION";
    public static final String CALLLOGS_DATE = "CALLLOGS_DATE";
    public static final String CALLLOGS_PHONENUMBER = "CALLLOGS_PHONENUMBER";
    public static final String CALLLOGS_DIRECTION = "CALLLOGS_DIRECTION";
    public static final String CALLLOGS_SAVED = "CALLLOGS_SAVED";
    public static final String CALLLOGS_CONTACTID = "CALLLOGS_CONTACTID";

    public DataBaseHelper2(@Nullable Context context) {
        super(context, "callLogs.db", null, 1);
    }
    // this is called the first time a database is accessed.There should be code in here that creates a database
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement =
                "CREATE TABLE " + CALLLOGS_TABLE + " (" + CALLLOGS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + CALLLOGS_DURATION + "  TEXT, " + CALLLOGS_DATE + "  TEXT UNIQUE, " + CALLLOGS_PHONENUMBER + " TEXT , " + CALLLOGS_DIRECTION + " TEXT ," + CALLLOGS_SAVED + " INT," + CALLLOGS_CONTACTID + " TEXT," +
                        " FOREIGN KEY ("+CALLLOGS_CONTACTID+") REFERENCES CONTACT_TABLE(CONTACT_ID) ON DELETE CASCADE ) ";
//private String duration;
//    private String time;
//    private String date;
//    private String phoneNbre;
//    private int saved=0;
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public boolean addOne(CallLogs callLogs){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CALLLOGS_DIRECTION,callLogs.getDirection());
        cv.put(CALLLOGS_SAVED,callLogs.getSaved());
        cv.put(CALLLOGS_DURATION,callLogs.getDuration());
        cv.put(CALLLOGS_PHONENUMBER,callLogs.getPhoneNbre());
        cv.put(CALLLOGS_DATE,callLogs.getDate());
        cv.put(CALLLOGS_CONTACTID,callLogs.getCallerid());
        long insert= db.insert(CALLLOGS_TABLE,null,cv);

        if(insert == -1){
            return false;
        }else{
            return true;
        }

    }


    public List<CallLogs> getEveryone(){


        List<CallLogs> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM " +CALLLOGS_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.rawQuery(queryString,null)) {
            if (cursor.moveToFirst()) {
                //if there are result i will loop into the results
//                "CREATE TABLE " + CALLLOGS_TABLE + " (" +
//                CALLLOGS_ID + " TEXT PRIMARY KEY AUTOINCREMENT,"
//                + CALLLOGS_DURATION + "  TEXT, " +
//                CALLLOGS_DATE+ "  TEXT UNIQUE, " +
//                CALLLOGS_PHONENUMBER + " TEXT , " +
//                CALLLOGS_DIRECTION +
//                " TEXT ,"
//                + CALLLOGS_SAVED + " INT ) ";                //


                do {
                    String calllogsID = cursor.getString(0);
                    String duration = cursor.getString(1);
                    String date = cursor.getString(2);
                    String phonenumber = cursor.getString(3);
                    String direction = cursor.getString(4);
                    String saved = cursor.getString(5);
                    String callerid =cursor.getString(6);

                    //  public CallLogs(String duration, String direction, String date, String phoneNbre, int saved, String callername) {
                    CallLogs c = new CallLogs(calllogsID,duration,direction,date,phonenumber,saved,callerid);

                    returnList.add(c);
                } while (cursor.moveToNext());
                cursor.close();

            }}catch(Exception e){
            Log.e("error",e.toString());


        }

        return returnList;
    }

public void modifySaved(CallLogs c){
    String queryString = "  UPDATE "+CALLLOGS_TABLE+" SET "+CALLLOGS_SAVED+" = \"true\" WHERE "+CALLLOGS_ID+" = "+c.getCalllogid()+" ;" ;

    SQLiteDatabase db = this.getReadableDatabase();
    CallLogs cl = null;
    Cursor cursor = db.rawQuery(queryString,null) ;
        cursor.moveToFirst();

            cursor.close();




}
    public void modifyContactid(String date,ContactModel c){
        String queryString = "  UPDATE "+CALLLOGS_TABLE+" SET "+CALLLOGS_CONTACTID+" = \""+
            c.getContact_id()    +"\" WHERE "+CALLLOGS_DATE+" = "+date+" ;" ;

        SQLiteDatabase db = this.getReadableDatabase();
        CallLogs cl = null;
        Cursor cursor = db.rawQuery(queryString,null) ;
        cursor.moveToFirst();

        cursor.close();




    }
public CallLogs getLoginfo(String id){
    String queryString = "SELECT * FROM " + CALLLOGS_TABLE + " WHERE "+ CALLLOGS_ID + " = "+id+"";
    SQLiteDatabase db = this.getReadableDatabase();
    CallLogs cl = null;
    try (Cursor cursor = db.rawQuery(queryString,null)) {
        cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            //if there are result i will loop into the results

            Log.d("contact",">>>>>fouund");

            String calllogsID = cursor.getString(0);

            String duration = cursor.getString(1);
            String date = cursor.getString(2);
            String phonenumber = cursor.getString(3);
            String direction = cursor.getString(4);
            String saved = cursor.getString(5);
            String callerid =cursor.getString(6);
            //  public CallLogs(String duration, String direction, String date, String phoneNbre, int saved, String callername) {
             cl = new CallLogs(calllogsID,duration,direction,date,phonenumber,saved,callerid);
            //c = new ContactModel(contactID,contactfname,contactlname,contactcompany,contactjob,contactemail,contactmobilephone);
            Log.d("logsfound",cl.toString());
            Log.d("contactttt",cursor.toString());
            cursor.close();

        }
    }catch(Exception e){
        Log.e("error",e.toString());
    }
    return cl;

}

/*
    public ContactModel fetchcontact(String number){
        //  String queryString = "SELECT * FROM " +CONTACT_TABLE + " WHERE "+ CONTACT_MOBILEPHONE + " = "+ number + "";
        String queryString = "SELECT * FROM " + CONTACT_TABLE + " WHERE "+ CONTACT_MOBILEPHONE + " = 70753661";
        SQLiteDatabase db = this.getReadableDatabase();
        ContactModel c = null;
        try (Cursor cursor = db.rawQuery(queryString,null)) {
            cursor.moveToFirst();
            if (cursor.moveToFirst()) {
                //if there are result i will loop into the results

                Log.d("contact",">>>>>fouund");

                String contactID = cursor.getString(0);

                String contactfname = cursor.getString(1);
                Log.d("name",contactfname);
                String contactlname = cursor.getString(2);
                String contactcompany = cursor.getString(3);
                String contactjob = cursor.getString(4);
                String contactemail = cursor.getString(5);
                String contactmobilephone = cursor.getString(6);

                c = new ContactModel(contactID,contactfname,contactlname,contactcompany,contactjob,contactemail,contactmobilephone);
                Log.d("contactttt",c.toString());
                cursor.close();

            }
        }catch(Exception e){
            Log.e("error",e.toString());
        }
        return c;

    }
    public String getContactId(String email){

        String queryString = "SELECT "+CONTACT_ID+" FROM " + CONTACT_TABLE + " WHERE "+ CONTACT_EMAIL + " = \""+email+"\"";
        SQLiteDatabase db = this.getReadableDatabase();
        String contactid = null;
        try (Cursor cursor = db.rawQuery(queryString,null)) {
            cursor.moveToFirst();
            if (cursor.moveToFirst()) {
                //if there are result i will loop into the results

                Log.d("contact",">>>>>fouund");

                contactid = cursor.getString(0);

                cursor.close();

            }else{
                Log.d("contact",">>>>>notfouund");
            }
        }catch(Exception e){
            Log.e("error",e.toString());
        }
        return contactid;



    }


    public String getContactIdByPhone(String phone){

        String queryString = "SELECT "+CONTACT_ID+" FROM " + CONTACT_TABLE + " WHERE "+ CONTACT_MOBILEPHONE + "="+phone+"";
        SQLiteDatabase db = this.getReadableDatabase();
        String contactid = null;
        try (Cursor cursor = db.rawQuery(queryString,null)) {
            cursor.moveToFirst();
            if (cursor.moveToFirst()) {
                //if there are result i will loop into the results

                Log.d("contact",">>>>>fouund");

                contactid = cursor.getString(0);

                cursor.close();

            }else{
                Log.d("contact",">>>>>notfouund");
            }
        }catch(Exception e){
            Log.e("error",e.toString());
        }
        return contactid;



    }


 */

    public CallLogs fetchByDate(String date1){

       // String queryString = "SELECT * FROM " + CALLLOGS_TABLE + " WHERE "+ CALLLOGS_DATE + " = \" "+date1+"\"";
        String queryString = "SELECT * FROM  CALLLOGS_TABLE  WHERE  CALLLOGS_DATE  = \""+date1+"\"";
        SQLiteDatabase db = this.getReadableDatabase();
        CallLogs cl = null;
        try (Cursor cursor = db.rawQuery(queryString,null)) {
            cursor.moveToFirst();
            if (cursor.moveToFirst()) {
                //if there are result i will loop into the results

                Log.d("contact",">>>>>fouund");

                 String callogID = cursor.getString(0);

                String duration = cursor.getString(1);
                String date = cursor.getString(2);
                String phonenumber = cursor.getString(3);
                String direction = cursor.getString(4);
                String saved = cursor.getString(5);
                String callerid =cursor.getString(6);
                //  public CallLogs(String duration, String direction, String date, String phoneNbre, int saved, String callername) {
                cl = new CallLogs(callogID,duration,direction,date,phonenumber,saved,callerid);
                Log.d("logfound",cl.toString());
                //c = new ContactModel(contactID,contactfname,contactlname,contactcompany,contactjob,contactemail,contactmobilephone);
                Log.d("contactttt",cursor.toString());
                cursor.close();

            }
        }catch(Exception e){
            Log.e("error",e.toString());
        }
        return cl;


    }
}
