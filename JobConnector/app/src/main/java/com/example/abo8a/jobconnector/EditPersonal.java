package com.example.abo8a.jobconnector;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
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

import static com.example.abo8a.jobconnector.RegisterFragment1.age;
import static com.example.abo8a.jobconnector.RegisterFragment1.country;
import static com.example.abo8a.jobconnector.RegisterFragment1.current_work;
import static com.example.abo8a.jobconnector.RegisterFragment1.fname;
import static com.example.abo8a.jobconnector.RegisterFragment1.lname;
import static com.example.abo8a.jobconnector.RegisterFragment1.phone_nb;
import static com.example.abo8a.jobconnector.RegisterFragment1.workInSpinner;

public class EditPersonal extends AppCompatActivity {
    private EditText fname;
    private EditText lname;
    private EditText age;
    private EditText number;
    private Spinner city;
    int uid;
    HashMap<String,Integer> cities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_edit_personal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        init();
        uid = user.id;
    }


    private void updatePersonal() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://sloppier-citizens.000webhostapp.com/job/updatePersonal.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    System.out.println("++++++++" + response);
                    JSONObject json = new JSONObject(response);
                    if (json.getBoolean("success")) {
                        Intent k = new Intent(EditPersonal.this, user.class);//ActivitySearch
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
                params.put("fname", fname.getText().toString());
                params.put("lname", lname.getText().toString());
                params.put("age", age.getText().toString());
                params.put("city", city.getSelectedItem().toString());
                params.put("country", "Lebanon");
                params.put("phone",  number.getText().toString());
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
                updatePersonal();
            }
                break;
            case android.R.id.home:
                finish();
            default:
                break;
        }
        return true;
    }

    public void init() {
        cities=new HashMap<>();
        fname = findViewById(R.id.first_name);
        lname = findViewById(R.id.last_name);
        age = findViewById(R.id.age);
        number = findViewById(R.id.contact_nb);
        city = findViewById(R.id.City);
        fname.setText(EmployeeProfile.fname);
        lname.setText(EmployeeProfile.lname);
        age.setText(EmployeeProfile.age);
       // if(EmployeeProfile.number.charAt(0)=='+')
        //number.setText(EmployeeProfile.number.substring(4,EmployeeProfile.number.length()));

       // else
            number.setText(EmployeeProfile.number);

     String[] c=this.getResources().getStringArray(R.array.cities);

     for(int i=0;i<c.length;i++)
     {
         cities.put(c[i],i);
     }
     if(cities.get(EmployeeProfile.city)!=null)
     city.setSelection(cities.get(EmployeeProfile.city));

        age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
              show();
            }
        });
    }

    public boolean validate() {
        boolean valid = true;

        String fr1fName = fname.getText().toString();
        String fr1lName = lname.getText().toString();
        String fr1age = age.getText().toString();

        String fr1PhNb = number.getText().toString();


        if (fr1fName.isEmpty()) {
            fname.setError("Should not be empty");
            valid = false;
        }
        if (fr1lName.isEmpty()) {
            lname.setError("Should not be empty");
            valid = false;
        }
        if (fr1age.isEmpty()) {
            age.setError("Should not be empty");
            valid = false;
        }

        if (fr1PhNb.length() != 8 || fr1PhNb.isEmpty() ) {
            number.setError("enter a valid Phone Number (8 numbers)");
            valid = false;
        }



        return valid;
    }



    public void show() {

        final Dialog npDialog = new Dialog(EditPersonal.this, R.style.Theme_Dialog);
        npDialog .requestWindowFeature(Window.FEATURE_NO_TITLE);
        npDialog.setContentView(R.layout.dialog_number_picker);
        Button setBtn = (Button) npDialog.findViewById(R.id.setbtn);
        Button cnlBtn = (Button) npDialog.findViewById(R.id.cancelbtn);

        final NumberPicker numberPicker = (NumberPicker) npDialog.findViewById(R.id.number_picker);
        numberPicker.setMaxValue(50);
        numberPicker.setMinValue(16);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                // TODO Auto-generated method stub
            }
        });

        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

            //    Toast.makeText(getApplicationContext(), "Number selected: " + numberPicker.getValue(), Toast.LENGTH_SHORT).show();
                age.setText(numberPicker.getValue() + "");
                npDialog.dismiss();
            }
        });

        cnlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                npDialog.dismiss();
            }
        });

        npDialog.show();
    }

}
