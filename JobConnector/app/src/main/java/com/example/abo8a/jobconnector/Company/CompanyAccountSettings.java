package com.example.abo8a.jobconnector.Company;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.abo8a.jobconnector.Notification.Constants;
import com.example.abo8a.jobconnector.R;
import com.example.abo8a.jobconnector.chat.util.data.StaticConfig;
import com.example.abo8a.jobconnector.login.LoginActivity;
import com.example.abo8a.jobconnector.login.OnBoardingActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CompanyAccountSettings extends AppCompatActivity {
    private ListView setting;
    int cid;
    EditText oldpass;
    EditText newpass;
    String emailold;
    EditText emailnew;
    EditText usernameET;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        init();
        cid = CompanyMainActivity.cid;
        emailold = CompanyMainActivity.email;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
            default:
                break;
        }
        return true;
    }

    public void init() {


        String options[] = {"Change Email", "Change Password","Change Username", "Close Account"};
        setting = findViewById(R.id.accoutsettings_listview);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options);
        setting.setAdapter(arrayAdapter);

        setting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // intent = new Intent(LanguageLevel.this, EmployeeProfile.class);

                if (position == 0) {//change email
                    showChangEmailDialog();
                } else if (position == 1) {//change password
                    showChangPpasswordDialog();
                } else if (position == 2) {//change Username
                    showChangUsernameDialog();
                }
                else if (position == 3) {//close Account


                    closeAccount();

                }

            }
        });
    }

    public void showChangUsernameDialog() {

        final Dialog Dialog = new Dialog(CompanyAccountSettings.this, R.style.Theme_Dialog);
        Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Window window = npDialog.getWindow();
        //    Dialog.setTitle("level");
        Dialog.setContentView(R.layout.dialog_change_username);
        Button submit = (Button) Dialog.findViewById(R.id.btn_submit_changepassword);
        usernameET = Dialog.findViewById(R.id.newUsername);
        usernameET.setText(CompanyMainActivity.companyName);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                if(usernameET.getText().toString().length()>2) {
                    updateUsername();

                    Dialog.dismiss();
                }

            }
        });

        Dialog.show();
    }

    private void updateUsername() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://sloppier-citizens.000webhostapp.com/company/changeUsername.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    System.out.println("++++++++" + response);
                    JSONObject json = new JSONObject(response);
                    if (json.getBoolean("success")) {
                        updateNameFirebase(usernameET.getText().toString());
                        CompanyMainActivity.companyName=usernameET.getText().toString();
                        LoginActivity.editor.putString("username", usernameET.getText().toString()).commit();
                        Toast.makeText(getApplicationContext(), "updated Succesfully ", Toast.LENGTH_SHORT).show();
                    } else {
                        System.out.println("success not 1 ---------------------");

                        Toast.makeText(getApplicationContext(), "Failed to Update,Check your Password and try again!!", Toast.LENGTH_SHORT).show();


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("Json exception--------------------");
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("jobSearch", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Check your Internet Connection and try Again", Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();


                params.put("cid", cid + "");
                params.put("new", usernameET.getText().toString());


                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);


    }

    public void showChangPpasswordDialog() {

        final Dialog Dialog = new Dialog(CompanyAccountSettings.this, R.style.Theme_Dialog);
        Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Window window = npDialog.getWindow();
        //    Dialog.setTitle("level");
        Dialog.setContentView(R.layout.dialog_changepassword);
        Button submit = (Button) Dialog.findViewById(R.id.btn_submit_changepassword);
        oldpass = Dialog.findViewById(R.id.oldpassword);
        newpass = Dialog.findViewById(R.id.newpassword);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if(validatepassword()) {
                    updatePassword();

                    Dialog.dismiss();
                }


            }
        });

        Dialog.show();
    }

    private void updatePassword() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://sloppier-citizens.000webhostapp.com/company/changePassword.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    System.out.println("++++++++" + response);
                    JSONObject json = new JSONObject(response);
                    if (json.getBoolean("success")) {
                        updateFirebasePassword();
                        LoginActivity.editor.putString("password", newpass.getText().toString()).commit();
                        Toast.makeText(getApplicationContext(), "updated Succesfully ", Toast.LENGTH_SHORT).show();
                    } else {
                        System.out.println("success not 1 ---------------------");

                        Toast.makeText(getApplicationContext(), "Failed to Update,Check your Password and try again!!", Toast.LENGTH_SHORT).show();


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("Json exception--------------------");
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("jobSearch", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Check your Internet Connection and try Again", Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();


                params.put("cid", cid + "");// etsearch.getText().toString()
                params.put("new", newpass.getText().toString());
                params.put("old", oldpass.getText().toString());

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);


    }

    private void updateEmail() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://sloppier-citizens.000webhostapp.com/company/changeEmail.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    System.out.println("++++++++" + response);
                    JSONObject json = new JSONObject(response);
                    if (json.getBoolean("success")) {
                        updateFirebaseEmail();
                        CompanyMainActivity.email=emailnew.getText().toString();
                        LoginActivity.editor.putString("email", emailnew.getText().toString()).commit();
                        Toast.makeText(getApplicationContext(), "updated Succesfully ", Toast.LENGTH_SHORT).show();
                    } else {
                        System.out.println("success not 1 ---------------------");

                        Toast.makeText(getApplicationContext(), "Failed to Update!!", Toast.LENGTH_SHORT).show();


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("Json exception--------------------");
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("jobSearch", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Check your Internet Connection and try Again", Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();


                params.put("cid", cid + "");// etsearch.getText().toString()
                params.put("new", emailnew.getText().toString());
                params.put("old", emailold);

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);


    }

    public void showChangEmailDialog() {

        final Dialog Dialog = new Dialog(CompanyAccountSettings.this, R.style.Theme_Dialog);
        Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Dialog.setContentView(R.layout.dialog_changemail);
        Button submit = (Button) Dialog.findViewById(R.id.btn_submit_changeemail);
        emailnew = Dialog.findViewById(R.id.changed_email);
        emailnew.setText(emailold);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if(validateemail()) {
                    updateEmail();
                    Dialog.dismiss();
                }

            }
        });

        Dialog.show();
    }
    public boolean validatepassword()
    {
        boolean valid = true;

        String old = oldpass.getText().toString();
        String newpas = newpass.getText().toString();


        if (old.length() <6 ) {
            oldpass.setError("6 characters at least");
            valid = false;
        }
        if ( newpas.length()<6) {
            newpass.setError("6 characters at least");
            valid = false;
        }



        return valid;
    }
    public boolean validateemail()
    {
        boolean valid=true;
        String email=emailnew.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailnew.setError("enter a valid email address");
            valid = false;
        }
        return valid;
    }

    private void updateFirebasePassword() {

        final FirebaseUser user;
        user = FirebaseAuth.getInstance().getCurrentUser();
        final String email = user.getEmail();

        AuthCredential credential = EmailAuthProvider.getCredential(email,oldpass.getText().toString());

        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    user.updatePassword(newpass.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(!task.isSuccessful()){
                                //Toast.makeText(AccountSettings.this, "Something went wrong. Please try again later", Toast.LENGTH_SHORT).show();
                            }else {
                                // Toast.makeText(AccountSettings.this, "Password successfully modified", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }else {
                    // Toast.makeText(AccountSettings.this, "Athentication Failed", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


    private void updateFirebaseEmail() {

        final FirebaseUser user;
        user = FirebaseAuth.getInstance().getCurrentUser();
        final String email = user.getEmail();

        user.updateEmail(emailnew.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(CompanyAccountSettings.this, "Something went wrong. Please try again later", Toast.LENGTH_SHORT).show();
                }else {
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child(Constants.ARG_USERS)
                            .child(StaticConfig.UID)
                            .child("email")
                            .setValue(emailnew.getText().toString());
                    Toast.makeText(CompanyAccountSettings.this, "Email successfully modified", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }
    private void closeAccount() {

        new AlertDialog.Builder(CompanyAccountSettings.this)
                .setTitle("Alert!")
                .setMessage("Are you sure you want to close Your Account Permanently!!\nclicking ok will delete all your posts and information...")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteAccountfireBase();

                        deletAcountHost();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).show();






    }
    private void deleteAccountfireBase() {
        Log.d("CompanyAccount Setting", "deleteAccount");
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        currentUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    System.out.println("+++++++++++++++ account deleted from firebase");
                } else {
                    Log.w("Company Account Setting","Something is wrong!");
                }
            }
        });
    }
    private void deletAcountHost(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://sloppier-citizens.000webhostapp.com/company/closeAccount.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    System.out.println("++++++++" + response);
                    JSONObject json = new JSONObject(response);
                    if (json.getBoolean("success")) {
                        OnBoardingActivity.editor.putBoolean("signIn",false).commit();
                        startActivity(new Intent(CompanyAccountSettings.this, OnBoardingActivity.class));
                        Toast.makeText(getApplicationContext(), "Account closed Succesfully ", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        System.out.println("success not 1 ---------------------");

                        Toast.makeText(getApplicationContext(), "Failed to close!!", Toast.LENGTH_SHORT).show();


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("Json exception--------------------");
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("jobSearch", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Check your Internet Connection and try Again", Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();


                params.put("id", cid + "");


                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);

    }
    private void updateNameFirebase(String name) {

        FirebaseDatabase.getInstance()
                .getReference()
                .child(Constants.ARG_USERS)
                .child(StaticConfig.UID)
                .child("name")
                .setValue(name);
    }

}
