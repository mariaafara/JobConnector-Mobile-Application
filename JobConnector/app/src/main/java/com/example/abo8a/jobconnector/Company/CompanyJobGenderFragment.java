package com.example.abo8a.jobconnector.Company;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.abo8a.jobconnector.R;


public class CompanyJobGenderFragment extends Fragment {


    public static RadioButton males;
    public static RadioButton females;
    //    public static RadioButton no;
//    public static RadioButton yes;
    public static RadioGroup group;
    public static String gender;

    public static RadioButton no;
    public static RadioButton yes;

    // private int cid;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.company_job_gender_fragment, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        males = (RadioButton) getActivity().findViewById(R.id.btn_males);
        females = (RadioButton) getActivity().findViewById(R.id.btn_females);
        no = (RadioButton) getActivity().findViewById(R.id.btn_no);
        yes = (RadioButton) getActivity().findViewById(R.id.btn_yes);
        group = (RadioGroup) getActivity().findViewById(R.id.radiogroup);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                group.setVisibility(View.GONE);
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                group.setVisibility(View.VISIBLE);

            }
        });


    }


}
