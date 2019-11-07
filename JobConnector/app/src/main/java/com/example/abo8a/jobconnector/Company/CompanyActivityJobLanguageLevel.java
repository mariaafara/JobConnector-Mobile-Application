package com.example.abo8a.jobconnector.Company;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.abo8a.jobconnector.EmployeeProfile;
import com.example.abo8a.jobconnector.R;
import com.example.abo8a.jobconnector.user;

public class CompanyActivityJobLanguageLevel extends AppCompatActivity {
    private ListView level_listview;
    private Intent intent;
    private String language;
    private int eid;
    private String level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_activity_job_language_level);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        final Intent i = getIntent();
        eid = i.getIntExtra("id", 9999);
        System.out.println("++++++language++++++++" + eid);

        language = i.getStringExtra("lang");
        String levels[] = {"Beginner", "Intermediate", "Advanced", "Native"};
        level_listview = findViewById(R.id.listView_languagelevel);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, levels);
        level_listview.setAdapter(arrayAdapter);

        level_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                level = parent.getItemAtPosition(position).toString();


                EmployeeProfile.goactivitylanguage++;

                intent = new Intent(CompanyActivityJobLanguageLevel.this, CompanyPostJobActivity.class);
                intent.putExtra("comming", true);
                user.visited = true;
                intent.putExtra("id", eid);
                startActivity(intent);

                overridePendingTransition(R.anim.push_left_out, R.anim.push_left_in);

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
