package com.teknesya.jeevanbimacamp.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.teknesya.jeevanbimacamp.R;
import com.teknesya.jeevanbimacamp.UserProfileData;
import com.teknesya.jeevanbimacamp.model.CustomerlListings;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReferralRecyclerAdapter extends RecyclerView.Adapter<ReferralRecyclerAdapter.ViewHolder> {


    private List<UserProfileData> listItems;
    private Context context;


    public ReferralRecyclerAdapter(List<UserProfileData> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    public interface OnLoadMoreListener{
        void onLoadMore();
    }
    public List<UserProfileData> getListItems() {
        return listItems;
    }

    public void setListItems(List<UserProfileData> listItems) {
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.referral_listing, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        UserProfileData listItem = listItems.get(position);

        try {
            Picasso.get().load(listItem.getImage())
                    .placeholder(R.drawable.avtar).fit().centerCrop().into(holder.imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

         final String id;
        holder.name.setText(listItem.getName());
        holder.email.setText(listItem.getEmail());

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
        CircleImageView imageView;
        TextView name, email;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (CircleImageView) itemView.findViewById(R.id.image);
            name = (TextView) itemView.findViewById(R.id.name);
            email = (TextView) itemView.findViewById(R.id.email);
        }
    }
}
