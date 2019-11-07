package com.example.abo8a.jobconnector.Company;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.abo8a.jobconnector.R;


import java.util.ArrayList;
import java.util.Arrays;

public class CompanyJobLanguagesFragment extends Fragment {

    ListView listview;
    String[] ListViewItems = new String[]{
            "English",
            "Arabic",
            "Chinese",
            "French",
            "Espaniol",
            "Latin",
            "Japanese",
            "Indian"

    };

    SparseBooleanArray sparseBooleanArray;
    public static String ValueHolder;
    public static ArrayList array=new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.company_job_languages_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        listview = (ListView) getActivity().findViewById(R.id.list);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_list_item_multiple_choice, ListViewItems);

        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub

                sparseBooleanArray = listview.getCheckedItemPositions();

                ValueHolder = "";
                array.clear();
                int i = 0;

                while (i < sparseBooleanArray.size()) {

                    if (sparseBooleanArray.valueAt(i)) {

                        ValueHolder += ListViewItems[sparseBooleanArray.keyAt(i)] + ",";
                        array.add(ListViewItems[sparseBooleanArray.keyAt(i)]+"");
                    }

                    i++;
                }

                ValueHolder = ValueHolder.replaceAll("(,)*$", "");

                //     Toast.makeText(getActivity(), "ListView Selected Values = " + ValueHolder, Toast.LENGTH_LONG).show();

            }
        });

    }


}
