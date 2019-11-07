package com.example.abo8a.jobconnector;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class CommentFragment extends Fragment {

    CommentAdapter commentAdapter; //Array Adapter
    ArrayList<Comment> data;
    int averageRate;
    int nbRates = -1;
    public int id;
    public static Context context;

    View mainview;
    public CommentFragment(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainview = inflater.inflate(R.layout.view_rate_layout, container, false);

        return mainview;
    }

    @Override
    public void onStart() {
        super.onStart();
        context = getActivity();
        init();
    }

    private void init() {

        id = user.id;
        System.out.println("++++++++++ id in Commentactivity is " + id);
// the setContentView() method is used to set the custom layout for the dialog

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading...");
        final RatingBar ratingBar;
        final TextView averageRating;
        final TextView nbComments;
        final RecyclerView recyclerViewComment;
        final TextView ratingName =getActivity(). findViewById(R.id.ratingName);
        ratingName.setText(user.user);
// using window set the hight and width of custom dialog


        ratingBar = (RatingBar) getActivity().findViewById(R.id.rating_bar);
        ratingBar.setEnabled(false);
        averageRating = (TextView) getActivity().findViewById(R.id.averageRateTV);
        nbComments = (TextView) getActivity().findViewById(R.id.NbComments);
        recyclerViewComment = (RecyclerView) getActivity().findViewById(R.id.RecycleViewComments);
        recyclerViewComment.setHasFixedSize(true);
        recyclerViewComment.setLayoutManager(new LinearLayoutManager(getActivity()));
        data = new ArrayList<>();


        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://sloppier-citizens.000webhostapp.com/rate/getEmployeeRate.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    progressDialog.dismiss();
                    System.out.println("++++++++" + response);
                    JSONObject json = new JSONObject(response);
                    if (json.getBoolean("success")) {

                        Iterator<String> iter = json.keys();

                        while (iter.hasNext()) {
                            String key = iter.next();
                            try {

                                System.out.println("++++++++ the key is " + key);
                                Object value = json.get(key);
                                System.out.println("++++++++ the value is " + value.toString());


                                if (!value.equals(true)) {
                                    JSONObject jsonArray = (JSONObject) value;


                                    Comment comment = null;
                                    try {
                                        comment = new Comment(jsonArray.getInt("Rating"), jsonArray.getString("Comment"),

                                                jsonArray.getString("date_rated"), jsonArray.getString("email")

                                                , jsonArray.getString("name"));
                                        data.add(comment);
                                        System.out.println("+++++++++ADDING Comment+++++");
                                    } catch (JSONException e1) {

                                        try {
                                            if (nbRates == -1) {
                                                nbRates = jsonArray.getInt("nbRates");
                                                nbComments.setText("   " + nbRates + " Comments");
                                                System.out.println("+++++++++Not A Comment++ in nbRates +++ " + nbRates + "  " + averageRate);

                                            } else {
                                                if (jsonArray.get("sumRates") != null) {
                                                    averageRate = jsonArray.getInt("sumRates");
                                                    System.out.println("+++++++++Not A Comment+++ in sumRates++ " + nbRates + "  " + averageRate);
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
                        commentAdapter = new CommentAdapter(getActivity(), data);
                        recyclerViewComment.setAdapter(commentAdapter);
                        averageRating.setText(Math.round((Float.parseFloat(averageRate + "") / nbRates) * 10) / 10.0 + "/5");
                        ratingBar.setNumStars(5);
                        ratingBar.setBackgroundColor(Color.blue(1));
                        ratingBar.setRating(Float.parseFloat(averageRate + "") / nbRates);
                        nbComments.setText("   " + nbRates + " Comments");

                    } else {
                        System.out.println("success not 1 ---------------------");

                        setViewLayout(R.layout.employee_not_rated);



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
                params.put("Eid", id + "");


                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(stringRequest);


    }

    private void setViewLayout(int id){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainview = inflater.inflate(id, null);
        ViewGroup rootView = (ViewGroup) getView();
        rootView.removeAllViews();
        rootView.addView(mainview);
    }

}
