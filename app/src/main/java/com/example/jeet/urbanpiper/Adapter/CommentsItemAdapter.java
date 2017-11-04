package com.example.jeet.urbanpiper.Adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jeet.urbanpiper.Models.CommentsModel;
import com.example.jeet.urbanpiper.R;

import java.util.ArrayList;

/**
 * Created by jeet on 10/28/2017.
 */

public class CommentsItemAdapter extends RecyclerView.Adapter<CommentsItemAdapter.MyViewHolder> {

    private ArrayList<CommentsModel> commentsModelArrayList;
    private Context context;

    public CommentsItemAdapter(ArrayList<CommentsModel> commentsModels,Context context)
    {
        this.context=context;
        this.commentsModelArrayList=commentsModels;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView user,time,comments;

        public MyViewHolder(View view)
        {
            super(view);

            user=(TextView)view.findViewById(R.id.user_comments);
            time=(TextView)view.findViewById(R.id.time_comments);
            comments=(TextView)view.findViewById(R.id.text_comments);
        }
    }

    @Override
    public int getItemCount() {
        return (null != commentsModelArrayList ? commentsModelArrayList.size() : 0);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comments_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        //realm = RealmController.getInstance().getRealm();

        //NewsItem newsItem=newsItemArrayList.get(position);
        CommentsModel commentsModel=commentsModelArrayList.get(position);

        holder.user.setText(commentsModel.getBy());
        holder.time.setText(commentsModel.getTime());
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            holder.comments.setText((Html.fromHtml(commentsModel.getText(), Html.FROM_HTML_MODE_COMPACT)));
        else
            holder.comments.setText((Html.fromHtml(commentsModel.getText())));
    }
}
