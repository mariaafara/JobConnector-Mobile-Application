package com.example.abo8a.jobconnector;

import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterFragment3 extends Fragment {
    int mDay = 15;
    int mMonth = 7; // August, month starts from 0
    int mYear = 2018;



    public static   EditText edu_startdate;
    public static   EditText completionDate;
    public static   EditText uni;
    public static   EditText degree;
    public static   EditText major;


public static boolean checkfrag3=true;
    private final String urlInsert = "https://sloppier-citizens.000webhostapp.com/job/insertEducation.php";



    private static final String TAG = "RegisterFragment3";

    /**
     * This handles the message send from DatePickerDialogFragment on setting date
     */


    public RegisterFragment3() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private DatePickerDialog.OnDateSetListener startDateSetListener;

    public void addingEducation() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlInsert, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    System.out.println("++++++++" + response);
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        checkfrag3=true;
                        Toast.makeText(getActivity().getApplicationContext(), "added successfully", Toast.LENGTH_SHORT).show();

                    } else {
                        checkfrag3=false;
                        Toast.makeText(getActivity().getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity().getApplicationContext(), "canot add details!!!", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();


               // params.put("id", id + "");
                params.put("uni", uni.getText().toString());
                params.put("major", major.getText().toString());
                params.put("start_date", edu_startdate.getText().toString());
                params.put("end_date", completionDate.getText().toString());
                params.put("degree", degree.getText().toString());
                return params;
            }

        };
      //  RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
       // requestQueue.add(stringRequest);

    }


    @Override
    public void onStart() {
        super.onStart();
        Bundle bundle = getArguments();

        edu_startdate = getActivity().findViewById(R.id.education_start_date);
        completionDate = getActivity().findViewById(R.id.education_completion_date);
        uni = getActivity().findViewById(R.id.uni);
        degree = getActivity().findViewById(R.id.degree);
        major = getActivity().findViewById(R.id.major);
        //   finish=getActivity().findViewById(R.id.finish);

        edu_startdate.setOnClickListener(listener);
        completionDate.setOnClickListener(listener);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register_fragment3, container, false);
        return view;
    }

    /**
     * Click Event Handler for button
     */
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            /** Creating a bundle object to pass currently set date to the fragment */
            Bundle b = new Bundle();

            /** Adding currently set day to bundle object */
            b.putInt("set_day", mDay);

            /** Adding currently set month to bundle object */
            b.putInt("set_month", mMonth);

            /** Adding currently set year to bundle object */
            b.putInt("set_year", mYear);


            /** Getting fragment manger for this activity */
            FragmentManager fm = getActivity().getFragmentManager();

            /** Starting a fragment transaction */
            FragmentTransaction ft = fm.beginTransaction();
            /** Instantiating DatePickerDialogFragment */
            if (v.getId() == R.id.education_start_date) {
                DatePickerDialogFragment datePicker = new DatePickerDialogFragment(StartHandler);



                /** Setting the bundle object on datepicker fragment */
                datePicker.setArguments(b);
                /** Adding the fragment object to the fragment transaction */
                ft.add(datePicker, "date_picker");
            } else {
                DatePickerDialogFragment datePicker = new DatePickerDialogFragment(CompletionHandler);

                /** Setting the bundle object on datepicker fragment */
                datePicker.setArguments(b);
                /** Adding the fragment object to the fragment transaction */
                ft.add(datePicker, "date_picker");
            }

            /** Opening the DatePicker fragment */
            ft.commit();
        }
    };


    Handler StartHandler = new Handler() {
        @Override
        public void handleMessage(Message m) {
            /** Creating a bundle object to pass currently set date to the fragment */
            Bundle b = m.getData();

            /** Getting the day of month from bundle */
            mDay = b.getInt("set_day");

            /** Getting the month of year from bundle */
            mMonth = b.getInt("set_month");

            /** Getting the year from bundle */
            mYear = b.getInt("set_year");

            /** Displaying a short time message containing date set by Date picker dialog fragment */

            Toast.makeText(getContext(), b.getString("set_date"), Toast.LENGTH_LONG).show();
            edu_startdate.setText(b.getString("set_date"));
        }
    };

    Handler CompletionHandler = new Handler() {
        @Override
        public void handleMessage(Message m) {
            /** Creating a bundle object to pass currently set date to the fragment */
            Bundle b = m.getData();

            /** Getting the day of month from bundle */
            mDay = b.getInt("set_day");

            /** Getting the month of year from bundle */
            mMonth = b.getInt("set_month");

            /** Getting the year from bundle */
            mYear = b.getInt("set_year");

            /** Displaying a short time message containing date set by Date picker dialog fragment */

            Toast.makeText(getContext(), b.getString("set_date"), Toast.LENGTH_LONG).show();
            completionDate.setText(b.getString("set_date"));
        }
    };


}
