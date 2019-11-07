package com.example.abo8a.jobconnector;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.abo8a.jobconnector.Notification.Constants;
import com.example.abo8a.jobconnector.chat.util.data.SharedPreferenceHelper;
import com.example.abo8a.jobconnector.chat.util.data.StaticConfig;
import com.example.abo8a.jobconnector.chat.util.model.User;
import com.example.abo8a.jobconnector.chat.util.service.ServiceUtils;
import com.example.abo8a.jobconnector.login.LoginActivity;
import com.example.abo8a.jobconnector.login.OnBoardingActivity;
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

public class user extends AppCompatActivity {
    static String email;
    public static String user;
    public static int id;
    private BottomNavigationView bottomNavigationView;
    public static boolean visited = false;
    public static NonSwipeableViewPager viewPager;

    private AuthUtils authUtils;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser users;

    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //super.onResume();
        setContentView(R.layout.activity_user);

        i = getIntent();
        id = i.getIntExtra("id", -1);
        user=i.getStringExtra("user");
        email = i.getStringExtra("email");//3m jiba men l login activity


        System.out.println("++++++++++++++++ from user class id,user,email  ...   "+id+"   "+user+"    "+email);


        LoginActivity.finishNow=true;
        OnBoardingActivity.finishNow=true;
        init();

    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//    }

    private void init() {

        if(!isNetworkAvailable()) {
            new AlertDialog.Builder(this)
                    .setTitle("Alert")
                    .setCancelable(false)
                    .setMessage("Ooops!! No Internet Connection...")
                    .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            initFirebase();
                            authUtils.signIn(LoginActivity.sharedPref.getString("email", ""),LoginActivity.sharedPref.getString("password", ""));

                            init();


                        }


                    })
                    .setNegativeButton("quit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    }).show();
        }

        else {

            FirebaseDatabase.getInstance()
                    .getReference()
                    .child(Constants.ARG_USERS)
                    .child(StaticConfig.UID)
                    .child(Constants.ARG_FIREBASE_TOKEN)
                    .setValue((String) FirebaseInstanceId.getInstance().getToken());
            viewPager = (NonSwipeableViewPager) findViewById(R.id.content_user);
            UserViewPagerAdapter adapter = new UserViewPagerAdapter(user.this.getSupportFragmentManager());

            adapter.addFragment(new SearchFragment(), "title0");//0
            adapter.addFragment(new MyJobs(), "title1");//1
            adapter.addFragment(new CommentFragment(), "title2");//2
            adapter.addFragment(new EmployeeProfile(), "title3");//3
            adapter.addFragment(new MoreFragment(), "title4");//4

            viewPager.setAdapter(adapter);

            //**********************************************************************************///


            bottomNavigationView = findViewById(R.id._bottomNavigationView);

            if (i.getBooleanExtra("comming", false)) {
                viewPager.setCurrentItem(3);
                bottomNavigationView.setSelectedItemId(R.id._profileMenu);
            }


            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    if (item.getItemId() == R.id._searchMenu) {

                        viewPager.setCurrentItem(0);
                        return true;

                    }
                    if (item.getItemId() == R.id._savedMenu) {

                        viewPager.setCurrentItem(1);
                        return true;
                    }
                    if (item.getItemId() == R.id._ratingsMenu) {

                        viewPager.setCurrentItem(2);
                        return true;
                    }
                    if (item.getItemId() == R.id._profileMenu) {

                        viewPager.setCurrentItem(3);
                        return true;
                    }
                    if (item.getItemId() == R.id._moreMenu) {

                        viewPager.setCurrentItem(4);
                        return true;
                    }
                    return false;
                }
            });
        }
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

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateFirebaseStatus(StaticConfig.UID,false);

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void updateFirebaseStatus(String uid, boolean status) {

        FirebaseDatabase.getInstance()
                .getReference()
                .child(Constants.ARG_USERS)
                .child(uid)
                .child("status")
                .child("isOnline")
                .setValue(status);
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
                    .addOnCompleteListener(user.this, new OnCompleteListener<AuthResult>() {
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
                    .addOnCompleteListener(user.this, new OnCompleteListener<AuthResult>() {
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

                        ServiceUtils.updateUserStatus(user.this,true);

                        SharedPreferenceHelper.getInstance(user.this).saveUserInfo(userInfo);
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
            newUser.email = users.getEmail();
            newUser.name = users.getEmail().substring(0, users.getEmail().indexOf("@"));
            newUser.avata = StaticConfig.STR_DEFAULT_BASE64;
            newUser.token = (String) FirebaseInstanceId.getInstance().getToken();
            newUser.status.isOnline=true;
            FirebaseDatabase.getInstance().getReference().child("user/" + users.getUid()).setValue(newUser);
        }
    }

}
