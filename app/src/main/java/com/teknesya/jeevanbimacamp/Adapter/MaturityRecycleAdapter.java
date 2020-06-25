package com.teknesya.jeevanbimacamp.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teknesya.jeevanbimacamp.ItemLead;
import com.teknesya.jeevanbimacamp.R;

import java.util.ArrayList;

public class MaturityRecycleAdapter extends RecyclerView.Adapter<MaturityRecycleAdapter.ViewHolder> {


    private ArrayList<ItemLead> itemList;


    // Constructor of the class
    Context context;
    public MaturityRecycleAdapter(ArrayList<ItemLead> itemList, Context context) {
        this.itemList = itemList;
        this.context=context;
    }

    // get the size of the list
    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }


    // specify the row layout file and click for each row
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        ViewHolder myViewHolder = new ViewHolder(view);
        return myViewHolder;
    }

    // load data in each row element
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int listPosition) {

        final ProgressDialog pbar=new ProgressDialog(context);
        pbar.setMessage("Please Wait");
        pbar.setCanceledOnTouchOutside(false);

        holder.key.setText(itemList.get(listPosition).getKey());
        holder.value.setText(itemList.get(listPosition).getValue());



    }

    // Static inner class to initialize the views of rows
    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView key;
        public TextView value;
        public ViewHolder(View itemView) {
            super(itemView);
            key = (TextView) itemView.findViewById(R.id.key);
            value = (TextView) itemView.findViewById(R.id.value);
        }


    }
}