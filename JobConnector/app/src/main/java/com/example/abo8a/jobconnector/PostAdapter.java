package com.example.abo8a.jobconnector;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.abo8a.jobconnector.Company.CompanyAppliedPostsActivity;
import com.example.abo8a.jobconnector.Company.CompanyMainActivity;
import com.example.abo8a.jobconnector.Company.CompanyMainMyJobsFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 3/19/2018.
 */
//this is the class plays an important role in handling the data in Recyclerview.


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    int pid;
    int cid;
    static int state = 1;
    View mainView;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView jobtype;
        TextView companyName;
        TextView nbrappliers;
        TextView setting;


        public ViewHolder(View v) {
            super(v);
            mainView = v;
            jobtype = itemView.findViewById(R.id.post_job_type);
            companyName = itemView.findViewById(R.id.post_company_name);
            setting = itemView.findViewById(R.id.post_setting);
            nbrappliers = itemView.findViewById(R.id.post_applicants);

        }
    }


    private List Data;
    private Context mContext; //to be able to have access to certain methods,

    public PostAdapter(Context mContext, List Data) {
        this.Data = Data;
        this.mContext = mContext;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.postitem,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
//bothot

        final PostData classdata = (PostData) Data.get(position);//list hhayy

        cid = CompanyMainActivity.cid;

        pid = classdata.getPid();
        System.out.println("+++++++++++++++++++++++++++++ Pid,cid+++++++++++++++++++++++++++++" +
                "+++++++++++++++++ " + pid + "++===" + cid);
        holder.companyName.setText(classdata.getCompany_name());
        holder.jobtype.setText(classdata.getJob_type());
        holder.nbrappliers.setText(classdata.getNbr_applicants() + "");

        holder.setting.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                popupWindowsetting(position);
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("+++++++++++++++++++++++++++++ Pid+++++++++++++++++++++++++++++" +
                        "+++++++++++++++++ " + pid);

                Intent mIntent = new Intent(mContext, CompanyAppliedPostsActivity.class);
                mIntent.putExtra("applicants", classdata.getNbr_applicants());
                mIntent.putExtra("pid", classdata.getPid());
                mIntent.putExtra("companyName",classdata.getCompany_name());
                mContext.startActivity(mIntent);


            }
        });

    }


    @Override
    public int getItemCount() {
        return Data.size();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PopupWindow popupWindowsetting(final int position) {

        // initialize a pop up window type
        final PopupWindow popupWindow;
        popupWindow = new PopupWindow(mContext);

        ArrayList<String> sortList = new ArrayList<String>();
        sortList.add("Close Job");



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
                android.R.layout.simple_spinner_dropdown_item, sortList);
        // the drop down list is a list view
        ListView listViewSort = new ListView(mContext);

        // set our adapter and pass our pop up window contents
        listViewSort.setAdapter(adapter);

        listViewSort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CompanyMainActivity.context);
                    builder.setTitle("Closing Job")
                            .setMessage("Are you sure you want to close the job ? ")
                            .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                       closePost(position);
                                      Data.remove(position);
                                    CompanyMainMyJobsFragment.myAppAdapter = new PostAdapter(mContext, Data);
                                    CompanyMainMyJobsFragment.recyclerView.setAdapter( CompanyMainMyJobsFragment.myAppAdapter);
                                    popupWindow.dismiss();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                } else popupWindow.dismiss();

            }
        });
        // set on item selected
        //  listViewSort.setOnItemClickListener(onItemClickListener());

        // some other visual settings for popup window
        popupWindow.setFocusable(true);
        popupWindow.setWidth(250);
        //popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.white));
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        // set the list view as pop up window content
        popupWindow.setContentView(listViewSort);

        popupWindow.setBackgroundDrawable(mContext.getDrawable(android.R.drawable.picture_frame));
        // popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_clear));
        popupWindow.setElevation(20);
        popupWindow.showAtLocation(mainView, Gravity.RELATIVE_LAYOUT_DIRECTION, 0, 0);
        return popupWindow;
    }

    public void closePost(final int pos)
    {
        String url="https://sloppier-citizens.000webhostapp.com/company/closePost.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    System.out.println("++++++++" + response);
                    JSONObject jObj = new JSONObject(response);

                    if (jObj.getBoolean("success")) {

                       Toast.makeText(mContext,"Post closed",Toast.LENGTH_SHORT).show();

                    } else {
                        System.out.println(" success not 1 for closePost---------------------");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("Json exception--------------------");
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("PostAdapter", "ClosePost Error: " + error.getMessage());
                Toast.makeText(mContext, "Check your Internet Connection and try Again", Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();

                PostData postData=(PostData)Data.get(pos);
                params.put("pid",postData.getPid()+"");

                return params;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(stringRequest);
    }

}


