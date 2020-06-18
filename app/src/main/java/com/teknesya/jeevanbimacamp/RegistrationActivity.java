
package com.teknesya.jeevanbimacamp;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.teknesya.jeevanbimacamp.Utils.Date;

import java.util.Arrays;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class RegistrationActivity extends AppCompatActivity {

    private ImageView logo, joinus;
    private EditText username, email, password,security;
    private Button signup;
    private TextView signin;
    private ProgressDialog pbar;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    DatabaseReference dRef,root,update_data_to_topic,check_user_DB,mRoot,update_user_type;
    String referral ="",agent_uid="",referral_cost="0";
     boolean checker = false;
     String inputName;
    String inputEmail;
    String inputPw,account=null;



    private CircleImageView google,fb;
    GoogleSignInClient mgooglesinginclint;
    private CallbackManager mCallbackManager;
    private int resume=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_scr);
        setContentView(R.layout.activity_registration);

        initializeGUI();

        mCallbackManager = CallbackManager.Factory.create();

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account="Google";

                try {
                    referral = security.getText().toString().trim();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                ckeckAgentReferral(referral);

            }
        });
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account="Facebook";
                try {
                    referral = security.getText().toString().trim();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
               ckeckAgentReferral(referral);
            }

        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                account="Email";

                inputName= username.getText().toString().trim();
                  inputPw = password.getText().toString().trim();
                  inputEmail = email.getText().toString().trim();
                try {
                    referral = security.getText().toString().trim();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

                validateInput(inputName, inputPw, inputEmail);


            }
        });


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
            }
        });

    }

    private void signIn() {
        Intent signInIntent = mgooglesinginclint.getSignInIntent();
        startActivityForResult(signInIntent, 101);
    }

    private void initializeGUI(){
        logo = findViewById(R.id.ivRegLogo);
        joinus = findViewById(R.id.ivJoinUs);
        username = findViewById(R.id.atvUsernameReg);
        email =  findViewById(R.id.atvEmailReg);
        security=findViewById(R.id.security);
        password = findViewById(R.id.atvPasswordReg);
        signin = findViewById(R.id.tvSignIn);
        signup = findViewById(R.id.btnSignUp);
        pbar = new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);
        pbar.setMessage("Please Wait..");
        google=findViewById(R.id.google);
        fb=findViewById(R.id.fb);
        mRoot=FirebaseDatabase.getInstance().getReference();
        check_user_DB=mRoot;


        firebaseAuth = FirebaseAuth.getInstance();
        update_data_to_topic=FirebaseDatabase.getInstance().getReference();
    }




    private void validateInput(String inName, String inPw, String inEmail){

        if(inName.isEmpty()){
            username.setError("Username is empty.");
            username.requestFocus();

        }
       else  if(inPw.isEmpty()||inPw.length()<6){
            password.setError("Password is empty.");
             password.requestFocus();

        }
       else  if(inEmail.isEmpty())
        {
            email.setError("Email is empty.");
            email.requestFocus();

        }
         else {pbar.show();
             ckeckAgentReferral(referral);
        }


    }

    private void registerUser(final String inputName, final String inputPw, final String inputEmail) {

        pbar.setMessage("Creating New Account");
        pbar.show();


        firebaseAuth.createUserWithEmailAndPassword(inputEmail,inputPw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    sendUserData(inputName, inputEmail);

                }}

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pbar.dismiss();
                Toasty.error(RegistrationActivity.this,e.getMessage(),5000,true).show();

            }
        });

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sendUserData(String username, String email_id){

        String devide_token = FirebaseInstanceId.getInstance().getToken();
        pbar.setMessage("Updating Profile..");
        ;
        dRef=mRoot.child("users").child("customer").child("registered")
                .child("detail");
        dRef.child(firebaseAuth.getUid()).child("name").setValue(username);
        dRef.child(firebaseAuth.getUid()).child("email").setValue(email_id);
        dRef.child(firebaseAuth.getUid()).child("token").setValue(devide_token);
        dRef.child(firebaseAuth.getUid()).child("id").setValue(firebaseAuth.getUid());
        dRef.child(firebaseAuth.getUid()).child("presentation").setValue(referral_cost);
        dRef.child(firebaseAuth.getUid()).child("profile").setValue("0");
        dRef.child(firebaseAuth.getUid()).child("wallet").setValue("0");
        dRef.child(firebaseAuth.getUid()).child("type").setValue("customer");
        dRef.child(firebaseAuth.getUid()).child("account").setValue(account);
        dRef.child(firebaseAuth.getUid()).child("creationdate").setValue(Date.getDate());
        dRef.child(firebaseAuth.getUid()).child("device").setValue(android.os.Build.MODEL);
        dRef.child(firebaseAuth.getUid()).child("lastlogin").setValue(Date.getDate());
        DatabaseReference user_type=mRoot.child("checkuser").child(firebaseAuth.getUid());
        user_type.setValue("customer");

        SharedPreferenceValue share=new SharedPreferenceValue(RegistrationActivity.this);
        share.updateLoginValue("customer");
        share.updatePresentation("0");



        if(!referral.isEmpty() &&  !agent_uid.isEmpty())
        {
            dRef.child(firebaseAuth.getUid()).child("agentId").setValue(agent_uid);
            DatabaseReference pending=mRoot.child("users").child("customer").child("registered")
                    .child("referral").child("pending").child(agent_uid).child(firebaseAuth.getUid());
            pending.setValue(Date.getDate());
        }

           DatabaseReference agent_updateDB=mRoot.
                   child("users").child("customer").child("newUser").
                    child("detail").child(firebaseAuth.getUid());
            agent_updateDB.child("id").setValue(firebaseAuth.getUid());

        Toasty.success(RegistrationActivity.this,"You've been registered successfully.", Toasty.LENGTH_LONG,true).show();

        if(account.equals("Email")) {

            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    pbar.dismiss();
                    firebaseAuth.signOut();

                    AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
                    builder.setCancelable(false);
                    builder.setIcon(R.drawable.logo);
                    builder.setTitle(R.string.app_name);
                    builder.setMessage("Registration Successful\nVerify Your E-mail to Login");
                    builder.setPositiveButton("Latter", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent it1 = new Intent(getApplicationContext(), LoginActivity.class);
                            it1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(it1);
                        }
                    }).setNegativeButton("Verify E-Mail Now", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            resume=1;
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                            try {
                                startActivity(intent);
                                startActivity(Intent.createChooser(intent, "ChoseEmailClient"));

                            } catch (Exception ex) {
                                try {
                                    Snackbar.make(Objects.requireNonNull(getCurrentFocus()), ex.toString(), Snackbar.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                    builder.create().show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pbar.dismiss();
                    Toasty.error(getApplicationContext(), e.getMessage(), 5000, true).show();
                }
            });
        }
        else
        {
            pbar.dismiss();
            startActivity(new Intent(RegistrationActivity.this, CustHomePage.class));

        }



    }

    @Override
    protected void onResume() {
        super.onResume();
        if(resume==1)
        {
            Intent it1 = new Intent(getApplicationContext(), LoginActivity.class);
            it1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(it1);

        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        {
                            checkUser();


                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pbar.dismiss();
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
    public void sendUser()
    {
//        String devide_token = FirebaseInstanceId.getInstance().getToken();
//
//        check_user_DB.child("users").child("customer").child("registered").
//                child("detail").child(firebaseAuth.getUid()).child("token").setValue(devide_token);
//        pbar.dismiss();
//        Toast.makeText(getApplicationContext(),"Login Successful", Toast.LENGTH_SHORT).show();
//        finish();
//        startActivity(new Intent(getApplicationContext(),RegistrationActivity.class));


        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
        builder.setCancelable(false);
        builder.setIcon(R.drawable.logo);
        builder.setTitle(R.string.app_name);
        builder.setMessage("Account Is Already Registered With Us\nLogin To Continue..");
        builder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent it1 = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(it1);
            }
        });
        builder.create().show();


    }
    private void checkUser() {

        check_user_DB.child("users").child("customer").child("registered").child("detail").
        addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(firebaseAuth.getUid())) {
                    sendUser();
                }
                else {
                    FirebaseUser user=firebaseAuth.getCurrentUser();
                    String name = user.getDisplayName();
                    String email="";
                    email= user.getEmail();
                    sendUserData(name,email);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pbar.dismiss();
                Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 101) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                pbar.dismiss();
                // Google Sign In failed, update UI appropriately
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                // ...
            }
        }
        else
            mCallbackManager.onActivityResult(requestCode, resultCode, data);


    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("TAG", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful())
                        {
                            checkUser();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pbar.dismiss();
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void ckeckAgentReferral(final String secure)
    {
    if(secure.length()>0)
    {
        pbar.show();
        pbar.setMessage("Validating Referral Code");
        root=mRoot.
                child("unique").child("referral").child("code");
        DatabaseReference referral_value_info=FirebaseDatabase.getInstance().getReference()
                .child("unique").child("referral").child("value");

        referral_value_info.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("refregistered"))
                {
                    referral_cost=  dataSnapshot.child("refregistered").getValue().toString();
                    root.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(secure))
                            {
                                agent_uid=dataSnapshot.child(secure).child("id").getValue().toString();
                                next();
                            }
                            else
                            {
                                pbar.dismiss();
                                security.setError("Enter Valid referral Code. ");
                                security.requestFocus();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            pbar.dismiss();
                            Toasty.error(RegistrationActivity.this, databaseError.getMessage(), 5000,true).show();

                        }
                    });



                }
                else
                {
                    Toasty.error(getApplicationContext(),"Currently We Do not have referral program Active",5000,true).show();
                    pbar.dismiss();
                    security.setError("Remove Referral Code before registration. ");
                    security.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pbar.dismiss();
                Toast.makeText(RegistrationActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
    else
        next();


    }

    public  void next()
    {

        if(account.equals("Email"))
            registerUser(inputName, inputPw, inputEmail);
        else if(account.equals("Google"))
        {
            pbar.show();
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            mgooglesinginclint= GoogleSignIn.getClient(RegistrationActivity.this,gso);
            signIn();
        }
        else if(account.equals("Facebook"))
        {
            pbar.show();
            LoginManager.getInstance()
                    .logInWithReadPermissions(RegistrationActivity.this, Arrays.asList("email", "public_profile"));
            LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    Log.d("TAG", "facebook:onSuccess:" + loginResult);
                    handleFacebookAccessToken(loginResult.getAccessToken());
                }

                @Override
                public void onCancel() {
                    Log.d("TAG", "facebook:onCancel");
                    Toasty.error(getApplicationContext(),"Cancelled",Toasty.LENGTH_LONG,true).show();

                    pbar.dismiss();
                    // ...
                }

                @Override
                public void onError(FacebookException error) {
                    Log.d("TAG", "facebook:onError", error);
                    Toasty.error(getApplicationContext(),error.getMessage(),Toasty.LENGTH_LONG,true).show();
                    pbar.dismiss();
                    // ...
                }

            });
        }


    }

}
