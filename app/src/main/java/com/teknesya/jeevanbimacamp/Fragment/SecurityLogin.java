package com.teknesya.jeevanbimacamp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teknesya.jeevanbimacamp.R;

import es.dmoral.toasty.Toasty;

public class SecurityLogin extends Fragment {
    private FirebaseAuth mAuth;

    TextView creation_date, last_logn, account, device;
    DatabaseReference update_info, mRoot;
    View progress;
    TextView ptext;
    LinearLayout change_pass_layout;
    EditText n_pass,c_pass,old_pass;
    Button update_password;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.secutity_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        initilize(view);
        getInfo();
        update_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (old_pass.getText().toString().isEmpty()) {
                    old_pass.setError("Email field is empty.");

                }
               else if (n_pass.getText().toString().isEmpty()) {
                    n_pass.setError("Email field is empty.");

                }
                else if (c_pass.getText().toString().isEmpty()) {
                    c_pass.setError("Email field is empty.");

                }
                else if(n_pass.getText().toString().equals(c_pass.getText().toString())) {
                    progress.setVisibility(View.VISIBLE);
                    ptext.setText("Authenticating User");
                    changePassword(old_pass.getText().toString(), n_pass.getText().toString());
                }
                else {
                    Toasty.error(getContext(), "Password Not Matched", Toasty.LENGTH_LONG, true).show();
                    n_pass.setError("Password not matched");
                    c_pass.setError("Password not matched");
                }
            }
        });




    }

    public void initilize(View v) {
        progress = getActivity().findViewById(R.id.progress_bar);
        ptext = progress.findViewById(R.id.progress_text);
        progress.setVisibility(View.VISIBLE);
        ptext.setText("Please wait");

        mAuth = FirebaseAuth.getInstance();
        mRoot = FirebaseDatabase.getInstance().getReference();
        creation_date =v.findViewById(R.id.creation_date);
        last_logn = v.findViewById(R.id.last_login);
        device = v.findViewById(R.id.login_device);
        account = v.findViewById(R.id.s_account);
        old_pass = v.findViewById(R.id.old_pass);
        n_pass = v.findViewById(R.id.n_pass);
        c_pass = v.findViewById(R.id.c_pass);
        update_password = v.findViewById(R.id.update_pass);
        change_pass_layout = (LinearLayout) v.findViewById(R.id.change_pass);

        TextView textview = (TextView) getActivity().findViewById(R.id.tv_name);
        textview.setText("Login & Security");
    }

    public void getInfo() {

        update_info = mRoot.child("users").child("customer")
                .child("registered").child("detail").child(mAuth.getUid());
        update_info.keepSynced(true);
        update_info.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("creationdate"))
                    creation_date.setText(dataSnapshot.child("creationdate").getValue().toString());
                if (dataSnapshot.hasChild("lastlogin"))
                    last_logn.setText(dataSnapshot.child("lastlogin").getValue().toString());
                if (dataSnapshot.hasChild("account"))
                    account.setText(dataSnapshot.child("account").getValue().toString());
                if (dataSnapshot.hasChild("device"))
                    device.setText(dataSnapshot.child("device").getValue().toString());
                if (account.getText().toString().equals("Email"))
                    change_pass_layout.setVisibility(View.VISIBLE);


                progress.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toasty.error(getContext(), databaseError.getMessage(), Toasty.LENGTH_LONG, true).show();

            }
        });
    }

public void changePassword(final String oldpass, final String newPass)
{
     final FirebaseUser user;
    user = FirebaseAuth.getInstance().getCurrentUser();
    final String email = user.getEmail();
    AuthCredential credential = EmailAuthProvider.getCredential(email,oldpass);

    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if(task.isSuccessful()){
                ptext.setText("Updating Password..");
                user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(!task.isSuccessful()){
                             Toasty
                                    .error(getContext(), "Something went wrong. Please try again later", Toasty.LENGTH_LONG,true).show();
                             progress.setVisibility(View.GONE);

                        }else {
                          Toasty.success(getContext(), "Password Successfully Modified", Toasty.LENGTH_LONG,true).show();
                            progress.setVisibility(View.GONE);
                        }
                    }
                });
            }else {
                progress.setVisibility(View.GONE);
                Toasty.error(getContext(), "Incorrect Password, Try again", Toasty.LENGTH_LONG,true).show();
                old_pass.setError("Incorrect Password");

            }
        }
    });
}


}
