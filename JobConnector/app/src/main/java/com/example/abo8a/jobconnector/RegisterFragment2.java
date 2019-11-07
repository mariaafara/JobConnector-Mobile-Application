package com.example.abo8a.jobconnector;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;


public class RegisterFragment2 extends Fragment {

    LinearLayout l;

    private final String urlInsert = "https://sloppier-citizens.000webhostapp.com/job/insertExperience.php";
    static int state = 1;

    public static EditText companyName;
    public static EditText jobIn;
    public static SeekBar job_experience_duration;
    public static EditText job_country;
    public static EditText job_city;
    public static EditText description;
    public static boolean check_experience;
    public static EditText seekBarValue;

    static int c = -1;
    ListView listView_experience;
    String yes_no[] = {"Yes", "No"};
    //   CustomAdapter customAdapter;
    ArrayList<String> list = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    public RegisterFragment2() {

        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_fragment2, container, false);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        companyName = getActivity().findViewById(R.id.company_name);
        jobIn = getActivity().findViewById(R.id.job_in);
        job_experience_duration = getActivity().findViewById(R.id.experience_duration);
        job_country = getActivity().findViewById(R.id.job_Country);
        job_city = getActivity().findViewById(R.id.job_City);
        description = getActivity().findViewById(R.id.job_description);

        forSeekBar();


        listView_experience = getActivity().findViewById(R.id.listView_workExperience);

        list.add("Yes");
        list.add("No");

        adapter = new ArrayAdapter<String>(getActivity(), R.layout.experiencelistview_customlayout, list);
        listView_experience.setAdapter(adapter);
        l = getActivity().findViewById(R.id.linearLayout_workExperience);


        //   customAdapter = new CustomAdapter();
        // listView_experience.setAdapter(customAdapter);


        listView_experience.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                view.setSelected(true);
                if (position == 0) {
                    check_experience = true;
                    l.setVisibility(View.VISIBLE);
                } else {
                    check_experience = false;
                    l.setVisibility(View.GONE);
                }
                if (check_experience) {
                    listView_experience.getChildAt(0).setBackgroundColor(Color.GRAY);
                    listView_experience.getChildAt(1).setBackgroundColor(Color.blue(1));

                } else {
                    listView_experience.getChildAt(1).setBackgroundColor(Color.GRAY);
                    listView_experience.getChildAt(0).setBackgroundColor(Color.blue(1));
                }
            }
        });

    }

    public void forSeekBar() {
        SeekBar seekBar = (SeekBar) getActivity().findViewById(R.id.experience_duration);
        seekBarValue = getActivity().findViewById(R.id.seekbar_duration);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                if (progress == 0)
                    seekBarValue.setText("No Experience");
                if (progress >= 1 && progress < 12)
                    seekBarValue.setText(String.valueOf(progress) + " month");
                else if (progress == 12) {
                    int value = 1;
                    seekBarValue.setText(value + " year");
                } else if (progress > 12 && progress <= 13) {
                    double value = 1.5;
                    seekBarValue.setText(value + " years");
                } else if (progress > 13 && progress <= 14) {
                    int value = 2;
                    seekBarValue.setText(value + " years");
                } else if (progress > 14 && progress <= 15) {
                    double value = 2.5;
                    seekBarValue.setText(value + " years");
                } else if (progress > 15 && progress <= 16) {
                    int value = 3;
                    seekBarValue.setText(value + " years");
                } else if (progress > 16 && progress <= 17) {
                    double value = 3.5;
                    seekBarValue.setText(value + " years");
                } else if (progress > 17 && progress <= 18) {
                    int value = 4;
                    seekBarValue.setText(value + " years");
                } else if (progress > 18 && progress <= 19) {
                    seekBarValue.setText(4.5 + " years");
                } else if (progress > 19 && progress <= 20) {

                    seekBarValue.setText(5 + " years");
                } else if (progress > 20 && progress <= 21) {

                    seekBarValue.setText(5.5 + " years");
                } else if (progress > 21 && progress <= 22) {

                    seekBarValue.setText(6 + "+ years");
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

    }

//    class CustomAdapter extends BaseAdapter {
//        @Override
//        public int getCount() {
//            return yes_no.length;
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View view, ViewGroup parent) {
//            view = getLayoutInflater().inflate(R.layout.experiencelistview_customlayout, null);
//            TextView yes = view.findViewById(R.id.yes_or_no);
//            TextView selected = view.findViewById(R.id.select);
//            yes.setText(yes_no[position]);
//            return view;
//
//        }
//    }


}
