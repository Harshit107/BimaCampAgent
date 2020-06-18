package com.teknesya.jeevanbimacamp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;
import com.teknesya.jeevanbimacamp.R;

public class SliderAdapterExample extends SliderViewAdapter<SliderAdapterExample.SliderAdapterVH> {

    public SliderAdapterExample(Context context) {
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {


        switch (position) {
            case 0:
                Picasso.get()
                        .load("https://firebasestorage.googleapis.com/v0/b/jeevanbimacamp-lifeinsurance.appspot.com/o/slidingview%2Fimage3.jpg?alt=media&token=7ee35a54-670a-461e-a70f-b745c3c634e2")
                        .into(viewHolder.imageViewBackground);
//                viewHolder.textViewDescription.setText("LIC");

                break;
            case 1:
                Picasso.get()
                        .load("https://firebasestorage.googleapis.com/v0/b/jeevanbimacamp-lifeinsurance.appspot.com/o/slidingview%2Fimage4.jpg?alt=media&token=a5c3d9f2-a368-4cd7-b34e-f6d967488ec4")
                        .into(viewHolder.imageViewBackground);
//                viewHolder.textViewDescription.setText("Second");
                break;
            case 2:
                Picasso.get()
                        .load("https://firebasestorage.googleapis.com/v0/b/jeevanbimacamp-lifeinsurance.appspot.com/o/slidingview%2Fimage5.jpg?alt=media&token=2d9f13f7-5ab7-4ab7-a4d7-fd6557a55ea3")
                        .into(viewHolder.imageViewBackground);
//                viewHolder.textViewDescription.setText("Third");
                break;
            default:
                Picasso.get()
                        .load("https://firebasestorage.googleapis.com/v0/b/jeevanbimacamp-lifeinsurance.appspot.com/o/slidingview%2Fimage1.jpg?alt=media&token=fdcae0e7-e43a-477a-b6b8-07c7e3d2399b")
                        .into(viewHolder.imageViewBackground);
//                viewHolder.textViewDescription.setText("Fourth");
                break;

        }

    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return 4;
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        TextView textViewDescription;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
            this.itemView = itemView;
        }
    }
}
