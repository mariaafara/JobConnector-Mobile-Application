package com.example.abo8a.jobconnector.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.abo8a.jobconnector.AppSingleton;
import com.example.abo8a.jobconnector.Company.CompanyMainActivity;
import com.example.abo8a.jobconnector.CompanyTypeRegisration;
import com.example.abo8a.jobconnector.CompleteRegistration;
import com.example.abo8a.jobconnector.Notification.Constants;
import com.example.abo8a.jobconnector.R;
import com.example.abo8a.jobconnector.chat.util.data.SharedPreferenceHelper;
import com.example.abo8a.jobconnector.chat.util.data.StaticConfig;
import com.example.abo8a.jobconnector.chat.util.model.User;
import com.example.abo8a.jobconnector.chat.util.service.ServiceUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    public static boolean shouldFinish = false;
    public static String username;
    public static boolean finishNow = false;

    private String sharedEmail;
    private String sharedPassword;

    private Button Backbtn;
    private static final int REQUEST_SIGNUP = 0;

    public static SharedPreferences sharedPref;
    public static SharedPreferences.Editor editor;


    private AuthUtils authUtils;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;


    private EditText _emailText;
    private EditText _passwordText;
    private Button _loginButton;
    private TextView _signupLink;
    //-----------------connections---
    private static final String TAG = "LoginActivity";
    private static final String URL_FOR_LOGIN = "https://sloppier-citizens.000webhostapp.com/job/logins.php";
    private static final String URL_FOR_LOGINcompany = "https://sloppier-citizens.000webhostapp.com/company/logins.php";
    ProgressDialog progressDialog;

    public static String comefrom;
    public static int nbr_active_posts;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        initFirebase();
        setContentView(R.layout.activity_login);
        Intent in = getIntent();
        comefrom = in.getStringExtra("comefrom");
        sharedPref = getSharedPreferences("check", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        Backbtn=findViewById(R.id.btn_login_back);
        Backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnBoardingActivity.editor.putBoolean("signIn", false).commit();
                startActivity(new Intent(LoginActivity.this,OnBoardingActivity.class));
                finish();
            }
        });

        if (comefrom == null) {
            System.out.println("+++++++++++++\n+++++++++++++++ Iam paased logIn");

            if (sharedPref.getString("comeFrom", "").equals("findjob")) {
                comefrom="findjob";
                authUtils.signIn(sharedPref.getString("email", ""), sharedPref.getString("password", ""));
                Intent intent;
                if (!sharedPref.getBoolean("foundBasic", false)) {
//                    intent = new Intent(
//                            LoginActivity.this,
//                            CompleteRegistration.class);
                } else {
                    intent = new Intent(
                            LoginActivity.this,
                            com.example.abo8a.jobconnector.user.class);
                    username = sharedPref.getString("username", "");
                    intent.putExtra("user", username);
                    intent.putExtra("id", sharedPref.getInt("eid", 0));
                    intent.putExtra("email", sharedPref.getString("email", ""));
                    startActivity(intent);
                }


            } else {

                comefrom="hire";
                Intent intent;
                if (sharedPref.getBoolean("foundBasic", false)) {
                    authUtils.signIn(sharedPref.getString("email", ""), sharedPref.getString("password", ""));

                    System.out.println("+++++++++++ from login the saved email and password are "+sharedPref.getString("email", "")+" ++ "+sharedPref.getString("password", ""));

                    username = sharedPref.getString("username", "");

                    intent = new Intent(LoginActivity.this, CompanyMainActivity.class);
                    intent.putExtra("user", username);
                    intent.putExtra("cid", sharedPref.getInt("cid", 0));
                    intent.putExtra("nbr_active_posts", sharedPref.getInt("nbr_active_posts", 0));
                    intent.putExtra("email", sharedPref.getString("email", ""));
                    intent.putExtra("password",sharedPref.getString("password", ""));
                    startActivity(intent);
                }
                else
                {
                    username = sharedPref.getString("username", "");

                    intent = new Intent(LoginActivity.this, CompanyTypeRegisration.class);

                    intent.putExtra("user", username);
                    intent.putExtra("email",sharedPref.getString("email", ""));
                    intent.putExtra("cid",sharedPref.getInt("cid", 0));
                    startActivity(intent);
                }


            }


        } else {

            editor.putString("comeFrom", comefrom);
            System.out.println("+++++++++ come from " + comefrom + "\n+++++++");
            editor.commit();

        }

