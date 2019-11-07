package com.example.abo8a.jobconnector.Company;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.abo8a.jobconnector.R;

import static android.content.ContentValues.TAG;


public class CompanyRegisterFragment1 extends Fragment {
    public static EditText company_name;
    public static EditText bus_type;
    public static EditText company_info;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.company_register_fragment1, container, false);

        return view;
    }

    @Override
    public void onStart() {

        // init();
        super.onStart();
        company_name = getActivity().findViewById(R.id.company_name);
        company_info = getActivity().findViewById(R.id.company_info);
        bus_type = getActivity().findViewById(R.id.bus_type);


    }

//    @Override
//    public void onResume() {
//
//        super.onResume();
//        if (TextUtils.isEmpty(company_name.getText().toString())
//                && TextUtils.isEmpty(company_info.getText().toString())
//                && TextUtils.isEmpty(bus_type.getText().toString())) {
//            CompanyRegistration.btn_next.setFocusable(true);
//            CompanyRegistration.btn_next.setEnabled(true);
//        } else {
//            CompanyRegistration.btn_next.setFocusable(false);
//            CompanyRegistration.btn_next.setEnabled(false);
//
//        }
//        Toast.makeText(getActivity(), "onResumeFragment():", Toast.LENGTH_SHORT).show();
//    }
//
    public void init() {


    }
}
