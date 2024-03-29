package com.example.abo8a.jobconnector;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.DatePicker;

@SuppressLint("ValidFragment")
public class DatePickerDialogFragment extends DialogFragment {
    Handler mHandler ;
    int mDay;
    int mMonth;
    int mYear;

    public DatePickerDialogFragment(Handler h){
        /** Getting the reference to the message handler instantiated in MainActivity class */
        mHandler = h;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        /** Creating a bundle object to pass currently set date to the fragment */
        Bundle b = getArguments();

        /** Getting the day of month from bundle */
        mDay = b.getInt("set_day");

        /** Getting the month of year from bundle */
        mMonth = b.getInt("set_month");

        /** Getting the year from bundle */
        mYear = b.getInt("set_year");

        /** DatePickerDialog's "Set" click listener */
        DatePickerDialog.OnDateSetListener listener  = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mDay = dayOfMonth;
                mMonth = monthOfYear;
                mYear = year;

                /** Creating a bundle object to pass currently set date to the fragment */
                Bundle b = new Bundle();

                /** Adding currently set day to bundle object */
                b.putInt("set_day", mDay);

                /** Adding currently set month to bundle object */
                b.putInt("set_month", mMonth);

                /** Adding currently set year to bundle object */
                b.putInt("set_year", mYear);

                /** Adding Current date in a string to bundle object */
                b.putString("set_date",  Integer.toString(mYear) + "-" + Integer.toString(mMonth+1) + "-" + Integer.toString(mDay));

                /** Creating an instance of Message */
                Message m = new Message();

                /** Setting bundle object on the message object m */
                m.setData(b);

                /** Message m is sending using the message handler instantiated in MainActivity class */
                mHandler.sendMessage(m);
            }
        };

        /** Opening the DatePickerDialog window */
        return new DatePickerDialog(getActivity(), listener, mYear, mMonth, mDay);
    }

}