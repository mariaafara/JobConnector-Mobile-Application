package com.example.abo8a.jobconnector;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.abo8a.jobconnector.chat.util.ImageUtils;
import com.example.abo8a.jobconnector.chat.util.data.SharedPreferenceHelper;
import com.example.abo8a.jobconnector.chat.util.data.StaticConfig;
import com.example.abo8a.jobconnector.chat.util.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class EmployeeProfile extends Fragment {

    private TextView link_addLanguage;
    private TextView link_MoreaddLanguage;
    public static RecyclerView recyclerView_languages;
    public static RecyclerView recyclerView_Experience;
    public static RecyclerView recyclerView_Skills;

    private TextView link_addSkill;

    private static final int PICK_IMAGE = 1994;

    ImageView avatar;
    private DatabaseReference userDB;
    private User myAccount;
    private LovelyProgressDialog waitingDialog;

    //********************************//
    private static TextView edu_uni;
    private static TextView edu_major;
    private static TextView edu_degree;
    private static TextView edu_start_date;
    private static TextView edu_completion_date;
    public static String uni;
    public static String major;
    public static String degree;
    public static String start;
    public static String end;
    //********************************//
    private TextView noExp;
    private static TextView profile_fname;
    private static TextView profile_lname;
    private static TextView profile_age;
    private static TextView profile_country;
    private static TextView profile_city;
    private static TextView profile_gender;
    private static TextView profile_phone;
    private static TextView profile_target_work;
    private static TextView profile_username;
    private static TextView link_addExperience;

    public static String fname;
    public static String lname;
    public static String age;
    public static String city;
    public static String country;
    public static String code;
    public static String number;

   String userEmail="";

    public static int goactivitylanguage = 0;
    /////////////////////////////////////////////////////////
    public static LanguageRecyclerAdapter languageAppAdapter; //Array Adapter
    public static ExperienceRecycleAdapter ExperienceAdapter; //Array Adapter
    public static SkillRecycleAdapter SkillAdapter; //Array Adapter


    private int id;
    private static RecyclerView.LayoutManager languageLayoutManager;
    private static RecyclerView.LayoutManager expLayoutManager;
    private static RecyclerView.LayoutManager skillLayoutManager;


    private static ArrayList<Language> languagedata;
    private static ArrayList<Experience> experienceData;
    private static ArrayList<Skill> skillData;

    ///////////////////////////
    private static TextView link_editEducation;
    private static TextView link_editPersonal;
    private Toolbar toolbar;
    private DataBaseHandler db;

    /////////////////////////////////////////////////////////
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        userDB = FirebaseDatabase.getInstance().getReference().child("user").child(StaticConfig.UID);

        System.out.println("++++++  UID IS  " + StaticConfig.UID);

        //userDB.addListenerForSingleValueEvent(userListener);
        // Inflate the layout for this fragment

        SharedPreferenceHelper prefHelper = SharedPreferenceHelper.getInstance(getActivity());
        myAccount = prefHelper.getUserInfo();
        if (myAccount != null) {
            //setupArrayListInfo(myAccount);
            System.out.println("++++++EmployeeProfile+++++++ the myaccount is not null   " + myAccount.email + "  " + myAccount.name + "  ");
        } else
            System.out.println("++++++EmployeeProfile+++++++ the myaccount is null");

        // setImageAvatar(getActivity(), myAccount.avata);
        waitingDialog = new LovelyProgressDialog(getActivity());


        return inflater.inflate(R.layout.fragment_employee_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
        init();
    }


    public void initialize() {
        id = user.id;
        System.out.println("+++++++  employee+++++++" + id);

        avatar = (ImageView) getActivity().findViewById(R.id.img_avatar);
        avatar.setOnClickListener(onAvatarClick);


        edu_uni = getActivity().findViewById(R.id.edu_uni);
        edu_major = getActivity().findViewById(R.id.edu_major);
        edu_degree = getActivity().findViewById(R.id.edu_degree);
        edu_start_date = getActivity().findViewById(R.id.edu_start_date);
        edu_completion_date = getActivity().findViewById(R.id.edu_completion_date);

        noExp = getActivity().findViewById(R.id.have_no_experience);
        profile_fname = getActivity().findViewById(R.id.profilefirstname);
        profile_lname = getActivity().findViewById(R.id.profilelastname);
        profile_age = getActivity().findViewById(R.id.profileage);
        profile_country = getActivity().findViewById(R.id.profilelocation_country);
        profile_city = getActivity().findViewById(R.id.profilelocation_city);
        profile_target_work = getActivity().findViewById(R.id.profiletargetWork);
        profile_gender = getActivity().findViewById(R.id.profilegender);
        profile_phone = getActivity().findViewById(R.id.profilephone);
        profile_username = getActivity().findViewById(R.id.profileUsername);
        //  profileImage=findViewById(R.id.profileImage);
        link_addSkill = getActivity().findViewById(R.id.link_addSkills);

        link_addLanguage = getActivity().findViewById(R.id.link_addLanguages);
        link_MoreaddLanguage = getActivity().findViewById(R.id.link_addmoreLanguages);
        link_addExperience = getActivity().findViewById(R.id.link_addExperience);
        ///////////////////////////////////////////////
        ///////////////////////////////////////////////
        link_editEducation = getActivity().findViewById(R.id.edit_education);

        link_editPersonal = getActivity().findViewById(R.id.edit_personalinfo);
        link_editEducation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), editEducation.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
        link_editPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditPersonal.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
        //////////////////////////////////////////
        ///////////////////////////////////////////////
        recyclerView_languages = getActivity().findViewById(R.id.listView_languages);
        recyclerView_Experience = getActivity().findViewById(R.id.listView_experience);
        recyclerView_Skills = getActivity().findViewById(R.id.listView_skills);


        link_addLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), Languages.class);
                intent.putExtra("id", id);

                ArrayList<String> langs = new ArrayList<String>();
                for (int j = 0; j < languagedata.size(); j++) {
                    langs.add(languagedata.get(j).getLanguage());
                }

                intent.putExtra("arrayList", langs);

                startActivity(intent);
            }
        });
        link_MoreaddLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), Languages.class);
                intent.putExtra("id", id);

                ArrayList<String> langs = new ArrayList<String>();
                for (int j = 0; j < languagedata.size(); j++) {
                    langs.add(languagedata.get(j).getLanguage());
                }

                intent.putExtra("arrayList", langs);

                startActivity(intent);

                //overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        link_addSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), addSkill.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        link_addExperience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), addExperience.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
        //hon lzm shuf bl db iza id l user 3ndoo languagess or no
        //iza m3ndoo bynlo bs l add lang
        if (goactivitylanguage == 0) {
            link_MoreaddLanguage.setVisibility(View.GONE);
            link_addLanguage.setVisibility(View.VISIBLE);

        } else if (goactivitylanguage != 0) { //izaa 3ndooo bynlo l add more wb3rdloo languages l3ndo hne bl database
            link_MoreaddLanguage.setVisibility(View.VISIBLE);
            link_addLanguage.setVisibility(View.GONE);

            //so holl 3mlon comment cz byt8yroo cz alreaddy bss tnzed lo8a 3l database 3m y3rdon bikoun


            languageAppAdapter = new LanguageRecyclerAdapter(getActivity(), languagedata);
            recyclerView_languages.setAdapter(languageAppAdapter);
        }

        //NOTEE ++++++++++++bl adapter kbst l remove btm7e l lang mnl db



        //recyclerView.setHasFixedSize(true);
        // use a linear layout manager

        languageLayoutManager = new LinearLayoutManager(getActivity());
        expLayoutManager = new LinearLayoutManager(getActivity());
        skillLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView_Skills.setLayoutManager(skillLayoutManager);
        recyclerView_Skills.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));

        recyclerView_Experience.setLayoutManager(expLayoutManager);
        recyclerView_Experience.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        recyclerView_languages.setLayoutManager(languageLayoutManager);
        recyclerView_languages.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
    }

    @SuppressLint("WrongViewCast")
    public void init() {

        fillData();
    }

    public void fillData() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());


        final String url = "https://sloppier-citizens.000webhostapp.com/job/getInfo" +
                ".php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject json = new JSONObject(response);


                    if (json.getBoolean("success")) {

                        profile_fname.setText(json.getString("fname"));
                        profile_lname.setText(json.getString("lname"));
                        profile_age.setText(json.getString("age"));
                        profile_city.setText(json.getString("city"));
                        profile_gender.setText(json.getString("gender"));
                        profile_phone.setText(json.getString("contact_nb"));
                        userEmail=json.getString("loginemail");
                        ////////////////////////////////////////////////////////////////////
                        profile_target_work.setText(json.getString("current_work"));
                        //   profile_username.setText("@" + json.getString("username"));
                        profile_username.setVisibility(View.GONE);
                        ///////////////////////////////////////////////////////////////////////
                        fname = json.getString("fname");
                        lname = json.getString("lname");
                        age = json.getString("age");
                        city = json.getString("city");
                        country = json.getString("country");

                        String phone = json.getString("contact_nb");

                        code = "+961";
                        number = phone;

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
                            languageAppAdapter = new LanguageRecyclerAdapter(getActivity(), languagedata);
                            recyclerView_languages.setAdapter(languageAppAdapter);

                        }


                        JSONArray jsonExperience = json.getJSONArray("experience");
                        experienceData = new ArrayList<Experience>();

                        for (int i = 0; i < jsonExperience.length(); i++) {
                            noExp.setVisibility(View.GONE);
                            JSONObject j = (JSONObject) jsonExperience.get(i);

                            Experience E = new Experience(j.getInt("exid"), j.getString("job_in")
                                    ,experienceToyears( j.getInt("duration")+""), j.getString("company_name"), j.getString("job_country"), j.getString("job_city"), j.getString("descreption"));
                            experienceData.add(E);
                            ExperienceAdapter = new ExperienceRecycleAdapter(getActivity(), experienceData);
                            recyclerView_Experience.setAdapter(ExperienceAdapter);

                        }

                        JSONArray jsonSkills = json.getJSONArray("skills");
                        skillData = new ArrayList<Skill>();
                        SkillAdapter = new SkillRecycleAdapter(getActivity(), skillData);

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
                Toast.makeText(getActivity().getApplicationContext(), "Check your Internet Connection and try Again", Toast.LENGTH_LONG).show();

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
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(stringRequest);

        setImage(user.email,avatar);

    }


    private View.OnClickListener onAvatarClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            new AlertDialog.Builder(getActivity())
                    .setMessage("Are you sure want to change your profile?")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_PICK);
                            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();
        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                Toast.makeText(getActivity(), "No Image Selected", Toast.LENGTH_LONG).show();
                return;
            }
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(data.getData());

                Bitmap imgBitmap = BitmapFactory.decodeStream(inputStream);
                imgBitmap = ImageUtils.cropToSquare(imgBitmap);
                InputStream is = ImageUtils.convertBitmapToInputStream(imgBitmap);
                final Bitmap liteImage = ImageUtils.makeImageLite(is,
                        imgBitmap.getWidth(), imgBitmap.getHeight(),
                        ImageUtils.AVATAR_WIDTH, ImageUtils.AVATAR_HEIGHT
                );
                avatar.setImageDrawable(ImageUtils.roundedImage(getActivity(), liteImage));

                String imageBase64 = ImageUtils.encodeBase64(liteImage);
                myAccount.avata = imageBase64;

                waitingDialog.setCancelable(false)
                        .setTitle("Profile updating....")
                        .setTopColorRes(R.color.colorPrimary)
                        .show();

                userDB.child("avata").setValue(imageBase64)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    waitingDialog.dismiss();
                                    SharedPreferenceHelper preferenceHelper = SharedPreferenceHelper.getInstance(getActivity());
                                    preferenceHelper.saveUserInfo(myAccount);
                                    avatar.setImageDrawable(ImageUtils.roundedImage(getActivity(), liteImage));

                                    new LovelyInfoDialog(getActivity())
                                            .setTopColorRes(R.color.colorPrimary)
                                            .setTitle("Success")
                                            .setMessage("Update profile successfully!")
                                            .show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                waitingDialog.dismiss();
                                Log.d("Update Profile", "failed");
                                new LovelyInfoDialog(getActivity())
                                        .setTopColorRes(R.color.colorAccent)
                                        .setTitle("False")
                                        .setMessage("False to update profile")
                                        .show();
                            }
                        });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }




    public void setImage(final String email, final ImageView avatar) {

        if (email != null) {
            FirebaseDatabase.getInstance().getReference().child("user").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    System.out.println("++++n+++"+email+"++++++++ dataSnap shot in EmployeeProfille is \n"+dataSnapshot.toString());
                    if (dataSnapshot.getValue() != null) {
                        String id = ((HashMap) dataSnapshot.getValue()).keySet().iterator().next().toString();
                        HashMap userMap = (HashMap) ((HashMap) dataSnapshot.getValue()).get(id);
                        setImageAvatar(getActivity(), (String) userMap.get("avata"), avatar);

                    } else {
                        setImageAvatar(getActivity(), null, avatar);
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
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
