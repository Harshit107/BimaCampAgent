package com.teknesya.jeevanbimacamp.Utils;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.tntkhang.gmailsenderlibrary.GMailSender;
import com.github.tntkhang.gmailsenderlibrary.GmailListener;
import com.teknesya.jeevanbimacamp.R;

import es.dmoral.toasty.Toasty;


public class MailSender {

    String body, title, reciverEmail;
    Context context;

  public   MailSender(Context context, String msg, String title, String email) {
        body = msg;
       this. title = title;
        reciverEmail = email;
        this.context=context;
    }


     public  void sendEmail() {


        GMailSender.withAccount("harshitkeshari765@gmail.com", "harshhit765")
                .withTitle(title)
                .withBody(body)
                .withSender("JeevanBimacamp")
                .toEmailAddress(reciverEmail) // one or multiple addresses separated by a comma
                .withListenner(new GmailListener() {
                    @Override
                    public void sendSuccess() {
                        Toasty.success(context, "Success", Toast.LENGTH_SHORT,true).show();
                    }

                    @Override
                    public void sendFail(String err) {
                        Log.d("MailError",err);
                        //Toasty.error(context, "Fail"+err, Toast.LENGTH_SHORT,true).show();
                    }
                })
                .send();


    }
}