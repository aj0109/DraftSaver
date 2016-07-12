package com.surroundsync.ashrayjoshi.draftsaver;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ashray Joshi on 28-Jun-16.
 */
public class CustomAdapter extends ArrayAdapter<Drafts> {

    //MainActivity objMain = new MainActivity();
   public static ArrayList<Drafts> request;

    public CustomAdapter(Context context, ArrayList<Drafts> requests) {
        super(context, R.layout.custom_row, requests);
        this.request = requests;
    }

    static class Holder{

        TextView textViewId,textViewDraft,textViewStatus;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("value", String.valueOf(request.get(position)));
        Holder holder =null;

        if(convertView == null){
            holder = new Holder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.custom_row,parent,false);
            holder.textViewId = (TextView)convertView.findViewById(R.id.activity_tv_single_row_id);
            holder.textViewDraft = (TextView)convertView.findViewById(R.id.activity_tv_single_row_draft);
            holder.textViewStatus = (TextView)convertView.findViewById(R.id.activity_tv_single_row_status);


            convertView.setTag(holder);
        }else{
            holder = (Holder)convertView.getTag();
        }
        Log.d("value", String.valueOf(request.get(position)));
        holder.textViewId.setText(request.get(position).getID());
        holder.textViewDraft.setText(request.get(position).getDrafts());
        holder.textViewStatus.setText(""+request.get(position).isImage());


        return convertView;
    }
}
