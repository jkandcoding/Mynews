package com.klemar.android.factorytask.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.klemar.android.factorytask.R;
import com.klemar.android.factorytask.model.NewsModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    Context context;
    private List<NewsModel> newsList;

    public NewsAdapter(Context context, ArrayList<NewsModel> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public NewsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.MyViewHolder holder, int position) {
        NewsModel newsModel = newsList.get(position);

        holder.tv_newsListItem.setText(newsModel.getTitle());

        //loading image using Picasso
        Picasso.get().load(newsModel.getUrlToImage()).into(holder.iv_newsListItem);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo newsListActivity.onNewsItemClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        Log.d("hjhjh", "getItemCount: " + newsList.size());
        return newsList.size();

    }

    public void setNewsList(List<NewsModel> newsList) {
        this.newsList = newsList;
        if (newsList.size() == 0) {

        }
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_newsListItem;
        public TextView tv_newsListItem;
        // public ProgressBar pb_loadNewsItem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_newsListItem = itemView.findViewById(R.id.iv_news_list_item);
            tv_newsListItem = itemView.findViewById(R.id.tv_news_list_item);
            //  pb_loadNewsItem = itemView.findViewById(R.id.pb_news_list_item);
        }
    }
}
