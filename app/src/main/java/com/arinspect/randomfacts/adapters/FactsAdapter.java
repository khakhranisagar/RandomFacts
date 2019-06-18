package com.arinspect.randomfacts.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.arinspect.randomfacts.R;
import com.arinspect.randomfacts.entities.Fact;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


import java.util.ArrayList;
import java.util.List;


public class FactsAdapter extends RecyclerView.Adapter<FactsAdapter.FactHolder> {
    private List<Fact> facts = new ArrayList<>();
    private Context context;

    public FactsAdapter(List<Fact> facts, Context context) {
        this.facts = facts;
        this.context = context;
    }

    @NonNull
    @Override
    public FactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_fact_list,parent,false);
        return new FactHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FactHolder holder, int position) {
        Fact currentFact = facts.get(position);
        holder.tvTitle.setText(currentFact.getTitle());
        holder.tvDescription.setText(currentFact.getDescription());
        Glide.with(context).load(currentFact.getImageHref()).diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_placeholder)
                .into(holder.ivFactImg);
    }

    @Override
    public int getItemCount() {
        return facts.size();
    }

    class FactHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle, tvDescription;
        public ImageView ivFactImg;
        public FactHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title_fact);
            tvDescription = itemView.findViewById(R.id.tv_desc_fact);
            ivFactImg = itemView.findViewById(R.id.iv_fact_img);

        }
    }
}