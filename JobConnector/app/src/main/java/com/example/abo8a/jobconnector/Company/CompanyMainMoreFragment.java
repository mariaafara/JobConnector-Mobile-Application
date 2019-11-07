package com.example.abo8a.jobconnector.Company;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.abo8a.jobconnector.CompanyRequests;
import com.example.abo8a.jobconnector.Notification.Constants;
import com.example.abo8a.jobconnector.R;
import com.example.abo8a.jobconnector.chat.util.data.FriendDB;
import com.example.abo8a.jobconnector.chat.util.data.GroupDB;
import com.example.abo8a.jobconnector.chat.util.data.StaticConfig;
import com.example.abo8a.jobconnector.chat.util.service.ServiceUtils;
import com.example.abo8a.jobconnector.chatMainActivity;
import com.example.abo8a.jobconnector.login.LoginActivity;
import com.example.abo8a.jobconnector.login.OnBoardingActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class CompanyMainMoreFragment extends Fragment {

    private ListView moreoptionslistview;
    private int position;
    private Intent intent;
    static int cid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.company_main_more_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        cid = CompanyMainActivity.cid;

        String more[] = {"Chatting","Company Requests","Account Settings", "Sign out"};
        moreoptionslistview = getActivity().findViewById(R.id.morefragment_listview);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, more);
        moreoptionslistview.setAdapter(arrayAdapter);

        moreoptionslistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // intent = new Intent(LanguageLevel.this, EmployeeProfile.class);

                if(position==0) {//my messages
                    intent = new Intent(getActivity(), chatMainActivity.class);
                     intent.putExtra("id",cid);
                    intent.putExtra("come","company");
                    startActivity(intent);

                }
                if(position==1)//requests
                {
                    intent = new Intent(getActivity(), CompanyRequests.class);
                    startActivity(intent);
                }


                if (position == 2) {//account

                    intent = new Intent(getActivity(), CompanyAccountSettings.class);
                    startActivity(intent);
                    //   getActivity().overridePendingTransition(R.anim.push_left_out, R.anim.push_left_in);

                } else if (position == 3) {//log out
                    LoginActivity.shouldFinish=true;
                    LoginActivity.finishNow=false;
                    OnBoardingActivity.finishNow=false;
                    OnBoardingActivity.editor.putBoolean("signIn",false);
                    OnBoardingActivity.editor.commit();
                    updateFirebaseToken(StaticConfig.UID,"");
                    FirebaseAuth.getInstance().signOut();
                    FriendDB.getInstance(getContext()).dropDB();
                    GroupDB.getInstance(getContext()).dropDB();
                    ServiceUtils.stopServiceFriendChat(getContext().getApplicationContext(), true);
                    startActivity(new Intent(getActivity(),OnBoardingActivity.class));

                //    getActivity().finish();

                }
            }
        });
    }
    private void updateFirebaseToken(String uid, String token) {

        FirebaseDatabase.getInstance()
                .getReference()
                .child(Constants.ARG_USERS)
                .child(uid)
                .child(Constants.ARG_FIREBASE_TOKEN)
                .setValue(token);
    }
}
