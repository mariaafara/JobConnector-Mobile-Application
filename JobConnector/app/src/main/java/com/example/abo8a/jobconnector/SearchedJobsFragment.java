package com.example.abo8a.jobconnector;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;


public class SearchedJobsFragment extends Fragment {

    public static RecyclerView jobrecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private JobAdapter myAppAdapter;

    public static ImageView no_results;
    public static View jobSEarchView;
    ArrayList<JobData> data;
    ArrayList<String> bundlearray;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        no_results=getActivity().findViewById(R.id.no_results_found);
        jobrecyclerView = getActivity().findViewById(R.id.recyclerViewForSearch); //Recylcerview Declaration

        jobrecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());

        jobrecyclerView.setLayoutManager(mLayoutManager);

        jobrecyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), DividerItemDecoration.VERTICAL));


        myAppAdapter = new JobAdapter(getActivity().getApplicationContext(), data);

        Bundle bundle = this.getArguments();
        if(bundle != null) {
            bundlearray = bundle.getStringArrayList("data");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        jobSEarchView = inflater.inflate(R.layout.searched_jobs_fragment, container, false);

        return jobSEarchView;
    }


    public  void setViewLayout(int id){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        jobSEarchView = inflater.inflate(id, null);
        ViewGroup rootView = (ViewGroup) getView();
        rootView.removeAllViews();
        rootView.addView(jobSEarchView);
    }




}
