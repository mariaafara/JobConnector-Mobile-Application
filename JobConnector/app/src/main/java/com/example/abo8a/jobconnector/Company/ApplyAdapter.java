package com.example.abo8a.jobconnector.Company;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.abo8a.jobconnector.ApplyData;
import com.example.abo8a.jobconnector.R;

import java.util.List;

/**
 * Created by lenovo on 3/19/2018.
 */
//this is the class plays an important role in handling the data in Recyclerview.


public class ApplyAdapter extends RecyclerView.Adapter<ApplyAdapter.ViewHolder> {

    int pid;
    int cid;
    View mainView;

    public  class ViewHolder extends RecyclerView.ViewHolder {
        TextView status;

        TextView nbrappliers;


        public ViewHolder(View v) {
            super(v);
            mainView = v;
            status = itemView.findViewById(R.id.status);
            nbrappliers = itemView.findViewById(R.id.nbrEmployee);

        }
    }


    private List Data;
    private Context mContext; //to be able to have access to certain methods,

    public ApplyAdapter(Context mContext, List Data) {
        this.Data = Data;
        this.mContext = mContext;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.applyitem,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
//bothot

        final ApplyData classdata = (ApplyData) Data.get(position);//list hhayy

        cid = CompanyMainActivity.cid;
        pid = classdata.getPid();
        holder.status.setText(classdata.getStatus());
        holder.nbrappliers.setText(classdata.getNbr_applicants() + "");


        if(classdata.getNbr_applicants()!=0)
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext, CompanyAppliedEmployeeActivity.class);
                mIntent.putExtra("pid", classdata.getPid());
                mIntent.putExtra("status", classdata.getStatus());

                mContext.startActivity(mIntent);

            }
        });

    }


    @Override
    public int getItemCount() {
        return Data.size();
    }


}


