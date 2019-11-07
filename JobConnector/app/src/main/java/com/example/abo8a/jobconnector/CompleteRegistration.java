package com.example.abo8a.jobconnector;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abo8a.jobconnector.login.LoginActivity;

public class CompleteRegistration extends AppCompatActivity {

    private Button complete;
    Intent intent,i;
    private TextView tvCongrats;
    private Button quit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complete_registration);
        intent = new Intent(this, RegistrationActivity.class);
        complete = findViewById(R.id.btn_completeRegistration);
        tvCongrats=findViewById(R.id.id_congrats);

        i =getIntent();

        tvCongrats.setText("Congratulations! "+i.getStringExtra("user"));
        quit=findViewById(R.id.btn_Quit);
        complete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                intent.putExtra("id", i.getIntExtra("id",0));
                intent.putExtra("user", i.getStringExtra("user"));
                intent.putExtra("email", i.getStringExtra("email"));
                startActivity(intent);

            }
        });

        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LoginActivity.comefrom="findjob";
                finish();
            }
        });
    }



    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override

            public void run() {
                doubleBackToExitPressedOnce = false;
            }

        }, 2000);
    }

}
