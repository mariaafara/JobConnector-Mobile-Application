package com.example.abo8a.jobconnector.Company;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.abo8a.jobconnector.R;
import com.example.abo8a.jobconnector.chat.util.ImageUtils;
import com.example.abo8a.jobconnector.chat.util.data.SharedPreferenceHelper;
import com.example.abo8a.jobconnector.chat.util.data.StaticConfig;
import com.example.abo8a.jobconnector.chat.util.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class CompanyMainProfileFragment extends Fragment {

    public static TextView name;
    public static TextView type;
    public static TextView desc;
    public static TextView companyurl;
    public static TextView phone;
    public static TextView editInfo;
    public static TextView editDesc;
    int cid;
    ImageView avatar;
    private User myAccount;
    private DatabaseReference userDB;
    private LovelyProgressDialog waitingDialog;


    private static final int PICK_IMAGE = 1994;


    public static EditText editname;
    public static EditText edittype;
    public static EditText editdesc;
    public static EditText editcompanyurl;
    public static EditText editphone;

    Dialog Dialog;
    Button submitInfo;
    Dialog Dialog2;
    Button submitdesc;
String curl="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        userDB = FirebaseDatabase.getInstance().getReference().child("user").child(StaticConfig.UID);

        System.out.println("++++++  UID in companymainprofile IS  " +StaticConfig.UID);

        userDB.addListenerForSingleValueEvent(userListener);

        SharedPreferenceHelper prefHelper = SharedPreferenceHelper.getInstance(getActivity());
        myAccount = prefHelper.getUserInfo();
        if(myAccount!=null) {
           // setImageAvatar(getActivity(), myAccount.avata);
            System.out.println("+++++++++++++ the account is not null  "+myAccount.email+"  "+myAccount.name+"  ");
        }
        else
            System.out.println("+++++++++++++ the account is null");


        waitingDialog = new LovelyProgressDialog(getActivity());


        return inflater.inflate(R.layout.company_main_profile_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        cid = CompanyMainActivity.cid;

        init();
        fillProfile();
        initTollbar();
        initDialogs();


        editInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditInfoDialog();

            }
        });
        editDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDescDialog();

            }
        });

        companyurl.setMovementMethod(LinkMovementMethod.getInstance());
        companyurl.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse(curl));
                startActivity(browserIntent);
            }
        });
    }

    public void initDialogs() {

        Dialog = new Dialog(getContext(), R.style.Theme_Dialog);
        Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Dialog.setContentView(R.layout.dialog_editinfo);
        editname = Dialog.findViewById(R.id.edit_company_profile_name);
        edittype = Dialog.findViewById(R.id.edit_company_profile_tpe);
        editphone = Dialog.findViewById(R.id.edit_company_profile_phone);
        editcompanyurl = Dialog.findViewById(R.id.edit_company_profile_url);
        //*****************************************///
        Dialog2 = new Dialog(getContext(), R.style.Theme_Dialog);
        Dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Dialog2.setContentView(R.layout.dialog_editdesc);
        editdesc = Dialog2.findViewById(R.id.edit_company_profile_description);
    }

    public void showEditDescDialog() {
        submitdesc = (Button) Dialog2.findViewById(R.id.btn_submit_changecompanydesc);
        submitdesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                updateDesc();
                fillProfile();
                Dialog2.dismiss();

            }
        });

        Dialog2.show();
    }

    private void updateDesc() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://sloppier-citizens.000webhostapp.com/company/updateProfile.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    System.out.println("++++++++" + response);
                    JSONObject json = new JSONObject(response);
                    if (json.getBoolean("success")) {
                        Toast.makeText(getContext(), "updated Succesfully ", Toast.LENGTH_SHORT).show();
                    } else {
                        System.out.println("success not 1 ---------------------");

                        Toast.makeText(getContext(), "Failed to Update!!", Toast.LENGTH_SHORT).show();


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("Json exception--------------------");
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("jobSearch", "Login Error: " + error.getMessage());
                Toast.makeText(getContext(), "Check your Internet Connection and try Again", Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();


                params.put("cid", cid + "");// etsearch.getText().toString()
                params.put("desc", editdesc.getText().toString());
                params.put("edit", "desc");

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(stringRequest);


    }

    public void showEditInfoDialog() {
        submitInfo = (Button) Dialog.findViewById(R.id.btn_submit_changecompanyInfo);
        submitInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                updateInfo();
                fillProfile();
                Dialog.dismiss();

            }
        });

        Dialog.show();
    }

    private void updateInfo() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://sloppier-citizens.000webhostapp.com/company/updateProfile.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    System.out.println("++++++++" + response);
                    JSONObject json = new JSONObject(response);
                    if (json.getBoolean("success")) {
                        Toast.makeText(getContext(), "updated Succesfully ", Toast.LENGTH_SHORT).show();
                    } else {
                        System.out.println("success not 1 ---------------------");

                        Toast.makeText(getContext(), "Failed to Update!!", Toast.LENGTH_SHORT).show();


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("Json exception--------------------");
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("jobSearch", "Login Error: " + error.getMessage());
                Toast.makeText(getContext(), "Check your Internet Connection and try Again", Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();


                params.put("cid", cid + "");// etsearch.getText().toString()
                params.put("name", editname.getText().toString());
                params.put("phone", editphone.getText().toString());
                params.put("url", editcompanyurl.getText().toString());
                params.put("type", edittype.getText().toString());
                params.put("edit", "info");
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(stringRequest);


    }

    public void init() {
        name = getActivity().findViewById(R.id.company_profile_name);
        companyurl = getActivity().findViewById(R.id.company_profile_url);
        type = getActivity().findViewById(R.id.company_profile_tpe);
        desc = getActivity().findViewById(R.id.company_profile_description);
        phone = getActivity().findViewById(R.id.company_profile_phone);
        editDesc = getActivity().findViewById(R.id.edit_companypdescription);
        editInfo = getActivity().findViewById(R.id.edit_commpanyprofile_info);
        avatar=getActivity().findViewById(R.id.Company_img_avatar);
        setImageAvatar(getActivity(),CompanyMainActivity.image);
        avatar.setOnClickListener(onAvatarClick);

    }

    public void fillProfile() {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        final String url = "https://sloppier-citizens.000webhostapp.com/company/getProfile.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {
                          System.out.println("+++++++" + response);

                    JSONObject json = new JSONObject(response);

                    System.out.println("++++++++" + response);

                    if (json.getBoolean("success")) {

                        curl=json.getString("url");
                        name.setText(json.getString("name"));
                        companyurl.setText(curl);
                      //  companyurl.setText(json.getString("url"));
                        type.setText(json.getString("type"));
                        phone.setText(json.getString("phone"));
                        desc.setText(json.getString("desc"));
                        //*****************************//
                        editname.setText(json.getString("name"));
                        editcompanyurl.setText(json.getString("url"));
                        edittype.setText(json.getString("type"));
                        editphone.setText(json.getString("phone"));
                        //***********************//
                        editdesc.setText(json.getString("desc"));
                    } else {
                        System.out.println("success not 1 ---------------------");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("Json22--------------------");
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("CompanyProfile", " Error: " + error.getMessage());
                Toast.makeText(getActivity().getApplicationContext(), "Check your Internet Connection and try Again", Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();


                params.put("cid", cid + "");


                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(stringRequest);


    }

    private void initTollbar() {

        final Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbarCompany);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        final CollapsingToolbarLayout coll = getActivity().findViewById(R.id.company_collapsing);
        AppBarLayout appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.app_bar_layout_company);
        toolbar.setTitle("");
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                    toolbar.setBackgroundColor(getResources().getColor(R.color.transparent));
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true;
                    coll.setTitle("My Profile");
                    toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                } else if (isShow) {
                    coll.setTitle("");
                    toolbar.setBackgroundColor(getResources().getColor(R.color.transparent));

                    isShow = false;
                }
            }
        });

    }
    private View.OnClickListener onAvatarClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            new AlertDialog.Builder(getActivity())
                    .setMessage("Are you sure want to change your profile?")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_PICK);
                            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();
        }
    };


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                Toast.makeText(getActivity(), "No Image Selected", Toast.LENGTH_LONG).show();
                return;
            }
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(data.getData());

                Bitmap imgBitmap = BitmapFactory.decodeStream(inputStream);
                imgBitmap = ImageUtils.cropToSquare(imgBitmap);
                InputStream is = ImageUtils.convertBitmapToInputStream(imgBitmap);
                final Bitmap liteImage = ImageUtils.makeImageLite(is,
                        imgBitmap.getWidth(), imgBitmap.getHeight(),
                        ImageUtils.AVATAR_WIDTH, ImageUtils.AVATAR_HEIGHT
                );
                avatar.setImageDrawable(ImageUtils.roundedImage(getActivity(), liteImage));

                String imageBase64 = ImageUtils.encodeBase64(liteImage);
                myAccount.avata = imageBase64;

                waitingDialog.setCancelable(false)
                        .setTitle("Avatar updating....")
                        .setTopColorRes(R.color.colorPrimary)
                        .show();

                userDB.child("avata").setValue(imageBase64)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    waitingDialog.dismiss();
                                    SharedPreferenceHelper preferenceHelper = SharedPreferenceHelper.getInstance(getActivity());
                                    preferenceHelper.saveUserInfo(myAccount);
                                    avatar.setImageDrawable(ImageUtils.roundedImage(getActivity(), liteImage));

                                    new LovelyInfoDialog(getActivity())
                                            .setTopColorRes(R.color.colorPrimary)
                                            .setTitle("Success")
                                            .setMessage("Update avatar successfully!")
                                            .show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                waitingDialog.dismiss();
                                Log.d("Update Avatar", "failed");
                                new LovelyInfoDialog(getActivity())
                                        .setTopColorRes(R.color.colorAccent)
                                        .setTitle("False")
                                        .setMessage("False to update avatar")
                                        .show();
                            }
                        });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }



    private ValueEventListener userListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            myAccount = dataSnapshot.getValue(User.class);


            if (myAccount != null) {
                setImageAvatar(getActivity(), myAccount.avata);
                SharedPreferenceHelper preferenceHelper = SharedPreferenceHelper.getInstance(getActivity());
                preferenceHelper.saveUserInfo(myAccount);
            }
            else
                setImageAvatar(getActivity(), "default");

        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
          //  Log.e(CompanyMainProfileFragment.class.getName(), "loadPost:onCancelled", databaseError.toException());
        }
    };




    private void setImageAvatar(Context context, String imgBase64){
        try {
            Resources res = getResources();
            Bitmap src;
            if (imgBase64.equals("default")) {
                src = BitmapFactory.decodeResource(res, R.drawable.default_avata);
            } else {
                byte[] decodedString = Base64.decode(imgBase64, Base64.DEFAULT);
                src = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            }

            avatar.setImageDrawable(ImageUtils.roundedImage(context, src));
        }catch (Exception e){
        }
    }

}
