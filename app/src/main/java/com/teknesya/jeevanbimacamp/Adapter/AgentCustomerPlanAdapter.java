package com.teknesya.jeevanbimacamp.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teknesya.jeevanbimacamp.PlanRecyclearData;
import com.teknesya.jeevanbimacamp.R;

import java.util.ArrayList;

public class AgentCustomerPlanAdapter extends RecyclerView.Adapter<AgentCustomerPlanAdapter.ViewHolder> {

    //All methods in this adapter are required for a bare minimum recyclerview adapter
    private ArrayList<PlanRecyclearData> itemList;
    // Constructor of the class
    String key;
    Context context;
    public AgentCustomerPlanAdapter(ArrayList<PlanRecyclearData> itemList, Context context) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_plan_recycle, parent, false);
        ViewHolder myViewHolder = new ViewHolder(view);
        return myViewHolder;
    }

    // load data in each row element
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int listPosition) {

        final ProgressDialog pbar=new ProgressDialog(context);
        pbar.setMessage("Please Wait");
        pbar.setCanceledOnTouchOutside(false);

        holder.sumassured.setText(itemList.get(listPosition).getSumassured());
        holder.maturity.setText(itemList.get(listPosition).getMaturity());
        holder.planName.setText(itemList.get(listPosition).getPlan());
        holder.premium.setText(itemList.get(listPosition).getPremium());
        holder.date.setText(itemList.get(listPosition).getStartdate());
        key=itemList.get(listPosition).getKey();






    }

    // Static inner class to initialize the views of rows
    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView planName;
        public TextView maturity;
        public TextView premium;
        public TextView sumassured;
        public TextView date;

        public ViewHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.cd_date);
            planName = (TextView) itemView.findViewById(R.id.cd_plan_name);
            maturity = (TextView) itemView.findViewById(R.id.cd_maturity);
            premium = (TextView) itemView.findViewById(R.id.cd_premium);
            sumassured = (TextView) itemView.findViewById(R.id.cd_sum_assure);


        }


    }
}