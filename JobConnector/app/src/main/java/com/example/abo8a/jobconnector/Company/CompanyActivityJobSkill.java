package com.example.abo8a.jobconnector.Company;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.abo8a.jobconnector.R;
import com.example.abo8a.jobconnector.user;

public class CompanyActivityJobSkill extends AppCompatActivity {
    private EditText skill;
    private EditText level;
    private int id;
    private Button set;
    private Button cancel;
    private RadioButton begginer;
    private RadioButton intermediate;
    private RadioButton expert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_activity_job_skill);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        init();


}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_done: {

                Intent intent = new Intent(this, CompanyPostJobActivity.class);
                // intent.putExtra("id", id);
                startActivity(intent);
            }
            break;
            case android.R.id.home:
                finish();
            default:
                break;
        }
        return true;
    }

    public void init() {

        id = user.id;
        skill = findViewById(R.id.skill_name);
        level = findViewById(R.id.skill_level);
        level.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                showlevelDialog();
            }
        });

    }

    public void showlevelDialog() {

        final Dialog Dialog = new Dialog(this, R.style.Theme_Dialog);
        //Window window = npDialog.getWindow();
        Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Dialog.setContentView(R.layout.dialog_skill_level);
        Button setBtn = (Button) Dialog.findViewById(R.id.setbtn);
        Button cnlBtn = (Button) Dialog.findViewById(R.id.cancelbtn);
        begginer = (RadioButton) Dialog.findViewById(R.id.radioBeginner);
        intermediate = (RadioButton) Dialog.findViewById(R.id.radioIntermediate);
        expert = (RadioButton) Dialog.findViewById(R.id.radioExpert);

        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                if (begginer.isChecked()) {
                    level.setText("Begginer");
                    Dialog.dismiss();
                } else if (intermediate.isChecked()) {
                    level.setText("Intermediate");
                    Dialog.dismiss();
                } else if (expert.isChecked()) {
                    level.setText("Expert");
                    Dialog.dismiss();
                }
            }
        });

        cnlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Dialog.dismiss();
            }
        });

        Dialog.show();
    }

}
