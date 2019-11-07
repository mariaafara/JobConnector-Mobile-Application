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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.abo8a.jobconnector.EmployeeProfile.recyclerView_Experience;

/**
 * Created by abo8a on 4/1/2018.
 */

public class ExperienceRecycleAdapter extends RecyclerView.Adapter<ExperienceRecycleAdapter.ViewHolder> {

    View mainView;
    TextView CName;
    TextView CJobIn;
    TextView CPosition;
    TextView CDuration;
    TextView CCountry;
    TextView CCity;
    TextViewExpandableAnimation CDescription;
    TextView cExid;
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView CompanyName;
        TextView JobIn;
        TextView Position;
        TextView Duration;
        TextView Country;
        TextView City;
        TextView exid;
        TextViewExpandableAnimation desc;

        public ViewHolder(View v) {
            super(v);
            mainView = v;

            CompanyName = itemView.findViewById(R.id.expCompanyName);
            JobIn = itemView.findViewById(R.id.expJob);
            Position = itemView.findViewById(R.id.expPosition);
            Duration = itemView.findViewById(R.id.expDuration);
            Country = itemView.findViewById(R.id.expCountry);
            City = itemView.findViewById(R.id.expCity);
            exid = itemView.findViewById(R.id.expEexid);
            desc = (TextViewExpandableAnimation) itemView.findViewById(R.id.expDescription);

            CName = CompanyName;
            CJobIn = JobIn;
            CPosition = Position;
            CDuration = Duration;
            CCountry = Country;
            CCity = City;
            CDescription = desc;
            cExid=exid;
        }
    }


    private ArrayList<Experience> Data;
    private Context mContext; //to be able to have access to certain methods,

    public ExperienceRecycleAdapter(Context mContext, ArrayList<Experience> Data) {
        this.Data = Data;
        this.mContext = mContext;
    }


    @Override
    public ExperienceRecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.experience_layout,
                parent, false);

        return new ExperienceRecycleAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ExperienceRecycleAdapter.ViewHolder holder, final int position) {


     Experience classdata = (Experience) Data.get(position);
        //     final ClassListItems classListItems = values.get(position);
        //    holder.textTitle.setText(classListItems.getTitle() + "");

        mainView.setOnLongClickListener(new View.OnLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onLongClick(View view) {
                popupWindowsort(position);
                return false;
            }
        });

        System.out.println(" the exeed in class data is "+classdata.getId());
        holder.exid.setText(classdata.getId()+"");
        holder.CompanyName.setText(classdata.getCompany_name());
        holder.JobIn.setText(classdata.getJob_In());
        holder.desc.setText(classdata.getDescription());
        holder.Duration.setText(classdata.getExperience_duration());
        holder.Country.setText(classdata.getJob_country());
        holder.City.setText(classdata.getJob_city());

    }


    @Override
    public int getItemCount() {
        return Data.size();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PopupWindow popupWindowsort(final int position) {

        // initialize a pop up window type
        final PopupWindow popupWindow;
        popupWindow = new PopupWindow(mContext);

        final Experience classdata=(Experience)Data.get(position);
        ArrayList<String> sortList = new ArrayList<String>();
        sortList.add("Edit");
        sortList.add("Delete");


//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
//                android.R.layout.simple_dropdown_item_1line, sortList);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
                android.R.layout.simple_spinner_dropdown_item, sortList);
        // the drop down list is a list view
        ListView listViewSort = new ListView(mContext);

        // set our adapter and pass our pop up window contents
        listViewSort.setAdapter(adapter);

        listViewSort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Delete Experience")
                            .setMessage("Are you sure you want to delete ")
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    removeExperience(Data.get(position).getId());
                                    Data.remove(position);
                                    EmployeeProfile.ExperienceAdapter = new ExperienceRecycleAdapter(mContext, Data);
                                    recyclerView_Experience.setAdapter(EmployeeProfile.ExperienceAdapter);
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


                } else if (i == 0) {



                    Intent intent = new Intent(mContext, editExperience.class);
                    System.out.println("++++++++ EDIT the Exid is    "+classdata.getId());
                    intent.putExtra("id",classdata.getId());
                    intent.putExtra("cname",      classdata.getCompany_name());
                    intent.putExtra("jobIn"      ,classdata.getJob_In());
                    intent.putExtra("description",classdata.getDescription());
                    intent.putExtra("duration"   ,classdata.getExperience_duration());
                    intent.putExtra("country",    classdata.getJob_country());
                    intent.putExtra("city",       classdata.getJob_city());

                    mContext.startActivity(intent);

                } else
                    popupWindow.dismiss();

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


    public void removeExperience(final int exid) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://sloppier-citizens.000webhostapp.com/job/removeExperience.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    //

                    System.out.println("++++++++" + response);
                    JSONObject json = new JSONObject(response);


                    if (json.getBoolean("success")) {

                        Toast.makeText(mContext, " Deleted", Toast.LENGTH_SHORT).show();

                    } else {
                        System.out.println("success not 1 ---------------------");

                        Toast.makeText(mContext, "Failed to delete", Toast.LENGTH_SHORT).show();


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("Json exception--------------------");
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LanguageRecycler", error.getMessage());
                Toast.makeText(mContext, "Check your Internet Connection and try Again", Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();


                params.put("exid", exid+"");


                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(stringRequest);


    }

}
