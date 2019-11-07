package com.example.abo8a.jobconnector.Company;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.abo8a.jobconnector.R;


public class CompanyRegisterFragment2 extends Fragment {

    public static EditText company_url;
    public static EditText company_phone;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.company_register_fragment2, container, false);

        init();
        return view;
    }

    @Override
    public void onStart() {

       // init();
        super.onStart();
        company_url = getActivity().findViewById(R.id.company_url);
        company_phone = getActivity().findViewById(R.id.company_phone);
    }

    public void init() {

    }




}
