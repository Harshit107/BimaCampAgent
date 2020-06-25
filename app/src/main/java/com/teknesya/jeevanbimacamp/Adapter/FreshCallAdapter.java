package com.teknesya.jeevanbimacamp.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teknesya.jeevanbimacamp.ItemLead;
import com.teknesya.jeevanbimacamp.R;
import com.teknesya.jeevanbimacamp.ViewLeadDetail;

import java.util.ArrayList;

public class FreshCallAdapter extends RecyclerView.Adapter<FreshCallAdapter.ViewHolder> {

    //All methods in this adapter are required for a bare minimum recyclerview adapter
    private ArrayList<ItemLead> itemList;
    // Constructor of the class
    Context context;
    public FreshCallAdapter(ArrayList<ItemLead> itemList, Context context, Activity activity) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lead_iten, parent, false);
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

        holder.value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+itemList.get(listPosition).getValue()));
                context.startActivity(intent);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // get position
                // Toasty.success(context,itemList.get(listPosition).getNodeId()).show();
                Intent it=new Intent(context, ViewLeadDetail.class);
                it.putExtra("nodeId",itemList.get(listPosition).getNodeId());
                context.startActivity(it);
            }
        });


    }

    // Static inner class to initialize the views of rows
    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView key;
        public ImageView value;
        public  String phone;
        public ViewHolder(View itemView) {
            super(itemView);
            key = (TextView) itemView.findViewById(R.id.key);
            value = (ImageView) itemView.findViewById(R.id.value);
        }


    }
}