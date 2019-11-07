package com.example.abo8a.jobconnector;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.abo8a.jobconnector.RegisterFragment1.Female;
import static com.example.abo8a.jobconnector.RegisterFragment1.Male;
import static com.example.abo8a.jobconnector.RegisterFragment1.age;
import static com.example.abo8a.jobconnector.RegisterFragment1.city;
import static com.example.abo8a.jobconnector.RegisterFragment1.country;
import static com.example.abo8a.jobconnector.RegisterFragment1.current_work;
import static com.example.abo8a.jobconnector.RegisterFragment1.fname;
import static com.example.abo8a.jobconnector.RegisterFragment1.lname;
import static com.example.abo8a.jobconnector.RegisterFragment1.phone_code;
import static com.example.abo8a.jobconnector.RegisterFragment1.phone_nb;
import static com.example.abo8a.jobconnector.RegisterFragment1.workInSpinner;
import static com.example.abo8a.jobconnector.RegisterFragment2.check_experience;
import static com.example.abo8a.jobconnector.RegisterFragment2.companyName;
import static com.example.abo8a.jobconnector.RegisterFragment2.description;
import static com.example.abo8a.jobconnector.RegisterFragment2.jobIn;
import static com.example.abo8a.jobconnector.RegisterFragment2.job_city;
import static com.example.abo8a.jobconnector.RegisterFragment2.job_country;
import static com.example.abo8a.jobconnector.RegisterFragment2.seekBarValue;
import static com.example.abo8a.jobconnector.RegisterFragment3.completionDate;
import static com.example.abo8a.jobconnector.RegisterFragment3.degree;
import static com.example.abo8a.jobconnector.RegisterFragment3.edu_startdate;
import static com.example.abo8a.jobconnector.RegisterFragment3.major;
import static com.example.abo8a.jobconnector.RegisterFragment3.uni;


public class RegistrationActivity extends AppCompatActivity {

    private LinearLayout pager_indicator;
    private int dotsCount;
    private ImageView[] dots;
    private NonSwipeableViewPager pager;
    private Registration_onBoard_Adapter mAdapter;
    private Button btn_previous;
    private Button btn_continue;
    private DataBaseHandler db;
    private AuthUtils authUtils;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser users;
    public static String EmployeeName = "";

    static AddingToDatabase task;
    public static int id;

    private final boolean adding = false;
    private final boolean updating = true;


    private final String urlInsertBasic = "https://sloppier-citizens.000webhostapp.com/job/insertEmployee.php";
    private final String urlInsertEducation = "https://sloppier-citizens.000webhostapp.com/job/insertEducation.php";
    private final String urlInsertExperience = "https://sloppier-citizens.000webhostapp.com/job/insertExperience.php";


    private static final String TAG = "RegistrationActivity";

