package com.teknesya.jeevanbimacamp.Adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.teknesya.jeevanbimacamp.R;
import com.teknesya.jeevanbimacamp.javamessage;

import java.io.File;
import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.messageviewholder> {
    private List<javamessage> usermessagelist;
    private FirebaseAuth mAuth;
    public static int CHECKER=0;

    Context context;


    public ChatMessageAdapter(List<javamessage> usermessagelist,Context context) {
        this.usermessagelist = usermessagelist;
        this.context=context;
    }


    class messageviewholder extends RecyclerView.ViewHolder {

        TextView recivermessageText, recivername, recivertime, reciver_image_time;
        CircleImageView reciverprofilepic;
        ImageView messagereciverpicture;

        messageviewholder(@NonNull View itemView) {
            super(itemView);
            recivermessageText = (TextView) itemView.findViewById(R.id.reciver_message);
            reciverprofilepic = (CircleImageView) itemView.findViewById(R.id.message_profile_image);
            messagereciverpicture = (ImageView) itemView.findViewById(R.id.message_reciver_picture);
            recivername = (TextView) itemView.findViewById(R.id.reciver_name);
            recivertime = (TextView) itemView.findViewById(R.id.reciver_time);
            reciver_image_time = (TextView) itemView.findViewById(R.id.reciver_image_time);


        }
    }

    @NonNull
    @Override
    public messageviewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_message_layout, viewGroup, false);
        mAuth = FirebaseAuth.getInstance();
        return new messageviewholder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final messageviewholder messageviewholder, final int position) {
        final javamessage message = usermessagelist.get(position);
        String fromUserID = message.getFrom();


        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().
                child("users").child("customer").child("registered").child(fromUserID);

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("image")) {
                    String receiverImage = dataSnapshot.child("image").getValue().toString();
                    try {
                        Picasso.get().load(receiverImage).placeholder(R.drawable.logo).into(messageviewholder.reciverprofilepic);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        messageviewholder.reciverprofilepic.setVisibility(View.INVISIBLE);
        messageviewholder.recivermessageText.setVisibility(View.INVISIBLE);
        messageviewholder.messagereciverpicture.setVisibility(View.GONE);
        messageviewholder.reciver_image_time.setVisibility(View.GONE);
        messageviewholder.recivertime.setVisibility(View.INVISIBLE);
        messageviewholder.recivertime.setVisibility(View.INVISIBLE);
        messageviewholder.recivername.setVisibility(View.INVISIBLE);


        if (message.getType().equals("message")) {
            messageviewholder.recivername.setVisibility(View.VISIBLE);
            messageviewholder.recivertime.setVisibility(View.VISIBLE);
            messageviewholder.reciverprofilepic.setVisibility(View.VISIBLE);
            messageviewholder.recivermessageText.setVisibility(View.VISIBLE);
            messageviewholder.recivermessageText.setBackgroundResource(R.drawable.reciver_message_layout);
            messageviewholder.recivermessageText.setText(message.getMessage());
            messageviewholder.recivername.setText(message.getName());
            messageviewholder.recivertime.setText(message.getTime() + " - " + message.getDate());

        } else if (message.getType().equals("image")) {

            try {
                Log.d("Tag","Checking");
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(message.getMessage()));
//                int w=bitmap.getWidth();
//                int h=bitmap.getHeight();
//                Toasty.success(context,w+" "+h).show();
//
//                try {
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    options.inJustDecodeBounds = true;
//                    BitmapFactory.decodeFile(new File(Uri.parse(message.getMessage()).getPath()).getAbsolutePath(), options);
//                    int h = options.outHeight;
//                    int w = options.outWidth;
//                    Toasty.success(context,w+" "+h).show();
////                    messageviewholder.messagereciverpicture.getLayoutParams().width=w;
////                    messageviewholder.messagereciverpicture.getLayoutParams().height=h;
////                    messageviewholder.messagereciverpicture.requestLayout();
//                } catch (Exception e) {
//                    Toasty.success(context,e.getMessage()).show();
//                    e.printStackTrace();
//                }
//                Bitmap b = ((BitmapDrawable)imageView.getBackground()).getBitmap();
//                int w = b.getWidth();
//                int h = b.getHeight();

            } finally {

                messageviewholder.reciver_image_time.setVisibility(View.VISIBLE);
                messageviewholder.recivername.setVisibility(View.VISIBLE);
                messageviewholder.messagereciverpicture.setVisibility(View.VISIBLE);
                messageviewholder.reciverprofilepic.setVisibility(View.VISIBLE);
                messageviewholder.recivername.setText(message.getName());
                messageviewholder.reciver_image_time.setText(message.getTime() + " - " + message.getDate());
                Picasso.get().load(message.getMessage()).placeholder(R.drawable.logo).fit()
                        .centerInside().into(messageviewholder.messagereciverpicture);
                messageviewholder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(message.getMessage()));
                        messageviewholder.itemView.getContext().startActivity(it);
                    }
                });
            }
        } else if (message.getType().equals("xlsx")) {

            messageviewholder.reciver_image_time.setVisibility(View.VISIBLE);
            messageviewholder.recivername.setVisibility(View.VISIBLE);
            messageviewholder.messagereciverpicture.setVisibility(View.VISIBLE);
            messageviewholder.reciverprofilepic.setVisibility(View.VISIBLE);
            messageviewholder.recivername.setText(message.getName());
            messageviewholder.reciver_image_time.setText(message.getTime() + " - " + message.getDate());
            messageviewholder.messagereciverpicture.setImageResource(R.drawable.excel);
            messageviewholder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(message.getMessage()));

                    try {
                        messageviewholder.itemView.getContext().startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(messageviewholder.itemView.getContext(), "Application not found", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        } else if (message.getType().equals("pdf")) {

            messageviewholder.reciver_image_time.setVisibility(View.VISIBLE);
            messageviewholder.recivername.setVisibility(View.VISIBLE);
            messageviewholder.messagereciverpicture.setVisibility(View.VISIBLE);
            messageviewholder.reciverprofilepic.setVisibility(View.VISIBLE);
            messageviewholder.recivername.setText(message.getName());
            messageviewholder.reciver_image_time.setText(message.getTime() + " - " + message.getDate());
            messageviewholder.messagereciverpicture.setImageResource(R.drawable.pdf_image);
            messageviewholder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent();
                    try {
                        intent.setDataAndType(Uri.parse(message.getMessage()), "application/pdf");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    messageviewholder.itemView.getContext().startActivity(intent);
                }
            });

        } else {

            messageviewholder.reciver_image_time.setVisibility(View.VISIBLE);
            messageviewholder.recivername.setVisibility(View.VISIBLE);
            messageviewholder.messagereciverpicture.setVisibility(View.VISIBLE);
            messageviewholder.reciverprofilepic.setVisibility(View.VISIBLE);
            messageviewholder.recivername.setText(message.getName());
            messageviewholder.reciver_image_time.setText(message.getTime() + " - " + message.getDate());
            messageviewholder.messagereciverpicture.setImageResource(R.drawable.msword);
            messageviewholder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(message.getMessage()));

                    try {
                        messageviewholder.itemView.getContext().startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(messageviewholder.itemView.getContext(), "Application not found", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }


    @Override
    public int getItemCount() {
        return usermessagelist.size();
    }

}
