package com.example.abo8a.jobconnector.searchedprofiles;

import android.Manifest;
import
        android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.abo8a.jobconnector.AppSingleton;
import com.example.abo8a.jobconnector.CommentActivity;
import com.example.abo8a.jobconnector.Company.CompanyAppliedEmployeeActivity;
import com.example.abo8a.jobconnector.Company.CompanyAppliedPostsActivity;
import com.example.abo8a.jobconnector.Company.CompanyMainActivity;
import com.example.abo8a.jobconnector.Company.CompanyMainMyJobsFragment;
import com.example.abo8a.jobconnector.Experience;
import com.example.abo8a.jobconnector.Language;
import com.example.abo8a.jobconnector.Notification.FcmNotificationBuilder;
import com.example.abo8a.jobconnector.R;
import com.example.abo8a.jobconnector.SearchedResultPagerAdapter;
import com.example.abo8a.jobconnector.Skill;
import com.example.abo8a.jobconnector.chat.util.ImageUtils;
import com.example.abo8a.jobconnector.chat.util.data.FriendDB;
import com.example.abo8a.jobconnector.chat.util.data.StaticConfig;
import com.example.abo8a.jobconnector.chat.util.model.Friend;
import com.example.abo8a.jobconnector.chat.util.model.ListFriend;
import com.example.abo8a.jobconnector.chat.util.ui.ChatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityEmployeeViewprofile extends AppCompatActivity {
    //    private ViewPager pager;
    private static final int REQUEST_CALL = 1;
    private TextView call;
    private static int id;
    public static Context context;
    private SearchedResultPagerAdapter adapter;

    private static final String URL_FOR_Ratting = "https://sloppier-citizens.000webhostapp.com/rate/rate.php";
    private static final String TAG = "ActivityEmployeeViewprofile";
    private Dialog Dialog;

    LovelyProgressDialog dialog;
    public static String EmployeeEmail = "";
    private ArrayList<String> listFriendID = null;
    private ListFriend dataListFriend = null;

    ImageView avatar;
    boolean onClick=true,onLongClick=false;
///------------------------------------------------------------


    public static RecyclerView recyclerView_languages;
    public static RecyclerView recyclerView_Experience;
    public static RecyclerView recyclerView_Skills;


    //********************************//
    private Button accept_btn;
    private Button reject_btn;
    private Button employee_state_btn;
    private Button request_btn;


    private TextView edu_uni;
    private TextView edu_major;
    private TextView edu_degree;
    private TextView edu_start_date;
    private TextView edu_completion_date;
    public static String uni;
    public static String major;
    public static String degree;
    public static String start;
    public static String end;
    //********************************//
    private TextView noExp;
    private TextView profile_fname;
    private TextView profile_lname;
    private TextView profile_age;
    private TextView profile_country;
    private TextView profile_city;
    private TextView profile_email;
    private TextView profile_phone;
    private TextView profile_target_work;
    private TextView profile_username;

    /////////////////////////////////////////////////////////
    public static LanguageAdapter languageAppAdapter; //Array Adapter
    public static ExperienceAdapter ExperienceAdapter; //Array Adapter
    public static SkillAdapter SkillAdapter; //Array Adapter

    private RecyclerView.LayoutManager languageLayoutManager;
    private RecyclerView.LayoutManager expLayoutManager;
    private RecyclerView.LayoutManager skillLayoutManager;


    private static ArrayList<Language> languagedata;
    private static ArrayList<Experience> experienceData;
    private static ArrayList<Skill> skillData;

    ///////////////////////////
    int cid;
    LinearLayout linearPending;
    LinearLayout linear_status;
    LinearLayout linear_request;
    ProgressDialog progressDialog;
    ////////////////////////////
    static String employee_email;
    static String fullname;
    static String jobdesc;
    static String jobcity;
    /////////////////////
    static String spinnervalue;
    static int spinnerposition;

    String  request="";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = ActivityEmployeeViewprofile.this;
        setContentView(R.layout.activity_employee_view_profile);
        Intent i = getIntent();
        cid = CompanyMainActivity.cid;
        id = i.getIntExtra("eid", -1);
        EmployeeEmail = i.getStringExtra("email");
        request=i.getStringExtra("request");
        System.out.println("+++++++++ email from ActiveEmployeeViewProfile is " + EmployeeEmail);



        initTollbar();
        initFloatingActionBar();
        init();
        initListFrind();
        if(request.equals(""))
        {
            onClick=true;
            onLongClick=false;
            request_btn.setText("Request");
        }
        else
        {
            if(request.equals("Pending")) {
                onClick = false;
                onLongClick = true;
                request_btn.setText("Cancel request");
            }
            else
            {
                onClick = false;
                onLongClick = false;
                if(request.equals("Accepted")) {
                    request_btn.setText("Accepted");
                    request_btn.setTextColor(Color.GREEN);
                }

                else {
                    request_btn.setText("Rejected");
                    request_btn.setTextColor(Color.RED);
                }
                request_btn.setEnabled(false);
            }
        }
    }


    @SuppressLint("WrongViewCast")
    public void init() {

        System.out.println("+++++++  employee+++++++" + id);

        accept_btn = findViewById(R.id.EmployeeAcceptbtn);
        reject_btn = findViewById(R.id.EmployeeRejectbtn);
        request_btn = findViewById(R.id.EmployeeRequestbtn);
        employee_state_btn = findViewById(R.id.EmployeeState);

        linearPending = findViewById(R.id.linearLayout_Employee_accept_reject);
        linear_status = findViewById(R.id.linearLayout_Employee_State);
        linear_request = findViewById(R.id.linearLayout_Employee_Request);

        if (CompanyAppliedEmployeeActivity.status != null) {
            if (CompanyAppliedEmployeeActivity.status.equals("PENDING")) {
                linearPending.setVisibility(View.VISIBLE);
                linear_status.setVisibility(View.GONE);
                linear_request.setVisibility(View.GONE);
            } else {
                if (CompanyAppliedEmployeeActivity.status.equals("Accepted") || CompanyAppliedEmployeeActivity.status.equals("Rejected")) {
                    linearPending.setVisibility(View.GONE);
                    linear_status.setVisibility(View.VISIBLE);
                    linear_request.setVisibility(View.GONE);
                    if (CompanyAppliedEmployeeActivity.status.equals("Accepted"))
                        employee_state_btn.setText("Accepted");
                    else
                        employee_state_btn.setText("Rejected");
                }
            }
        } else {
            linear_request.setVisibility(View.VISIBLE);
            linearPending.setVisibility(View.GONE);
            linear_status.setVisibility(View.GONE);
            ///byeb2a hon el2ato iza ken requested or not
        }

        request_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClick)
                    requestDialog();

            }
        });
        request_btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                new AlertDialog.Builder(ActivityEmployeeViewprofile.this)
                        .setTitle("Alert")
                        .setMessage("Are you sure want to Cancel Request ?")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialog = new LovelyProgressDialog(ActivityEmployeeViewprofile.this);

                                dialog.setMessage("Wait a second...");
                                dialog.show();
                                if(onLongClick)
                                    request("",0);

                                //EmailDialog();
                            }


                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();


                return true;
            }

        });
        reject_btn.setOnClickListener(onButtonClick);
        accept_btn.setOnClickListener(onButtonClick);

        employee_state_btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                String msg;
                if (employee_state_btn.getText().toString().equals("Accepted"))
                    msg = "Are you sure want to UnAccept this Employee?";
                else
                    msg = "Are you sure want to UnReject this Employee?";

                new AlertDialog.Builder(ActivityEmployeeViewprofile.this)
                        .setTitle("Alert!")
                        .setMessage(msg)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                updateStatus(id, CompanyAppliedEmployeeActivity.pid, "pending");
                                getrecieverToken(EmployeeEmail,2);

                                linearPending.setVisibility(View.VISIBLE);
                                linear_status.setVisibility(View.GONE);
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();

                return false;
            }
        });
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        avatar = findViewById(R.id.Employee_img_avatar);
        setImage(EmployeeEmail, avatar);
        edu_uni = findViewById(R.id.edu_uni);
        edu_major = findViewById(R.id.edu_major);
        edu_degree = findViewById(R.id.edu_degree);
        edu_start_date = findViewById(R.id.edu_start_date);
        edu_completion_date = findViewById(R.id.edu_completion_date);

        noExp = findViewById(R.id.have_no_experience);
        profile_fname = findViewById(R.id.profilefirstname);
        profile_lname = findViewById(R.id.profilelastname);
        profile_age = findViewById(R.id.profileage);
        profile_country = findViewById(R.id.profilelocation_country);
        profile_city = findViewById(R.id.profilelocation_city);
        profile_target_work = findViewById(R.id.profiletargetWork);
        profile_email = findViewById(R.id.profileemail);
        profile_phone = findViewById(R.id.profilephone);
        profile_username = findViewById(R.id.profileUsername);
        //  profileImage=findViewById(R.id.profileImage);


        recyclerView_languages = findViewById(R.id.listView_languages);
        recyclerView_Experience = findViewById(R.id.listView_experience);
        recyclerView_Skills = findViewById(R.id.listView_skills);


        languageAppAdapter = new LanguageAdapter(getApplicationContext(), languagedata);
        recyclerView_languages.setAdapter(languageAppAdapter);


        languageLayoutManager = new LinearLayoutManager(getApplicationContext());
        expLayoutManager = new LinearLayoutManager(getApplicationContext());
        skillLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView_Skills.setLayoutManager(skillLayoutManager);
        recyclerView_Skills.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                DividerItemDecoration.VERTICAL));

        recyclerView_Experience.setLayoutManager(expLayoutManager);
        recyclerView_Experience.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                DividerItemDecoration.VERTICAL));
        recyclerView_languages.setLayoutManager(languageLayoutManager);
        recyclerView_languages.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                DividerItemDecoration.VERTICAL));
        fillData();
        call=findViewById(R.id.call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });
    }

    private void request(final String message, final int pid) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://sloppier-citizens.000webhostapp.com/company/request.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if( dialog!=null)
                    dialog.dismiss();

                System.out.println("++++++++++++++++"+response);
                try {
                    JSONObject json = new JSONObject(response);
                    if (json.getBoolean("success"))
                    {
                        if(message!="") {
                            request_btn.setText("Cancel Request");
                            onLongClick = true;
                            onClick = false;
                            getrecieverToken(employee_email,CompanyMainActivity.companyName,message);
                        }
                        else {
                            onLongClick = false;
                            onClick = true;
                            request_btn.setText("Request");
                            Toast.makeText(ActivityEmployeeViewprofile.this, "Request Cancelled", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        System.out.println("not requested success not 1 ---------------------");


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("Json exception--------------------");
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("request", "request error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Check your Internet Connection and try Again", Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("eid", id + "");
                params.put("cid",cid+"");
                if(message=="")
                    params.put("request",0+"");
                else
                    params.put("request",1+"");
                params.put("pid",pid+"");
                System.out.println("+++++++ params in request is "+params.toString());

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);




    }


    View.OnClickListener onButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.EmployeeAcceptbtn) {

                updateStatus(id, CompanyAppliedEmployeeActivity.pid, "accepted");
                getrecieverToken(EmployeeEmail,1);
                linearPending.setVisibility(View.GONE);
                linear_status.setVisibility(View.VISIBLE);
                employee_state_btn.setText("Accepted");

            } else {
                updateStatus(id, CompanyAppliedEmployeeActivity.pid, "rejected");
                getrecieverToken(EmployeeEmail,0);

                linearPending.setVisibility(View.GONE);
                linear_status.setVisibility(View.VISIBLE);
                employee_state_btn.setText("Rejected");
            }

        }
    };

    /////////////////////////////////////////////
    ////////////////////////////////////
    public void EmailDialog() {

        Dialog = new Dialog(this, R.style.Theme_Dialog);
        Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Dialog.setContentView(R.layout.dialog_sendemail);

        final Spinner jobsspinner = Dialog.findViewById(R.id.jobs_spinner);
        Button request = (Button) Dialog.findViewById(R.id.EmailBtn);

        jobsspinner.setAdapter(new ArrayAdapter<String>(ActivityEmployeeViewprofile.this,
                android.R.layout.simple_spinner_item,
                CompanyMainMyJobsFragment.jobstype));


        jobsspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub


                spinnerposition = jobsspinner.getSelectedItemPosition();

                spinnervalue = jobsspinner.getSelectedItem().toString();

                for (int i = 0; i < CompanyMainMyJobsFragment.jobscity.size(); i++) {
                    jobcity = CompanyMainMyJobsFragment.jobscity.get(spinnerposition);
                    jobdesc = CompanyMainMyJobsFragment.jobsdescs.get(spinnerposition);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                //employeeemail
                /////////////////////////////////////////////
                //////////////hon email luser le bs y3mel sign in 3le/////////////////
                ////////////////////////////////////////////////
                /////////////////////////////////////////////////
                String emailTo = "maria-afara5@hotmail.com";
                //////////////////////////////////////////////////
                /////////////////////////////////////////////////
                String emailSubject = "Job Request for " + spinnervalue + " Job at " + CompanyMainMyJobsFragment.company_name
                        + " Company ";
                String emailContent = "Dear " + fullname + ",\nWe request you to work for us as " + spinnervalue + " in " +
                        "our humble company in " + jobcity + ".\nHere's the description about the job\n" + jobdesc + "\nFor your answer please contact us" +
                        " at this email. \n Phone number : " + CompanyMainMyJobsFragment.company_phone + " ";

                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailTo});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
                emailIntent.putExtra(Intent.EXTRA_TEXT, emailContent);
                emailIntent.setType("message/rfc822");

                startActivity(Intent.createChooser(emailIntent, "Select an Email Client:"));

                Dialog.dismiss();

            }
        });

        Dialog.show();
    }


    ////////////////////////////////////
    /////////////////////////////////////////////
    private void makePhoneCall() {
        String number = profile_phone.getText().toString();

        if (ContextCompat.checkSelfPermission(ActivityEmployeeViewprofile.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ActivityEmployeeViewprofile.this,
                    new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else {
            String dial = "tel:" + number;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
    ////////////////////////////////////

/////////////////////////////////////////////

    public void requestDialog() {

        Dialog = new Dialog(this, R.style.Theme_Dialog);
        Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Dialog.setContentView(R.layout.dialog_sendemail);

        final Spinner jobsspinner = Dialog.findViewById(R.id.jobs_spinner);
        final Button request = (Button) Dialog.findViewById(R.id.EmailBtn);

        jobsspinner.setAdapter(new ArrayAdapter<String>(ActivityEmployeeViewprofile.this,
                android.R.layout.simple_spinner_item,
                CompanyMainMyJobsFragment.jobstype));


        jobsspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub


                spinnerposition = jobsspinner.getSelectedItemPosition();

                spinnervalue = jobsspinner.getSelectedItem().toString();

                for (int i = 0; i < CompanyMainMyJobsFragment.jobscity.size(); i++) {
                    jobcity = CompanyMainMyJobsFragment.jobscity.get(spinnerposition);
                    jobdesc = CompanyMainMyJobsFragment.jobsdescs.get(spinnerposition);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {



                //////////////////////////////////////////////////
                /////////////////////////////////////////////////

                String message=CompanyMainMyJobsFragment.company_name+" Requested you to work for it as "+spinnervalue;
                request(message,CompanyMainMyJobsFragment.jobsId.get(spinnerposition));

                Dialog.dismiss();

            }
        });

        Dialog.show();
    }
    public void fillData() {
//        final ProgressDialog progressDialog = new ProgressDialog(getApplication());
//        progressDialog.setMessage("Loading...");
//        progressDialog.show();

        final String url = "https://sloppier-citizens.000webhostapp.com/job/getInfo" +
                ".php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {
                    System.out.println("+++++++" + response);

                    JSONObject json = new JSONObject(response);

                    // System.out.println("++++++++" + response);

                    if (json.getBoolean("success")) {

                        profile_fname.setText(json.getString("fname"));
                        profile_lname.setText(json.getString("lname"));

                        fullname = json.getString("fname") + " " + json.getString("lname");
                        profile_age.setText(json.getString("age"));

                        profile_country.setText(json.getString("country"));
                        profile_city.setText(json.getString("city"));

                        profile_email.setText(json.getString("loginemail"));

                        employee_email = json.getString("loginemail");
                        profile_phone.setText(json.getString("contact_nb"));
                        profile_target_work.setText(json.getString("current_work"));
                        profile_username.setText( json.getString("username"));
                        //fill education
                        uni = json.getString("uni");
                        major = json.getString("major");
                        degree = json.getString("degree");
                        start = json.getString("starting_date");
                        end = json.getString("completion_date");
                        edu_uni.setText(uni);
                        edu_major.setText(major);
                        edu_degree.setText(degree);
                        edu_start_date.setText(start);
                        edu_completion_date.setText(end);

                        //fill languages,skills and experiences

                        JSONArray jsonLanguage = json.getJSONArray("languages");
                        languagedata = new ArrayList<Language>();
                        for (int i = 0; i < jsonLanguage.length(); i++) {
                            JSONObject j = (JSONObject) jsonLanguage.get(i);

                            Language l = new Language(j.getInt("Eid"), j.getString("language"), j.getString("level"));
                            languagedata.add(l);
                            languageAppAdapter = new LanguageAdapter
                                    (getApplicationContext(), languagedata);
                            recyclerView_languages.setAdapter(languageAppAdapter);

                        }

                        JSONArray jsonExperience = json.getJSONArray("experience");
                        experienceData = new ArrayList<Experience>();

                        for (int i = 0; i < jsonExperience.length(); i++) {
                            noExp.setVisibility(View.GONE);
                            JSONObject j = (JSONObject) jsonExperience.get(i);

                            Experience E = new Experience(j.getInt("Eid"), j.getString("job_in")
                                    ,experienceToyears( j.getInt("duration")+""), j.getString("company_name"), j.getString("job_country"), j.getString("job_city"), j.getString("descreption"));
                            experienceData.add(E);
                            ExperienceAdapter = new ExperienceAdapter(getApplicationContext(), experienceData);
                            recyclerView_Experience.setAdapter(ExperienceAdapter);

                        }

                        JSONArray jsonSkills = json.getJSONArray("skills");
                        skillData = new ArrayList<Skill>();
                        SkillAdapter = new SkillAdapter(getApplicationContext(), skillData);

                        for (int i = 0; i < jsonSkills.length(); i++) {

                            JSONObject j = (JSONObject) jsonSkills.get(i);

                            Skill s = new Skill(j.getInt("Eid"), j.getString("skill_name"), j.getString("skill_level"));
                            skillData.add(s);

                            recyclerView_Skills.setAdapter(SkillAdapter);


                        }

                    } else {
                        System.out.println("success not 1 ---------------------");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("Json22--------------------");
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("EmployeeAccountProfile", " Error: " + error.getMessage());
                Toast.makeText(getApplicationContext().getApplicationContext(), "Check your Internet Connection and try Again", Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();


                params.put("eid", id + "");


                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext().getApplicationContext());
        queue.add(stringRequest);


    }

    private void initTollbar() {

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        //this line shows back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        collapsingToolbar.setTitle("");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                    collapsingToolbar.setTitle(profile_username.getText());
                    toolbar.setBackgroundColor(getResources().getColor(R.color.transparent));
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(profile_username.getText());
                    isShow = true;
                    toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                } else if (isShow) {
                    collapsingToolbar.setTitle(profile_username.getText());
                    toolbar.setBackgroundColor(getResources().getColor(R.color.transparent));
                    isShow = false;
                }
            }
        });
        NestedScrollView scrollView = (NestedScrollView) findViewById(R.id.nest_scrollview);
        scrollView.setFillViewport(true);
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initFloatingActionBar() {
        // Create an icon
        ImageView icon = new ImageView(this);
        icon.setImageResource(R.mipmap.ic_launcher);

        ImageView icon_main = new ImageView(this);
        icon_main.setImageResource(R.drawable.ic_plus_float);


        FloatingActionButton fab = new FloatingActionButton.Builder(this)
                .setContentView(icon_main)
                .build();
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        // repeat many times:
        ImageView itemIcon1 = new ImageView(this);
        itemIcon1.setImageResource(R.drawable.comment_white);

        ImageView itemIcon2 = new ImageView(this);
        itemIcon2.setImageResource(R.drawable.chat_white);

        ImageView itemIcon3 = new ImageView(this);
        itemIcon3.setImageResource(R.drawable.star_yellow);

        SubActionButton viewcommentsbutton1 = itemBuilder.setContentView(itemIcon1).build();
        SubActionButton chatbutton2 = itemBuilder.setContentView(itemIcon2).build();
        SubActionButton ratebutton3 = itemBuilder.setContentView(itemIcon3).build();

        //attach the sub buttons to the main button
        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(viewcommentsbutton1)
                .addSubActionView(chatbutton2)
                .addSubActionView(ratebutton3)
                .attachTo(fab)
                .build();
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(
                R.color.colorPrimary)));
        viewcommentsbutton1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(
                R.color.colorPrimary)));
        chatbutton2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(
                R.color.colorPrimary)));
        ratebutton3.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(
                R.color.colorPrimary)));
        FloatingActionButton.LayoutParams params = new FloatingActionButton.LayoutParams(200, 200);
        viewcommentsbutton1.setLayoutParams(params);
        chatbutton2.setLayoutParams(params);
        ratebutton3.setLayoutParams(params);


        viewcommentsbutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(ActivityEmployeeViewprofile.this, CommentActivity.class);
                i.putExtra("id", id);
                i.putExtra("EmployeeName", profile_fname.getText().toString() + " " + profile_lname.getText().toString());
                startActivity(i);

            }
        });

        chatbutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Wait a sec...", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                System.out.println("++++++++++++++The email in the ActivityEmployeeViewProfile is " + EmployeeEmail);
                findIDEmail(EmployeeEmail);


            }
        });
        ratebutton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Snackbar.make(view, "rate", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                RateEmployeeDialog();
            }
        });

    }


    //******************
    public void RateEmployeeDialog() {
        final int[] count = {0};
        Dialog = new Dialog(this, R.style.Theme_Dialog);
        Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Dialog.setContentView(R.layout.dialog_rate_employee);

        final RatingBar ratingBar = (RatingBar) Dialog.findViewById(R.id.rating_bar);
        Button submit = (Button) Dialog.findViewById(R.id.btn_ratesubmit);
        Button later = (Button) Dialog.findViewById(R.id.btn_ratelater);
        final EditText comment = Dialog.findViewById(R.id.rate_message);
        final LinearLayout view_rating_message = Dialog.findViewById(R.id.view_rating_message);
        final LinearLayout view_5_star_rating = Dialog.findViewById(R.id.view_5_star_rating);
        later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Dialog.dismiss();
            }
        });
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                if (ratingBar.getRating() == 5) {
                    view_5_star_rating.setVisibility(View.VISIBLE);

                } else {
                    view_5_star_rating.setVisibility(View.GONE);
                    view_rating_message.setVisibility(View.GONE);
                }

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (count[0] == 0) {
                    if (ratingBar.getRating() == 0) {
                        Toast.makeText(getApplication(), "please provide us stars", Toast.LENGTH_SHORT).show();
                    } else {//save database
                        view_rating_message.setVisibility(View.VISIBLE);
                    }
                    count[0]++;
                } else {//eid,cid
                    rateEmployee(id, cid, (int) ratingBar.getRating(), comment.getText().toString());
                    Dialog.dismiss();
                }
            }
        });
        Dialog.show();
    }

    private void rateEmployee(final Integer eid, final Integer cid, final Integer rate, final String comment) {
        // Tag used to cancel the request
        String cancel_req_tag = "rate";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_FOR_Ratting, new Response.Listener<String>() {

            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "rate Response: " + response.toString());

                try {
                    System.out.println("+++++++++++++++++++++++++++++++++" +
                            "++++++++++++++++++++++++++++++++++++++++"
                            + "+++++++++++++++++++++++++++++++++++++++++");
                    System.out.println("+++++++++++++" + response);
                    JSONObject jObj = new JSONObject(response);
                    boolean success = jObj.getBoolean("success");

                    if (success) {

                        Toast.makeText(getApplicationContext(), " Rated Successfully", Toast.LENGTH_SHORT).show();
                        //dismiss dialog
                        Dialog.dismiss();
//                        finish();
                    } else {
                        System.out.println("+++++++++++++++++++++++++++++++++" +
                                "+++++++++++++++++failed+++++++++++++++++++++++"
                                + "+++++++++++++++++++++++++++++++++++++++++");
                        Toast.makeText(getApplicationContext(),
                                "Failed to Rate", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("\ncatch a jason exception  in request\n");
                }

            }
        }, new Response.ErrorListener() {

            @SuppressLint("LongLogTag")
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("Eid", eid + "");
                params.put("Cid", cid + "");
                params.put("Rating", rate + "");
                params.put("Comment", comment);

                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }


    private void findIDEmail(String email) {


        FirebaseDatabase.getInstance().getReference().child("user").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.getValue() != null) {
                    String id = ((HashMap) dataSnapshot.getValue()).keySet().iterator().next().toString();

                    HashMap userMap = (HashMap) ((HashMap) dataSnapshot.getValue()).get(id);


                    Friend user = new Friend();
                    user.name = (String) userMap.get("name");
                    user.email = (String) userMap.get("email");
                    user.avata = (String) userMap.get("avata");
                    user.id = id;
                    user.idRoom = id.compareTo(StaticConfig.UID) > 0 ? (StaticConfig.UID + id).hashCode() + "" : "" + (id + StaticConfig.UID).hashCode();
                    checkBeforAddFriend(id, user);

                    Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                    intent.putExtra(StaticConfig.INTENT_KEY_CHAT_FRIEND, user.name);
                    ArrayList<CharSequence> idFriend = new ArrayList<CharSequence>();
                    idFriend.add(id);
                    intent.putCharSequenceArrayListExtra(StaticConfig.INTENT_KEY_CHAT_ID, idFriend);
                    intent.putExtra(StaticConfig.INTENT_KEY_CHAT_ROOM_ID, user.idRoom);
                    ChatActivity.bitmapAvataFriend = new HashMap<>();
                    if (!user.avata.equals(StaticConfig.STR_DEFAULT_BASE64)) {
                        byte[] decodedString = Base64.decode(user.avata, Base64.DEFAULT);
                        ChatActivity.bitmapAvataFriend.put(id, BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));
                    } else {
                        ChatActivity.bitmapAvataFriend.put(id, BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.default_avata));
                    }
                    getApplication().startActivity(intent);

