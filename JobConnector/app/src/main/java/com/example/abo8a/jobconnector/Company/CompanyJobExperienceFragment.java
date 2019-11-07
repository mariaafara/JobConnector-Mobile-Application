package com.example.abo8a.jobconnector.Company;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;

import com.example.abo8a.jobconnector.R;
import com.example.abo8a.jobconnector.radiobuttons.GridListAdapter;

import java.util.ArrayList;


public class CompanyJobExperienceFragment extends Fragment {
    private Context context;
    private GridListAdapter adapter;
    private ArrayList<String> arrayList;

    public  static EditText seekBarvalue;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.company_job_experience_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // loadListView(view);

    }
    @Override
    public void onStart() {

        super.onStart();
        init();

    }
    void init() {

        SeekBar seekBar = (SeekBar) getActivity().findViewById(R.id.min_experience_SeekBar);
        seekBarvalue = getActivity().findViewById(R.id.min_experience_seekbarvalue);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                if (progress == 0)
                    seekBarvalue.setText(  "No Experience");
                if (progress >= 1 && progress < 12)
                    seekBarvalue.setText(String.valueOf(progress) + " month");
                else if (progress == 12) {
                    int value = 1;
                    seekBarvalue.setText(value + " year");
                } else if (progress > 12 && progress <= 13) {
                    double value = 1.5;
                    seekBarvalue.setText(value + " years");
                } else if (progress > 13 && progress <= 14) {
                    int value = 2;
                    seekBarvalue.setText(value + " years");
                } else if (progress > 14 && progress <= 15) {
                    double value = 2.5;
                    seekBarvalue.setText(value + " years");
                } else if (progress > 15 && progress <= 16) {
                    int value = 3;
                    seekBarvalue.setText(value + " years");
                } else if (progress > 16 && progress <= 17) {
                    double value = 3.5;
                    seekBarvalue.setText(value + " years");
                } else if (progress > 17 && progress <= 18) {
                    int value = 4;
                    seekBarvalue.setText(value + " years");
                } else if (progress > 18 && progress <= 19) {
                    seekBarvalue.setText(4.5 + " years");
                } else if (progress > 19 && progress <= 20) {

                    seekBarvalue.setText(5 + " years");
                } else if (progress > 20 && progress <= 21) {

                    seekBarvalue.setText(5.5 + " years");
                } else if (progress > 21 && progress <= 22) {

                    seekBarvalue.setText(6 + "+ years");
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
        });
        seekBarvalue.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if ( !seekBarvalue.getText().toString().equals("")) {
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
//    private void loadListView(View view) {
//        ListView listView = (ListView) view.findViewById(R.id.list);
//        arrayList = new ArrayList<>();
//        arrayList.add("Yes");
//        arrayList.add("No");
//
//        adapter = new GridListAdapter(context, arrayList, true);
//        listView.setAdapter(adapter);
//    }

}
