package com.ssd.ssd.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences {

    static final String KEY_USER_TEREGISTER = "user", KEY_PASS_TEREGISTER = "pass";
    static final String KEY_USERNAME_SEDANG_LOGIN = "Username_logged_in";
    static final String KEY_STATUS_SEDANG_LOGIN = "Status_logged_in";
    static final String KEY_ISLOGIN = "Login";

    private static SharedPreferences getSharedPreference(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setIsLogin(Context context, String tipe){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(KEY_ISLOGIN, tipe);
        editor.apply();
    }


    public static String getIsLogin(Context context){
        return getSharedPreference(context).getString(KEY_ISLOGIN,"false");
    }




    public static void setIsFirstname(Context context, String tipe){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString("firstname", tipe);
        editor.apply();
    }
    public static String getIsFirstname(Context context){
        return getSharedPreference(context).getString("firstname","");
    }

    public static void setIsNamabelakang(Context context, String tipe){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString("namabelakang", tipe);
        editor.apply();
    }
    public static String getIsNamabelakang(Context context){
        return getSharedPreference(context).getString("namabelakang","");
    }


    public static void setIsNohp(Context context, String tipe){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString("nohp", tipe);
        editor.apply();
    }
    public static String getIsNohp(Context context){
        return getSharedPreference(context).getString("nohp","");
    }



    public static void setIsEmail(Context context, String tipe){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString("email", tipe);
        editor.apply();
    }

    public static String getIsEmail(Context context){
        return getSharedPreference(context).getString("email","");
    }


    public static void setIsPassword(Context context, String tipe){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString("password", tipe);
        editor.apply();
    }

    public static String getIsPassword(Context context){
        return getSharedPreference(context).getString("password","");
    }


    public static void setIsAlamat(Context context, String tipe){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString("alamat", tipe);
        editor.apply();
    }

    public static String getIsAlamat(Context context){
        return getSharedPreference(context).getString("alamat","");
    }


    public static void setIsKota(Context context, String tipe){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString("kota", tipe);
        editor.apply();
    }

    public static String getIsKota(Context context){
        return getSharedPreference(context).getString("kota","");
    }



    public static void setIsIdUser(Context context, Integer tipe){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putInt("iduser", tipe);
        editor.apply();
    }

    public static Integer getIsIdUser(Context context){
        return getSharedPreference(context).getInt("iduser",0);
    }

    public static void clearLoggedInUser(Context context) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.remove(KEY_USERNAME_SEDANG_LOGIN);
        editor.remove(KEY_STATUS_SEDANG_LOGIN);
        editor.apply();
    }
}
