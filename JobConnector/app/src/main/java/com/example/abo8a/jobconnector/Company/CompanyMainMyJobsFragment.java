package com.example.abo8a.jobconnector.Company;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.abo8a.jobconnector.PostAdapter;
import com.example.abo8a.jobconnector.PostData;
import com.example.abo8a.jobconnector.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class CompanyMainMyJobsFragment extends Fragment {

    public static PostAdapter myAppAdapter; //Array Adapter
    public static RecyclerView recyclerView; //RecyclerView
    private RecyclerView.LayoutManager mLayoutManager;
    private final static String url = "https://sloppier-citizens.000webhostapp.com/company/getPostedJobs.php";
    ArrayList<PostData> data;
    int nbr_active_posts;
    static int cid;
    FloatingActionButton addflot;
    public static ArrayList<String> jobstype;
    public static ArrayList<Integer> jobsId;
    public static ArrayList<String> jobscity;
    public static ArrayList<String> jobsdescs;
    public static String company_name;
    public static String company_phone;

    // private Spinner jobsspinner;
    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v;

//        if(CompanyMainActivity.nbr_active_posts==0)
//            v= inflater.inflate(R.layout.company_post_job_fragment, container, false);
//        else
//        if(CompanyMainActivity.nbr_active_posts>0)
        v = inflater.inflate(R.layout.company_main_my_jobs_fragment, container, false);
        return v;


    }

    @Override
    public void onStart() {
        super.onStart();

        jobsId = new ArrayList<>();
        jobstype = new ArrayList<>();
        jobscity = new ArrayList<>();
        jobsdescs = new ArrayList<>();
//        jobsId.clear();
//        jobstype.clear();


        nbr_active_posts = CompanyMainActivity.nbr_active_posts;
        cid = CompanyMainActivity.cid;

        initialize();
        init();
        addflot = getActivity().findViewById(R.id.morePostJob);
        addflot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(getContext(), CompanyPostJobActivity.class);//ActivitySearch
                startActivity(k);
            }
        });

    }

    private void initialize() {

        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerViewForCompanyMyJobs); //Recylcerview Declaration
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);


    }

    private void init() {
        data = new ArrayList<>();

        myAppAdapter = new PostAdapter(getActivity().getApplicationContext(), data);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    System.out.println("++++++++" + response);
                    JSONObject json = new JSONObject(response);

                    if (json.getBoolean("success")) {
                        // String name = json.getString("name");
                        Iterator<String> iter = json.keys();

                        while (iter.hasNext()) {
                            String key = iter.next();
                            //     System.out.println("+_+_+_+_+_+_ key is "+key);
                            try {

                                Object value = json.get(key);
                                if (!value.equals(true)) {
                                    JSONObject jsonArray = (JSONObject) value;

                                    PostData job_post = null;
                                    try {
                                        job_post = new PostData(jsonArray.getInt("Pid"), cid, jsonArray.getString("job_type")
                                                , jsonArray.getString("name"), jsonArray.getInt(
                                                "nbrEmployee"
                                        ));
                                        company_name = jsonArray.getString("name");
                                        company_phone=jsonArray.getString("phone");
                                        jobsId.add(jsonArray.getInt("Pid"));
                                        jobstype.add(jsonArray.getString("job_type"));
                                        jobscity.add(jsonArray.getString("job_city"));
                                        jobsdescs.add(jsonArray.getString("job_description"));
                                        data.add(job_post);
                                        System.out.println(data.size());
                                        for (int i = 0; i < data.size(); i++)
                                            System.out.println(data.get(i).getCompany_name());
                                        recyclerView.setAdapter(myAppAdapter);
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }


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
                        //Toast.makeText(getActivity().getApplicationContext(), "No Data Found", Toast.LENGTH_SHORT).show();


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
                Toast.makeText(getActivity().getApplicationContext(), "Check your Internet Connection and try Again", Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();

                params.put("cid", cid + "");// etsearch.getText().toString()

                System.out.println("++++++++++++++ params in Companymainmyjobs are "+params.toString());
                return params;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(stringRequest);


    }

}
