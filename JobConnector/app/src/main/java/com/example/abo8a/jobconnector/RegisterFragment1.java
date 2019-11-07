package com.example.abo8a.jobconnector;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import static com.example.abo8a.jobconnector.RegistrationActivity.id;

public class RegisterFragment1 extends Fragment {

    public static EditText fname;
    public static EditText lname;
    public static EditText age;
    public static EditText current_work;
    public static EditText phone_nb;
    public static EditText phone_code;
    public static EditText country;
    public static Spinner city;
    public static EditText email;
    public static Spinner workInSpinner;

    private static final String TAG = "RegisterFragment1";
    public static boolean checkfrag1 = true;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

   public static RadioButton Male,Female;
   private RadioGroup radioGroup;

    public RegisterFragment1() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        sharedPref = getActivity().getSharedPreferences("test", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        editor.putInt("testing", 2);


        final Bundle bundle = getArguments();


        callBasic();

        age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                show();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register_fragment1, container, false);


        return view;
    }

    public void callBasic() {

        if (id == -1) {
            System.out.println("-------id is -1");


        } else {
            System.out.println("-------id is " + id + "++++++++++++");


            fname = getActivity().findViewById(R.id.first_name);
            lname = getActivity().findViewById(R.id.last_name);
            age = getActivity().findViewById(R.id.age);
//            email = getActivity().findViewById(R.id.email);
            current_work = getActivity().findViewById(R.id.current_work);
            country = getActivity().findViewById(R.id.Country);
            city = getActivity().findViewById(R.id.register_city_spinner);
            phone_nb = getActivity().findViewById(R.id.contact_nb);
            phone_code = getActivity().findViewById(R.id.contact_nb_code);

            workInSpinner=getActivity().findViewById(R.id.register_work_spinner);
            workInSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                    if(position==10)
                        current_work.setVisibility(View.VISIBLE);
                    else
                        current_work.setVisibility(View.GONE);


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            Male = (RadioButton)getActivity().findViewById(R.id.radMale);
            Female = (RadioButton)getActivity().findViewById(R.id.radFemale);
            radioGroup = (RadioGroup)getActivity().findViewById(R.id.radGroup);
        }

    }

    public void show() {

        final Dialog npDialog = new Dialog(getActivity(), R.style.Theme_Dialog);
        npDialog .requestWindowFeature(Window.FEATURE_NO_TITLE);
        npDialog.setContentView(R.layout.dialog_number_picker);
        Button setBtn = (Button) npDialog.findViewById(R.id.setbtn);
        Button cnlBtn = (Button) npDialog.findViewById(R.id.cancelbtn);

        final NumberPicker numberPicker = (NumberPicker) npDialog.findViewById(R.id.number_picker);
        numberPicker.setMaxValue(50);
        numberPicker.setMinValue(16);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                // TODO Auto-generated method stub
            }
        });

        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

             //   Toast.makeText(getActivity(), "Number selected: " + numberPicker.getValue(), Toast.LENGTH_SHORT).show();
                age.setText(numberPicker.getValue() + "");
                npDialog.dismiss();
            }
        });

        cnlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                npDialog.dismiss();
            }
        });

        npDialog.show();
    }


}


