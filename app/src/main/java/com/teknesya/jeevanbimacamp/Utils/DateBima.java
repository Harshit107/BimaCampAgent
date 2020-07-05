package com.teknesya.jeevanbimacamp.Utils;

import android.icu.util.Calendar;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;

public class DateBima {

   @RequiresApi(api = Build.VERSION_CODES.N)

   public static String getDate()
   {
       java.util.Date c = Calendar.getInstance().getTime();
       System.out.println("Current time => " + c);

       SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
       String formattedDate = df.format(c);
       return formattedDate;
   }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String getDateInStringNumFormate()
    {
        Calendar cd=Calendar.getInstance();
        cd.add(Calendar.DATE,-1);
        int mm=cd.get(Calendar.MONTH);
        int dd=cd.get(Calendar.DATE);
        int yy=cd.get(Calendar.YEAR);
        ++mm;
        String formattedDate=(String.valueOf(dd)+"-"+String.valueOf(mm)+"-"+String.valueOf(yy)  );
        return formattedDate;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String getTommorowDateMonth()
    {
        Calendar cd=Calendar.getInstance();
        cd.add(Calendar.DATE,1);
        int mm=cd.get(Calendar.MONTH);
        int dd=cd.get(Calendar.DATE);
        ++mm;
        String formattedDate=(String.valueOf(dd)+"-"+String.valueOf(mm)  );
        return formattedDate;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String getTodaysDateMonth()
    {
        Calendar calendar=Calendar.getInstance();
        int mm=calendar.get(Calendar.MONTH);
        int dd=calendar.get(Calendar.DATE);
        ++mm;
        String formattedDateToday=(String.valueOf(dd)+"-"+String.valueOf(mm)  );
        return formattedDateToday;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String getMaturityDate(int dur)
    {
        Calendar cd=Calendar.getInstance();
        cd.add(Calendar.MONTH,dur);
        int mm=cd.get(Calendar.MONTH);
        int dd=cd.get(Calendar.DATE);
        ++mm;

        String formattedDate=(String.valueOf(dd)+"-"+String.valueOf(mm)+"-"+String.valueOf(cd.get(Calendar.YEAR))  );
        return formattedDate;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public static int getYear()
    {
        Calendar now = Calendar.getInstance();
       return now.get(Calendar.YEAR);
        //String yearInString = String.valueOf(year);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static int getMonth()
    {
        Calendar now = Calendar.getInstance();
        return  now.get(Calendar.MONTH)+1;
        //String yearInString = String.valueOf(year);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static int getDay()
    {
        Calendar now = Calendar.getInstance();
       return now.get(Calendar.DATE);
        //String yearInString = String.valueOf(year);
    }

    public static int convertDateMonthToMonth(String dateMonth)
    {
        String[] m=dateMonth.split("-");
        return Integer.parseInt(m[1]);

    }



}
