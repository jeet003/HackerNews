package com.example.jeet.urbanpiper.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jeet.urbanpiper.models.NewsItem;
import com.example.jeet.urbanpiper.R;

import io.realm.*;

/**
 * Created by jeet on 10/28/2017.
 *
 * Adapter class for the news item
 */

public class NewsItemAdapter extends RealmRecyclerViewAdapter<NewsItem,NewsItemAdapter.MyViewHolder> {

    //private ArrayList<NewsItem> newsItemArrayList;
    private Context context;
    private String name;
    private Typeface typeface;

    public NewsItemAdapter(RealmResults<NewsItem> newsItems,String name,Context context)
    {
        super(newsItems,true);
        this.context=context;
        //this.newsItemArrayList=newsItemArrayList;
        this.name=name;
        typeface=Typeface.createFromAsset(context.getAssets(),"fontawesome-webfont.ttf");
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView likes,title,url,time,comments,icon;

        public MyViewHolder(View view)
        {
            super(view);
            likes=(TextView)view.findViewById(R.id.likes_news);
            title=(TextView)view.findViewById(R.id.title_news);
            url=(TextView)view.findViewById(R.id.url_news);
            time=(TextView)view.findViewById(R.id.time_news);
            comments=(TextView)view.findViewById(R.id.comments_news);
            icon=(TextView)view.findViewById(R.id.icon_comment);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        //realm = RealmController.getInstance().getRealm();

        //NewsItem newsItem=newsItemArrayList.get(position);
        NewsItem newsItem=getItem(position);

        holder.likes.setText(newsItem.getScore());
        holder.title.setText(newsItem.getTitle());
        holder.url.setText(newsItem.getUrl());
        holder.time.setText(newsItem.getTime()+" @ "+newsItem.getBy());
        holder.comments.setText(newsItem.getDescendents());
        holder.icon.setTypeface(typeface);
    }
}
