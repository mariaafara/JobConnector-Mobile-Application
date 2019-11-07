package com.example.abo8a.jobconnector.Company;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.abo8a.jobconnector.AppSingleton;
import com.example.abo8a.jobconnector.NonSwipeableViewPager;
import com.example.abo8a.jobconnector.R;
import com.example.abo8a.jobconnector.login.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.abo8a.jobconnector.Company.CompanyRegisterFragment1.bus_type;
import static com.example.abo8a.jobconnector.Company.CompanyRegisterFragment1.company_info;
import static com.example.abo8a.jobconnector.Company.CompanyRegisterFragment1.company_name;
import static com.example.abo8a.jobconnector.Company.CompanyRegisterFragment2.company_phone;
import static com.example.abo8a.jobconnector.Company.CompanyRegisterFragment2.company_url;

public class CompanyRegistration extends AppCompatActivity {

    public static NonSwipeableViewPager pager;
    public CompanyRegistration_onBoard_Adapter mAdapter;
    private int cid;
    public static Button btn_next;
    private ImageButton btn_back;

    private static final String URL_FOR_REGISTRATION = "https://sloppier-citizens.000webhostapp.com/company/registercompany.php";
    private static final String TAG = "CompanyRegistration";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_registration);

        init();
        initPager();
        Intent i = getIntent();
        cid = i.getIntExtra("cid", -1);

    }

    public void init() {
        company_name = findViewById(R.id.company_name);
        company_info = findViewById(R.id.company_info);
        bus_type = findViewById(R.id.bus_type);
        company_url = findViewById(R.id.company_url);
        company_phone = findViewById(R.id.company_phone);

    }

    private void registerCompany(final int cid, final String name, final String type, final String
            info, final String url, final String phone) {
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
                        LoginActivity.editor.putBoolean("foundBasic", true).commit();

                        String user = jObj.getString("username");
                        Toast.makeText(getApplicationContext(), "Hi " + user + ", You are successfully Added!", Toast.LENGTH_SHORT).show();

                        // Launch type of  comapny to determine registration forms
                        Intent intent = new Intent(CompanyRegistration.this, CompanyMainActivity.class);
                        intent.putExtra("cid", jObj.getInt("id"));
                        intent.putExtra("user",getIntent().getStringExtra("user"));
                        intent.putExtra("email",getIntent().getStringExtra("email"));
                        startActivity(intent);
                        finish();
                    } else {


                        Toast.makeText(getApplicationContext(),
                                "Failed ", Toast.LENGTH_LONG).show();
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

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("cid", cid + "");
                params.put("name", name);
                params.put("type", type);
                params.put("info", info);
                params.put("url", url);
                params.put("phone", phone);

                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }


    public void check_btn_next() {
        if (pager.getCurrentItem() == 0) {

            if (!TextUtils.isEmpty(company_name.getText().toString())
                    && !TextUtils.isEmpty(company_info.getText().toString())
                    && !TextUtils.isEmpty(bus_type.getText().toString())) {

            } else {
                btn_next.setFocusable(false);
                btn_next.setEnabled(false);

            }
        } else if (pager.getCurrentItem() == 1) {

            if (!TextUtils.isEmpty(company_url.getText().toString())
                    && !TextUtils.isEmpty(company_phone.getText().toString())) {
                btn_next.setFocusable(true);
                btn_next.setEnabled(true);
            } else {
                btn_next.setFocusable(false);
                btn_next.setEnabled(false);

            }
        }
    }

    public void initPager() {

        btn_next = findViewById(R.id.btn_Next);
        btn_back = findViewById(R.id.btn_back);
        pager = (NonSwipeableViewPager) findViewById(R.id.pager_company_register);
        mAdapter = new CompanyRegistration_onBoard_Adapter(getSupportFragmentManager());
        pager.setAdapter(mAdapter);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nextposition = pager.getCurrentItem() + 1;
                if (nextposition < 3) {
                    if (pager.getCurrentItem() == 0) {
                        if (validate1()) {
                            btn_back.setVisibility(View.VISIBLE);
                            pager.setCurrentItem(nextposition);
                        }
                    }

               else   if (pager.getCurrentItem() == 1) {

                        if (validate2()) {
                            btn_back.setVisibility(View.VISIBLE);
                            pager.setCurrentItem(nextposition);
                        }

                    }

                }
            else    if (pager.getCurrentItem() == 2) {

                    btn_next.setText("finish");
                }
                if (nextposition == 3)
                    registerCompany(cid, company_name.getText().toString(), bus_type.getText().toString(), company_info.getText().toString()
                            , company_url.getText().toString(), company_phone.getText().toString());


            }

        });


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int previousposition = pager.getCurrentItem() - 1;

                pager.setCurrentItem(previousposition);
                if (pager.getCurrentItem() != 2) {
                    btn_next.setText("Next");

                }
                if (pager.getCurrentItem() == 1)
                    btn_back.setVisibility(View.VISIBLE);
                if (pager.getCurrentItem() == 0)
                    btn_back.setVisibility(View.INVISIBLE);
            }
        });
    }

    public boolean validate1() {
        boolean valid = true;

        String fr2CompanyName = company_name.getText().toString();
        String fr2description = company_info.getText().toString();
        String fr2jobIn = bus_type.getText().toString();


        if (fr2CompanyName.isEmpty()) {
            company_name.setError("*");
            valid = false;
        }
        if (fr2description.isEmpty()) {
            company_info.setError("*");
            valid = false;
        }
        if (fr2jobIn.isEmpty()) {
            bus_type.setError("*");
            valid = false;
        }


        return valid;
    }

    public boolean validate2() {
        boolean valid = true;

        String fr2CompanyName = company_url.getText().toString();
        String fr2description = company_phone.getText().toString();


        if (fr2CompanyName.isEmpty()) {
            company_phone.setError("*");
            valid = false;
        }
        if (fr2description.isEmpty()) {
            company_url.setError("*");
            valid = false;
        }


        return valid;
    }

}
