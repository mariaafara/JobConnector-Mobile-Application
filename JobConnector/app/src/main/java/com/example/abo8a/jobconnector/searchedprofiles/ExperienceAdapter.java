package com.example.abo8a.jobconnector.searchedprofiles;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.abo8a.jobconnector.Experience;
import com.example.abo8a.jobconnector.R;
import com.example.abo8a.jobconnector.TextViewExpandableAnimation;

import java.util.ArrayList;

/**
 * Created by abo8a on 4/1/2018.
 */

public class ExperienceAdapter extends RecyclerView.Adapter<ExperienceAdapter.ViewHolder> {

    View mainView;
    TextView CName;
    TextView CJobIn;
    TextView CPosition;
    TextView CDuration;
    TextView CCountry;
    TextView CCity;
    TextViewExpandableAnimation CDescription;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView CompanyName;
        TextView JobIn;
        TextView Position;
        TextView Duration;
        TextView Country;
        TextView City;
        TextView Description;
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

            desc = (TextViewExpandableAnimation) itemView.findViewById(R.id.expDescription);

            CName = CompanyName;
            CJobIn = JobIn;
            CPosition = Position;
            CDuration = Duration;
            CCountry = Country;
            CCity = City;
            CDescription = desc;
        }
    }


    private ArrayList<Experience> Data;
    private Context mContext; //to be able to have access to certain methods,

    public ExperienceAdapter(Context mContext, ArrayList<Experience> Data) {
        this.Data = Data;
        this.mContext = mContext;
    }


    @Override
    public ExperienceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.experience_layout,
                parent, false);

        return new ExperienceAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ExperienceAdapter.ViewHolder holder, final int position) {

        Experience classdata = (Experience) Data.get(position);
        //     final ClassListItems classListItems = values.get(position);
        //    holder.textTitle.setText(classListItems.getTitle() + "");


        holder.CompanyName.setText(classdata.getCompany_name());
        holder.JobIn.setText(classdata.getJob_In());
        holder.desc.setText(classdata.getDescription());
        holder.Duration.setText(classdata.getExperience_duration()+"");
        holder.Country.setText(classdata.getJob_country());
        holder.City.setText(classdata.getJob_city());
        holder.desc.setText(classdata.getDescription());

    }


    @Override
    public int getItemCount() {
        return Data.size();
    }


}
