package com.example.abo8a.jobconnector;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.abo8a.jobconnector.Company.CompanyRegistration;
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

import java.util.HashMap;

public class CompanyTypeRegisration extends AppCompatActivity {
    private Button btn_continue;
    private RadioButton comp;
    private RadioButton indiv;
    private int cid;
    private AuthUtils authUtils;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_type_regisration);
        Intent in = getIntent();   //intent for come from
        cid = in.getIntExtra("cid", -1);//to know signing up from hich button


        btn_continue = (Button) findViewById(R.id.btn_Continue);
        comp = (RadioButton) findViewById(R.id.btn_Company);
        initFirebase();

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (comp.isChecked()) {
                    Intent k = new Intent(CompanyTypeRegisration.this, CompanyRegistration.class);
                    k.putExtra("cid", cid);
                    k.putExtra("user",getIntent().getStringExtra("user"));
                    k.putExtra("email",getIntent().getStringExtra("email"));
                    k.putExtra("password",getIntent().getStringExtra("password"));
                    startActivity(k);
                }
                else
                    Toast.makeText(getApplicationContext(), "please select a type", Toast.LENGTH_SHORT).show();

            }

        });

        checkEmail(getIntent().getStringExtra("email"));

    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        authUtils = new AuthUtils();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                users = firebaseAuth.getCurrentUser();
                if (users != null) {
                    // User is signed in
                    StaticConfig.UID = users.getUid();
                    Log.d("User", "onAuthStateChanged:signed_in:" + users.getUid());

                } else {

                    Log.d("User", "onAuthStateChanged:signed_out");
                }

            }
        };


    }
    public class AuthUtils {


        void createUser(String email, String password) {

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(CompanyTypeRegisration.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d("User", "createUserWithEmail:onComplete:" + task.isSuccessful());
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
                    .addOnCompleteListener(CompanyTypeRegisration.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d("User", "signInWithEmail:onComplete:" + task.isSuccessful());


                            if (!task.isSuccessful()) {
                                ////////////////////


                                authUtils.createUser(email, password);

                                ///////////////////////////
                                Log.w("User", "signInWithEmail:failed", task.getException());


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
            System.out.println("+++++++++++++ Uid in saveUserInfo in login is "+ StaticConfig.UID);
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

                        ServiceUtils.updateUserStatus(CompanyTypeRegisration.this,true);

                        SharedPreferenceHelper.getInstance(CompanyTypeRegisration.this).saveUserInfo(userInfo);
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
            users=mAuth.getCurrentUser();
            User newUser = new User();
            newUser.email = users.getEmail();
            newUser.name = getIntent().getStringExtra("user");
            newUser.avata = StaticConfig.STR_DEFAULT_BASE64;
            newUser.token = (String) FirebaseInstanceId.getInstance().getToken();
            newUser.status.isOnline=true;
            StaticConfig.UID=users.getUid();
            FirebaseDatabase.getInstance().getReference().child("user/" + users.getUid()).setValue(newUser);
        }
    }

    private void checkEmail(final String email) {

        FirebaseDatabase.getInstance().getReference().child("user").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("+++++++ the email is "+email+"    +++   dataSnapShot is "+dataSnapshot.toString());
                if(dataSnapshot.getValue()==null)
                    authUtils.initNewUserInfo();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
