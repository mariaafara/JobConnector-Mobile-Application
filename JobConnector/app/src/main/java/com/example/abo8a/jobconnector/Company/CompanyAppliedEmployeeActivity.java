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
import com.example.abo8a.jobconnector.EmployeeAdapter;
import com.example.abo8a.jobconnector.EmployeeData;
import com.example.abo8a.jobconnector.R;
import com.example.abo8a.jobconnector.login.OnBoardingActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CompanyAppliedEmployeeActivity extends AppCompatActivity {
    private EmployeeAdapter myAppAdapter; //Array Adapter
    private RecyclerView recyclerView; //RecyclerView
    private RecyclerView.LayoutManager mLayoutManager;
    private final static String url = "https://sloppier-citizens.000webhostapp.com/company/getAppliedEmployees.php";
    ArrayList<EmployeeData> data;
    public static int pid;
    public static String status;
    Toolbar toolbar;
    boolean comeFromNotification=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_applied_employee);
        Intent i = getIntent();
        status = i.getStringExtra("status");
        if(status==null) {
            status = "PENDING";
            comeFromNotification=true;
        }
        pid = i.getIntExtra("pid", -1);
        initialize();

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
            case android.R.id.home: {
                if(!comeFromNotification)
                finish();
                else
                {
                    Intent intent=new Intent(CompanyAppliedEmployeeActivity.this, OnBoardingActivity.class);
                    startActivity(intent);
                }
            }
            default:
                break;
        }
        return true;
    }

    private void initialize() {

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewAppliedEmployees); //Recylcerview Declaration
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);


    }


    private void init() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(status);
        data = new ArrayList<>();

        myAppAdapter = new EmployeeAdapter(getApplicationContext(), data);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    System.out.println("++++++++" + response);
                    JSONObject json = new JSONObject(response);

                    if (json.getBoolean("success")) {
                        Iterator<String> iter = json.keys();
                        while (iter.hasNext()) {
                            String key = iter.next();
                            try {

                                Object value = json.get(key);
                                if (!value.equals(true)) {
                                    String key1=iter.next();

                                    JSONObject jsonArray = (JSONObject) value;
                                    JSONObject jsonArray1=(JSONObject)json.get(key1);

                                    EmployeeData job_post = null;

                                    try {
                                        String ex = "";
                                        String l = "";
                                        if (jsonArray.getString("total").equals("unknown"))
                                            ex = jsonArray.getString("total");
                                        else
                                            ex = experienceToyears(jsonArray.getString("total"));

                                        if (jsonArray.getString("languages").equals("null"))
                                            l = "no specific languages";
                                        else
                                            l = jsonArray.getString("languages");
                                        job_post = new EmployeeData(jsonArray.getInt("Eid"), jsonArray.getString("fname"),
                                                jsonArray.getString("lname")
                                                , jsonArray.getString("city"),
                                                jsonArray.getString("category"), jsonArray.getString("LogInEmail"),
                                                ex,
                                                l,jsonArray1.getDouble("rating"),"");

                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }

                                    data.add(job_post);
                                    Collections.sort(data, new RateComparator());

                                    System.out.println(data.size());

                                    recyclerView.setAdapter(myAppAdapter);
                                }

                            } catch (JSONException e) {
                                // Something went wrong!
                                e.printStackTrace();
                                System.out.println("Json-- exeption --- error in response" +
                                        "------------------");
                            }
                        }
                    } else {
                        System.out.println("success not 1 ---------------------");
                        recyclerView.setAdapter(myAppAdapter);
                        Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_SHORT).show();


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
                params.put("status", status);
                return params;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);
    }


    public String experienceToyears(String experience) {
        if ((experience != "null"&&!experience.equals("No Experience"))) {
            int exp = Integer.parseInt(experience);
            if (exp > 11) {
                if (exp == 12)
                    return "1 year";

                if (exp == 6 * 12)
                    return "6+ years";
                else {
                    float year;
                    year = (float) (exp) / 12;

                    if (year % 1 == 0)
                        return (int) (year) + " years";
                    else
                        return year + " years";
                }
            } else {
                return exp + " month";
            }

        } else return "no experience";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        status = "";
    }
    public  class  RateComparator  implements Comparator<EmployeeData> {



        @Override
        public int compare(EmployeeData o1, EmployeeData o2) {
            return compareRate(o1.getRate(),o2.getRate());
        }
    }

    public int compareRate(Double d1, Double d2) {
        if(d1>d2)
            return -1;
        if(d1==d2)
            return 0;
        return 1;
    }

    @Override
    public void onBackPressed() {
        if(comeFromNotification)

        {
            Intent intent = new Intent(CompanyAppliedEmployeeActivity.this, OnBoardingActivity.class);
            startActivity(intent);
        }

        super.onBackPressed();
    }
}
