package com.example.jeet.urbanpiper.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jeet.urbanpiper.Adapter.CommentsItemAdapter;
import com.example.jeet.urbanpiper.Interface.FragmentCommunicator;
import com.example.jeet.urbanpiper.Models.CommentsModel;
import com.example.jeet.urbanpiper.R;

import java.util.ArrayList;

/**
 * Created by jeet on 10/29/2017.
 */

public class NewsDetailComments extends Fragment{

    private CommentsItemAdapter commentsItemAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    TextView empty;
    FragmentCommunicator.FragmentComments fragmentComments;

    public NewsDetailComments(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_comments, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        empty=(TextView) getActivity().findViewById(R.id.empty_comments);
        fragmentComments=(FragmentCommunicator.FragmentComments)getActivity();
        recyclerView=(RecyclerView) getActivity().findViewById(R.id.comments_list);
        ArrayList<CommentsModel> commentsModelsArrayList=fragmentComments.returnComments();
        ArrayList<CommentsModel> commentsModelsArrayList1=new ArrayList<>();
        for(CommentsModel commentsModel:commentsModelsArrayList)
        {
            if(!commentsModel.getText().equals(""))
                commentsModelsArrayList1.add(commentsModel);
        }
        if(commentsModelsArrayList1.size()!=0) {
            commentsItemAdapter = new CommentsItemAdapter(commentsModelsArrayList1, getActivity());
            linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(commentsItemAdapter);
        }else {
            recyclerView.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        }

    }
}
