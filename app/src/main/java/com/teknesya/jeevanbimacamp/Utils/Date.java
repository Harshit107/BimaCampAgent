package com.teknesya.jeevanbimacamp.Utils;

import android.icu.util.Calendar;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;

public class Date {

   @RequiresApi(api = Build.VERSION_CODES.N)

   public static String getDate()
   {
       java.util.Date c = Calendar.getInstance().getTime();
       System.out.println("Current time => " + c);

       SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
       String formattedDate = df.format(c);
       return formattedDate;
   }
}
