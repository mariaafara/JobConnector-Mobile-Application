package com.example.abo8a.jobconnector;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.abo8a.jobconnector.chat.util.ImageUtils;
import com.example.abo8a.jobconnector.searchedprofiles.ActivityEmployeeViewprofile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

/**
 * Created by lenovo on 3/19/2018.
 */
//this is the class plays an important role in handling the data in Recyclerview.


public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.ViewHolder> {

    int eid;
    int pid;
    String email;

    HashMap<String,String> hashMap=new HashMap<>();

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView fname;
        TextView lname;
        TextView city;
        TextView category;
        TextView totalexpeerience;
        TextView languages;
        ImageView avatar;
        RatingBar ratingBar;
        public ViewHolder(View v) {
            super(v);
            fname = itemView.findViewById(R.id.fname_employeeitem);
            languages = itemView.findViewById(R.id.languages_employeeitem);
            lname = itemView.findViewById(R.id.lname_employeeitem);
            totalexpeerience = itemView.findViewById(R.id.totalexperience_employeeitem);
            category = itemView.findViewById(R.id.category_employeeitem);
            city = itemView.findViewById(R.id.city_employeeitem);
            avatar=itemView.findViewById(R.id.image_employeeitem);
            ratingBar=itemView.findViewById(R.id.rating_bar_employee);
        }
    }


    private List Data;
    private Context mContext; //to be able to have access to certain methods,

    public EmployeeAdapter(Context mContext, List Data) {
        this.Data = Data;
        this.mContext = mContext;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.employeeitem,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
//bothot

        final EmployeeData classdata = (EmployeeData) Data.get(position);//list hhayy

        // cid = CompanyMainActivity.cid;

        eid = classdata.getEid();


        email=classdata.getEmail();
        holder.fname.setText(classdata.getFname());
        holder.lname.setText(classdata.getLname());
        holder.city.setText(classdata.getCity());
        holder.category.setText(classdata.getCategory());
        holder.totalexpeerience.setText(classdata.getTotalexperience());
        holder.languages.setText(classdata.getLanguages());

        holder.ratingBar.setRating((float) classdata.getRate());

        System.out.println("+++++++++++++ Email is "+email);
        if(hashMap.get(email)==null)
            setImage(email,holder.avatar);
        else
            setImageAvatar(mContext,hashMap.get(email),holder.avatar);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EmployeeData ed=(EmployeeData)Data.get(position);
                Intent mIntent = new Intent(mContext, ActivityEmployeeViewprofile.class);
                mIntent.putExtra("eid", ed.getEid());
                mIntent.putExtra("email",ed.getEmail());
                mIntent.putExtra("request",ed.getRequest());
                mContext.startActivity(mIntent);

            }
        });

    }


    @Override
    public int getItemCount() {
        return Data.size();
    }

    public void setImage(final String email, final ImageView avatar){

        System.out.println("++++++++++++++ in EmployeeAdapter the emailhhhhhhhhhhh in fire base is notnull ; " + email);

        if(email!=null) {
            FirebaseDatabase.getInstance().getReference().child("user").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getValue() != null) {
                        System.out.println("++++++++++++++ in EmployeeAdapter the data snapshot is not null ; " + email);

                        String id = ((HashMap) dataSnapshot.getValue()).keySet().iterator().next().toString();
                        HashMap userMap = (HashMap) ((HashMap) dataSnapshot.getValue()).get(id);
                        setImageAvatar(mContext, (String) userMap.get("avata"), avatar);

                        hashMap.put(email,(String) userMap.get("avata"));
                    } else {
                        setImageAvatar(mContext,null, avatar);
                        System.out.println("++++++++++++++ in EmployeeAdapter the email in fire base is not null ; " + email);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });
        }
        else
            System.out.println("++++++++++++++ the email in fire base is null ; ");


    }
    private void setImageAvatar(Context context, String imgBase64,ImageView avatar){
        try {

            Resources res = context.getResources();
            Bitmap src;
            if (imgBase64.equals("default")||imgBase64.equals(null)) {
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


