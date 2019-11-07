package com.example.abo8a.jobconnector.Company;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.abo8a.jobconnector.R;
import com.example.abo8a.jobconnector.chat.util.ImageUtils;
import com.example.abo8a.jobconnector.chat.util.data.SharedPreferenceHelper;
import com.example.abo8a.jobconnector.chat.util.data.StaticConfig;
import com.example.abo8a.jobconnector.chat.util.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

import java.io.FileNotFoundException;
import java.io.InputStream;


public class CompanyRegisterFragment3 extends Fragment {
    private DatabaseReference userDB;

    ImageView avatar;
    private static final int PICK_IMAGE = 1994;
    private User myAccount;
    private LovelyProgressDialog waitingDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        waitingDialog = new LovelyProgressDialog(getActivity());
        userDB = FirebaseDatabase.getInstance().getReference().child("user").child(StaticConfig.UID);

        System.out.println("++++++  UID IS  " + StaticConfig.UID);

      //  userDB.addListenerForSingleValueEvent(userListener);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.company_register_fragment3, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        avatar=getActivity().findViewById(R.id.previewImage);
        avatar.setOnClickListener(onAvatarClick);
        SharedPreferenceHelper prefHelper = SharedPreferenceHelper.getInstance(getActivity());
        myAccount = prefHelper.getUserInfo();

    }


    private View.OnClickListener onAvatarClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            System.out.println("++++++++++++++ iam in onClickListener");

            new AlertDialog.Builder(getActivity())
                    .setTitle("Avatar")
                    .setMessage("Are you sure want to change avatar profile?")
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("++++++++++++++ iam in onActivityResult");


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

//    private ValueEventListener userListener = new ValueEventListener() {
//        @Override
//        public void onDataChange(DataSnapshot dataSnapshot) {
//            myAccount = dataSnapshot.getValue(User.class);
//            setImageAvatar(getActivity(), myAccount.avata);
//            SharedPreferenceHelper preferenceHelper = SharedPreferenceHelper.getInstance(getActivity());
//            preferenceHelper.saveUserInfo(myAccount);
//        }
//
//        @Override
//        public void onCancelled(DatabaseError databaseError) {
//            Log.e(UserProfileFragment.class.getName(), "loadPost:onCancelled", databaseError.toException());
//        }
//    };
//    private void setImageAvatar(Context context, String imgBase64) {
//        try {
//            Resources res = getResources();
//            Bitmap src;
//            if (imgBase64.equals("default")) {
//                src = BitmapFactory.decodeResource(res, R.drawable.default_avata);
//            } else {
//                byte[] decodedString = Base64.decode(imgBase64, Base64.DEFAULT);
//                src = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//            }
//
//            avatar.setImageDrawable(ImageUtils.roundedImage(context, src));
//        } catch (Exception e) {
//        }
//    }
}
