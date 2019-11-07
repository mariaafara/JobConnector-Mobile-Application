package com.example.abo8a.jobconnector.Company;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.abo8a.jobconnector.R;
import com.example.abo8a.jobconnector.radiobuttons.GridListAdapter;

import java.util.ArrayList;

public class CompanyJobCategoryFragment extends Fragment {


    private Context context;
    public static GridListAdapter adapter;
    private ArrayList<String> arrayList;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.company_job_category_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadListView(view);
        // onClickEvent(view);
    }

//    String categories[] = {"Beauty", "Health", "Cook", "Cleaning", "Construction", "Education", "Office", "Sales & Marketing",
//            "Waiter", "Other"};

    private void loadListView(View view) {
        ListView listView = (ListView) view.findViewById(R.id.list);
        arrayList = new ArrayList<>();
        arrayList.add("Beauty           ");
        arrayList.add("Health           ");
        arrayList.add("Finance           ");
        arrayList.add("Cook             ");
        arrayList.add("Cleaning         ");
        arrayList.add("Construction     ");
        arrayList.add("Education        ");
        arrayList.add("Office           ");
        arrayList.add("It               ");
        arrayList.add("Sales & Marketing");
        arrayList.add("Waiter           ");
        arrayList.add("Other");
        adapter = new GridListAdapter(context, arrayList, true);
        listView.setAdapter(adapter);



    }
    @Override
    public void onStart() {
        super.onStart();
    }

                                             /*
    private void onClickEvent(View view) {
        view.findViewById(R.id.show_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get the selected position
                adapter.getSelectedItem();
            }
        });
        view.findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Delete the selected position
                adapter.deleteSelectedPosition();
            }
        });

    }
    */
}