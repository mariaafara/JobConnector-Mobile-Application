package com.example.abo8a.jobconnector;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
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
import java.util.List;
import java.util.Map;

import static com.example.abo8a.jobconnector.SearchedJobsFragment.no_results;


public class EmployeeSearchActivity extends AppCompatActivity {
    private ViewPager pager;
    public static Context context;
    private SearchedResultPagerAdapter adapter;
    public static Button btnOk;
    public static Dialog customDialog;
    public List<String> languages;
    public static ImageButton fireSearch;
    public static ImageButton filterSearch;
    public static  ClearableEditText searchViewlocation;
    MultiSelectSpinner multiSpinner;
    // SearchView searchViewJob;
    public static SearchView searchViewJob;

    String selectedLanguages="";

    EditText companyname;
    TextView seekBarvalue;
    public static String gender="";


    public RecyclerView jobrecyclerView;
    private JobAdapter myAppAdapter; //Array Adapter
    private RecyclerView.LayoutManager mLayoutManager;
    private final static String url = "https://sloppier-citizens.000webhostapp.com/job/getPosts.php";

   static ArrayList<JobData> data;
  static int counterforjobData=0;
    ProgressDialog progressDialog;
    public static String queryFromsearch = "";
    static List<Address> addresses;

     static private String q="All Jobs in All Locations";
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_search_activity);
        start();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void start(){
        context=this.getApplicationContext();
        final android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbarsearch);
        setSupportActionBar(toolbar);

        //this line shows back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsingsearch);


        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_barsearch);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    // collapsingToolbar.setTitle("mmm");

                    getSupportActionBar().setTitle(q);

                    toolbar.setVisibility(View.VISIBLE);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle("");
                    toolbar.setVisibility(View.GONE);
                    isShow = false;
                }
            }
        });
        NestedScrollView scrollView = (NestedScrollView) findViewById(R.id.nest_scrollview);
        scrollView.setFillViewport(true);

        initPager();
        initialize();
        //  init();


        String location=searchViewlocation.getText().toString();
        if(location==null)
            location="";
        if ( !location.equals(""))
            q = "All Jobs in " + searchViewlocation.getText().toString();
        else  q = "All Jobs in All Locations";

    }

    private void initPager() {
        pager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new SearchedResultPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    public void initialize() {

        Intent i=getIntent();

        no_results=findViewById(R.id.no_results_found);
        fireSearch=findViewById(R.id.fireSearch);

        filterSearch=findViewById(R.id.filterlist);
        searchViewJob = (SearchView) findViewById(R.id.searchViewLocation);
        searchViewlocation = (ClearableEditText)findViewById(R.id.searchViewJob);
        searchViewJob.setQueryHint("Search for Job");
        searchViewJob.setIconified(false);
        searchViewJob.clearFocus();
        searchViewJob.setFocusable(false);
        fireSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String job=searchViewJob.getQuery().toString();
                if(job==null)
                    job="";

                String location=searchViewlocation.getText().toString();
                if(location==null)
                    location="";
                if (job.equals("") && location.equals(""))
                    q = "All Jobs in All Locations";//All Locations
                else if (job.equals("") && !location.equals(""))
                    q = "All Jobs in " + searchViewlocation.getText().toString();
                else if (!job.equals("") && !location.equals(""))
                    q = searchViewJob.getQuery().toString() + " in " + searchViewlocation.getText().toString();
                else if (!job.equals("") && location.equals(""))
                    q = ""+searchViewJob.getQuery().toString() + " in All Locations";
                init();
            }
        });
        searchViewJob.setOnQueryTextListener(new SearchView.OnQueryTextListener() {


            @Override
            public boolean onQueryTextSubmit(String query) {

                String job=searchViewJob.getQuery().toString();
                if(job==null)
                    job="";

                String location=searchViewlocation.getText().toString();
                if(location==null)
                    location="";
                if (job.equals("") && location.equals(""))
                    q = "All Jobs in All Locations";//All Locations
                else if (job.equals("") && !location.equals(""))
                    q = "All Jobs in " + searchViewlocation.getText().toString();
                else if (!job.equals("") && !location.equals(""))
                    q = searchViewJob.getQuery().toString() + " in " + searchViewlocation.getText().toString();
                else if (!job.equals("") && location.equals(""))
                    q = ""+searchViewJob.getQuery().toString() + " in All Locations";

                init();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Called when the query text is changed by the user.
                return true;
            }
        });
        filterSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();
            }
        });

        if(i.getStringExtra("location")!=null)
        {
            searchViewlocation.setText(i.getStringExtra("location")
            );
        }


    }


    private void showCustomDialog() {

        customDialog = new Dialog(EmployeeSearchActivity.this);

// the setContentView() method is used to set the custom layout for the dialog

        customDialog.setContentView(R.layout.employee_search_filter);
        final RadioGroup genderRadioGroup;


        SeekBar seekBar;

        gender = "";
        final String langs[] = {"English", "Arabic", "French", "Chinese", "Portuguese", "Japanese", "Bengali", "Russian",
                "Swedish", "Italian", "German", "Spanish"};
        languages=new ArrayList<String>();
// using window set the hight and width of custom dialog

        for(int i=0;i<langs.length;i++)
        {
            languages.add(langs[i]);
        }

        Window window = customDialog.getWindow();

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        window.setGravity(Gravity.CENTER);


// bind all controlss with custom dialog



        multiSpinner=customDialog.findViewById(R.id.multiSpinner);

        btnOk = (Button) customDialog.findViewById(R.id.btnOK);
        seekBar=(SeekBar) customDialog.findViewById(R.id.seekBarfilter);
        genderRadioGroup = (RadioGroup) customDialog.findViewById(R.id.genderRadioGroup);
        seekBarvalue=customDialog.findViewById(R.id.tvExperienceFilter);



        multiSpinner.setItems(langs);




        companyname = (EditText) customDialog.findViewById(R.id.Company_name_filter);




        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {


                if (checkedId == R.id.radioButtonMale) {

                    gender = "Male";

                } else if (checkedId == R.id.radioButtonFemale) {

                    gender = "Female";

                }
                else
                    gender="";
            }

        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                if (progress == 0)
                    seekBarvalue.setText(  "No Experience");
                if (progress >= 1 && progress < 12)
                    seekBarvalue.setText(String.valueOf(progress) + " month");
                else if (progress == 12) {
                    int value = 1;
                    seekBarvalue.setText(value + " year");
                } else if (progress > 12 && progress <= 13) {
                    double value = 1.5;
                    seekBarvalue.setText(value + " years");
                } else if (progress > 13 && progress <= 14) {
                    int value = 2;
                    seekBarvalue.setText(value + " years");
                } else if (progress > 14 && progress <= 15) {
                    double value = 2.5;
                    seekBarvalue.setText(value + " years");
                } else if (progress > 15 && progress <= 16) {
                    int value = 3;
                    seekBarvalue.setText(value + " years");
                } else if (progress > 16 && progress <= 17) {
                    double value = 3.5;
                    seekBarvalue.setText(value + " years");
                } else if (progress > 17 && progress <= 18) {
                    int value = 4;
                    seekBarvalue.setText(value + " years");
                } else if (progress > 18 && progress <= 19) {
                    seekBarvalue.setText(4.5 + " years");
                } else if (progress > 19 && progress <= 20) {

                    seekBarvalue.setText(5 + " years");
                } else if (progress > 20 && progress <= 21) {

                    seekBarvalue.setText(5.5 + " years");
                } else if (progress > 21 && progress <= 22) {

                    seekBarvalue.setText(6 + "+ years");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                customDialog.dismiss();

                selectedLanguages=multiSpinner.buildSelectedItemString();
                init();



            }

        });


        customDialog.show();// this show() method is used to show custom dialog

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        counterforjobData=0;
        System.out.println("++++++++++++ onDestroy is called");
    }

    private void init() {

        counterforjobData=1;
        data = new ArrayList<>();


        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        progressDialog.setMessage("Getting Data,Please wait...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://sloppier-citizens.000webhostapp.com/job/getPosts.php", new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(String response) {

                try {
                    if(companyname!=null){
                    companyname.setText("");
                    seekBarvalue.setText("No Experience");
                    gender="";
                        selectedLanguages="";
                    }

                    progressDialog.dismiss();
                    JSONObject json = new JSONObject(response);
                    if (json.getBoolean("success")) {

                        no_results.setVisibility(View.GONE);
                        Iterator<String> iter = json.keys();


                        while (iter.hasNext()) {
                            String key = iter.next();
                            try {

                                Object value = json.get(key);

                                if (!value.equals(true)) {
                                    String key2 = iter.next();
                                    Object languageValue=json.get(key2);
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
                                        job_post = new JobData(jsonArray.getInt("Pid"), jsonArray.getInt("Cid"),

                                                jsonArray.getString("name"), jsonArray.getString("job_type")

                                                , jsonArray.getString("job_city"),

                                                jsonArray.getString("post_time"),experienceToyears(jsonArray.getString("minExperience")),jsonArray.getString("gender"),jsonArray.getString("chatEmail")
                                                ,jsonArray.getString("job_description"),jsonArray.getString("job_city"),jsonArray.getString("job_category"),
                                                jsonLang.getString("languages"),status,1);
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }
                                    data.add(job_post);


                                    System.out.println("+++++++++ADDING+++++");
                                }

                            } catch (JSONException e) {
                                // Something went wrong!
                                e.printStackTrace();
                                System.out.println("Json-- exeption --- error in response" +
                                        "------------------");
                            }
                            Collections.sort(data, new DateComparator());
                            jobrecyclerView = findViewById(R.id.recyclerViewForSearch);
                            myAppAdapter = new JobAdapter(getApplicationContext(), data);
                            jobrecyclerView.setAdapter(myAppAdapter);
                        }
                    } else {
                        System.out.println("success not 1 ---------------------");
                        jobrecyclerView = findViewById(R.id.recyclerViewForSearch);
                        myAppAdapter = new JobAdapter(EmployeeSearchActivity.this, data);
                        jobrecyclerView.setAdapter(myAppAdapter);
                        no_results.setVisibility(View.VISIBLE);
                      //  Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_SHORT).show();

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
                progressDialog.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();



                params.put("eid",user.id+"");
                params.put("searchFor",EmployeeSearchActivity.searchViewJob.getQuery().toString());

                params.put("searchIn", EmployeeSearchActivity.searchViewlocation.getText().toString());// etsearch.getText().toString()

                if(companyname==null)
                    params.put("cname","");
                else
                    params.put("cname",companyname.getText().toString());


                if(seekBarvalue==null)
                    params.put("experience","");
                else {
                    if (seekBarvalue.getText().toString().equals("No Experience"))
                        params.put("experience", "");

                    else
                        params.put("experience",experienceToMonthes(seekBarvalue.getText().toString())+"");
                }
                params.put("gender",gender);

                if(multiSpinner!=null)
                    params.put("languages",selectedLanguages);
                else
                    params.put("languages","");

                System.out.println("++++++ params are +++ "+params.toString());

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);


    }

    @Override
    protected void onStart() {
        super.onStart();

        if(counterforjobData==0)
        init();
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
            return (int)(Float.valueOf(s[0])*12);

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
