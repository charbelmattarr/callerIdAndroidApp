package com.example.calleridapplication;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalStorage {
    private static final String SHARED_PREFERENCE_NAME="Token_preference";
    private static final int SHARED_PREFERENCE_MODE = Context.MODE_PRIVATE;
    private static final String TOKEN_KEY = "access_token";
    private static final String REFRESH_TOKEN_KEY="refresh_token_key";
    private static final String EXPIRE_TIME_KEY = "expire_time";


    SharedPreferences preferences;
    public LocalStorage(Context context){
        preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME,SHARED_PREFERENCE_MODE);
    }

   public void SaveAuthenticationState(String accessToken){
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(TOKEN_KEY,accessToken);
      //  editor.putString(REFRESH_TOKEN_KEY,refreshToken);
      //  editor.putString(EXPIRE_TIME_KEY,expireTime);

        editor.commit();

   }

   public String getAccessToken(){
        String token = preferences.getString(TOKEN_KEY,null);
        return token;
   }

   public String getRefreshToken(){
        String token = preferences.getString(REFRESH_TOKEN_KEY,null);
        return token;
   }


   public String getExpireTime(){
        String expireTime = preferences.getString(EXPIRE_TIME_KEY,null);
        return expireTime;
   }



}
