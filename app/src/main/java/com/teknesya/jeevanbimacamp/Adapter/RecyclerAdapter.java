package com.teknesya.jeevanbimacamp.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.teknesya.jeevanbimacamp.R;
import com.teknesya.jeevanbimacamp.RecyclerData;
import com.teknesya.jeevanbimacamp.model.RemoveClickListner;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerItemViewHolder> {
    private ArrayList<RecyclerData> myList;
    int mLastPosition = 0;
    private RemoveClickListner mListner;
    int textSize=20;

    public RecyclerAdapter(ArrayList<RecyclerData> myList,RemoveClickListner listner) {
        this.myList = myList;
        mListner=listner;
    }
    public RecyclerAdapter(ArrayList<RecyclerData> myList, View.OnClickListener onClickListener){
        this.myList = myList;
    }

    public RecyclerItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        RecyclerItemViewHolder holder = new RecyclerItemViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(RecyclerItemViewHolder holder, final int position) {
        Log.d("onBindViewHoler ", String.valueOf(textSize));
        holder.etTitleTextView.setText(myList.get(position).getTitle());
        holder.etTitleTextView.setTextSize(textSize);
        mLastPosition =position;

    }
    @Override
    public int getItemCount() {
        return(null != myList?myList.size():0);
    }
    public void notifyData(ArrayList<RecyclerData> myList) {
        Log.d("notifyData ", myList.size() + "");
        this.myList = myList;
        notifyDataSetChanged();
    }

    public void setText(int i) {
        textSize=i;
        notifyDataSetChanged();
    }

    public class RecyclerItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView etTitleTextView;
        private LinearLayout mainLayout;
        public RecyclerItemViewHolder(final View parent) {
            super(parent);
            etTitleTextView = (TextView) parent.findViewById(R.id.txtTitle);
//
            mainLayout = (LinearLayout) parent.findViewById(R.id.mainLayout);

//
        }
    }
}