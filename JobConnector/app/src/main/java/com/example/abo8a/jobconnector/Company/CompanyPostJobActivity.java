package com.example.abo8a.jobconnector.Company;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.abo8a.jobconnector.AppSingleton;
import com.example.abo8a.jobconnector.NonSwipeableViewPager;
import com.example.abo8a.jobconnector.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.abo8a.jobconnector.Company.CompanyJobDescriptionFragment.job_pos;
import static com.example.abo8a.jobconnector.Company.CompanyJobGenderFragment.females;
import static com.example.abo8a.jobconnector.Company.CompanyJobGenderFragment.males;
import static com.example.abo8a.jobconnector.Company.CompanyJobGenderFragment.no;
import static com.example.abo8a.jobconnector.Company.CompanyJobGenderFragment.yes;


public class CompanyPostJobActivity extends AppCompatActivity {

    public static NonSwipeableViewPager pager;
    public CompanyPostJob_onBoard_Adapter mAdapter;
    private int id;
    public static Button btn_next;
    private ImageButton btn_back;
    int cid;
    private static final String URL_FOR_REGISTRATION = "https://sloppier-citizens.000webhostapp.com/company/postJob.php";
    private static final String TAG = "CompanyPostJobActivity";
    ProgressDialog progressDialog;

    // EditText seekBarvalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_post_job_activity);

        //Intent i = getIntent();
        cid = CompanyMainActivity.cid;

