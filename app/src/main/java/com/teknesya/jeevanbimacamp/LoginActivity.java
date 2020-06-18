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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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

public class LoginActivity extends AppCompatActivity {

    private ImageView logo, ivSignIn, btnTwitter;
    private AutoCompleteTextView email, password;
    private TextView forgotPass, signUp;
    private Button btnSignIn;
    private FirebaseAuth firebaseAuth, mauth;
    private FirebaseUser user;
    private ProgressDialog pbar;

    private CircleImageView google, fb;
    GoogleSignInClient mgooglesinginclint;
    private CallbackManager mCallbackManager;
    DatabaseReference root;
    private int resume = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        initializeGUI();

        user = firebaseAuth.getCurrentUser();
        root = FirebaseDatabase.getInstance().getReference();

        if (user != null) {
            Toasty.info(getApplicationContext(), "Account Logout Successfully \n Login Again to Continue..", 7000, true).show();
            firebaseAuth.signOut();
        }

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String inEmail = email.getText().toString();
                String inPassword = password.getText().toString();

                if (validateInput(inEmail, inPassword)) {
                    signUser(inEmail, inPassword);
                }

            }
        });


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, PWresetActivity.class));
            }
        });

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbar.show();
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
                mgooglesinginclint = GoogleSignIn.getClient(LoginActivity.this, gso);
                signIn();
            }
        });
        mCallbackManager = CallbackManager.Factory.create();

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbar.show();
                LoginManager.getInstance()
                        .logInWithReadPermissions(LoginActivity.this, Arrays.asList("email", "public_profile"));
                LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("TAG", "facebook:onSuccess:" + loginResult);
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.d("TAG", "facebook:onCancel");
                        Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_LONG).show();
                        pbar.dismiss();
                        // ...
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d("TAG", "facebook:onError", error);
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        pbar.dismiss();
                        // ...
                    }

                });
            }

        });


    }

    private void signIn() {
        Intent signInIntent = mgooglesinginclint.getSignInIntent();
        startActivityForResult(signInIntent, 101);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mauth.signInWithCredential(credential)
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
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void checkUser() {

        root.child("users").child("customer").child("registered").child("detail").
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(firebaseAuth.getUid())) {
                            sendUser();
                        } else {
                            pbar.dismiss();
                            mauth.signOut();
                            Toasty.error(LoginActivity.this, "User Does not Exist With This Credential ", 7000, true).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        pbar.dismiss();

                        Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

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
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                // ...
            }
        } else
            mCallbackManager.onActivityResult(requestCode, resultCode, data);


    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void sendUser() {
        String devide_token = FirebaseInstanceId.getInstance().getToken();
        root.child("users").child("customer").child("registered").
                child("detail").child(firebaseAuth.getUid()).child("token").setValue(devide_token);
        root.child("users").child("customer").child("registered").
                child("detail").child(firebaseAuth.getUid()).child("lastlogin").setValue(Date.getDate());
        root.child("users").child("customer").child("registered").
                child("detail").child(firebaseAuth.getUid()) .child("device").setValue(android.os.Build.MODEL);
        final DatabaseReference update_usertype = root.child("checkuser");

        final DatabaseReference update = root.child("users").child("agent");

        update.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(firebaseAuth.getUid())) {
                    update_usertype.child(firebaseAuth.getUid()).setValue("agent");
                    SharedPreferenceValue sharedPreferenceValue = new SharedPreferenceValue(LoginActivity.this);
                    sharedPreferenceValue.updateLoginValue("agent");
                    pbar.dismiss();
                    Toasty.success(getApplicationContext(), "Login Successful", 5000, true).show();
                    finish();
                    startActivity(new Intent(getApplicationContext(), AgentMainActivity.class));


                } else {
                    update_usertype.child(firebaseAuth.getUid()).setValue("customer");
                    SharedPreferenceValue sharedPreferenceValue = new SharedPreferenceValue(LoginActivity.this);
                    sharedPreferenceValue.updateLoginValue("customer");
                    pbar.dismiss();
                    Toasty.success(getApplicationContext(), "Login Successful", Toasty.LENGTH_LONG, true).show();
                    finish();
                    startActivity(new Intent(getApplicationContext(), CustHomePage.class));


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pbar.dismiss();
                Toasty.warning(getApplicationContext(), databaseError.getMessage(), Toasty.LENGTH_LONG, true).show();

            }
        });


    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void signUser(String email, String password) {

        pbar.setMessage("Validating...");
        pbar.show();

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String devide_token = FirebaseInstanceId.getInstance().getToken();
                    root.child("users").child("customer").child("registered").
                            child("detail").child(firebaseAuth.getUid()).child("token").setValue(devide_token);
                    final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    assert firebaseUser != null;
                    if (firebaseUser.isEmailVerified()) {
                        sendUser();
                    } else {
                        pbar.dismiss();

                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setCancelable(false);
                        builder.setIcon(R.drawable.logo);
                        builder.setTitle(R.string.app_name);
                        builder.setMessage("Your Email is not Verified\nVerify Your Email to Login.");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                firebaseAuth.signOut();
                                dialog.dismiss();
                            }
                        }).setNegativeButton("Verify E-Mail Now", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                firebaseAuth.signOut();
                                resume = 1;
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
                        }).setNeutralButton("Send Verification Mail Again", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toasty.success(getApplicationContext(), "Verification Mail sent Successful", Toasty.LENGTH_LONG, true).show();
                                            firebaseAuth.signOut();
                                            resume = 1;
                                        }

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toasty.error(getApplicationContext(), e.getMessage(), Toasty.LENGTH_LONG, true).show();
                                        pbar.dismiss();
                                        firebaseAuth.signOut();
                                    }
                                });
                            }
                        });
                        builder.create().show();

                    }
                } else {
                    pbar.dismiss();
                    Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void initializeGUI() {

        logo = findViewById(R.id.ivLogLogo);
        ivSignIn = findViewById(R.id.ivSignIn);
        google = findViewById(R.id.google);
        email = findViewById(R.id.atvEmailLog);
        password = findViewById(R.id.atvPasswordLog);
        forgotPass = findViewById(R.id.tvForgotPass);
        signUp = findViewById(R.id.tvSignIn);
        btnSignIn = findViewById(R.id.btnSignIn);
        fb = findViewById(R.id.fb);
        pbar = new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);
        pbar.setMessage("Please Wait..");

        mauth = FirebaseAuth.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("TAG", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mauth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            checkUser();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pbar.dismiss();
                mauth.signOut();
                Toasty.error(LoginActivity.this, e.getMessage(), Toasty.LENGTH_LONG, true).show();
            }
        });
    }


    public boolean validateInput(String inemail, String inpassword) {

        if (inemail.isEmpty()) {
            email.setError("Email field is empty.");
            return false;
        }
        if (inpassword.isEmpty() || inpassword.length() < 6) {
            password.setError("Password is too short.");
            return false;
        }

        return true;
    }


}
