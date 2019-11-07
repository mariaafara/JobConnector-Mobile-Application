package com.example.abo8a.jobconnector.Company;

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

import com.example.abo8a.jobconnector.NonSwipeableViewPager;
import com.example.abo8a.jobconnector.Notification.Constants;
import com.example.abo8a.jobconnector.R;
import com.example.abo8a.jobconnector.UserViewPagerAdapter;
import com.example.abo8a.jobconnector.chat.util.data.SharedPreferenceHelper;
import com.example.abo8a.jobconnector.chat.util.data.StaticConfig;
import com.example.abo8a.jobconnector.chat.util.model.User;
import com.example.abo8a.jobconnector.chat.util.service.ServiceUtils;
import com.example.abo8a.jobconnector.login.LoginActivity;
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

public class CompanyMainActivity extends AppCompatActivity {
    public static int cid;
    private BottomNavigationView bottomNavigationView;
    private NonSwipeableViewPager viewPager;
    static String email;
    public static int nbr_active_posts;
    public static String companyName="";
    public static Context context;
    private AuthUtils authUtils;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser users;
   public static String image="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_main);
        context=this;
        getImage(email);
        Intent i = getIntent();
        cid = i.getIntExtra("cid", -1);
        email = i.getStringExtra("email");
        nbr_active_posts = i.getIntExtra("nbr_active_posts", 0);


        init();
        checkAccount(email);
        updateFirebaseStatus(true);

    }

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
            fillFragments();

        }

    }


    public void fillFragments() {

        FirebaseDatabase.getInstance()
                .getReference()
                .child(Constants.ARG_USERS)
                .child(StaticConfig.UID)
                .child(Constants.ARG_FIREBASE_TOKEN)
                .setValue((String) FirebaseInstanceId.getInstance().getToken());

        viewPager = (NonSwipeableViewPager) findViewById(R.id.content_company);

        UserViewPagerAdapter adapter = new UserViewPagerAdapter(CompanyMainActivity.this.getSupportFragmentManager());
        //    adapter.addFragment(new CompanyPostJobFragment(), "title");//0
        adapter.addFragment(new CompanyMainMyJobsFragment(), "title");//0
        adapter.addFragment(new CompanyPostJobFragment(), "title");//1
        //   adapter.addFragment(new CompanyMainChatFragment(), "title");//3
        adapter.addFragment(new CompanyMainProfileFragment(), "title");//3
        adapter.addFragment(new CompanyMainSearchFragment(), "title");//2
        adapter.addFragment(new CompanyMainMoreFragment(), "title");//4
        //5
        viewPager.setAdapter(adapter);

        if (nbr_active_posts > 0)
            viewPager.setCurrentItem(0);
        else
            viewPager.setCurrentItem(1);
        bottomNavigationView = findViewById(R.id._bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id._companymyJobsMenu) {
                    if (nbr_active_posts > 0)
                        viewPager.setCurrentItem(0);
                    else
                        viewPager.setCurrentItem(1);

                    return true;

                }

                if (item.getItemId() == R.id._companysearchMenu) {

                    viewPager.setCurrentItem(3);
                    return true;
                }

//                if (item.getItemId() == R.id._companychatMenu) {
//
//                    viewPager.setCurrentItem(3);
//                    return true;
//                }
                if (item.getItemId() == R.id._companyprofileMenu) {

                    viewPager.setCurrentItem(2);
                    return true;
                }
                if (item.getItemId() == R.id._companymoreMenu) {

                    viewPager.setCurrentItem(4);
//                    Intent k  = new Intent(CompanyMainActivity.this,ActivityEmployeeViewprofile.class);//ActivitySearch
//                    startActivity(k);
                    return true;
                }
                return false;
            }
        });
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
    protected void onDestroy() {
        super.onDestroy();
        updateFirebaseStatus(false);
    }

    private void updateFirebaseStatus(boolean state) {

        FirebaseDatabase.getInstance()
                .getReference()
                .child(Constants.ARG_USERS)
                .child(StaticConfig.UID)
                .child("status")
                .child("isOnline")
                .setValue(state);
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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

    public void getImage(final String email) {

        if (email != null) {
            FirebaseDatabase.getInstance().getReference().child("user").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getValue() != null) {
                        String id = ((HashMap) dataSnapshot.getValue()).keySet().iterator().next().toString();
                        HashMap userMap = (HashMap) ((HashMap) dataSnapshot.getValue()).get(id);
                      image= (String) userMap.get("avata");

                    } else {
                       image="";
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });
        } else
            System.out.println("++++++++++++++ the email in fire base is null ; ");


    }

    public class AuthUtils {

        void createUser(String email, String password) {

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(CompanyMainActivity.this, new OnCompleteListener<AuthResult>() {
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
                    .addOnCompleteListener(CompanyMainActivity.this, new OnCompleteListener<AuthResult>() {
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

                        ServiceUtils.updateUserStatus(CompanyMainActivity.this,true);

                        SharedPreferenceHelper.getInstance(CompanyMainActivity.this).saveUserInfo(userInfo);
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

    public void checkAccount(final String email) {




        FirebaseDatabase.getInstance().getReference().child("user").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("++++++ the datasnapshot is "+dataSnapshot.toString());

                if( dataSnapshot.getValue()==null) {
                    deleteAccountfireBase();
                    authUtils.signIn(LoginActivity.sharedPref.getString("email", ""), LoginActivity.sharedPref.getString("password", ""));

                }
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("++++++ token is failed");

            }
        });
    }




}