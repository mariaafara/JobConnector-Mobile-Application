package com.example.abo8a.jobconnector.Company;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.example.abo8a.jobconnector.R;


public class CompanyJobLocationFragment extends Fragment {

    public static Spinner job_city;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.company_job_location_fragment, container, false);
    }
    @Override
    public void onStart() {

        super.onStart();
        job_city = getActivity().findViewById(R.id.post_job_city_spinner);




    }

}