//
//
//        if(comefrom==null)
//            finish();


        sharedEmail = sharedPref.getString("email", "");
        sharedPassword = sharedPref.getString("password", "");


        _emailText = findViewById(R.id.input_email);
        _passwordText = findViewById(R.id.input_password);
        _loginButton = findViewById(R.id.btn_login);
        _signupLink = findViewById(R.id.link_signup);
        _emailText.clearFocus();
        _emailText.setText(sharedEmail);
        _passwordText.setText(sharedPassword);
        _passwordText.clearFocus();


        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                if (comefrom.equals("hire")) {


                    intent.putExtra("comefrom", "hire");
                    startActivityForResult(intent, REQUEST_SIGNUP);
                    finish();
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

                } else

                    //iza jey men kabset lfind job
                    if (comefrom.equals("findjob")) {

                        intent.putExtra("comefrom", "findjob");
                        startActivityForResult(intent, REQUEST_SIGNUP);
                        finish();
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

                    }


            }
        });


        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validate()) {
                    onLoginFailed();
                    return;
                }
                if (comefrom.equals("hire")) {
                    loginCompany(_emailText.getText().toString(),
                            _passwordText.getText().toString());


                } else

                    //iza jey men kabset lfind job
                    if (comefrom.equals("findjob")) {

                        loginUser(_emailText.getText().toString(),
                                _passwordText.getText().toString());

                    }


            }
        });
    }

    private void loginCompany(final String email, final String password) {
        // Tag used to cancel the request
        String cancel_req_tag = "login";
        progressDialog.setMessage("Logging you in...");
        authUtils.signIn(email, password);
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_FOR_LOGINcompany, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {

                    System.out.println("+++++++++++++" + response);

                    JSONObject jObj = new JSONObject(response);


                    boolean success = jObj.getBoolean("success");

                    if (success) {
                        OnBoardingActivity.editor.putBoolean("signIn", true);
                        OnBoardingActivity.editor.commit();

                        String users = jObj.getString("username");
                        username = users;
                        editor.putString("username", username);
                        editor.putInt("cid", Integer.parseInt(jObj.getString("id")));
                        editor.putString("email", _emailText.getText().toString());
                        editor.putString("password", _passwordText.getText().toString());
                        boolean foundBasic = jObj.getBoolean("foundBasic");
                        editor.putBoolean("foundBasic", foundBasic);
                        editor.putInt("nbr_active_posts", jObj.getInt("nbr_active_posts"));
                        editor.commit();

                        // Launch User activity
                        Intent intent;

                        if (foundBasic) {
                            intent = new Intent(LoginActivity.this, CompanyMainActivity.class);
                            intent.putExtra("cid", Integer.parseInt(jObj.getString("id")));
                            intent.putExtra("nbr_active_posts", jObj.getInt("nbr_active_posts"));
                            intent.putExtra("user",username);
                            intent.putExtra("email", jObj.getString("email"));
                            startActivity(intent);
                        }
                        else
                        {
                            // Launch type of  comapny to determine registration forms
                            intent = new Intent(LoginActivity.this, CompanyTypeRegisration.class);

                            intent.putExtra("user", username);
                            intent.putExtra("email",email);
                            intent.putExtra("cid", jObj.getInt("id"));
                            startActivity(intent);

                        }
                        //    intent.putExtra("user", user);

                        finish();
                    } else {


                        Toast.makeText(getApplicationContext(),
                                "wrong Username or Password", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
        //hon st5dmo fiya l volley
        //request w add la queu wst5dm fiya lvolly
    }

    private void loginUser(final String email, final String password) {
        // Tag used to cancel the request
        String cancel_req_tag = "login";
        authUtils.signIn(email, password);
        progressDialog.setMessage("Logging you in...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_FOR_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {

                    System.out.println("+++++++++++++" + response);

                    JSONObject jObj = new JSONObject(response);


                    boolean success = jObj.getBoolean("success");

                    if (success) {
                        OnBoardingActivity.editor.putBoolean("signIn", true);
                        OnBoardingActivity.editor.commit();


                        String users = jObj.getString("username");

                        username = users;
                        editor.putString("username", username);
                        editor.putInt("eid", Integer.parseInt(jObj.getString("id")));
                        editor.putString("email", _emailText.getText().toString());
                        editor.putString("password", _passwordText.getText().toString());
                        boolean foundBasic = jObj.getBoolean("foundBasic");
                        editor.putBoolean("foundBasic", foundBasic);
                        System.out.println("++++++++++++ editor in login " + editor.toString());
                        editor.commit();


                        // Launch User activity
                        Intent intent;
                        if (!foundBasic) {
                            intent = new Intent(
                                    LoginActivity.this,
                                    CompleteRegistration.class);
                        } else {
                            intent = new Intent(
                                    LoginActivity.this,
                                    com.example.abo8a.jobconnector.user.class);

                        }
                        intent.putExtra("user", users);
                        intent.putExtra("id", Integer.parseInt(jObj.getString("id")));
                        intent.putExtra("email", jObj.getString("email"));
                        startActivity(intent);
                        finish();

                    } else {


                        Toast.makeText(getApplicationContext(),
                                "wrong Username or Password", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "No Internet Connection!!\nPlease Reconnect to Internet and Try Again...", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                params.put("comefrom", "findjob");
                return params;
            }

        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
        //hon st5dmo fiya l volley
        //request w add la queu wst5dm fiya lvolly
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

//    public void login() {
//        Log.d(TAG, "Login");
//
//        if (!validate()) {
//            onLoginFailed();
//            return;
//        }
//
//        _loginButton.setEnabled(false);
//
//        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
//                R.style.AppTheme_Dark_Dialog);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setMessage("Authenticating...");
//        progressDialog.show();
//
//        // TODO: Implement your own authentication logic here.
//
//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        // On complete call either onLoginSuccess or onLoginFailed
//                        onLoginSuccess();
//                        // onLoginFailed();
//                        progressDialog.dismiss();
//                    }
//                }, 3000);
//    }




    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override


            public void run() {
                doubleBackToExitPressedOnce = false;
            }

        }, 2000);
    }


    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 10) {
            _passwordText.setError("between 6 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    public class AuthUtils {

        void createUser(String email, String password) {

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                            System.out.println("fireBaseAuth::::createUserWithEmail:onComplete:" + task.isSuccessful());
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {

                            } else {
                                initNewUserInfo();
                                saveUserInfo();


                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    })
            ;
        }



        void signIn(final String email, final String password) {

            System.out.println("+++++++++++++ inSignin the email is "+email);
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());


                            if (!task.isSuccessful()) {
                                ////////////////////


                                authUtils.createUser(email, password);

                                ///////////////////////////
                                Log.w(TAG, "signInWithEmail:failed", task.getException());


                            } else {
                                System.out.println( "+++++++++++++++++ the task in login Activity is "+task.getResult().toString());
                                saveUserInfo();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
        }



        void saveUserInfo() {
            System.out.println("+++++++++++++ Uid in saveUserInfo in login is "+StaticConfig.UID);
            FirebaseDatabase.getInstance().getReference().child("user/" + StaticConfig.UID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getValue() == null)
                        initNewUserInfo();
                    else {

                        HashMap hashUser = (HashMap) dataSnapshot.getValue();
                        User userInfo = new User();
                        userInfo.name = (String) hashUser.get("name");
                        userInfo.email = (String) hashUser.get("email");
                        userInfo.avata = (String) hashUser.get("avata");
                        userInfo.token = (String) FirebaseInstanceId.getInstance().getToken();
                        userInfo.status.isOnline=true;

                        System.out.println("++++ the token that is saved is " + userInfo.token);

                        updateFirebaseToken(StaticConfig.UID, userInfo.token);
                        ServiceUtils.updateUserStatus(LoginActivity.this,true);

                        SharedPreferenceHelper.getInstance(LoginActivity.this).saveUserInfo(userInfo);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


        /**
         * Khoi tao thong tin mac dinh cho tai khoan moi
         */
        void initNewUserInfo() {
            User newUser = new User();
            newUser.email = user.getEmail();
            newUser.name = user.getEmail().substring(0, user.getEmail().indexOf("@"));
            newUser.avata = StaticConfig.STR_DEFAULT_BASE64;
            newUser.token = (String) FirebaseInstanceId.getInstance().getToken();
            newUser.status.isOnline=true;
            FirebaseDatabase.getInstance().getReference().child("user/" + user.getUid()).setValue(newUser);
        }
    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        authUtils = new AuthUtils();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    StaticConfig.UID = user.getUid();
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                } else {

                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }

            }
        };


    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void updateFirebaseToken(String uid, String token) {

        FirebaseDatabase.getInstance()
                .getReference()
                .child(Constants.ARG_USERS)
                .child(uid)
                .child(Constants.ARG_FIREBASE_TOKEN)
                .setValue(token);
    }

    @Override
    protected void onResume() {

        if (shouldFinish) {

            startActivity(new Intent(this, OnBoardingActivity.class));
            finish();

        }
        if (finishNow)
            finish();
        super.onResume();
    }
}
