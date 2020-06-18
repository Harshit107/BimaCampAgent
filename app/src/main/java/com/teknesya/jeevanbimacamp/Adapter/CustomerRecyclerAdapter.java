package com.teknesya.jeevanbimacamp.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;
import com.teknesya.jeevanbimacamp.R;
import com.teknesya.jeevanbimacamp.model.CustomerlListings;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerRecyclerAdapter extends RecyclerView.Adapter<CustomerRecyclerAdapter.ViewHolder> {


    private List<CustomerlListings> listItems;
    private Context context;
    OnLoadMoreListener onLoadMoreListener;

    public CustomerRecyclerAdapter(List<CustomerlListings> listItems, Context context, OnLoadMoreListener onLoadMoreListener) {
        this.listItems = listItems;
        this.context = context;
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener{
        void onLoadMore();
    }
    public List<CustomerlListings> getListItems() {
        return listItems;
    }

    public void setListItems(List<CustomerlListings> listItems) {
        this.listItems = listItems;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_listing, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        CustomerlListings listItem = listItems.get(position);
        if (position==getItemCount()-1){
            onLoadMoreListener.onLoadMore();

        }

        try {
            Picasso.get().load(listItem.getImage())
                    .placeholder(R.drawable.avtar).fit().centerCrop().into(holder.Image);
        } catch (Exception e) {
            e.printStackTrace();
        }

         final String id;
        id=listItems.get(position).getNodeId();
        holder.Company.setText(listItem.getName());
        holder.Address.setText(listItem.getEmail());
        holder.Category.setText(listItem.getPlan());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Tag",id);
                Toast.makeText(context, "Please Be Patient, will be added Soon...", Toast.LENGTH_SHORT).show();
            }
        });
        //Onclick action to the RecyclearView

        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
    }
    @Override
    public int getItemCount() {
        return listItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView Image;
        TextView Address, Company, Category;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            Image = (CircleImageView) itemView.findViewById(R.id.image);

            Address = (TextView) itemView.findViewById(R.id.plan);
            Company = (TextView) itemView.findViewById(R.id.name);
            Category = (TextView) itemView.findViewById(R.id.email);
        }
    }
}