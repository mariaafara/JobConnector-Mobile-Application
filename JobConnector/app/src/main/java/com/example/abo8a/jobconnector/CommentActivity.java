package com.example.abo8a.jobconnector;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.RatingBar;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CommentActivity extends AppCompatActivity {
    CommentAdapter commentAdapter; //Array Adapter
    ArrayList<Comment> data;
    int averageRate;
    int nbRates = -1;
    public int id;
    public static Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_rate_layout);
        context = CommentActivity.this;
        init();
    }


    private void init() {

        Intent i = getIntent();
        id = i.getIntExtra("id", 0);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Loading...");
        final RatingBar ratingBar;
        final TextView averageRating;
        final TextView nbComments;
        final RecyclerView recyclerViewComment;
        final TextView ratingName = findViewById(R.id.ratingName);
        ratingName.setText(i.getStringExtra("EmployeeName"));
// using window set the hight and width of custom dialog


        ratingBar = (RatingBar) findViewById(R.id.rating_bar);
        ratingBar.setEnabled(false);
        averageRating = (TextView) findViewById(R.id.averageRateTV);
        nbComments = (TextView) findViewById(R.id.NbComments);
        recyclerViewComment = (RecyclerView) findViewById(R.id.RecycleViewComments);
        recyclerViewComment.setHasFixedSize(true);
        recyclerViewComment.setLayoutManager(new LinearLayoutManager(this));
        data = new ArrayList<>();


        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://sloppier-citizens.000webhostapp.com/rate/getEmployeeRate.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    progressDialog.dismiss();
                    JSONObject json = new JSONObject(response);
                    if (json.getBoolean("success")) {

                        Iterator<String> iter = json.keys();

                        while (iter.hasNext()) {
                            String key = iter.next();
                            try {


                                Object value = json.get(key);


                                if (!value.equals(true)) {
                                    JSONObject jsonArray = (JSONObject) value;


                                    Comment comment = null;
                                    try {
                                        comment = new Comment(jsonArray.getInt("Rating"), jsonArray.getString("Comment"),

                                                jsonArray.getString("date_rated"), jsonArray.getString("email")

                                                , jsonArray.getString("name"));
                                        data.add(comment);
                                    } catch (JSONException e1) {

                                        try {
                                            if (nbRates == -1) {
                                                nbRates = jsonArray.getInt("nbRates");
                                                nbComments.setText("   " + nbRates + " Comments");

                                            } else {
                                                if (jsonArray.get("sumRates") != null) {
                                                    averageRate = jsonArray.getInt("sumRates");
                                                }
                                            }
                                        } catch (JSONException e2) {
                                            e2.printStackTrace();
                                        }
                                        ///   e1.printStackTrace();

                                    }

                                }


                            } catch (JSONException e) {
                                // Something went wrong!
                                e.printStackTrace();
                                System.out.println("Json-- exeption --- error in response" +
                                        "------------------");
                            }

                        }
                        commentAdapter = new CommentAdapter(getApplicationContext(), data);
                        recyclerViewComment.setAdapter(commentAdapter);
                        averageRating.setText(Math.round((Float.parseFloat(averageRate + "") / nbRates) * 10) / 10.0 + "/5");
                        ratingBar.setNumStars(5);
                        ratingBar.setBackgroundColor(Color.blue(1));
                        ratingBar.setRating(Float.parseFloat(averageRate + "") / nbRates);
                    } else {
                        System.out.println("success not 1 ---------------------");
                        setContentView(R.layout.employee_not_rated);


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

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("Eid", id + "");


                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);


    }

}
