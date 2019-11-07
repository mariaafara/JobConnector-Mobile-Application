package com.example.abo8a.jobconnector.searchedprofiles;

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
import com.example.abo8a.jobconnector.EmployeeProfile;
import com.example.abo8a.jobconnector.R;
import com.example.abo8a.jobconnector.Skill;
import com.example.abo8a.jobconnector.user;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.abo8a.jobconnector.EmployeeProfile.recyclerView_Skills;

/**
 * Created by abo8a on 4/1/2018.
 */

public class SkillAdapter extends RecyclerView.Adapter<SkillAdapter.ViewHolder> {


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Skill;
        TextView level;
        TextView remove;

        public ViewHolder(View v) {
            super(v);
            Skill = itemView.findViewById(R.id.recycleSkill);
            level = itemView.findViewById(R.id.recycleSkillLevel);
            remove = itemView.findViewById(R.id.Link_Remove_Skill);
        }
    }


    private ArrayList<Skill> Data;
    private Context mContext; //to be able to have access to certain methods,

    public SkillAdapter(Context mContext, ArrayList<Skill> Data) {
        this.Data = Data;
        this.mContext = mContext;
    }


    @Override
    public SkillAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.skill_recycle_view,
                parent, false);

        return new SkillAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SkillAdapter.ViewHolder holder, final int position) {

        final Skill classdata = (Skill) Data.get(position);
        //     final ClassListItems classListItems = values.get(position);
        //    holder.textTitle.setText(classListItems.getTitle() + "");

        holder.Skill.setText(classdata.getSkill());
        holder.level.setText(classdata.getLevel());
        holder.remove.setVisibility(View.GONE);
        //  System.out.println("+++++from holder++ pos is+++"+position+"\n ++++++++ languages is "+classdata.getLanguage());


    }


    @Override
    public int getItemCount() {
        return Data.size();
    }


}
