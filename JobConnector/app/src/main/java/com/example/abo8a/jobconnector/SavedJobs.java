package com.example.abo8a.jobconnector;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class SavedJobs extends Fragment {

    private JobAdapter myAppAdapter; //Array Adapter
    private RecyclerView recyclerView; //RecyclerView
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<JobData> data;
    ArrayList<Integer> datafound;
    private final static String url = "https://sloppier-citizens.000webhostapp.com/job/getSavedPosts.php";

    TextView noSaved;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved_jobs, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        initialize();
        init();
    }

    private void initialize() {
        noSaved=getActivity().findViewById(R.id.no_saved);
        recyclerView = (RecyclerView)getActivity(). findViewById(R.id.recyclerViewSavedPosts); //Recylcerview Declaration
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(),
                DividerItemDecoration.VERTICAL));


    }

    private void init() {
        data = new ArrayList<>();
        datafound=new ArrayList<>();
        myAppAdapter = new JobAdapter(getActivity().getApplicationContext(), data);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {


                    System.out.println("++++++++"+response);
                    JSONObject json = new JSONObject(response);



                    if (json.getBoolean("success")) {

                        noSaved.setVisibility(View.GONE);
                        Iterator<String> iter = json.keys();

                        while (iter.hasNext()) {
                            String key = iter.next();
                            System.out.println("+_+_+_+_+_+_ key is "+key);
                            try {

                                Object value = json.get(key);

                                if (!value.equals(true)) {
                                    Object languageValue=json.get(iter.next());
                                    JSONObject jsonArray = (JSONObject) value;
                                    JSONObject jsonLang=(JSONObject)languageValue;

                                    String Key3=iter.next();
                                    Object statusValue=json.get(Key3);
                                    String status="";
                                    if(!json.get(Key3).equals(null)) {
                                        JSONObject jsonStat=(JSONObject)statusValue;
                                        status = jsonStat.getString("status");
                                    }

                                    JobData job_post = null;
                                    try {
                                        if(!datafound.contains(jsonArray.getInt("Pid"))) {
                                            job_post = new JobData(jsonArray.getInt("Pid"), jsonArray.getInt("Cid"),

                                                    jsonArray.getString("name"), jsonArray.getString("job_type")

                                                    , jsonArray.getString("job_city"),

                                                    jsonArray.getString("post_time"), experienceToyears(jsonArray.getString("minExperience")), jsonArray.getString("gender"), jsonArray.getString("chatEmail"), jsonArray.getString("job_description"), jsonArray.getString("job_city"), jsonArray.getString("job_category"), jsonLang.getString("languages"), status, 1);
                                            data.add(job_post);
                                            datafound.add(jsonArray.getInt("Pid"));
                                        }

                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }
                                    Collections.sort(data, new DateComparator());

                                    myAppAdapter = new JobAdapter(getActivity().getApplicationContext(), data);
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
                        noSaved.setVisibility(View.VISIBLE);


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
        })  {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();



                params.put("eid",user.id+"");// etsearch.getText().toString()
                System.out.println("+++___+_+_+_+_+_+_ id is "+user.id);

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(stringRequest);


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
            if(exp==0)
                return "No Experience";
            return exp+" month";
        }

    }

    public  class  DateComparator  implements Comparator<JobData> {



        @Override
        public int compare(JobData o1, JobData o2) {
            return compareDate(o1.getmTime().split(" ")[0],o2.getmTime().split(" ")[0]);
        }
    }

    public int compareDate(String d1, String d2) {
        String[] a1 = d1.split("-");
        String[] a2 = d2.split("-");

        if (Integer.parseInt(a1[0]) > Integer.parseInt(a2[0]))
            return -1;
        if (Integer.parseInt(a1[0]) == Integer.parseInt(a2[0])) {
            if (Integer.parseInt(a1[1]) > Integer.parseInt(a2[1]))
                return -1;
            if (Integer.parseInt(a1[1]) == Integer.parseInt(a2[1])) {
                if (Integer.parseInt(a1[2]) > Integer.parseInt(a2[2]))
                    return -1;
                else
                if (Integer.parseInt(a1[2]) == Integer.parseInt(a2[2]))
                    return 0;
            }
        }


        return 1;
    }


}
