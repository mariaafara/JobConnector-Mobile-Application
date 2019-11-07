package com.example.abo8a.jobconnector.Company;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.abo8a.jobconnector.R;

public class CompanyJobDescriptionFragment extends Fragment {

    public static EditText job_pos;
    public static EditText job_desc;

    public CompanyJobDescriptionFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.company_job_description_fragment, container, false);
    }
    @Override
    public void onStart() {

        // init();
        super.onStart();
        job_desc = getActivity().findViewById(R.id.company_jobdescription);
        job_pos = getActivity().findViewById(R.id.company_jobposition);

        job_pos.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(job_desc.getText().toString())
                        && !job_pos.getText().toString().equals("")) {
                    CompanyPostJobActivity.btn_next.setFocusable(true);
                    CompanyPostJobActivity.btn_next.setEnabled(true);
                    CompanyPostJobActivity.btn_next.setBackgroundResource(R.drawable.buttonscontinuestyle);
                } else {
                    CompanyPostJobActivity.btn_next.setFocusable(false);
                    CompanyPostJobActivity.btn_next.setEnabled(false);
                    CompanyPostJobActivity.btn_next.setBackgroundResource(R.drawable.buttonscontinuestyledisabled);
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
        job_desc.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(job_pos.getText().toString())
                        &&
                        !job_desc.getText().toString().equals("")) {
                    CompanyPostJobActivity.btn_next.setFocusable(true);
                    CompanyPostJobActivity.btn_next.setEnabled(true);
                    CompanyPostJobActivity.btn_next.setBackgroundResource(R.drawable.buttonscontinuestyle);
                } else {
                    CompanyPostJobActivity.btn_next.setFocusable(false);
                    CompanyPostJobActivity.btn_next.setEnabled(false);
                    CompanyPostJobActivity.btn_next.setBackgroundResource(R.drawable.buttonscontinuestyledisabled);
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });


    }

}
