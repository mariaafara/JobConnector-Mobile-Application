package com.example.abo8a.jobconnector.searchedprofiles;

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
import com.example.abo8a.jobconnector.EmployeeProfile;
import com.example.abo8a.jobconnector.Language;
import com.example.abo8a.jobconnector.R;
import com.example.abo8a.jobconnector.user;

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
public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder> {


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView language;
        TextView level;
        TextView remove;

        public ViewHolder(View v) {
            super(v);
            language = itemView.findViewById(R.id.custom_language);
            level = itemView.findViewById(R.id.custom_level);
            remove = itemView.findViewById(R.id.Link_Remove_language);
        }
    }


    private ArrayList<Language> Data;
    private Context mContext; //to be able to have access to certain methods,

    public LanguageAdapter(Context mContext, ArrayList<Language> Data) {
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
        holder.remove.setVisibility(View.GONE);
        //  System.out.println("+++++from holder++ pos is+++"+position+"\n ++++++++ languages is "+classdata.getLanguage());


    }


    @Override
    public int getItemCount() {
        //  return Data.size();
        return (Data == null) ? 0 : Data.size();
    }


}


