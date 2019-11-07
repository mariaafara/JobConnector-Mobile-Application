package com.example.abo8a.jobconnector.Company;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.abo8a.jobconnector.R;


public class CompanyPostJobFragment extends Fragment {
    private Button postJobbtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.company_post_job_fragment, container, false);
    }


    @Override
    public void onStart() {

        // init();
        super.onStart();

//

        postJobbtn = getActivity().findViewById(R.id.btn_postJob);
        postJobbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent k = new Intent(getContext(), CompanyPostJobActivity.class);
                startActivity(k);

            }
        });

    }
}
