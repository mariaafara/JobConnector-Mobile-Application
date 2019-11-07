package com.example.abo8a.jobconnector;

import android.content.Context;
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
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.abo8a.jobconnector.chat.util.ImageUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        RatingBar ratingBar;
        TextView comment;
        TextView date;
        TextView CompanyName;

        public ViewHolder(View v) {
            super(v);
            avatar = itemView.findViewById(R.id.image_Company_comment);
            ratingBar = itemView.findViewById(R.id.rating_bar_comment);
            comment = itemView.findViewById(R.id.comment_text);
            date = itemView.findViewById(R.id.dateComment);
            CompanyName=itemView.findViewById(R.id.Comment_Company_Name);
        }
    }

    private List Data;
    private Context mContext; //to be able to have access to certain methods,

    public CommentAdapter(Context mContext, ArrayList<Comment> Data) {
        this.Data = Data;

        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Comment classdata = (Comment) Data.get(position);

        holder.comment.setText(classdata.getComment());
        holder.date.setText(classdata.getDate());
        holder.ratingBar.setNumStars(5);
        holder.ratingBar.setBackgroundColor(Color.blue(1));
        holder.ratingBar.setRating(classdata.getRating());
        setImage(classdata.getCompanyEmail(),holder.avatar);
        holder.CompanyName.setText(classdata.getCompanyName());

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

                    } else {
                        setImageAvatar(mContext,null, avatar);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });
        }



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
