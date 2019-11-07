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
import android.widget.RadioButton;
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

import static com.example.abo8a.jobconnector.RegisterFragment3.degree;
import static com.example.abo8a.jobconnector.RegisterFragment3.uni;

public class addSkill extends AppCompatActivity {
    private EditText skill;
    private EditText level;
    private int id;
    private Button set;
    private Button cancel;
    private RadioButton begginer;
    private RadioButton intermediate;
    private RadioButton expert;
    private static final String TAG = "addSkill";
    private static final String url= "https://sloppier-citizens.000webhostapp.com/job/insertSkill.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_skill);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

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
                addSkill(id,skill.getText().toString(),level.getText().toString());

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

        id = user.id;
        skill = findViewById(R.id.skill_name);
        level = findViewById(R.id.skill_level);
        level.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                showlevelDialog();
            }
        });

    }
    public boolean validate() {
        boolean valid = true;


        String s = skill.getText().toString();
        String l = level.getText().toString();



        if (s.isEmpty()) {
            skill.setError("Should Fill it");
            valid = false;
        }
        if (l.isEmpty()) {
            level.setError("Should Fill it");
            valid = false;
        }

        return valid;
    }

    public void showlevelDialog() {

        final Dialog Dialog = new Dialog(addSkill.this, R.style.Theme_Dialog);
        //Window window = npDialog.getWindow();
        Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Dialog.setContentView(R.layout.dialog_skill_level);
        Button setBtn = (Button) Dialog.findViewById(R.id.setbtn);
        Button cnlBtn = (Button) Dialog.findViewById(R.id.cancelbtn);
        begginer = (RadioButton) Dialog.findViewById(R.id.radioBeginner);
        intermediate = (RadioButton) Dialog.findViewById(R.id.radioIntermediate);
        expert = (RadioButton) Dialog.findViewById(R.id.radioExpert);

        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                if (begginer.isChecked()) {
                    level.setText("Begginer");
                    Dialog.dismiss();
                } else if (intermediate.isChecked()) {
                    level.setText("Intermediate");
                    Dialog.dismiss();
                } else if (expert.isChecked()) {
                    level.setText("Expert");
                    Dialog.dismiss();
                }
            }
        });

        cnlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Dialog.dismiss();
            }
        });

        Dialog.show();
    }



    private void addSkill(final int id, final String skill, final String lev) {

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "skill adding Response: " + response.toString());

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
                        Intent k = new Intent(addSkill.this, user.class);//ActivitySearch
                        k.putExtra("id", user.id);
                        k.putExtra("user",user.user);
                        k.putExtra("email",user.email);
                        k.putExtra("comming",true);
                        startActivity(k);
                        Toast.makeText(getApplicationContext(), "added Succesfully ", Toast.LENGTH_SHORT).show();
                        finish();

                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Failed to add", Toast.LENGTH_LONG).show();
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
                params.put("Eid", id + "");
                params.put("skill", skill);
                params.put("level", lev);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(strReq);


    }

}
