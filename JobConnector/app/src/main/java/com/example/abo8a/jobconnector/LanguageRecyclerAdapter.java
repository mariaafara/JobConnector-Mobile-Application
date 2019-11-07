package com.example.abo8a.jobconnector;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.abo8a.jobconnector.EmployeeProfile.recyclerView_languages;

/**
 * Created by lenovo on 3/19/2018.
 */
//this is the class plays an important role in handling the data in Recyclerview.
public class LanguageRecyclerAdapter extends RecyclerView.Adapter<LanguageRecyclerAdapter.ViewHolder> {



    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView language;
        TextView level;
        TextView remove;

        public ViewHolder(View v) {
            super(v);
           language = itemView.findViewById(R.id.custom_language);
            level = itemView.findViewById(R.id.custom_level);
          remove=itemView.findViewById(R.id.Link_Remove_language);
        }
    }


    private ArrayList<Language> Data;
    private Context mContext; //to be able to have access to certain methods,

    public LanguageRecyclerAdapter(Context mContext,  ArrayList<Language> Data) {
        this.Data = Data;
        this.mContext = mContext;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.languagelistview_customlayout,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Language classdata = (Language) Data.get(position);
        //     final ClassListItems classListItems = values.get(position);
        //    holder.textTitle.setText(classListItems.getTitle() + "");

        holder.language.setText(classdata.getLanguage());
        holder.level.setText(classdata.getLevel());

      //  System.out.println("+++++from holder++ pos is+++"+position+"\n ++++++++ languages is "+classdata.getLanguage());

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Delete Language")
                        .setMessage("Are you sure you want to delete "+classdata.getLanguage())
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                removeLanguage(classdata.getLanguage());
                                Data.remove(position);
                                EmployeeProfile.languageAppAdapter= new LanguageRecyclerAdapter(mContext, Data);
                                recyclerView_languages.setAdapter(EmployeeProfile.languageAppAdapter);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();




            }
        });


//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent mIntent = new Intent(mContext, job_detail.class);
//                mIntent.putExtra("sender", holder.mSender.getText().toString());
//                mIntent.putExtra("title", holder.mEmailTitle.getText().toString());
//                mIntent.putExtra("details", holder.mEmailDetails.getText().toString());
//                mIntent.putExtra("time", holder.mEmailTime.getText().toString());
//                mIntent.putExtra("icon", holder.mIcon.getText().toString());
//                //  mIntent.putExtra("colorIcon", color);
//                mContext.startActivity(mIntent);
////
//            }
//        });

    }


    @Override
    public int getItemCount() {
      //  return Data.size();
        return (Data == null) ? 0 : Data.size();
    }

    public  void removeLanguage(final String Language)
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://sloppier-citizens.000webhostapp.com/job/removeLanguage.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    //

                    System.out.println("++++++++"+response);
                    JSONObject json = new JSONObject(response);



                    if (json.getBoolean("success"))
                    {

                            Toast.makeText(mContext,Language+" Deleted",Toast.LENGTH_SHORT).show();

                    }


                    else {
                        System.out.println("success not 1 ---------------------");

                        Toast.makeText(mContext,"Failed to delete",Toast.LENGTH_SHORT).show();


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("Json exception--------------------");
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LanguageRecycler", error.getMessage());
                Toast.makeText(mContext, "Check your Internet Connection and try Again", Toast.LENGTH_LONG).show();

            }
        })  {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();




                params.put("eid",user.id+"");
                params.put("language",Language);

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(stringRequest);



    }

}


