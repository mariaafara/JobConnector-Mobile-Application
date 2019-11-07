package com.example.abo8a.jobconnector.Company;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.abo8a.jobconnector.ApplyData;
import com.example.abo8a.jobconnector.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CompanyAppliedPostsActivity extends AppCompatActivity {

    private ApplyAdapter myAppAdapter; //Array Adapter
    private RecyclerView recyclerView; //RecyclerView
    private RecyclerView.LayoutManager mLayoutManager;
    private final static String url = "https://sloppier-citizens.000webhostapp.com/company/getnbrApplied.php";
    ArrayList<ApplyData> data;
    int nbr_emp;
    static int cid;
    int pid;
    public static String companyName="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_applied_posts_activity);
        cid = CompanyMainActivity.cid;

        initialize();

        Intent i = getIntent();
        pid = i.getIntExtra("pid", -1);
        companyName=i.getStringExtra("companyName");
        nbr_emp = i.getIntExtra("applicants", -1);
    }

    private void initialize() {

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewAppliedEmployees); //Recylcerview Declaration
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        data = new ArrayList<ApplyData>();

    }

    @Override
    protected void onResume() {
        super.onResume();
        init();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

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


    private void init() {

        data.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    System.out.println("++++++++" + response);
                    JSONObject jObj = new JSONObject(response);

                    if (jObj.getBoolean("success")) {

                        ApplyData pend = new ApplyData(pid, cid, "PENDING", jObj.getInt("pending"));
                        ApplyData acce = new ApplyData(pid, cid, "Accepted", jObj.getInt("accepted"));
                        ApplyData reje = new ApplyData(pid, cid, "Rejected",jObj.getInt("rejected"));
                        data.add(pend);
                        data.add(acce);
                        data.add(reje);
                        myAppAdapter = new ApplyAdapter(getApplicationContext(), data);
                        recyclerView.setAdapter(myAppAdapter);

                    } else {
                        System.out.println("success not 1 ---------------------");
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

                params.put("pid", pid + "");// etsearch.getText().toString()

                return params;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);


    }

}
