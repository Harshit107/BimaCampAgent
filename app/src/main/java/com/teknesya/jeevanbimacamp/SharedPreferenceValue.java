package com.teknesya.jeevanbimacamp;


import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SharedPreferenceValue  {

    Context context;
    String valueUser;

    public SharedPreferenceValue(Context contex)
    {
        this.context=contex;
    }


    public void updateLoginValue(String value)
    {


        SharedPreferences sharedPreferences=context.getSharedPreferences("user",MODE_PRIVATE);
        SharedPreferences.Editor edit=sharedPreferences.edit();
        edit.putString("user_login_info",value);
        edit.apply();

    }
    public String getValue()
    {

        SharedPreferences sharedPreferences=context.getSharedPreferences("user",MODE_PRIVATE);
        return sharedPreferences.getString("user_login_info","customer");

    }
    public void updatePresentation(String value)
    {


        SharedPreferences sharedPreferences=context.getSharedPreferences("user",MODE_PRIVATE);
        SharedPreferences.Editor edit=sharedPreferences.edit();
        edit.putString("presentation",value);
        edit.apply();

    }
    public String getPresentation()
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences("user",MODE_PRIVATE);
        return sharedPreferences.getString("presentation","0");

    }
    public void updateReminder(String reminder)
    {


        SharedPreferences sharedPreferences=context.getSharedPreferences("user",MODE_PRIVATE);
        SharedPreferences.Editor edit=sharedPreferences.edit();
        edit.putString("reminder",reminder);
        edit.apply();

    }
    public String getReminder()
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences("user",MODE_PRIVATE);
        return sharedPreferences.getString("reminder","-1");

    }
}
