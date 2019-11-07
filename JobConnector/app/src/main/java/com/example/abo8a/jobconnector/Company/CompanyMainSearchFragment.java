package com.example.abo8a.jobconnector.Company;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.abo8a.jobconnector.EmployeeAdapter;
import com.example.abo8a.jobconnector.EmployeeData;
import com.example.abo8a.jobconnector.MultiSelectSpinner;
import com.example.abo8a.jobconnector.R;
import com.example.abo8a.jobconnector.SearchedResultPagerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CompanyMainSearchFragment extends Fragment {
    public static Context context;
    private SearchedResultPagerAdapter adapter;
    public static Button btnOk;
    public static Dialog customDialog;
    public List<String> languages;
    public static TextView fireSearch;
    public static TextView filterSearch;
    MultiSelectSpinner multiSpinner;
    public static SearchView searchViewCategory;
    TextView seekBarvalue;
    public RecyclerView recyclerView;
    private EmployeeAdapter myAppAdapter; //Array Adapter
    private RecyclerView.LayoutManager mLayoutManager;
    static ArrayList<EmployeeData> data;
    static int counterforjobData=0;

    public static String searchFor="";
    public static String searchIn="";
    public static String experience="";
    public static String gender="";
    public static String languagesString="";

    ImageView no_results;
    public final String url="https://sloppier-citizens.000webhostapp.com/company/searchEmployees.php";
    ProgressDialog progressDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.company_main_search_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
        init();
    }


    private void showCustomDialog() {


        customDialog = new Dialog(getActivity());
        customDialog.setContentView(R.layout.employee_search_filter);
        final RadioGroup genderRadioGroup;

        SeekBar seekBar;


        gender = "";
        final String langs[] = {"English", "Arabic", "French", "Chinese", "Portuguese", "Japanese", "Bengali", "Russian",
                "Swedish", "Italian", "German", "Spanish"};
        languages=new ArrayList<String>();
// using window set the hight and width of custom dialog
        languagesString="";
        for(int i=0;i<langs.length;i++)
        {
            languages.add(langs[i]);

        }

        Window window = customDialog.getWindow();

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        window.setGravity(Gravity.CENTER);


// bind all controlss with custom dialog


        TextView location=customDialog.findViewById(R.id.companyNameOrLocation);
        location.setText("Location");
        final EditText ETlocation=customDialog.findViewById(R.id.Company_name_filter);
        multiSpinner=customDialog.findViewById(R.id.multiSpinner);
        btnOk = (Button) customDialog.findViewById(R.id.btnOK);
        seekBar=(SeekBar) customDialog.findViewById(R.id.seekBarfilter);
        genderRadioGroup = (RadioGroup) customDialog.findViewById(R.id.genderRadioGroup);
        seekBarvalue=customDialog.findViewById(R.id.tvExperienceFilter);



        multiSpinner.setItems(langs);


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
                languagesString=multiSpinner.buildSelectedItemString();
                searchIn=ETlocation.getText().toString();
                customDialog.dismiss();
                if(!seekBarvalue.getText().toString().equals("No Experience"))
                    experience=experienceToMonthes(seekBarvalue.getText().toString())+"";
                System.out.println("++++++++++++++ experience in filter in company is "+experience+"   "+languagesString);
                init();

            }

        });


        customDialog.show();// this show() method is used to show custom dialog

    }
    public void initialize() {

        no_results=getActivity().findViewById(R.id.no_results_found_company);
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recycleCompanySearch); //Recylcerview Declaration
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        filterSearch=getActivity().findViewById(R.id.FilterCompanySearch);
        searchViewCategory = (SearchView) getActivity().findViewById(R.id.searchViewLocation);
        searchViewCategory.setQueryHint("Search By Category");
        searchViewCategory.setIconified(false);
        searchViewCategory.clearFocus();
        searchViewCategory.setFocusable(false);
        searchViewCategory.setOnQueryTextListener(new SearchView.OnQueryTextListener() {


            @Override
            public boolean onQueryTextSubmit(String query) {
                init();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Called when the query text is changed by the user.
                searchFor=newText;
                return true;
            }
        });
        filterSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();
            }
        });

    }


    private void init() {

        data = new ArrayList<>();

        myAppAdapter = new EmployeeAdapter(getActivity(), data);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    System.out.println("++++++++" + response);
                    JSONObject json = new JSONObject(response);

                    if (json.getBoolean("success")) {
                        no_results.setVisibility(View.GONE);

                        Iterator<String> iter = json.keys();
                        while (iter.hasNext()) {
                            String key1 = iter.next();

//key1 for employee,key2 for languages,key3 for experience
                            try {

                                Object value = json.get(key1);
                                if (!value.equals(true)) {
                                    String key2 = iter.next();
                                    String key3 = iter.next();
                                    String key4=iter.next();
                                    String Key5=iter.next();
                                    JSONObject jsonArray1 = (JSONObject) value;
                                    JSONObject jsonArray2 = (JSONObject)json.get(key2);
                                    JSONObject jsonArray3 = (JSONObject) json.get(key3);
                                    JSONObject jsonArray4=(JSONObject) json.get(key4);

                                    EmployeeData job_post = null;

                                    try {String ex="";
                                        String l="";
                                        ex= experienceToyears(jsonArray3.getString("sumExperience"));


                                        l=jsonArray2.getString("languages");
                                        job_post = new EmployeeData(jsonArray1.getInt("Eid"), jsonArray1.getString("fname"),
                                                jsonArray1.getString("lname")
                                                , jsonArray1.getString("city"),
                                                jsonArray1.getString("category"),jsonArray1.getString("LogInEmail"),
                                                ex  ,
                                                l,jsonArray4.getDouble("rating"),json.getString(Key5));



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
                        no_results.setVisibility(View.VISIBLE);


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
                Toast.makeText(getActivity(), "Check your Internet Connection and try Again", Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("cid",CompanyMainActivity.cid+"");
                params.put("searchIn",  searchIn);
                params.put("searchFor", searchFor);
                params.put("experience",  experience);
                params.put("gender", gender);
                params.put("languages",  languagesString);
                searchFor=searchViewCategory.getQuery().toString();
                searchIn="";
                experience="";
                gender="";
                languagesString="";
                System.out.println("++++++++ params of searchCompany is "+params.toString());
                return params;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(stringRequest);
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
        if(exp==0)
            return "No Experience";
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


}
