package com.example.abo8a.jobconnector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

public class LanguageLevel extends AppCompatActivity {
    private ListView level_listview;
    private Intent intent;
    private String language;
    private int eid;
    private String level;
    private static final String TAG = "MainActivity";
    private static final String URL_FOR_LANGUAGE = "https://sloppier-citizens.000webhostapp.com/job/insertLanguage.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.language_level);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        final Intent i = getIntent();
        eid = i.getIntExtra("id", 9999);
        System.out.println("++++++language++++++++" + eid);

        language = i.getStringExtra("lang");
        String levels[] = {"Beginner", "Intermediate", "Advanced", "Native"};
        level_listview = findViewById(R.id.listView_languagelevel);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, levels);
        level_listview.setAdapter(arrayAdapter);

        level_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                level = parent.getItemAtPosition(position).toString();
                addlanguage(eid, language, level);

                EmployeeProfile.goactivitylanguage++;

                intent = new Intent(LanguageLevel.this, user.class);
                intent.putExtra("comming", true);
                user.visited = true;
                intent.putExtra("id", eid);
                startActivity(intent);

                overridePendingTransition(R.anim.push_left_out, R.anim.push_left_in);

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private void addlanguage(final int id, final String lan, final String lev) {

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_FOR_LANGUAGE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Language adding Response: " + response.toString());

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
                        DataBaseHandler db = new DataBaseHandler(LanguageLevel.this);
                        db.addLanguage(new Language(eid, language, level));
                        System.out.println("+++++++++++++++++ added to sqlite");
                        String userLanguage = jObj.getString("language");
                        Toast.makeText(getApplicationContext(), "... " + userLanguage + "!", Toast.LENGTH_SHORT).show();

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
                params.put("language", lan);
                params.put("level", lev);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(strReq);


    }


    @Override
    public void onBackPressed() {
        // Write your code here
        super.onBackPressed();
    }

    public void Back(View v) {
        onBackPressed();

    }


}
