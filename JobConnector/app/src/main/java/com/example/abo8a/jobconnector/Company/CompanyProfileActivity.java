package com.example.abo8a.jobconnector.Company;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.abo8a.jobconnector.AppSingleton;
import com.example.abo8a.jobconnector.Notification.FcmNotificationBuilder;
import com.example.abo8a.jobconnector.R;
import com.example.abo8a.jobconnector.chat.util.ImageUtils;
import com.example.abo8a.jobconnector.chat.util.data.StaticConfig;
import com.example.abo8a.jobconnector.user;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CompanyProfileActivity extends AppCompatActivity {

    public TextView name;
    public TextView type;
    public TextView desc;
    public TextView companyurl;
    public TextView phone;
    public TextView editInfo;
    public TextView editDesc;
    int cid;
    String email = "";
    ImageView avatar;
    String nam = "";
    LinearLayout linearAcceptReject;
    LinearLayout liniarState;

    Button acceptBtn, rejectBtn, stateBtn;

    String curl = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_profile_activity);



    }

    @Override
    protected void onResume() {
        super.onResume();
        initialize();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
            default:
                break;
        }
        return true;
    }
    public void initialize() {
        cid = getIntent().getIntExtra("cid", 0);
        email = getIntent().getStringExtra("email");

        init();
        fillProfile();
        initTollbar();


        linearAcceptReject = findViewById(R.id.linearLayout_Company_accept_reject);
        liniarState = findViewById(R.id.linearLayout_company_State);
        linearAcceptReject.setVisibility(View.GONE);
        liniarState.setVisibility(View.GONE);
        acceptBtn = findViewById(R.id.companyAcceptbtn);
        rejectBtn = findViewById(R.id.companyRejectbtn);
        stateBtn = findViewById(R.id.companyState);

        companyurl.setMovementMethod(LinkMovementMethod.getInstance());
        companyurl.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse(curl));
                startActivity(browserIntent);
            }
        });

        if (getIntent().getBooleanExtra("comeFromRequest", false)) {
            String status = getIntent().getStringExtra("status");
            if (status.equals("Pending")) {
                liniarState.setVisibility(View.GONE);
            } else {
                if (status.equals("Accepted"))
                    stateBtn.setText("Accepted");
                else
                    stateBtn.setText("Rejected");
                linearAcceptReject.setVisibility(View.GONE);

            }
        }

        acceptBtn.setOnClickListener(onButtonClick);
        rejectBtn.setOnClickListener(onButtonClick);

        stateBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String msg;
                if (stateBtn.getText().toString().equals("Accepted"))
                    msg = "Are you sure want to UnAccept this Request?";
                else
                    msg = "Are you sure want to UnReject this Request?";

                new AlertDialog.Builder(CompanyProfileActivity.this)
                        .setTitle("Alert!")
                        .setMessage(msg)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                updateStatus(cid, user.id, "Pending");
                                getrecieverToken(email, 2);

                                linearAcceptReject.setVisibility(View.VISIBLE);
                                liniarState.setVisibility(View.GONE);
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();

                return false;
            }
        });

    }

    public void init() {
        name = findViewById(R.id.company_profile_name);
        companyurl = findViewById(R.id.company_profile_url);
        type = findViewById(R.id.company_profile_tpe);
        desc = findViewById(R.id.company_profile_description);
        phone = findViewById(R.id.company_profile_phone);
        editDesc = findViewById(R.id.edit_companypdescription);
        editInfo = findViewById(R.id.edit_commpanyprofile_info);
        avatar = findViewById(R.id.Company_img_avatar);


        setImage(email, avatar);

    }

    View.OnClickListener onButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.companyAcceptbtn) {

                updateStatus(cid, user.id, "Accepted");
                getrecieverToken(email, 1);
                linearAcceptReject.setVisibility(View.GONE);
                liniarState.setVisibility(View.VISIBLE);
                stateBtn.setText("Accepted");

            } else {
                updateStatus(cid, user.id, "Rejected");
                getrecieverToken(email, 0);

                linearAcceptReject.setVisibility(View.GONE);
                liniarState.setVisibility(View.VISIBLE);
                stateBtn.setText("Rejected");

            }

        }
    };


    public void updateStatus(final int cid, final int eid, final String status) {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {


                StringRequest strReq = new StringRequest(Request.Method.POST,
                        "https://sloppier-citizens.000webhostapp.com/job/updateRequest.php", new Response.Listener<String>() {

                    @SuppressLint("LongLogTag")
                    @Override
                    public void onResponse(String response) {
                        Log.d("CompanyProfileActivity", "Request Response: " + response.toString());


                        try {
                            System.out.println("+++++++++++++" + response);
                            //  hideDialog();
                            JSONObject jObj = new JSONObject(response);
                            boolean success = jObj.getBoolean("success");


                            if (success) {
                                System.out.println("+++++++++++Successfly Updated Status for Request");
                            } else {


//                                Toast.makeText(getApplicationContext(),
//                                        "Failed to UpdateStatus", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println("\ncatch a jason exception  in request\n");
                        }

                    }
                }, new Response.ErrorListener() {

                    @SuppressLint("LongLogTag")
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("CompanyProfileActivity", "Request Error: " + error.getMessage());
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_LONG).show();
                        // hideDialog();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        // Posting params to register url
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("cid", cid + "");
                        params.put("eid", eid + "");
                        params.put("status", status);
                        return params;
                    }
                };
                // Adding request to request queue
                AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, "CompanyProfileActivity");


            }
        });


    }


    public void fillProfile() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        final String url = "https://sloppier-citizens.000webhostapp.com/company/getProfile.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {
                    //      System.out.println("+++++++" + response);

                    JSONObject json = new JSONObject(response);

                    System.out.println("++++++++" + response);

                    if (json.getBoolean("success")) {

                        curl = json.getString("url");
                        name.setText(json.getString("name"));
                        companyurl.setText(curl);
                        nam = json.getString("name");
                        //  companyurl.setText(json.getString("url"));
                        type.setText(json.getString("type"));
                        phone.setText(json.getString("phone"));
                        desc.setText(json.getString("desc"));
                        //*****************************//

                    } else {
                        System.out.println("success not 1 ---------------------");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("Json22--------------------");
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("CompanyProfile", " Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Check your Internet Connection and try Again", Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();


                params.put("cid", cid + "");

                System.out.print("\n+++++ params = " + params.toString());

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);


    }

    private void initTollbar() {

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCompany);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final CollapsingToolbarLayout coll = findViewById(R.id.company_collapsing);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout_company);
        toolbar.setTitle("");
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                    toolbar.setBackgroundColor(getResources().getColor(R.color.transparent));

                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true;
                    toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                    coll.setTitle(nam + "");
                } else if (isShow) {
                    coll.setTitle("");
                    toolbar.setBackgroundColor(getResources().getColor(R.color.transparent));

                    isShow = false;
                }
            }
        });

    }

    public void setImage(final String email, final ImageView avatar) {

        if (email != null) {
            FirebaseDatabase.getInstance().getReference().child("user").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getValue() != null) {
                        String id = ((HashMap) dataSnapshot.getValue()).keySet().iterator().next().toString();
                        HashMap userMap = (HashMap) ((HashMap) dataSnapshot.getValue()).get(id);
                        setImageAvatar(getApplicationContext(), (String) userMap.get("avata"), avatar);

                    } else {
                        setImageAvatar(getApplicationContext(), null, avatar);
                        System.out.println("++++++++++++++ in CommentAdapter the email in fire base is null ; " + email);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });
        } else
            System.out.println("++++++++++++++ the email in fire base is null ; ");


    }

    private void setImageAvatar(Context context, String imgBase64, ImageView avatar) {
        try {

            Resources res = context.getResources();
            Bitmap src;
            if (imgBase64.equals("default") || imgBase64.equals(null)) {
                src = BitmapFactory.decodeResource(res, R.drawable.default_avata);
            } else {
                byte[] decodedString = Base64.decode(imgBase64, Base64.DEFAULT);
                src = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            }
            avatar.setImageDrawable(ImageUtils.roundedImage(context, src));
        } catch (Exception e) {
        }
    }


    public void getrecieverToken(final String email, final int state) {

        FirebaseDatabase.getInstance().getReference().child("user").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //System.out.println("++++++ the datasnapshot is "+dataSnapshot.toString());

                if (dataSnapshot.getValue().toString() == null)
                    System.out.println("+++\n++++++\n+++++\n++ No token for this email since it is not rejestered yet\n");
                else {
                    String id = ((HashMap) dataSnapshot.getValue()).keySet().iterator().next().toString();
                    HashMap userMap = (HashMap) ((HashMap) dataSnapshot.getValue()).get(id);

                    System.out.println("+++\n+++ Email is " + email + " token is " + (String) userMap.get("token") + "     namee  " + id + " staticUid is " + StaticConfig.UID + "\n my token is " + FirebaseInstanceId.getInstance() + "+++\n");


                    if (state == 1)
                        sendPushNotificationToReceiver("JobConnector", user.user
                                + " accepted your job Request", StaticConfig.UID, FirebaseInstanceId.getInstance().getToken(), (String) userMap.get("token"));
                    if (state == 0)
                        sendPushNotificationToReceiver("JobConnector", user.user + " rejected your job Request", StaticConfig.UID, FirebaseInstanceId.getInstance().getToken(), (String) userMap.get("token"));
                    if (state == 2)
                        sendPushNotificationToReceiver("JobConnector", user.user + " returned you to pending", StaticConfig.UID, FirebaseInstanceId.getInstance().getToken(), (String) userMap.get("token"));


                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("++++++ token is failed");

            }
        });
    }

    private void sendPushNotificationToReceiver(String username,
                                                String message,
                                                String uid,
                                                String firebaseToken,
                                                String receiverFirebaseToken) {
        FcmNotificationBuilder.initialize()
                .title(username)
                .message(message)
                .username(username)
                .uid(uid)
                .firebaseToken(firebaseToken)
                .receiverFirebaseToken(receiverFirebaseToken)
                .send();
    }

}
