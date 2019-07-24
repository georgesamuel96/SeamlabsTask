package com.example.georgesamuel.seamlabstask.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.georgesamuel.seamlabstask.Activities.ArticleActivity;
import com.example.georgesamuel.seamlabstask.R;
import com.example.georgesamuel.seamlabstask.model.Article;

import java.util.ArrayList;
import java.util.List;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ArticlesViewHolder> {

    private List<Article> list = new ArrayList<>();
    private Context context;
    private ArticlesViewHolder articlesViewHolder;

    public ArticlesAdapter(Context context, List<Article> list){
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ArticlesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        ArticlesViewHolder viewHolder = new ArticlesViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ArticlesViewHolder holder, final int position) {

        this.articlesViewHolder = holder;

        Glide.with(context).load(list.get(position).getUrlToImage()).into(holder.imageView);
        holder.title.setText(list.get(position).getTitle());
        holder.owner.setText(list.get(position).getAuthor());
        holder.date.setText(list.get(position).getPublishedAt());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ArticleActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("title", list.get(position).getTitle());
                intent.putExtra("description", list.get(position).getDescription());
                intent.putExtra("imagePath", list.get(position).getUrlToImage());
                intent.putExtra("owner", list.get(position).getAuthor());
                intent.putExtra("date", list.get(position).getPublishedAt());
                intent.putExtra("url", list.get(position).getUrl());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ArticlesViewHolder extends RecyclerView.ViewHolder{

        private CardView cardView;
        private ImageView imageView;
        private TextView title, date, owner;
        public LinearLayout viewBackground, viewForeground;
        private ImageView imageShare;

        public ArticlesViewHolder(View view){
            super(view);

            cardView = view.findViewById(R.id.cardView);
            imageView = view.findViewById(R.id.image);
            title = view.findViewById(R.id.title);
            date = view.findViewById(R.id.date);
            owner = view.findViewById(R.id.owner);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
            imageShare = view.findViewById(R.id.imageShare);

        }
    }
}
