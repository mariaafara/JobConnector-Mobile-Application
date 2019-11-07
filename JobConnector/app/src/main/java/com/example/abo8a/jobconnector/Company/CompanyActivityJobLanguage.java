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

import com.example.abo8a.jobconnector.R;

import java.util.ArrayList;

public class CompanyActivityJobLanguage extends AppCompatActivity {
    private ListView languages_listview;
    private Intent intent;
    private int eid;
    int i, j, index = 0;
    ArrayList<String> displayedLanguages;
    ArrayList<String> langs;
    final String levels[] = {"English", "Arabic", "French", "Chinese", "Portuguese", "Japanese", "Bengali", "Russian",
            "Swedish", "Italian", "German", "Spanish"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_activity_job_language);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
init();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
    private void init() {
        if (getIntent().getSerializableExtra("arrayList") != null) {

            langs = (ArrayList<String>) getIntent().getSerializableExtra("arrayList");
            if (langs != null) {
                displayedLanguages = new ArrayList<>();

                for (i = 0; i < levels.length; i++) {

                    String temp = levels[i];
                    for (j = 0; j < langs.size(); j++) {
                        if (temp.compareTo(langs.get(j)) == 0) {
                            break;
                        }

                    }
                    if (j == langs.size()) {
                        displayedLanguages.add(temp);
                        index++;
                    }
                }

            }
        }

        languages_listview = findViewById(R.id.listView_languages);
        ArrayAdapter<String> arrayAdapter;
        if (displayedLanguages != null) {
            arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, displayedLanguages);
        } else {
            arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, levels);

        }
        languages_listview.setAdapter(arrayAdapter);

        languages_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                intent = new Intent(CompanyActivityJobLanguage.this, CompanyActivityJobLanguageLevel.class);
                intent.putExtra("lang", parent.getItemAtPosition(position).toString());
                intent.putExtra("id", eid);
                startActivity(intent);

            }
        });
    }
}
