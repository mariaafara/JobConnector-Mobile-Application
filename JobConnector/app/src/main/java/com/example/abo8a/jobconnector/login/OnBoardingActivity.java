package com.example.abo8a.jobconnector.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.abo8a.jobconnector.R;
import com.example.abo8a.jobconnector.RegistrationActivity;

import java.util.ArrayList;

public class OnBoardingActivity extends AppCompatActivity {


    public static boolean finishNow=false;
    private LinearLayout pager_indicator;
    private int dotsCount;
    private ImageView[] dots;
    private ViewPager onboard_pager;
    private OnBoard_Adapter mAdapter;
    private Button btn_hire;
    private Button btn_findJob;
    private TextView btn_skip;
    int previous_pos = 0;
    int pos;
    boolean isSignedIn=false;
    SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;

    ArrayList<OnBoardItem> onBoardItems = new ArrayList<>();

    Intent intent;
    Intent intent2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_on_boarding);

        LoginActivity.finishNow=false;
        sharedPreferences=getSharedPreferences("lunch", Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        isSignedIn = sharedPreferences.getBoolean("signIn", false);

        if(isSignedIn)
        {
            Intent i=new Intent(this,LoginActivity.class);



            startActivity(i);
            System.out.println("++++++++ iam in onboard...");
            finish();


        }

        btn_skip = (TextView) findViewById(R.id.btn_skip);
        btn_hire = (Button) findViewById(R.id.btn_hire);
        btn_findJob = (Button) findViewById(R.id.btn_findJob);
        onboard_pager = (ViewPager) findViewById(R.id.pager_introduction);
        pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);


        loadData();

        mAdapter = new OnBoard_Adapter(this, onBoardItems);
        onboard_pager.setAdapter(mAdapter);
        onboard_pager.setCurrentItem(0);
        onboard_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }


            @Override
            public void onPageSelected(int position) {

                // Change the current position intimation

                for (int i = 0; i < dotsCount; i++) {

                    dots[i].setImageDrawable(ContextCompat.getDrawable(
                            OnBoardingActivity.this, R.drawable.non_selected_item_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(
                        OnBoardingActivity.this, R.drawable.selected_item_dot));


                pos = position + 1;

                if (pos == dotsCount && previous_pos == (dotsCount - 1)) {
                    btn_skip.setVisibility(View.GONE);
                    show_animation();
                } else if (pos == (dotsCount - 1) && previous_pos == dotsCount) {

                    hide_animation();
                    btn_skip.setVisibility(View.VISIBLE);
                }
                previous_pos = pos;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btn_findJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(OnBoardingActivity.this, LoginActivity.class);
                intent.putExtra("comefrom","findjob");
                startActivity(intent);
                finish();
                //  Toast.makeText(OnBoardingActivity.this, "Redirect to wherever you want", Toast.LENGTH_LONG).show();
            }
        });



        intent2 = new Intent(this, RegistrationActivity.class);
        btn_hire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//LoginActivity
                Intent k  = new Intent(OnBoardingActivity.this,LoginActivity.class);//ActivitySearch
                k.putExtra("comefrom","hire");
                startActivity(k);
                finish();
                //  Toast.makeText(OnBoardingActivity.this, "Redirect to wherever you want", Toast.LENGTH_LONG).show();
            }
        });

        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos = 3;
                previous_pos = 2;
                onboard_pager.setCurrentItem(3);
                //  Toast.makeText(OnBoardingActivity.this, "here is the last page", Toast.LENGTH_LONG).show();
            }
        });


        setUiPageViewController();

    }

    // Load data into the viewpager

    public void loadData() {

        int[] header = {R.string.ob_header1, R.string.ob_header2, R.string.ob_header3};
        int[] desc = {R.string.ob_desc1, R.string.ob_desc2, R.string.ob_desc3};
        int[] imageId = {R.drawable.a, R.drawable.hireing, R.drawable.pichiring};
        //R.drawable.socialnetwork, R.drawable.hireing, R.drawable.pichiring

        for (int i = 0; i < imageId.length; i++) {
            OnBoardItem item = new OnBoardItem();
            item.setImageID(imageId[i]);
            item.setTitle(getResources().getString(header[i]));
            item.setDescription(getResources().getString(desc[i]));

            onBoardItems.add(item);
        }
    }

    // Button bottomUp animation

    public void show_animation() {
        Animation show = AnimationUtils.loadAnimation(this, R.anim.slide_up_anim);

        btn_hire.startAnimation(show);
        btn_findJob.startAnimation(show);
        show.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                btn_hire.setVisibility(View.VISIBLE);
                btn_findJob.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                btn_hire.clearAnimation();
                btn_findJob.clearAnimation();

            }

        });


    }

    // Button Topdown animation

    public void hide_animation() {
        Animation hide = AnimationUtils.loadAnimation(this, R.anim.slide_down_anim);

        btn_hire.startAnimation(hide);
        btn_findJob.startAnimation(hide);
        hide.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                btn_hire.clearAnimation();
                btn_hire.setVisibility(View.GONE);
                btn_findJob.clearAnimation();
                btn_findJob.setVisibility(View.GONE);

            }

        });


    }

    // setup the
    private void setUiPageViewController() {

        dotsCount = mAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(OnBoardingActivity.this, R.drawable.non_selected_item_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(6, 0, 6, 0);

            pager_indicator.addView(dots[i], params);
        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(OnBoardingActivity.this, R.drawable.selected_item_dot));
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoginActivity.shouldFinish=false;
        if(finishNow)
            finish();

    }
}
