package com.example.abo8a.jobconnector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class editExperience extends AppCompatActivity {
    private EditText company;
    private EditText pos;
    private EditText country;
    private EditText city;
    private EditText desc;
    private  EditText seekBarvalue;
    private int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_experience);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        init();

    }
    public boolean validate() {
        boolean valid = true;

        String com = company.getText().toString();
        String po = pos.getText().toString();
        String cou = country.getText().toString();
        String cit= city.getText().toString();
        String des = desc.getText().toString();
        String seek= seekBarvalue.getText().toString();



        if (com.isEmpty()) {
            company.setError("should not be empty");
            valid = false;
        }
        if (po.isEmpty()) {
            pos.setError("should not be empty");
            valid = false;
        }
        if (cou.isEmpty()) {
            country.setError("should not be empty");
            valid = false;
        }
        if (cit.isEmpty()) {
            city.setError("should not be empty");
            valid = false;
        }
        if (cou.isEmpty()) {
            country.setError("should not be empty");
            valid = false;
        }
        if (des.isEmpty()) {
            desc.setError("should not be empty");
            valid = false;
        }
        if (cou.isEmpty()) {
            country.setError("should not be empty");
            valid = false;
        }
        if (seek.isEmpty()) {
            seekBarvalue.setError("should be specified");
            valid = false;
        }






        return valid;
    }

    private void init() {
        Intent i=getIntent();
        id = i.getIntExtra("id",0);
        company      = findViewById(R.id.company_name);
        pos          = findViewById(R.id.job_in);
        country      = findViewById(R.id.job_Country);
        city        = findViewById(R.id.job_City);
        desc        = findViewById(R.id.job_description);
        seekBarvalue = findViewById(R.id.seekbarvalue);
        SeekBar seekBar = (SeekBar) findViewById(R.id.durationSeekBar);

        company.setText     (i.getStringExtra("cname"));
        pos.setText         (i.getStringExtra("jobIn"));
        desc .setText    (i.getStringExtra("description"));
        country.setText        (i.getStringExtra("country"));
        seekBarvalue .setText       (i.getStringExtra("duration"));
        city.setText(i.getStringExtra("city"));



        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                if (progress == 0)
                    seekBarvalue.setText("less " + 1 + " month");
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
                updateExperience(id,company.getText().toString(),pos.getText().toString(),seekBarvalue.getText().toString(),
                        country.getText().toString(),city.getText().toString(),desc.getText().toString());

            }
            break;
            case android.R.id.home:
                finish();
            default:
                break;
        }
        return true;
    }

    private static final String TAG = "addExperience";
    private static final String url = "https://sloppier-citizens.000webhostapp.com/job/editExperiense.php";


    private void updateExperience(final int id , final String company,final String pos , final String duration,final String country,
                               final String city, final String desc) {

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "exp adding Response: " + response.toString());

                try {
                    System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    System.out.println("+++++++++++++" + response);
                    System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    JSONObject jObj = new JSONObject(response);
                    boolean success = jObj.getBoolean("success");

                    if (success) {
                        Intent k = new Intent(editExperience.this, user.class);//ActivitySearch
                        k.putExtra("id", user.id);
                        k.putExtra("user",user.user);
                        k.putExtra("email",user.email);
                        k.putExtra("comming",true);

                        startActivity(k);
                        Toast.makeText(getApplicationContext(), "updated Succesfully ", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Failed to update", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                    System.out.println("\ncatch a jason exception  in request\n");
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "adding Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id + "");
                params.put("company_name", company);
                params.put("job_in", pos);
                params.put("duration",experienceToMonthes( duration)+"");
                params.put("job_country", country);
                params.put("job_city", city);
                params.put("description", desc);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(strReq);


    }
    public int experienceToMonthes(String experience) {
        String s[] = experience.split(" ");
        if (s[1].equals("month"))
            return Integer.valueOf(s[0]);
        else if (s[0].equals("6+"))
            return 6 * 12;
        else
            return (int) (Float.valueOf(s[0]) * 12);

    }

}