        init();
        males = (RadioButton) findViewById(R.id.btn_males);
        females = (RadioButton) findViewById(R.id.btn_females);
        //  seekBarvalue = findViewById(R.id.min_experience_seekbarvalue);


    }

    public void init() {

        btn_next = findViewById(R.id.btn_Next);
        btn_back = findViewById(R.id.btn_back);
        pager = (NonSwipeableViewPager) findViewById(R.id.pager_company_register);
        mAdapter = new CompanyPostJob_onBoard_Adapter(getSupportFragmentManager());
        pager.setAdapter(mAdapter);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nextposition = pager.getCurrentItem() + 1;
                if (nextposition < 6) {
                    if (pager.getCurrentItem() == 0) {
                        btn_back.setVisibility(View.VISIBLE);
                        if (!job_pos.getText().toString().equals("")) {
                            CompanyPostJobActivity.btn_next.setFocusable(true);
                            CompanyPostJobActivity.btn_next.setEnabled(true);
                            CompanyPostJobActivity.btn_next.setBackgroundResource(R.drawable.buttonscontinuestyle);
                        } else {
                            CompanyPostJobActivity.btn_next.setFocusable(false);
                            CompanyPostJobActivity.btn_next.setEnabled(false);
                            CompanyPostJobActivity.btn_next.setBackgroundResource(R.drawable.buttonscontinuestyledisabled);
                        }

                    }
                    if (pager.getCurrentItem() == 1) {
                        btn_back.setVisibility(View.VISIBLE);
                            CompanyPostJobActivity.btn_next.setFocusable(true);
                            CompanyPostJobActivity.btn_next.setEnabled(true);
                            CompanyPostJobActivity.btn_next.setBackgroundResource(R.drawable.buttonscontinuestyle);


                    }
                    if (pager.getCurrentItem() == 2) {
                            CompanyPostJobActivity.btn_next.setFocusable(true);
                            CompanyPostJobActivity.btn_next.setEnabled(true);
                            CompanyPostJobActivity.btn_next.setBackgroundResource(R.drawable.buttonscontinuestyle);


                    }
                    if (pager.getCurrentItem() == 3) {
                        btn_next.setFocusable(true);
                        btn_next.setEnabled(true);
                        btn_next.setBackgroundResource(R.drawable.buttonscontinuestyle);
                    }
                    if (pager.getCurrentItem() == 4) {
                        btn_next.setFocusable(true);
                        btn_next.setEnabled(true);
                        btn_next.setBackgroundResource(R.drawable.buttonscontinuestyle);
                    }
                    pager.setCurrentItem(nextposition);
                    if (pager.getCurrentItem() == 5) {
                        btn_next.setText("finish");
                        btn_next.setFocusable(true);
                        btn_next.setEnabled(true);
                        btn_next.setBackgroundResource(R.drawable.buttonscontinuestyle);


                    }

                } else {//6 ... kbset finish y3ne
                    String language = "";
                    for (int i = 0; i < CompanyJobLanguagesFragment.array.size(); i++) {
                        //  Toast.makeText(getApplicationContext(), "ListView Selected Values = " + CompanyJobLanguagesFragment.array.get(i), Toast.LENGTH_SHORT).show();
                        System.out.println(CompanyJobLanguagesFragment.array.get(i));
                        if (i == CompanyJobLanguagesFragment.array.size() - 1)
                            language += CompanyJobLanguagesFragment.array.get(i);
                        else
                            language += CompanyJobLanguagesFragment.array.get(i) + ",";
                    }
                    String category = CompanyJobCategoryFragment.adapter.getSelectedItem();
                    String experience = CompanyJobExperienceFragment.seekBarvalue.getText().toString();
                    String desc = CompanyJobDescriptionFragment.job_desc.getText().toString();
                    String pos = CompanyJobDescriptionFragment.job_pos.getText().toString();
                    String gender = "";
                    String city = CompanyJobLocationFragment.job_city.getSelectedItem().toString();
                    String country = "Lebanon";
                    boolean correct = false;

                    if (no.isChecked()) {
                        gender = "";
                        correct = true;
                    } else if (yes.isChecked()) {
                        if (females.isChecked()) {
                            gender = "female";
                            correct = true;
                        } else if (males.isChecked()) {
                            gender = "male";
                            correct = true;
                        } else {//if neither male nor female
                            Toast.makeText(getApplicationContext(), "please select males or females", Toast.LENGTH_SHORT).show();
                            correct = false;
                        }
                    }// end of yes  selected
                    else {
                        //neither  both selected
                        gender = "";
                        //add the post
                        post(cid,pos,desc,city,country,category,gender,experience,language);
                    }
                    if (correct == true) {
                        //add post
                        post(cid,pos,desc,city,country,category,gender,experience,language);
                    }

                }//kabaset lfinnish

            }

        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int previousposition = pager.getCurrentItem() - 1;

                pager.setCurrentItem(previousposition);
                if (pager.getCurrentItem() != 5) {
                    btn_next.setText("Next");
                    btn_next.setFocusable(true);
                    btn_next.setEnabled(true);
                    btn_next.setBackgroundResource(R.drawable.buttonscontinuestyle);

                }
                if (pager.getCurrentItem() == 1)
                    btn_back.setVisibility(View.VISIBLE);
                if (pager.getCurrentItem() == 0) {
                    //hon lezm erj3 3l activity lcopany eb3t id  luser
                    btn_back.setVisibility(View.GONE);
                }
            }
        });
    }

    private void post(final int cid,final String pos, final String desc, final String city, final String
            country, final String category, final String gender, final String exp, final String lang) {
        // Tag used to cancel the request
        String cancel_req_tag = "register";

        // progressDialog.setMessage("Adding you ...");
        //showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_FOR_REGISTRATION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());


                try {
                    System.out.println("+++++++++++++" + response);
                    //  hideDialog();
                    JSONObject jObj = new JSONObject(response);
                    boolean success = jObj.getBoolean("success");


                    if (success) {

                        CompanyMainActivity.nbr_active_posts++;
                        // Launch type of  comapny to determine registration forms
                        Intent intent = new Intent(CompanyPostJobActivity.this, CompanyMainActivity.class);
                        intent.putExtra("cid", cid);
                        intent.putExtra("nbr_active_posts",CompanyMainActivity.nbr_active_posts+1);
                        //kmen ymkn bdo email
                        intent.putExtra("email",CompanyMainActivity.email);

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
                // hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("cid", cid + "");
               // params.put("job", job);
                params.put("desc", desc);
                params.put("city", city);
                params.put("country", country);
                params.put("category", category);
                params.put("exp",experienceToMonthes(exp)+"");
                params.put("position", pos);
                params.put("gender", gender);
                params.put("languageString", lang);
                params.put("email",CompanyMainActivity.email);
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }
    public int experienceToMonthes(String experience)
    {
        String s[]=  experience.split(" ");
        if(s[1].equals("month"))
            return Integer.valueOf(s[0]);
        else
        if(s[0].equals("6+"))
            return 6*12;
        else
            if(experience.equals("No Experience"))
                return 0;
        else
            return (int)(Float.valueOf(s[0])*12);

    }
}
