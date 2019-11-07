package com.example.abo8a.jobconnector;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.example.abo8a.jobconnector.RegisterFragment3.completionDate;
import static com.example.abo8a.jobconnector.RegisterFragment3.degree;
import static com.example.abo8a.jobconnector.RegisterFragment3.edu_startdate;
import static com.example.abo8a.jobconnector.RegisterFragment3.major;
import static com.example.abo8a.jobconnector.RegisterFragment3.uni;

public class editEducation extends AppCompatActivity {
    private static final String TAG = "editEducation";
    private EditText edu_uni;
    private EditText edu_major;
    private EditText edu_degree;
    private EditText edu_start_date;
    private EditText edu_completion_date;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private DatePickerDialog.OnDateSetListener mDateSetListener2;
    int uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_education);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        init();
        uid = user.id;
    }
    public boolean validate() {
        boolean valid = true;


        String fr3uni = edu_uni.getText().toString();
        String fr3completionDate = edu_completion_date.getText().toString();
        String fr3degree = edu_degree.getText().toString();
        String fr3edu_startDate = edu_start_date.getText().toString();
        String fr3Major = edu_major.getText().toString();


        if (fr3uni.isEmpty()) {
            edu_uni.setError("enter a valid University name");
            valid = false;
        }
        if (fr3degree.isEmpty()) {
            edu_degree.setError("enter a valid Degree");
            valid = false;
        }
        if (fr3completionDate.isEmpty()) {
            edu_completion_date.setError("enter a valid Date");
            valid = false;
        }
        if (fr3edu_startDate.isEmpty()) {
            edu_start_date.setError("enter a valid Date");
            valid = false;
        }

        if (!compareDate(fr3edu_startDate, fr3completionDate)) {
            edu_completion_date.setError("enter a valid Date");
            edu_start_date.setError("enter a valid Date");
            valid = false;

        }


        if (fr3Major.isEmpty()) {
            edu_major.setError("enter a valid Major");
            valid = false;
        }


        return valid;
    }

    public boolean compareDate(String d1, String d2) {
        String[] a1 = d1.split("-");
        String[] a2 = d2.split("-");
        if(d1.equals("")||d2.equals(""))
            return false;
        System.out.println("+++++++  the year is +++++++++   " + a1[0]);
        if (Integer.parseInt(a1[0]) > Integer.parseInt(a2[0]))
            return false;
        if (Integer.parseInt(a1[0]) == Integer.parseInt(a2[0])) {
            if (Integer.parseInt(a1[1]) > Integer.parseInt(a2[1]))
                return false;
            if (Integer.parseInt(a1[1]) == Integer.parseInt(a2[1])) {
                if (Integer.parseInt(a1[2]) > Integer.parseInt(a2[2]))
                    return false;
            }
        }

        if (!(Integer.parseInt(a1[0]) < Integer.parseInt(a2[0]) - 2))
            return false;

        return true;
    }

    public void init() {
        edu_uni = findViewById(R.id.uni);
        edu_major = findViewById(R.id.major);
        edu_degree = findViewById(R.id.degree);
        edu_start_date = findViewById(R.id.education_start_date);
        edu_completion_date = findViewById(R.id.education_completion_date);
        edu_uni.setText(EmployeeProfile.uni);
        edu_major.setText(EmployeeProfile.major);
        edu_degree.setText(EmployeeProfile.degree);
        edu_start_date.setText(EmployeeProfile.start);
        edu_completion_date.setText(EmployeeProfile.end);


        edu_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        editEducation.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener2,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: yyy/mm/dd: " + year + "-" + month + "-" + day);

                String date = year + "-" + month + "-" + day;
                edu_start_date.setText(date);
            }
        };
        //////////////////////////////////
        edu_completion_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        editEducation.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: yyy/mm/dd: " + year + "-" + month + "-" + day);

                String date = year + "-" + month + "-" + day;
                edu_completion_date.setText(date);
            }
        };

    }

    private void updateEduc() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://sloppier-citizens.000webhostapp.com/job/updateEducation.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    System.out.println("++++++++" + response);
                    JSONObject json = new JSONObject(response);
                    if (json.getBoolean("success")) {
                        Intent k = new Intent(editEducation.this, user.class);//ActivitySearch
                        k.putExtra("id", uid);
                        k.putExtra("user",user.user);
                        k.putExtra("email",user.email);
                        k.putExtra("comming",true);


                        startActivity(k);
                        Toast.makeText(getApplicationContext(), "updated Succesfully ", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        System.out.println("success not 1 ---------------------");
                        Toast.makeText(getApplicationContext(), "Failed to Update!!", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("Json exception--------------------");
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("update", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Check your Internet Connection and try Again", Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();


                params.put("eid", uid + "");
                params.put("major", edu_major.getText().toString());
                params.put("degree", edu_degree.getText().toString());
                params.put("uni", edu_uni.getText().toString());
                params.put("start", edu_start_date.getText().toString());
                params.put("end", edu_completion_date.getText().toString());
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_done: {
                if(validate())
                updateEduc();
                //Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();
                break;
            }
            case android.R.id.home:
                finish();
            default:
                break;
        }
        return true;
    }
}