//                mapMark.put(id, null);
//                fragment.startActivityForResult(intent, FriendsFragment.ACTION_START_CHAT);


                } else
                    Toast.makeText(ActivityEmployeeViewprofile.this, "This Employee is Not Registered Yet!!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void checkBeforAddFriend(final String idFriend, Friend userInfo) {

        //Check xem da ton tai id trong danh sach id chua
        if (listFriendID.contains(idFriend)) {

            System.out.println("+++++++ friend already existsssssss");

        } else {
            addFriend(idFriend, true);
            listFriendID.add(idFriend);
            FriendDB.getInstance(getApplication()).getListFriend().getListFriend().add(userInfo);
            FriendDB.getInstance(getApplication()).addFriend(userInfo);
        }
    }

    private void addFriend(final String idFriend, boolean isIdFriend) {
        if (idFriend != null) {
            if (isIdFriend) {
                FirebaseDatabase.getInstance().getReference().child("friend/" + StaticConfig.UID).push().setValue(idFriend)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    addFriend(idFriend, false);
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {


                            }
                        });
            } else {
                FirebaseDatabase.getInstance().getReference().child("friend/" + idFriend).push().setValue(StaticConfig.UID).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            addFriend(null, false);
                        }
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
            }
        }
    }

    public void initListFrind() {
        if (dataListFriend == null) {
            dataListFriend = FriendDB.getInstance(getApplication()).getListFriend();
            System.out.println("++++++hohhoh+++++++++\n++++++++++++++" + dataListFriend.toString() + "\n++++++++");
            listFriendID = new ArrayList<>();
            if (dataListFriend.getListFriend().size() > 0) {

                for (Friend friend : dataListFriend.getListFriend()) {
                    listFriendID.add(friend.id);

                }
            }
        }
    }

    public void setImage(final String email, final ImageView avatar) {

        if (email != null) {
            FirebaseDatabase.getInstance().getReference().child("user").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getValue() != null) {
                        String id = ((HashMap) dataSnapshot.getValue()).keySet().iterator().next().toString();
                        HashMap userMap = (HashMap) ((HashMap) dataSnapshot.getValue()).get(id);
                        setImageAvatar(getApplicationContext(), (String) userMap.get("avata"), avatar);

                    } else {
                        setImageAvatar(getApplicationContext(), null, avatar);
                        System.out.println("++++++++++++++ in CommentAdapter the email in fire base is null ; " + email);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });
        } else
            System.out.println("++++++++++++++ the email in fire base is null ; ");


    }

    private void setImageAvatar(Context context, String imgBase64, ImageView avatar) {
        try {

            Resources res = context.getResources();
            Bitmap src;
            if (imgBase64.equals("default") || imgBase64.equals(null)) {
                src = BitmapFactory.decodeResource(res, R.drawable.default_avata);
            } else {
                byte[] decodedString = Base64.decode(imgBase64, Base64.DEFAULT);
                src = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            }
            avatar.setImageDrawable(ImageUtils.roundedImage(context, src));
        } catch (Exception e) {
        }
    }

    public void updateStatus(final int eid, final int pid, final String status) {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {


                StringRequest strReq = new StringRequest(Request.Method.POST,
                        "https://sloppier-citizens.000webhostapp.com/company/updateEmployeePostStatus.php", new Response.Listener<String>() {

                    @SuppressLint("LongLogTag")
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Register Response: " + response.toString());


                        try {
                            System.out.println("+++++++++++++" + response);
                            //  hideDialog();
                            JSONObject jObj = new JSONObject(response);
                            boolean success = jObj.getBoolean("success");


                            if (success) {
                                System.out.println("+++++++++++Successfly Updated");
                            } else {


                                Toast.makeText(getApplicationContext(),
                                        "Failed to UpdateStatus", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println("\ncatch a jason exception  in request\n");
                        }

                    }
                }, new Response.ErrorListener() {

                    @SuppressLint("LongLogTag")
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Registration Error: " + error.getMessage());
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_LONG).show();
                        // hideDialog();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        // Posting params to register url
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("pid", pid + "");
                        params.put("Eid", eid + "");
                        params.put("status", status);
                        return params;
                    }
                };
                // Adding request to request queue
                AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, "ActivityEmployeeViewProfile");


            }
        });


    }
    public String experienceToyears(String experience)
    {
        int exp=Integer.parseInt(experience);
        if(exp>11)
        {
            if(exp==12)
                return "1 year";

            if (exp == 6 * 12)
                return "6+ years";
            else{
                float year;
                year=(float)(exp)/12;

                if(year%1==0)
                    return  (int)(year)+" years";
                else
                    return year+" years";
            }
        }

        else{
            return exp+" month";
        }

    }


    public void getrecieverToken(final String email, final int state) {

        FirebaseDatabase.getInstance().getReference().child("user").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //System.out.println("++++++ the datasnapshot is "+dataSnapshot.toString());

                if( dataSnapshot.getValue().toString()==null)
                    System.out.println("+++\n++++++\n+++++\n++ No token for this email since it is not rejestered yet\n");
                else {
                    String id = ((HashMap) dataSnapshot.getValue()).keySet().iterator().next().toString();
                    HashMap userMap = (HashMap) ((HashMap) dataSnapshot.getValue()).get(id);

                    System.out.println("+++\n+++ Email is " + email + " token is " + (String) userMap.get("token") + "     namee  " + id + " staticUid is " + StaticConfig.UID + "\n my token is " + FirebaseInstanceId.getInstance()+"+++\n");


                    if(state==1)
                        sendPushNotificationToReceiver("JobConnector", CompanyAppliedPostsActivity
                                .companyName + " accepted your job Application", StaticConfig.UID, FirebaseInstanceId.getInstance().getToken(), (String) userMap.get("token"));
                    if(state==0)
                        sendPushNotificationToReceiver("JobConnector", CompanyAppliedPostsActivity.companyName + " rejected your job Application", StaticConfig.UID, FirebaseInstanceId.getInstance().getToken(), (String) userMap.get("token"));
                    if(state==2)
                        sendPushNotificationToReceiver("JobConnector", CompanyAppliedPostsActivity.companyName + " returned you to pending", StaticConfig.UID, FirebaseInstanceId.getInstance().getToken(), (String) userMap.get("token"));


                }
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("++++++ token is failed");

            }
        });
    }
    private void sendPushNotificationToReceiver(String username,
                                                String message,
                                                String uid,
                                                String firebaseToken,
                                                String receiverFirebaseToken) {
        FcmNotificationBuilder.initialize()
                .title(username)
                .message(message)
                .username(username)
                .uid(uid)
                .firebaseToken(firebaseToken)
                .receiverFirebaseToken(receiverFirebaseToken)
                .send();
    }

    public void getrecieverToken(final String email, final String mSender , final String message) {

        FirebaseDatabase.getInstance().getReference().child("user").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //
                // System.out.println("++++++ the datasnapshot is "+dataSnapshot.toString());

                if( dataSnapshot.getValue()==null)
                    System.out.println("+++\n++++++\n+++++\n++ No token for this email since it is not rejestered yet\n");
                else {
                    String id = ((HashMap) dataSnapshot.getValue()).keySet().iterator().next().toString();
                    HashMap userMap = (HashMap) ((HashMap) dataSnapshot.getValue()).get(id);

                    sendPushNotificationToReceiver(CompanyMainMyJobsFragment.company_name,  message, StaticConfig.UID, FirebaseInstanceId.getInstance().getToken(), (String) userMap.get("token"));


                }
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("++++++ token is failed");

            }
        });
    }

}
