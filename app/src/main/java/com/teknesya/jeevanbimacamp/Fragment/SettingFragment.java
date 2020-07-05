package com.teknesya.jeevanbimacamp.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.teknesya.jeevanbimacamp.AgentNotification;
import com.teknesya.jeevanbimacamp.CustHomePage;
import com.teknesya.jeevanbimacamp.AgentProfile;
import com.teknesya.jeevanbimacamp.R;
import com.teknesya.jeevanbimacamp.RechargeWallet;
import com.teknesya.jeevanbimacamp.ShareAndEarn;
import com.teknesya.jeevanbimacamp.UserForm;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class SettingFragment extends Fragment {
    private FirebaseAuth mAuth;

    TextView setting_name,setting_email;
    DatabaseReference update_info,mRoot;
    View progress;
    TextView ptext;
    CircleImageView profile_pic;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settingpage, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initilize();
        LinearLayout profile = view.findViewById(R.id.a_profile);
        LinearLayout addcustomer = view.findViewById(R.id.addcustomer);
        LinearLayout viewcustomer = view.findViewById(R.id.viewcustomer);
        LinearLayout share = view.findViewById(R.id.share);
        LinearLayout wallet = view.findViewById(R.id.wallet);
        LinearLayout notification = view.findViewById(R.id.notification);
        LinearLayout security = view.findViewById(R.id.security);
        LinearLayout contactUs = view.findViewById(R.id.contactus);
        LinearLayout about = view.findViewById(R.id.aboutus);

        LinearLayout privacy = view.findViewById(R.id.privacy);
        Button logout = view.findViewById(R.id.move);

        contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                composeEmail();
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWebPage("https://www.teknesya.com");
            }
        });
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWebPage("https://www.teknesya.com");
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AgentProfile.class));
            }
        });
        addcustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // startActivity(new Intent(getActivity(), UserForm.class));
                Intent it=new Intent(getContext(), UserForm.class);
                startActivity(it);
            }
        });
        security.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // startActivity(new Intent(getActivity(), UserForm.class));
                final Fragment f=new SecurityLogin();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.framelayout, f);
                ft.addToBackStack(null);
                ft.commit();

            }
        });
        viewcustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // startActivity(new Intent(getActivity(), UserForm.class));
                final Fragment f=new AgentCustomerSearchFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.framelayout, f);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                final Fragment f=new RechargeFragment();
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.replace(R.id.framelayout, f);
//                ft.addToBackStack(null);
//                ft.commit();
                Intent it=new Intent(getContext(), RechargeWallet.class);
                startActivity(it);
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent share=new Intent(getContext(), ShareAndEarn.class);
                startActivity(share);
            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent it=new Intent(getContext(),AgentNotification.class);
                    startActivity(it);
                } catch (Exception e) {
                    Toasty.normal(getContext(),e.getMessage()).show();
                }
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //mAuth.signOut();
                Intent it=new Intent(getContext(), CustHomePage.class);
                //it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(it);

            }
        });
    }
    public void initilize()
    {
        progress=getActivity().findViewById(R.id.progress_bar);
        ptext=progress.findViewById(R.id.progress_text);
        progress.setVisibility(View.VISIBLE);
        ptext.setText("Please wait");
        mAuth=FirebaseAuth.getInstance();
        mRoot= FirebaseDatabase.getInstance().getReference();
        TextView textview = (TextView)getActivity().findViewById(R.id.tv_name);
        textview.setText("Settings");


        profile_pic=getActivity().findViewById(R.id.profile_pic);
        setting_name=getActivity().findViewById(R.id.setting_name);
        setting_email=getActivity().findViewById(R.id.setting_email);
        update_info=mRoot.child("users").child("customer").child("registered")
                .child("detail").child(mAuth.getUid());

        update_info.keepSynced(true);
        update_info.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("name"))
                    setting_name.setText(dataSnapshot.child("name").getValue().toString());
                if(dataSnapshot.hasChild("image"))
                    Picasso.get().load(dataSnapshot.child("image").getValue().toString())
                            .placeholder(R.drawable.avtar).into(profile_pic);
                if(dataSnapshot.hasChild("email"))
                    setting_email.setText(dataSnapshot.child("email").getValue().toString());
                else
                    Toasty.warning(getContext(),"Warning : Update Your Email ",Toasty.LENGTH_LONG,true).show();
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toasty.error(getContext(),databaseError.getMessage(),Toasty.LENGTH_LONG,true).show();

            }
        });
    }


    public void composeEmail() {

        new AlertDialog.Builder(getActivity())
                .setTitle("Contact Us")
                .setMessage("Support E-Mail\n"+getResources().getString(R.string.email))
                .setPositiveButton("Open Mail", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent email = new Intent(Intent.ACTION_SEND);
                        email.putExtra(Intent.EXTRA_EMAIL, getResources().getString(R.string.email));
                        email.putExtra(Intent.EXTRA_SUBJECT, "");
                        email.putExtra(Intent.EXTRA_TEXT, "");
                        //need this to prompts email client only
                        email.setType("message/rfc822");
                        startActivity(Intent.createChooser(email, "Choose an Email client :"));                      }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create().show();
    }
    public void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

}






