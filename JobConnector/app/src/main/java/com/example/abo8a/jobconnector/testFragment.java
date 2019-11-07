package com.example.abo8a.jobconnector;

import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public class testFragment extends Fragment {
    int mDay = 15;
    int mMonth = 7; // August, month starts from 0
    int mYear = 2018;
    EditText startdate;
    private static final String TAG = "testFragment";

    /**
     * This handles the message send from DatePickerDialogFragment on setting date
     */
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message m) {
            /** Creating a bundle object to pass currently set date to the fragment */
            Bundle b = m.getData();

            /** Getting the day of month from bundle */
            mDay = b.getInt("set_day");

            /** Getting the month of year from bundle */
            mMonth = b.getInt("set_month");

            /** Getting the year from bundle */
            mYear = b.getInt("set_year");

            /** Displaying a short time message containing date set by Date picker dialog fragment */

            Toast.makeText(getContext(), b.getString("set_date"), Toast.LENGTH_LONG).show();
            startdate.setText( b.getString("set_date"));
        }
    };

    public testFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private DatePickerDialog.OnDateSetListener startDateSetListener;

    @Override
    public void onStart() {
        super.onStart();
//
//    final EditText startdate =  getActivity().findViewById(R.id.education_start_date);
//
//        startdate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Calendar cal = Calendar.getInstance();
//                int year = cal.get(Calendar.YEAR);
//                int month = cal.get(Calendar.MONTH);
//                int day = cal.get(Calendar.DAY_OF_MONTH);
//
//                DatePickerDialog dialog = new DatePickerDialog(
//                        RegisterFragment3.this),
//                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
//                        startDateSetListener,
//                        year,month,day);
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                dialog.show();
//            }
//        });
//
//       startDateSetListener = new DatePickerDialog.OnDateSetListener() {
//
//
//           @Override
//            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
//                month = month + 1;
//                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);
//
//                String date = month + "/" + day + "/" + year;
//                startdate.setText(date);
//            }
//        };
/** Click Event Handler for button */
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /** Creating a bundle object to pass currently set date to the fragment */
                Bundle b = new Bundle();

                /** Adding currently set day to bundle object */
                b.putInt("set_day", mDay);

                /** Adding currently set month to bundle object */
                b.putInt("set_month", mMonth);

                /** Adding currently set year to bundle object */
                b.putInt("set_year", mYear);

                /** Instantiating DatePickerDialogFragment */
                DatePickerDialogFragment datePicker = new DatePickerDialogFragment(mHandler);

                /** Setting the bundle object on datepicker fragment */
                datePicker.setArguments(b);

                /** Getting fragment manger for this activity */
                FragmentManager fm = getActivity().getFragmentManager();

                /** Starting a fragment transaction */
                FragmentTransaction ft = fm.beginTransaction();

                /** Adding the fragment object to the fragment transaction */
                ft.add(datePicker, "date_picker");

                /** Opening the DatePicker fragment */
                ft.commit();
            }
        };

        /** Getting an instance of Set Date button */
        //      EditText
        startdate = getActivity().findViewById(R.id.education_start_date);

        /** Setting click event listener for the button */
        startdate.setOnClickListener(listener);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register_fragment3, container, false);


        return view;
    }


}
