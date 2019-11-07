package com.example.abo8a.jobconnector;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
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
import com.example.abo8a.jobconnector.Company.CompanyProfileActivity;
import com.example.abo8a.jobconnector.Notification.FcmNotificationBuilder;
import com.example.abo8a.jobconnector.chat.util.ImageUtils;
import com.example.abo8a.jobconnector.chat.util.data.FriendDB;
import com.example.abo8a.jobconnector.chat.util.data.StaticConfig;
import com.example.abo8a.jobconnector.chat.util.model.Friend;
import com.example.abo8a.jobconnector.chat.util.model.ListFriend;
import com.example.abo8a.jobconnector.chat.util.ui.ChatActivity;
import com.example.abo8a.jobconnector.login.LoginActivity;
import com.example.abo8a.jobconnector.login.OnBoardingActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class job_detail extends AppCompatActivity {
    Context mContext=this;
    int eid;
    int doing = 1;
    int pid;
    int cid;
    int isActive;
    ImageView avatar;
    Button accept_btn;
    Button reject_btn;
    LinearLayout linear_accept_reject;
    boolean comeFromRequest;
    boolean comeFromNotification;
    View.OnLongClickListener onLongClickListener;
    View.OnClickListener onClickListener;
    String status="";
    private ArrayList<String> listFriendID = null;
    private ListFriend dataListFriend = null;
    Button uploadButton;
    boolean onclick=true;
    boolean onlongclick=false;
    String avatarBitmap="";
    static String chatEmail;
    TextView mGender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);

        init();
    }

    private void init() {
        eid = user.id;


        accept_btn=findViewById(R.id.request_accept);
        reject_btn=findViewById(R.id.request_reject);
        linear_accept_reject=findViewById(R.id.request_accept_reject);
        avatar=findViewById(R.id.postImage);
        final TextView companyName = (TextView) findViewById(R.id.tvCompanyname);
        TextView mEmailTitle = (TextView) findViewById(R.id.tvEmailTitle);
        TextView mEmailDetails = (TextView) findViewById(R.id.tvEmailDetails);
        TextView mEmailTime = (TextView) findViewById(R.id.tvEmailTime);
        final ImageView mFavorite = (ImageView) findViewById(R.id.star_jobDetail);
        uploadButton = (Button) findViewById(R.id.EmployeeApplyButton);
        final Button ChatButton = (Button) findViewById(R.id.EmployeeChatButton);
        chatEmail="";

        TextView mDescription = (TextView) findViewById(R.id.describtion_jobDetails);
        TextView mCity = (TextView) findViewById(R.id.city_jobDetails);
        TextView mCategory = (TextView) findViewById(R.id.category_jobDetails);
        TextView mLanguages = (TextView) findViewById(R.id.languages_jobDetails);
        TextView mExperience=findViewById(R.id.experience_jobDetails);
        mGender=findViewById(R.id.gender_jobDetails);

        accept_btn.setOnClickListener(onButtonClick);
        reject_btn.setOnClickListener(onButtonClick);


        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {

            avatarBitmap=mBundle.getString("avatar");

            status=mBundle.getString("status");
            companyName.setText(mBundle.getString("sender"));
            mEmailTitle.setText(mBundle.getString("title"));
            mEmailDetails.setText(mBundle.getString("details"));
            mEmailTime.setText(mBundle.getString("time"));
            cid=mBundle.getInt("cid",0);
            mDescription.setText(mBundle.getString("description"));
            mCity.setText(mBundle.getString("city"));
            mCategory.setText(mBundle.getString("category"));
            mLanguages.setText(mBundle.getString("languages"));
            mExperience.setText(mBundle.getString("experience"));
            comeFromRequest=mBundle.getBoolean("comeFromRequest",false);
            comeFromNotification=mBundle.getBoolean("comeFromNotification",false);

            if(mBundle.getString("gender").equals(""))
                mGender.setText("No Specific Gender");
            else
                mGender.setText(mBundle.getString("gender"));

            isActive=mBundle.getInt("isActive",1);
            if(mBundle.getBoolean("color"))
                mFavorite.setColorFilter(ContextCompat.getColor(this,
                        R.color.colorOrange));
            else
                mFavorite.clearColorFilter();


            pid = mBundle.getInt("pid");
            chatEmail=mBundle.getString("chatEmail");
            companyName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(job_detail.this, CompanyProfileActivity.class);
                    i.putExtra("cid",cid);
                    i.putExtra("email",chatEmail);
                    startActivity(i);

                }
            });

            if(comeFromRequest)
            {
                onclick = false;

                if (status.equals("Pending")) {

                    onlongclick = false;
                    uploadButton.setVisibility(View.GONE);
                    linear_accept_reject.setVisibility(View.VISIBLE);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mGender.getLayoutParams();
                    params.setMargins(0, 4, 0, 80); //substitute parameters for left, top, right, bottom
                    mGender.setLayoutParams(params);
                }
                if (status.equals("Accepted")) {

                    uploadButton.setText("Accepted");
                    onlongclick=true;
                    uploadButton.setTextColor(Color.GREEN);
                }
                if (status.equals("Rejected")) {

                    uploadButton.setText("Rejected");
                    onlongclick=true;
                    uploadButton.setTextColor(Color.RED);
                }




                }
            else {

                if (status == "") {
                    uploadButton.setText("Apply");
                    onclick = true;
                    onlongclick = false;
                }
                if (status.equals("pending")) {
                    uploadButton.setText("Pending");
                    onclick = false;
                    onlongclick = true;

                }
                if (status.equals("accepted")) {
                    uploadButton.setText("Accepted");
                    uploadButton.setEnabled(false);
                }
                if (status.equals("rejected")) {
                    uploadButton.setText("Rejected");
                    uploadButton.setEnabled(false);

                }
                if (isActive == 0 && status.equals("pending")) {
                    uploadButton.setText("Closed Job");
                    uploadButton.setTextColor(Color.RED);
                    uploadButton.setEnabled(false);
                }

            }

        }

        setImageAvatar(job_detail.this,avatarBitmap);//here i have send the image in Intent and put it here

        if (dataListFriend == null) {
            dataListFriend = FriendDB.getInstance(getApplication()).getListFriend();
            System.out.println("++++++hohhoh+++++++++\n++++++++++++++"+dataListFriend.toString()+  "\n++++++++");
            listFriendID = new ArrayList<>();
            if (dataListFriend.getListFriend().size() > 0) {

                for (Friend friend : dataListFriend.getListFriend()) {
                    listFriendID.add(friend.id);

                }
            }
        }

        onLongClickListener=new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (comeFromRequest) {
                    if(onlongclick) {
                        String message = "";
                        if (((Button) v).getText().equals("Accepted"))
                            message = "Are you sure want to UnAccept this job Request?";
                        else
                            message = "Are you sure want to UnReject this job Request?";


                        new AlertDialog.Builder(job_detail.this)
                                .setTitle("Alert")
                                .setMessage(message)
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        final LovelyProgressDialog dialog = new LovelyProgressDialog(job_detail.this);

                                        dialog.setMessage("Wait a second...");
                                        dialog.show();
                                        updateStatus(pid, user.id, "Pending");
                                        linear_accept_reject.setVisibility(View.VISIBLE);
                                        uploadButton.setVisibility(View.GONE);
                                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mGender.getLayoutParams();
                                        params.setMargins(0, 4, 0, 80); //substitute parameters for left, top, right, bottom
                                        mGender.setLayoutParams(params);
                                        dialog.dismiss();
                                    }
                                })
                                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        }).show();
                    }
                } else{ if (onlongclick) {


                    new AlertDialog.Builder(job_detail.this)
                            .setTitle("Alert")
                            .setMessage("Are you sure want to UnApply this post?")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    final LovelyProgressDialog dialog = new LovelyProgressDialog(job_detail.this);

                                    dialog.setMessage("Wait a second...");
                                    dialog.show();
                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://sloppier-citizens.000webhostapp.com/job/uploadAJob.php", new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {

                                            try {

                                                dialog.dismiss();
                                                //

                                                System.out.println("++++++++" + response);
                                                JSONObject json = new JSONObject(response);


                                                if (json.getBoolean("success")) {

                                                    uploadButton.setText("Apply");

                                                    onclick = true;
                                                    onlongclick = false;
                                                    Toast.makeText(getApplicationContext(), "Job UnApplied ", Toast.LENGTH_SHORT).show();


                                                } else {
                                                    System.out.println("success not 1 ---------------------");

                                                }

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                                System.out.println("Json exception--------------------" + e.getMessage());
                                            }
                                        }
                                    }, new Response.ErrorListener() {

                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.e("job_details", " Error: " + error.getMessage());
                                            Toast.makeText(getApplicationContext(), "Check your Internet Connection and try Again", Toast.LENGTH_LONG).show();

                                        }
                                    }) {
                                        @Override
                                        protected Map<String, String> getParams() {
                                            // Posting params to register url
                                            Map<String, String> params = new HashMap<String, String>();
                                            params.put("upload", 2 + "");

                                            params.put("Eid", eid + "");// etsearch.getText().toString()
                                            params.put("Pid", pid + "");

                                            return params;
                                        }
                                    };
                                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                                    queue.add(stringRequest);

                                }


                            })
                            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).show();


                    return true;
                }
            }
                    return false;
                }

        };

        onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onclick) {
                    final LovelyProgressDialog dialog = new LovelyProgressDialog(job_detail.this);

                    dialog.setMessage("Wait a second...");
                    dialog.show();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://sloppier-citizens.000webhostapp.com/job/uploadAJob.php", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {

                                dialog.dismiss();
                                //

                                System.out.println("++++++++" + response);
                                JSONObject json = new JSONObject(response);


                                if (json.getBoolean("success")) {


                                    uploadButton.setText("Pending");
                                    onlongclick=true;
                                    onclick=false;
                                    getrecieverToken(chatEmail, LoginActivity.username);
                                    Toast.makeText(getApplicationContext(), "Job Applied ", Toast.LENGTH_SHORT).show();


                                } else {
                                    System.out.println("success not 1 ---------------------");

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                System.out.println("Json exception--------------------" + e.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("job_details", " Error: " + error.getMessage());
                            Toast.makeText(getApplicationContext(), "Check your Internet Connection and try Again", Toast.LENGTH_LONG).show();

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            // Posting params to register url
                            Map<String, String> params = new HashMap<String, String>();

                            params.put("upload", 1 + "");


                            params.put("Eid", eid + "");// etsearch.getText().toString()
                            params.put("Pid", pid + "");

                            return params;
                        }
                    };
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    queue.add(stringRequest);

                }
            }



        };



        uploadButton.setOnLongClickListener(onLongClickListener);
        uploadButton.setOnClickListener(onClickListener);

        ChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                findIDEmail(chatEmail);

            }
        });


        mFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFavorite.getColorFilter() != null) {
                    mFavorite.clearColorFilter();
                    handleFavorite(0,pid);

                    doing = 0;
                } else {
                    mFavorite.setColorFilter(ContextCompat.getColor(job_detail.this,
                            R.color.colorOrange));
                    handleFavorite(1,pid);

                    doing = 1;
                }
            }
        });
    }

    View.OnClickListener onButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mGender.getLayoutParams();
            params.setMargins(0, 4, 0, 0); //substitute parameters for left, top, right, bottom
            mGender.setLayoutParams(params);
            if (v.getId() == R.id.request_accept) {

                updateStatus(pid, user.id, "Accepted");
                uploadButton.setText("Accepted");

                getrecieverToken(chatEmail,LoginActivity.username);
                linear_accept_reject.setVisibility(View.GONE);
                uploadButton.setVisibility(View.VISIBLE);
                uploadButton.setTextColor(Color.GREEN);
                onlongclick=true;

            } else {
                updateStatus(pid, user.id, "Rejected");
                uploadButton.setText("Rejected");

                getrecieverToken(chatEmail,LoginActivity.username);
                linear_accept_reject.setVisibility(View.GONE);
                uploadButton.setVisibility(View.VISIBLE);
                uploadButton.setTextColor(Color.RED);

                onlongclick=true;

            }

        }
    };


    public void getrecieverToken(final String email, final String mSender) {

        FirebaseDatabase.getInstance().getReference().child("user").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("++++++ the datasnapshot is "+dataSnapshot.toString());

                if( dataSnapshot.getValue()==null)
                    System.out.println("+++\n++++++\n+++++\n++ No token for this email since it is not rejestered yet\n");
                else {
                    String id = ((HashMap) dataSnapshot.getValue()).keySet().iterator().next().toString();
                    HashMap userMap = (HashMap) ((HashMap) dataSnapshot.getValue()).get(id);

                    System.out.println("+++\n+++ Email is " + chatEmail + " token is " + (String) userMap.get("token") + "     namee  " + id + " staticUid is " + StaticConfig.UID + "\n my token is " + FirebaseInstanceId.getInstance()+"+++\n");


                    if(!comeFromRequest)
                    sendPushNotificationToReceiver(mSender,  " Applyied to your Post,"+pid, StaticConfig.UID, FirebaseInstanceId.getInstance().getToken(), (String) userMap.get("token"));

                    else
                    {
                        if(uploadButton.getText().equals("Accepted"))
                            sendPushNotificationToReceiver(mSender,  " Accepted your Job Request/"+eid+"/"+email+"/Accepted", StaticConfig.UID, FirebaseInstanceId.getInstance().getToken(), (String) userMap.get("token"));

                        else
                            sendPushNotificationToReceiver(mSender,  " Rejected your Job Request/"+eid+"/"+email+"/Rejected", StaticConfig.UID, FirebaseInstanceId.getInstance().getToken(), (String) userMap.get("token"));


                    }

                }
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("++++++ token is failed");

            }
        });
    }

    private void findIDEmail(String email) {

        FirebaseDatabase.getInstance().getReference().child("user").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                 String id = ((HashMap) dataSnapshot.getValue()).keySet().iterator().next().toString();

                HashMap userMap = (HashMap) ((HashMap) dataSnapshot.getValue()).get(id);


                Friend user = new Friend();
                user.name = (String) userMap.get("name");
                user.email = (String) userMap.get("email");
                user.avata = (String) userMap.get("avata");
                user.id = id;
                user.idRoom = id.compareTo(StaticConfig.UID) > 0 ? (StaticConfig.UID + id).hashCode() + "" : "" + (id + StaticConfig.UID).hashCode();
                checkBeforAddFriend(id, user);

                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra(StaticConfig.INTENT_KEY_CHAT_FRIEND,user.name);
                ArrayList<CharSequence> idFriend = new ArrayList<CharSequence>();
                idFriend.add(id);
                intent.putCharSequenceArrayListExtra(StaticConfig.INTENT_KEY_CHAT_ID, idFriend);
                intent.putExtra(StaticConfig.INTENT_KEY_CHAT_ROOM_ID, user.idRoom);
                ChatActivity.bitmapAvataFriend = new HashMap<>();
                if (!user.avata.equals(StaticConfig.STR_DEFAULT_BASE64)) {
                    byte[] decodedString = Base64.decode(user.avata, Base64.DEFAULT);
                    ChatActivity.bitmapAvataFriend.put(id, BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));
                } else {
                    ChatActivity.bitmapAvataFriend.put(id, BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.default_avata));
                }
                getApplication().startActivity(intent);





            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void checkBeforAddFriend(final String idFriend, Friend userInfo) {

        //Check xem da ton tai id trong danh sach id chua
        if (listFriendID.contains(idFriend)) {

            System.out.println("+++++++ friend already existsssssss");

        } else {
            addFriend(idFriend, true);
            listFriendID.add(idFriend);
            FriendDB.getInstance(getApplication()).getListFriend().getListFriend().add(userInfo);
            FriendDB.getInstance(getApplication()).addFriend(userInfo);
        }
    }

    private void addFriend(final String idFriend, boolean isIdFriend) {
        if (idFriend != null) {
            if (isIdFriend) {
                FirebaseDatabase.getInstance().getReference().child("friend/" + StaticConfig.UID).push().setValue(idFriend)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    addFriend(idFriend, false);
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {


                            }
                        });
            } else {
                FirebaseDatabase.getInstance().getReference().child("friend/" + idFriend).push().setValue(StaticConfig.UID).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            addFriend(null, false);
                        }
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
            }
        }
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

    private void setImageAvatar(Context context, String imgBase64){
        try {
            Resources res = getResources();
            Bitmap src;
            if (imgBase64.equals("default")) {
                src = BitmapFactory.decodeResource(res, R.drawable.default_avata);
            } else {
                byte[] decodedString = Base64.decode(imgBase64, Base64.DEFAULT);
                src = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            }

            avatar.setImageDrawable(ImageUtils.roundedImage(context, src));
        }catch (Exception e){
        }
    }


    public void handleFavorite(final int action,final int pids){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://sloppier-citizens.000webhostapp.com/job/doEmployeePosts.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    //

                    System.out.println("++++++++"+response);
                    JSONObject json = new JSONObject(response);



                    if (json.getBoolean("success"))
                    {
                        if(action==1)
                            Toast.makeText(mContext,"Added to Saved Posts ",Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(mContext,"deleted from Saved Posts ",Toast.LENGTH_SHORT).show();

                    }


                    else {
                        System.out.println("success not 1 ---------------------");


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("Json exception--------------------");
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext, "Check your Internet Connection and try Again", Toast.LENGTH_LONG).show();

            }
        })  {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();




                params.put("eid",eid+"");// etsearch.getText().toString()
                params.put("Pid",pids+"");
                params.put("do",action+"");

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(stringRequest);



    }

    public void updateStatus(final int pid, final int eid, final String status) {

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


                                Toast.makeText(getApplicationContext(),
                                       "Employee Already Applied to your post or has been requested", Toast.LENGTH_LONG).show();
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
                        params.put("pid", pid + "");
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

    @Override
    public void onBackPressed() {
        if(comeFromNotification)
        startActivity(new Intent(this, OnBoardingActivity.class));
        super.onBackPressed();
    }
}
