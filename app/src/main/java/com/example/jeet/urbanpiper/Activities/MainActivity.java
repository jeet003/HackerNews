package com.example.jeet.urbanpiper.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jeet.urbanpiper.Adapter.NewsItemAdapter;
import com.example.jeet.urbanpiper.BackgroundServices.GetTopStories;
import com.example.jeet.urbanpiper.Interface.Volley;
import com.example.jeet.urbanpiper.Models.NewsItem;
import com.example.jeet.urbanpiper.R;
import com.example.jeet.urbanpiper.Utils.RealmController;
import com.example.jeet.urbanpiper.Utils.RecyclerItemClickListener;
import com.example.jeet.urbanpiper.Utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements Volley.GetTopStoriesInterface,Volley.GetTopStoriesDetail{

    private ProgressDialog progressDialog;
    private ArrayList<String> topStories;
    private ArrayList<String> newStories;
    private ArrayList<NewsItem> newsItemArrayList;
    private NewsItemAdapter newsItemAdapter;
    String name;
    Toolbar toolbar;
    TextView toolbar_title;
    int flag=1;
    public SharedPrefManager sharedPrefManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView recyclerView;
    Realm realm;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPrefManager=new SharedPrefManager(this);
        realm = RealmController.with(this).getRealm();
        init();
        setUpActionBar();

        if(sharedPrefManager.hasUserData().equals(false)) {
            flag=1;
            progressDialog=ProgressDialog.show(this,"Please Wait","Loading News for the first time");
            GetTopStories getTopStories = new GetTopStories(this);
            getTopStories.getStoriesId();
        }else{
            newsItemAdapter=new NewsItemAdapter(RealmController.with(this).getNews(),name,this);
            linearLayoutManager=new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(newsItemAdapter);
        }

    }
    public void setUpActionBar()
    {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //getSupportActionBar().setBackgroundDrawable(null);
        showUpdatedDiff();
    }
    public void showUpdatedDiff(){
        Date date1=new Date();
        long dateinsta=sharedPrefManager.getDate();
        if(dateinsta==0)
            dateinsta=date1.getTime();
        long diff = date1.getTime() - dateinsta;
        long diffMinutes = diff / (60 * 1000) % 60;
        String mint="";
        if(diffMinutes==0)
            mint="Updated 0 minutes ";
        else
            mint="Updated "+String.valueOf(diffMinutes)+" minutes ";

        String latest= mint+"ago";
        toolbar_title.setText("Top Stories \n"+latest);
    }

    void init(){
        newsItemArrayList=new ArrayList<>();
        recyclerView=(RecyclerView) findViewById(R.id.recycler_view);
        mSwipeRefreshLayout=(SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        toolbar=(Toolbar) findViewById(R.id.toolbar_newslist);
        toolbar_title=(TextView) toolbar.findViewById(R.id.toolbar_title);
        /*
        mSwipeRefreshLayout.setColorScheme(getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                        getResources().getColor(android.R.color.holo_orange_light),
                                getResources().getColor(android.R.color.holo_red_light));
                                */
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refresh();
            }
        });
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        NewsItem newsItem=RealmController.with(MainActivity.this).getNews().get(position);
                        Intent i=new Intent(getApplicationContext(), NewsDetail.class);
                        i.putExtra("model",newsItem);
                        i.putExtra("name",name);
                        startActivity(i);
                    }
                })
        );
        Intent i=getIntent();
        name=i.getStringExtra("NAME");

    }

    public void refresh(){
        flag=0;
        Date dateinstance=new Date();
        sharedPrefManager.saveDate(this,dateinstance.getTime());
        realm = RealmController.with(this).getRealm();
        GetTopStories getTopStories = new GetTopStories(this);
        getTopStories.getStoriesId();

    }

    @Override
    public void getTopStories(ArrayList<String> topStories, String rtrnValue) {


        if(rtrnValue.equals("SUCCESS"))
        {
            RealmResults<NewsItem> results=RealmController.with(this).getNews();
            if(results==null)
            {
                this.topStories=topStories;
                for(String id:this.topStories)
                {
                    Log.d("inside phase 1","");
                    GetTopStories getTopStories=new GetTopStories(this,id);
                    getTopStories.getStoriesDetail();
                }
            } else{
                newsItemArrayList=new ArrayList<>(results);
                this.topStories=new ArrayList<>();
                for(NewsItem newsItem:results)
                {
                    this.topStories.add(newsItem.getId());
                }
                this.newStories=new ArrayList<>();
                for (String s : topStories) {
                    if (!this.topStories.contains(s)) {
                        this.topStories.add(s);
                        this.newStories.add(s);
                    }
                }
                Log.d("new stories",String.valueOf(this.newStories.size()));
                if(this.newStories.size()==0)
                {
                    mSwipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(this, "Story List Up to Date", Toast.LENGTH_SHORT).show();
                    showUpdatedDiff();
                }
                else {
                    for (String id : this.newStories) {
                        Log.d("inside phase 2","");
                        GetTopStories getTopStories = new GetTopStories(this, id);
                        getTopStories.getStoriesDetail();
                    }
                }
            }

        }
        else{
            if(progressDialog.isShowing())
                progressDialog.dismiss();
        }

    }

    @Override
    public void getTopStoriesDetail(NewsItem newsItem, String rtrnValue) {

        if(rtrnValue.equals("SUCCESS"))
        {
            newsItemArrayList.add(newsItem);
            realm.beginTransaction();
            realm.copyToRealm(newsItem);
            realm.commitTransaction();
            sharedPrefManager.setHasUserData(this,true);
/*
            Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    */
                    if(newsItemArrayList.size()==topStories.size())
            {
                if(flag==1) {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    newsItemAdapter = new NewsItemAdapter(RealmController.with(MainActivity.this).getNews(), name,this);
                    linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(newsItemAdapter);
                    flag=0;
//                    realm.cancelTransaction();
                }
                else{
                    mSwipeRefreshLayout.setRefreshing(false);
                    newsItemAdapter.notifyDataSetChanged();
                    //Toast.makeText(MainActivity.this, "List Updated "+String.valueOf(topStories.size())+" items added", Toast.LENGTH_SHORT).show();
 //                   realm.cancelTransaction();
                }
                Date dateinstance=new Date();
                sharedPrefManager.saveDate(this,dateinstance.getTime());
                showUpdatedDiff();
            }
            /*
                }
            },3000);
            */



        }
        else{

        }
    }
}
