package com.pqq.banner.core;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pqq.banner.R;

import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.ViewHolder> {
    private List<String> data;
    private Context context;

    public BannerAdapter(Context context, List<String> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public BannerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.layout_banner_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerAdapter.ViewHolder holder, int position) {
        final Resources resources = holder.itemView.getResources();
        final int drawableId = resources.getIdentifier(data.get(position % data.size()), "drawable", holder.itemView.getContext().getPackageName());
        if (drawableId != 0) {
            holder.bannerImg.setImageResource(drawableId);
        }
//
//        Glide.with(context)
//                .load(data.get(position))ø
//                .into(holder.bannerImg);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : Integer.MAX_VALUE;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView bannerImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bannerImg = itemView.findViewById(R.id.item_img);
        }
    }

    public void setData(List<String> data) {
        this.data = data;
        notifyDataSetChanged();
    }
}
