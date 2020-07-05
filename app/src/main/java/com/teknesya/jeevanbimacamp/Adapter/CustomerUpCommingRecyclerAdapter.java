package com.teknesya.jeevanbimacamp.Adapter;

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

import com.squareup.picasso.Picasso;
import com.teknesya.jeevanbimacamp.AgentCustomerDetail;
import com.teknesya.jeevanbimacamp.Fragment.AgentCustomerSearchFragment;
import com.teknesya.jeevanbimacamp.R;
import com.teknesya.jeevanbimacamp.TabFragment.BirthDay;
import com.teknesya.jeevanbimacamp.UpComingEventDetail;
import com.teknesya.jeevanbimacamp.ViewLeadDetail;
import com.teknesya.jeevanbimacamp.model.CustomerlListings;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerUpCommingRecyclerAdapter extends RecyclerView.Adapter<CustomerUpCommingRecyclerAdapter.ViewHolder> {


    private List<CustomerlListings> listItems;
    private Context context;


    public CustomerUpCommingRecyclerAdapter(ArrayList<CustomerlListings> listItems, Context applicationContext, UpComingEventDetail onLoadMoreListener) {
        this.listItems = listItems;
        this.context = applicationContext;
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_upcoming_date, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        CustomerlListings listItem = listItems.get(position);

        try {
            Picasso.get().load(listItem.getImage())
                    .placeholder(R.drawable.avtar).fit().centerCrop().into(holder.Image);
        } catch (Exception e) {
            e.printStackTrace();
        }

         final String id;
        id=listItems.get(position).getNodeId();
        holder.name.setText(listItem.getName());
        holder.plan.setText(listItem.getEmail());
        holder.email.setText(listItem.getPlan());
        holder.date.setText(listItem.getDate());

        holder.phone=listItem.getPhone();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(context,id,Toast.LENGTH_LONG).show();

                if(UpComingEventDetail.typeEventDetail.equals("lead")){
                    Intent it = new Intent(context, ViewLeadDetail.class);
                    it.putExtra("nodeId", id);
                    context.startActivity(it);
                }
                else {

                    Intent it = new Intent(context, AgentCustomerDetail.class);
                    it.putExtra("nodeId", id);
                    context.startActivity(it);
                }
            }
        });
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+holder.phone));
                context.startActivity(intent);

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
        ImageView call;
        TextView plan, name, email,date;
        String phone;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            Image = (CircleImageView) itemView.findViewById(R.id.image);
            call=(ImageView)itemView.findViewById(R.id.call);
            plan = (TextView) itemView.findViewById(R.id.plan);
            date = (TextView) itemView.findViewById(R.id.date);
            name = (TextView) itemView.findViewById(R.id.name);
            email = (TextView) itemView.findViewById(R.id.email);
        }
    }
}
