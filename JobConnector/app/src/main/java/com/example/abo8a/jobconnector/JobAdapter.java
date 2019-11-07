package com.example.abo8a.jobconnector;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.abo8a.jobconnector.chat.util.ImageUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 3/19/2018.
 */
//this is the class plays an important role in handling the data in Recyclerview.



public class JobAdapter extends RecyclerView.Adapter<JobAdapter.ViewHolder> {

    int pid;
    static int eid;
    static int state=1;

    String avatarBitmap;
    int cid;

   HashMap<String,String> hashMap=new HashMap<>();
  final HashMap<Integer,String> hashcolor=new HashMap<>();

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mSender;
        TextView mEmailTitle;
        TextView mEmailDetails;
        TextView mEmailTime;
        ImageView mFavorite;
        TextView mCategory;
        TextView mGender;
        ImageView postIcon;
        String mChatEmail;
        TextView status;
        public ViewHolder(View v) {
            super(v);
            postIcon = itemView.findViewById(R.id.postIcon);
            mSender = itemView.findViewById(R.id.tvEmailSender);
            mEmailTitle = itemView.findViewById(R.id.tvEmailTitle);
            mEmailDetails = itemView.findViewById(R.id.tvEmailDetails);
            mEmailTime = itemView.findViewById(R.id.tvEmailTime);
            mFavorite = itemView.findViewById(R.id.ivFavorite);
            mCategory=itemView.findViewById(R.id.tvpostExperience);
            mGender=itemView.findViewById(R.id.tvGenderPost);
            status=itemView.findViewById(R.id.post_status);
        }
    }


    private List Data;
    public static Context mContext; //to be able to have access to certain methods,

    public JobAdapter(Context mContext, List Data) {
        this.Data = Data;
        this.mContext = mContext;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.jobitem,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final JobData classdata = (JobData) Data.get(position);


        cid=classdata.getCid();
        eid=user.id;
        pid=classdata.getPid();
        System.out.println("++++++++ Pid,Eid++++ "+pid+" "+eid);
        holder.mSender.setText(classdata.getmSender());
        holder.mEmailTitle.setText(classdata.getmTitle());
        holder.mEmailDetails.setText(classdata.getmDetails());
        holder.mEmailTime.setText(classdata.getmTime().split(" ")[0]);
        holder.mCategory.setText(classdata.getMexperience());
        holder.mGender.setText(classdata.getMgender());
        holder.mChatEmail=classdata.getmChatEmail();
        holder.status.setText(classdata.getStatus());
        if(holder.status.getText().equals("rejected"))
            holder.status.setTextColor(Color.RED);
        System.out.println("++++++++++++++"+holder.mChatEmail);
        System.out.println("+++++++++++++ggggg+"+hashMap.get(holder.mChatEmail));

        if(hashMap.get(holder.mChatEmail)==null) {
            setImage(holder.mChatEmail, holder.postIcon);

        }
        else
            setImageAvatar(mContext,hashMap.get(holder.mChatEmail),holder.postIcon);


        if(hashcolor.get(classdata.getPid())==null)
        handlecolor(classdata.getPid(), holder);
        else
        {
            if(hashcolor.get(classdata.getPid()).equals("1"))
                holder.mFavorite.setColorFilter(ContextCompat.getColor(mContext,
                        R.color.colorOrange));
            else
                holder.mFavorite.clearColorFilter();

        }




        holder.mFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.mFavorite.getColorFilter() != null) {
                    holder.mFavorite.clearColorFilter();
                    handleFavorite(0,classdata.getPid());
                } else {
                    holder.mFavorite.setColorFilter(ContextCompat.getColor(mContext,
                            R.color.colorOrange));
                    handleFavorite(1,classdata.getPid());
                }
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext, job_detail.class);
                mIntent.putExtra("cid",cid);
                mIntent.putExtra("sender", holder.mSender.getText().toString());
                mIntent.putExtra("title", holder.mEmailTitle.getText().toString());
                mIntent.putExtra("details", holder.mEmailDetails.getText().toString());
                mIntent.putExtra("time", holder.mEmailTime.getText().toString());
               // mIntent.putExtra("icon", holder.mIcon.getText().toString());
                mIntent.putExtra("experience", classdata.getMexperience()
                );
                mIntent.putExtra("gender", holder.mGender.getText().toString());
                mIntent.putExtra("chatEmail",holder.mChatEmail);
                mIntent.putExtra("pid",classdata.getPid());
                mIntent.putExtra("avatar",hashMap.get(holder.mChatEmail));
                mIntent.putExtra("description",classdata.getmDescription());
                mIntent.putExtra("city",classdata.getmCity());
                mIntent.putExtra("languages",classdata.getmLanguages());
                mIntent.putExtra("category",classdata.getmCategory());
                mIntent.putExtra("status",classdata.getStatus());
                if(classdata.getIsActive()==3)
                    mIntent.putExtra("comeFromRequest",true);
                if (holder.mFavorite.getColorFilter() != null) {
                    mIntent.putExtra("color",true);

                }

                else
                    mIntent.putExtra("color",false);

                //  mIntent.putExtra("colorIcon", color);
                mContext.startActivity(mIntent);
//
            }
        });

    }



    @Override
    public int getItemCount() {
        return Data.size();
    }


    public  void handleFavorite(final int action,final int pids){

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

    public  void handlecolor(final int pid,final ViewHolder holder)
    {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://sloppier-citizens.000webhostapp.com/job/checkPost.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {

                        hashcolor.put(pid,"1");
                        holder.mFavorite.setColorFilter(ContextCompat.getColor(mContext,
                                R.color.colorOrange));

                    } else {
                        hashcolor.put(pid,"0");

                        holder.mFavorite.clearColorFilter();


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(mContext.getPackageName(), "Login Error: " + error.getMessage());

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();



                params.put("eid",eid+"");// etsearch.getText().toString()
                params.put("Pid",pid+"");




                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);

    }

    public void setImage(final String email, final ImageView avatar){


        if(email!=null) {
            FirebaseDatabase.getInstance().getReference().child("user").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getValue() != null) {
                        String id = ((HashMap) dataSnapshot.getValue()).keySet().iterator().next().toString();
                        HashMap userMap = (HashMap) ((HashMap) dataSnapshot.getValue()).get(id);
                            setImageAvatar(mContext, (String) userMap.get("avata"), avatar);
                            avatarBitmap=(String) userMap.get("avata");
                            hashMap.put(email,avatarBitmap);

                        }

                     else {

                        setImageAvatar(EmployeeSearchActivity.context, "" +
                                "", avatar);

                        System.out.println("++++++++++++++ the email in fire base is null ; " + email);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });
        }
        else
            System.out.println("++++++++++++++ the email in fire base is null ; ");


    }
    private void setImageAvatar(Context context, String imgBase64,ImageView avatar){
        try {

            Resources res = context.getResources();
            Bitmap src;
            if (imgBase64.equals("default")||imgBase64.equals("")) {
                src = BitmapFactory.decodeResource(res, R.drawable.default_avata);
            } else {
                byte[] decodedString = Base64.decode(imgBase64, Base64.DEFAULT);
                src = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            }
            avatar.setImageDrawable(ImageUtils.roundedImage(context, src));
        }catch (Exception e){
        }
    }





}


