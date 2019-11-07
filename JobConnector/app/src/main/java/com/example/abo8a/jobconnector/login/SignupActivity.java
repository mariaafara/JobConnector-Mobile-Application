package com.example.abo8a.jobconnector.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
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
import com.example.abo8a.jobconnector.CompanyTypeRegisration;
import com.example.abo8a.jobconnector.CompleteRegistration;
import com.example.abo8a.jobconnector.R;
import com.example.abo8a.jobconnector.chat.util.data.StaticConfig;
import com.example.abo8a.jobconnector.chat.util.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    private EditText _nameText;
    private EditText _emailText;
    EditText _passwordText;
    EditText _reEnterPasswordText;
    private Button _signupButton;
    private TextView _loginLink;


    private AuthUtils authUtils;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;


    private static final String URL_FOR_REGISTRATION = "https://sloppier-citizens.000webhostapp.com/job/register.php";
    private static final String URL_FOR_REGISTRATIONCompany = "https://sloppier-citizens.000webhostapp.com/company/register.php";


    ProgressDialog progressDialog;

    //intent for come from
    String comefrom;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initFirebase();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        _nameText = findViewById(R.id.input_name);
        _signupButton = findViewById(R.id.btn_signup);
        _emailText = findViewById(R.id.input_email);
        _loginLink = findViewById(R.id.link_login);
        _passwordText = findViewById(R.id.input_password);
        _reEnterPasswordText = findViewById(R.id.input_reEnterPassword);

        Intent in = getIntent();   //intent for come from
        comefrom = in.getStringExtra("comefrom");//to know signing up from hich button


        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (comefrom.equals("hire")) {
                    signupCompany();

                } else if (comefrom.equals("findjob")) {
                    signupemployee();
                }

            }
        });


        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);

                if (comefrom.equals("hire")) {
                    intent.putExtra("comefrom", "hire");

                } else if (comefrom.equals("findjob")) {
                    intent.putExtra("comefrom", "findjob");
                }
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void signupCompany() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        showDialog();


        // TODO: Implement your own signup logic here.
/************hon fiye zid login fb or google or whatever*************/

//register employee
        registerCompany(_nameText.getText().toString(),
                _emailText.getText().toString(),
                _passwordText.getText().toString());


        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        hideDialog();
                    }
                }, 2000);
    }
    public void signupemployee() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        showDialog();


        // TODO: Implement your own signup logic here.
/************hon fiye zid login fb or google or whatever*************/

//register employee
        registerUser(_nameText.getText().toString(),
                _emailText.getText().toString(),
                _passwordText.getText().toString());


        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        hideDialog();
                    }
                }, 2000);
    }


    //register employee
    private void registerCompany(final String name, final String email, final String password) {
        // Tag used to cancel the request
        String cancel_req_tag = "register";

        progressDialog.setMessage("Adding you ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_FOR_REGISTRATIONCompany, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());


                try {
                    System.out.println("+++++++++++++" + response);
                    hideDialog();
                    JSONObject jObj = new JSONObject(response);
                    boolean success = jObj.getBoolean("success");


                    if (success) {

                        String user = jObj.getString("username");


                        OnBoardingActivity.editor.putBoolean("signIn", true);
                        OnBoardingActivity.editor.commit();

                        LoginActivity.editor.putString("username", name);
                        LoginActivity.editor.putInt("cid",  jObj.getInt("id"));
                        LoginActivity.editor.putString("email", email);
                        LoginActivity.editor.putString("password", password);
                        LoginActivity.editor.putBoolean("foundBasic", false);
                        LoginActivity.editor.putInt("nbr_active_posts", 0);
                        LoginActivity.editor.commit();

                        authUtils.createUser(email, password);
                        Toast.makeText(getApplicationContext(), "Hi " + user + ", You are successfully Added!", Toast.LENGTH_SHORT).show();

                        // Launch type of  comapny to determine registration forms
                        Intent intent = new Intent(SignupActivity.this, CompanyTypeRegisration.class);

                        intent.putExtra("user", name);
                        intent.putExtra("email",email);
                        intent.putExtra("cid", jObj.getInt("id"));
                        startActivity(intent);
                        finish();
                    } else {


                        Toast.makeText(getApplicationContext(),
                                "Failed to add", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("\ncatch a jason exception  in request\n");
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", name);
                params.put("email", email);
                params.put("password", password);
                // params.put("gender", gender);

                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }


    //register employee
    private void registerUser(final String name, final String email, final String password) {
        // Tag used to cancel the request
        String cancel_req_tag = "register";

        progressDialog.setMessage("Adding you ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_FOR_REGISTRATION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());


                try {
                    System.out.println("+++++++++++++" + response);
                    hideDialog();
                    JSONObject jObj = new JSONObject(response);
                    boolean success = jObj.getBoolean("success");


                    if (success) {



                        OnBoardingActivity.editor.putBoolean("signIn", true);
                        OnBoardingActivity.editor.commit();

                        LoginActivity.editor.putString("username", name);
                        LoginActivity.editor.putInt("eid",  jObj.getInt("id"));
                        LoginActivity.editor.putString("email", email);
                        LoginActivity.editor.putString("password", password);
                        LoginActivity.editor.putBoolean("foundBasic", false);
                        LoginActivity.editor.commit();


                        authUtils.createUser(email, password);
                        String user = jObj.getString("username");
                        Toast.makeText(getApplicationContext(), "Hi " + user + ", You are successfully Added!", Toast.LENGTH_SHORT).show();

                        // Launch login activity
                        Intent intent = new Intent(
                                SignupActivity.this,
                                CompleteRegistration.class);
                        intent.putExtra("user", name);
                        intent.putExtra("email",email);
                        intent.putExtra("id", jObj.getInt("id"));
                        startActivity(intent);
                        finish();
                    } else {


                        Toast.makeText(getApplicationContext(),
                                "Failed to add", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("\ncatch a jason exception  in request\n");
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", name);
                params.put("email", email);
                params.put("password", password);
                // params.put("gender", gender);

                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();

        String email = _emailText.getText().toString();

        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }
//
//        if (mobile.isEmpty() || mobile.length() != 10) {
//            _mobileText.setError("Enter Valid Mobile Number");
//            valid = false;
//        } else {
//            _mobileText.setError(null);
//        }

        if (password.isEmpty() || password.length() < 6 ) {
            _passwordText.setError("password must be > 6 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 6 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;
    }

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
    private void initFirebase() {
        //Khoi tao thanh phan de dang nhap, dang ky
        mAuth = FirebaseAuth.getInstance();
        authUtils = new AuthUtils();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    StaticConfig.UID = user.getUid();
                    System.out.println("++++++++++++ the Uid in signUp is "+user.getUid());
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                } else {
                    System.out.println("++++++++++++ the Uid in signUp is null");

                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }

            }
        };


    }


    public class AuthUtils {

        void createUser(String email, String password) {

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                            System.out.println("fireBaseAuth::::createUserWithEmail:onComplete:" + task.isSuccessful());
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                //Toast.makeText(SignupActivity.this,"firebase regestration failed",Toast.LENGTH_SHORT).show();
                            } else {
                                initNewUserInfo();

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

        void initNewUserInfo() {
            User newUser = new User();
            newUser.email = _emailText.getText().toString();
            newUser.name = _nameText.getText().toString();
            newUser.avata = StaticConfig.STR_DEFAULT_BASE64;
            newUser.status.isOnline=true;
            newUser.token=(String) FirebaseInstanceId.getInstance().getToken();

            FirebaseDatabase.getInstance().getReference().child("user/" + user.getUid()).setValue(newUser);
        }
    }

}