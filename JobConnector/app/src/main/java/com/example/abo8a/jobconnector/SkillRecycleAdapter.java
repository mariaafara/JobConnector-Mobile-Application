package com.example.abo8a.jobconnector;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import static com.example.abo8a.jobconnector.EmployeeProfile.recyclerView_Skills;

/**
 * Created by abo8a on 4/1/2018.
 */

public class SkillRecycleAdapter  extends RecyclerView.Adapter<SkillRecycleAdapter.ViewHolder> {


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Skill;
        TextView level;
        TextView remove;

        public ViewHolder(View v) {
            super(v);
            Skill = itemView.findViewById(R.id.recycleSkill);
            level = itemView.findViewById(R.id.recycleSkillLevel);
            remove=itemView.findViewById(R.id.Link_Remove_Skill);
        }
    }


    private ArrayList<Skill> Data;
    private Context mContext; //to be able to have access to certain methods,

    public SkillRecycleAdapter(Context mContext,  ArrayList<Skill> Data) {
        this.Data = Data;
        this.mContext = mContext;
    }


    @Override
    public SkillRecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.skill_recycle_view,
                parent, false);

        return new SkillRecycleAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SkillRecycleAdapter.ViewHolder holder, final int position) {

      final  Skill classdata = (Skill) Data.get(position);
        //     final ClassListItems classListItems = values.get(position);
        //    holder.textTitle.setText(classListItems.getTitle() + "");

        holder.Skill.setText(classdata.getSkill());
        holder.level.setText(classdata.getLevel());

        //  System.out.println("+++++from holder++ pos is+++"+position+"\n ++++++++ languages is "+classdata.getLanguage());

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Delete Language")
                        .setMessage("Are you sure you want to delete "+classdata.getSkill())
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                removeSkill(classdata.getSkill());
                                Data.remove(position);
                                EmployeeProfile.SkillAdapter= new SkillRecycleAdapter(mContext, Data);
                                recyclerView_Skills.setAdapter(EmployeeProfile.SkillAdapter);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();



            }
        });




    }

    private void removeSkill(final String skill) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://sloppier-citizens.000webhostapp.com/job/removeSkill" +
                ".php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject json = new JSONObject(response);

                    if (json.getBoolean("success"))
                    {

                        Toast.makeText(mContext,skill+" Deleted",Toast.LENGTH_SHORT).show();

                    }


                    else {
                        System.out.println("success not 1 ---------------------");

                        Toast.makeText(mContext,"Failed to delete",Toast.LENGTH_SHORT).show();


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
        })  {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();




                params.put("eid",user.id+"");
                params.put("skill",skill);

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(stringRequest);



    }


    @Override
    public int getItemCount() {
        return Data.size();
    }


}