    private static String gender;


    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initFirebase();
        init();
        checkEmail(getIntent().getStringExtra("email"));
    }

    public void init() {
        {

//for fragment 1
            fname = findViewById(R.id.first_name);
            lname = findViewById(R.id.last_name);
            age = findViewById(R.id.age);

            Male = (RadioButton) findViewById(R.id.radMale);
            Female = (RadioButton) findViewById(R.id.radFemale);

            current_work = findViewById(R.id.current_work);
            country = findViewById(R.id.Country);
            city = findViewById(R.id.register_city_spinner);
            phone_nb = findViewById(R.id.contact_nb);
            phone_code = findViewById(R.id.contact_nb_code);
            workInSpinner = findViewById(R.id.register_work_spinner);


//for fragment 2
            companyName = findViewById(R.id.company_name);
            jobIn = findViewById(R.id.job_in);
            seekBarValue = findViewById(R.id.seekbar_duration);

            job_country = findViewById(R.id.job_Country);
            job_city = findViewById(R.id.job_City);
            description = findViewById(R.id.job_description);

//for fragment 3
            edu_startdate = findViewById(R.id.education_start_date);
            completionDate = findViewById(R.id.education_completion_date);
            uni = findViewById(R.id.uni);
            degree = findViewById(R.id.degree);
            major = findViewById(R.id.major);


            db = new DataBaseHandler(getApplicationContext());
            btn_previous = (Button) findViewById(R.id.btn_previous);
            btn_continue = (Button) findViewById(R.id.btn_continue);
            pager = (NonSwipeableViewPager) findViewById(R.id.pager_register);
            pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);


//////////////////////////////////////////////////////////////////////////////////////////////////////////////
            Intent i = getIntent();
            id = i.getIntExtra("id", 0);
            System.out.println("+++++++++++ in Registration id is   " + id);
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

            mAdapter = new Registration_onBoard_Adapter(getSupportFragmentManager());
            pager.setAdapter(mAdapter);

            pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override // This method will be invoked when a new page becomes selected
                public void onPageSelected(int position) {
                    // Toast.makeText(RegistrationActivity.this, "page #" + position, Toast.LENGTH_SHORT).show();

                    // Change the current position intimation

                    for (int i = 0; i < dotsCount; i++) {
                        dots[i].setImageDrawable(ContextCompat.getDrawable(
                                RegistrationActivity.this, R.drawable.non_selected_item_dot));
                    }

                    dots[position].setImageDrawable(ContextCompat.getDrawable(
                            RegistrationActivity.this, R.drawable.selected_item_dot));

                    //      pos = position + 1;
                    if (pager.getCurrentItem() == 0) {
                        btn_previous.setVisibility(View.INVISIBLE);
                        btn_continue.setText("Continue");
                    }
                }

                @Override  // This method will be invoked when the current page is scrolled
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageScrollStateChanged(int arg0) {

                }
            });

            btn_previous.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int previousposition = pager.getCurrentItem() - 1;

                    pager.setCurrentItem(previousposition);
                    if (pager.getCurrentItem() != 2) {
                        btn_continue.setText("Continue");

                    }

                }
            });
            btn_continue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int nextposition = pager.getCurrentItem() + 1;
                    if (nextposition < dotsCount) {
                        if (pager.getCurrentItem() == 0)
                        {
                            if(validateFragment1())
                                pager.setCurrentItem(nextposition);

                        }

                        if (pager.getCurrentItem() == 1) {


                            btn_previous.setVisibility(View.VISIBLE);
                            if(check_experience) {
                                System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" +
                                        "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@((((((1))))@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" +
                                        "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" +
                                        "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" +
                                        "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                                if (validateFragment2())
                                    pager.setCurrentItem(nextposition);
                            }
                            else
                            {
                                pager.setCurrentItem(nextposition);

                            }

                        }
                        if (pager.getCurrentItem() == 2) {
                            btn_continue.setText("finish");

                        }
                    } else {//kabas y3ne 3l finish

                        if (validateFragment3()) {


//                        if (db.getBasic(id).getId() > 0)
//                            addPersonal(updating);
//                        else
//                            addPersonal(adding);
                            new AddingToDatabase().execute(urlInsertBasic);

                            if (check_experience == true) {
//                            if (db.getExperience(id).getId() > 0)
//                                addingExperience(updating);
//                            else
//                                addingExperience(adding);
                                new AddingToDatabase().execute(urlInsertExperience);
                            }
//                        if (db.getEducation(id).getId() > 0)
//                            addingEducation(updating);
//                        else
//                            addingEducation(adding);
                            new AddingToDatabase().execute(urlInsertEducation);

                            Intent i = new Intent(getApplicationContext(), user.class);
                            i.putExtra("id", id);
                            i.putExtra("user", getIntent().getStringExtra("user"));
                            i.putExtra("email", getIntent().getStringExtra("email"));


                            startActivity(i);


                        }
                    }
                }

            });

            setUiPageViewController();

        }
    }


    private void setUiPageViewController() {

        dotsCount = mAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(RegistrationActivity.this, R.drawable.non_selected_item_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(6, 0, 6, 0);

            pager_indicator.addView(dots[i], params);
        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(RegistrationActivity.this, R.drawable.selected_item_dot));
    }

    @Override
    public void onStart() {
        super.onStart();

    }


    private class AddingToDatabase extends AsyncTask<String, String, Void> {
        String url1;

        // A callback method executed on UI thread on starting the task        
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(String... params) {
            final String url = params[0];


            final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {

                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        if (success) {
                            publishProgress("Added successfully");

                            LoginActivity.editor.putBoolean("foundBasic", true).commit();


                        } else {
                            publishProgress("Failed to Add");

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        publishProgress("Exeption Failed to Add");
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Login Error: " + error.getMessage());

                    publishProgress("what!!!!!!");
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting params to login url
                    Map<String, String> params = new HashMap<String, String>();

                    if (url.equals(urlInsertEducation)) {
                        params.put("id", id + "");
                        params.put("uni", uni.getText().toString());
                        params.put("major", major.getText().toString());
                        params.put("start_date", edu_startdate.getText().toString());
                        params.put("end_date", completionDate.getText().toString());
                        params.put("degree", degree.getText().toString());
                        publishProgress("Education taken");
                    } else if (url.equals(urlInsertBasic)) {

                        params.put("id", id + "");
                        params.put("fname", fname.getText().toString());
                        params.put("lname", lname.getText().toString());
                        params.put("age", age.getText().toString());
                        if (Male.isChecked())
                            gender = "Male";
                        if (Female.isChecked())
                            gender = "Female";
//                        params.put("gender", email.getText().toString());
                        params.put("gender", gender);
                        if (workInSpinner.getSelectedItem().equals("Other")) {
                            params.put("currentWork", current_work.getText().toString());
                        } else
                            params.put("currentWork", workInSpinner.getSelectedItem().toString());

                        params.put("country", country.getText().toString());
                        params.put("city", city.getSelectedItem().toString());
                        params.put("contactNb", phone_code.getText().toString() + phone_nb.getText().toString());
                        publishProgress("Basic taken");

                    } else {
                        String seekvalue = "";
                        if (!seekBarValue.getText().toString().equals("No Experience"))
                            seekvalue = seekBarValue.getText().toString();
                        params.put("id", id + "");
                        params.put("company_name", companyName.getText().toString());
                        params.put("job_in", jobIn.getText().toString());

                        params.put("duration", experienceToMonthes(seekvalue) + "");
                        params.put("job_country", country.getText().toString());
                        params.put("job_city", job_city.getText().toString());
                        params.put("description", description.getText().toString());
                        publishProgress("EXperience taken");

                    }
                    System.out.println("++++++ url ++++" + url);
                    return params;
                }

            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);


            return null;
        }


        @Override
        protected void onProgressUpdate(String... values) {


            // Toast.makeText(getApplicationContext(),values[0].toString(),Toast.LENGTH_SHORT).show();
            System.out.println("_--------   " + values[0]);


        }

        // A callback method executed on UI thread, invoked after the completion of the task
        @Override
        protected void onPostExecute(Void result) {

            //Toast.makeText(getApplicationContext(),"******** Done ********",Toast.LENGTH_SHORT).show();
        }
    }


    public boolean validateFragment1() {
        boolean valid = true;

        String fr1fName = fname.getText().toString();
        String fr1lName = lname.getText().toString();
        String fr1age = age.getText().toString();
        String fr1Country = country.getText().toString();
        String fr1CurrentWork = current_work.getText().toString();
        String fr1PhNb = phone_nb.getText().toString();

        String fr1workSpinner = workInSpinner.getSelectedItem().toString();


        if (fr1fName.isEmpty()) {
            fname.setError("enter a valid name");
            valid = false;
        }
        if (fr1lName.isEmpty()) {
            lname.setError("enter a valid name");
            valid = false;
        }
        if (fr1age.isEmpty()) {
            age.setError("enter a valid Age");
            valid = false;
        }
        if (fr1Country.isEmpty()) {
            country.setError("enter a valid Country");
            valid = false;
        }


        if (fr1PhNb.length() != 8) {
            phone_nb.setError("enter a valid Phone Number (8 numbers)");
            valid = false;
        }
        if (fr1workSpinner.equals("Other")) {
            if (fr1CurrentWork.isEmpty()) {
                current_work.setError("enter a valid Work");
                valid = false;
            }
        }


        return valid;
    }

    public boolean validateFragment2() {
        boolean valid = true;

        String fr2CompanyName = companyName.getText().toString();
        String fr2description = description.getText().toString();
        String fr2jobIn = jobIn.getText().toString();
        String fr2job_City = job_city.getText().toString();
        String fr2job_Country = job_country.getText().toString();
        String fr2JobExperienceDuration = seekBarValue.getText().toString();


        if (fr2CompanyName.isEmpty()) {
            companyName.setError("enter a valid Company name");
            valid = false;
        }
        if (fr2description.isEmpty()) {
            description.setError("enter a valid Description");
            valid = false;
        }
        if (fr2jobIn.isEmpty()) {
            jobIn.setError("enter a valid Job");
            valid = false;
        }
        if (fr2job_Country.isEmpty()) {
            job_country.setError("enter a valid Country");
            valid = false;
        }
        if (fr2job_City.isEmpty()) {
            job_city.setError("enter a valid City");
            valid = false;
        }
        if (fr2JobExperienceDuration.isEmpty()) {
            seekBarValue.setError("enter a valid Duration");
            valid = false;
        }


        return valid;
    }

    public boolean validateFragment3() {
        boolean valid = true;


        String fr3uni = uni.getText().toString();
        String fr3completionDate = completionDate.getText().toString();
        String fr3degree = degree.getText().toString();
        String fr3edu_startDate = edu_startdate.getText().toString();
        String fr3Major = major.getText().toString();


        if (fr3uni.isEmpty()) {
            uni.setError("enter a valid University name");
            valid = false;
        }
        if (fr3degree.isEmpty()) {
            degree.setError("enter a valid Degree");
            valid = false;
        }
        if (fr3completionDate.isEmpty()) {
            completionDate.setError("enter a valid Date");
            valid = false;
        }
        if (fr3edu_startDate.isEmpty()) {
            edu_startdate.setError("enter a valid Date");
            valid = false;
        }

        if (!compareDate(fr3edu_startDate, fr3completionDate)) {
            completionDate.setError("enter a valid Date");
            edu_startdate.setError("enter a valid Date");
            valid = false;

        }


        if (fr3Major.isEmpty()) {
            major.setError("enter a valid Major");
            valid = false;
        }


        return valid;
    }

    public int experienceToMonthes(String experience) {
        String s[] = experience.split(" ");
        if (s[1].equals("month"))
            return Integer.valueOf(s[0]);
        else if (s[0].equals("6+"))
            return 6 * 12;
        else
            return (int) (Float.valueOf(s[0]) * 12);

    }

    public boolean compareDate(String d1, String d2) {
        String[] a1 = d1.split("-");
        String[] a2 = d2.split("-");
        if (d1.equals("") || d2.equals(""))
            return false;
        System.out.println("+++++++  the year is +++++++++   " + a1[0]);
        if (Integer.parseInt(a1[0]) > Integer.parseInt(a2[0]))
            return false;
        if (Integer.parseInt(a1[0]) == Integer.parseInt(a2[0])) {
            if (Integer.parseInt(a1[1]) > Integer.parseInt(a2[1]))
                return false;
            if (Integer.parseInt(a1[1]) == Integer.parseInt(a2[1])) {
                if (Integer.parseInt(a1[2]) > Integer.parseInt(a2[2]))
                    return false;
            }
        }

        if (!(Integer.parseInt(a1[0]) < Integer.parseInt(a2[0]) - 2))
            return false;

        return true;
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
                    .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
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
                    .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
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

                        ServiceUtils.updateUserStatus(RegistrationActivity.this,true);

                        SharedPreferenceHelper.getInstance(RegistrationActivity.this).saveUserInfo(userInfo);
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
