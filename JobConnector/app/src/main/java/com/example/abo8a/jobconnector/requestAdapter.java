package com.example.abo8a.jobconnector;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abo8a.jobconnector.Company.CompanyProfileActivity;
import com.example.abo8a.jobconnector.chat.util.ImageUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class requestAdapter  extends RecyclerView.Adapter<requestAdapter.ViewHolder> {

        int eid;
        int pid;
        String email;

        HashMap<String,String> hashMap=new HashMap<>();

public class ViewHolder extends RecyclerView.ViewHolder {
   ImageView avatar;
   TextView cname;
   TextView date;
   TextView status;
    public ViewHolder(View v) {
        super(v);
       cname=itemView.findViewById(R.id.request_item_name);
       date=itemView.findViewById(R.id.request_item_date);
        avatar=itemView.findViewById(R.id.request_item_image);
        status=itemView.findViewById(R.id.request_item_status);
    }
}


    private List Data;
    private Context mContext; //to be able to have access to certain methods,

    public requestAdapter(Context mContext, List Data) {
        this.Data = Data;
        this.mContext = mContext;
    }


    @Override
    public requestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_item_layout,
                parent, false);

        return new requestAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final requestAdapter.ViewHolder holder, final int position) {
//bothot

        final requestItem classdata = (requestItem) Data.get(position);//list hhayy

        // cid = CompanyMainActivity.cid;




        email=classdata.getEmail();
        holder.cname.setText(classdata.getCname());
        holder.date.setText(classdata.getDate().split(" ")[0]);
        holder.status.setText(classdata.getStatus());
        if(classdata.getStatus().equals("Rejected"))
            holder.status.setTextColor(Color.RED);

        if(hashMap.get(email)==null)
            setImage(email,holder.avatar);
        else
            setImageAvatar(mContext,hashMap.get(email),holder.avatar);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mIntent = new Intent(mContext, CompanyProfileActivity.class);
                mIntent.putExtra("cid", classdata.getCid());
                mIntent.putExtra("email",classdata.getEmail());
                mIntent.putExtra("comeFromRequest",true);
                mIntent.putExtra("status",classdata.getStatus());
                mContext.startActivity(mIntent);

            }
        });

    }


    @Override
    public int getItemCount() {
        return Data.size();
    }

    public void setImage(final String email, final ImageView avatar){

        if(email!=null) {
            FirebaseDatabase.getInstance().getReference().child("user").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getValue() != null) {
                        String id = ((HashMap) dataSnapshot.getValue()).keySet().iterator().next().toString();
                        HashMap userMap = (HashMap) ((HashMap) dataSnapshot.getValue()).get(id);
                        setImageAvatar(mContext, (String) userMap.get("avata"), avatar);

                        hashMap.put(email,(String) userMap.get("avata"));
                    } else {
                        setImageAvatar(mContext,null, avatar);
                        System.out.println("++++++++++++++ in EmployeeAdapter the email in fire base is notnull ; " + email);
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


