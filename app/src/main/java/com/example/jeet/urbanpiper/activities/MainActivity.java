package com.example.jeet.urbanpiper.activities;

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

import com.example.jeet.urbanpiper.adapter.NewsItemAdapter;
import com.example.jeet.urbanpiper.backgroundServices.GetTopStories;
import com.example.jeet.urbanpiper.interfaces.Volley;
import com.example.jeet.urbanpiper.models.NewsItem;
import com.example.jeet.urbanpiper.R;
import com.example.jeet.urbanpiper.utils.RealmController;
import com.example.jeet.urbanpiper.utils.RecyclerItemClickListener;
import com.example.jeet.urbanpiper.utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

/*
Main Activity where the list of news are displayed
 */

public class MainActivity extends AppCompatActivity implements Volley.GetTopStoriesInterface,Volley.GetTopStoriesDetail{

    private ProgressDialog progressDialog;
    private ArrayList<String> topStories;
    private ArrayList<String> newStories;
    private ArrayList<NewsItem> newsItemArrayList;
    private NewsItemAdapter newsItemAdapter;
    private String name;
    private Toolbar toolbar;
    private TextView toolbar_title;
    private int flag;
    private SharedPrefManager sharedPrefManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView recyclerView;
    private Realm realm;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setUpActionBar();
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

        if(!sharedPrefManager.hasUserData()) {
            newsItemAdapter = new NewsItemAdapter(RealmController.with(MainActivity.this).getNews(), name,this);
            linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(newsItemAdapter);
            flag=0;
            progressDialog=ProgressDialog.show(this,getResources().getString(R.string.loader_title),getResources().getString(R.string.loader_body_main));
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
        long date_instant=sharedPrefManager.getDate();
        if(date_instant==0)
            date_instant=date1.getTime();
        long diff = date1.getTime() - date_instant;
        long diffMinutes = diff / (60 * 1000) % 60;
        String minutes="";
        if(diffMinutes==0)
            minutes="Updated 0 minutes ";
        else
            minutes="Updated "+String.valueOf(diffMinutes)+" minutes ";

        String latest= getResources().getString(R.string.toolbar_title_text)+minutes+"ago";
        toolbar_title.setText(latest);
    }

    void init(){
        sharedPrefManager=new SharedPrefManager(this);
        realm = RealmController.with(this).getRealm();
        newsItemArrayList=new ArrayList<>();
        flag=1;
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
    }

    public void refresh(){
        flag=0;
        Date dateinstance=new Date();
        sharedPrefManager.saveDate(this,dateinstance.getTime());
        realm = RealmController.with(this).getRealm();
        GetTopStories getTopStories = new GetTopStories(this);
        getTopStories.getStoriesId();

    }

    /*
    Getting the top stories list
     */
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
                    Toast.makeText(this, getResources().getString(R.string.uptodate_text), Toast.LENGTH_SHORT).show();
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

    /*
    Getting the story details
     */

    @Override
    public void getTopStoriesDetail(NewsItem newsItem, String rtrnValue) {

        if(rtrnValue.equals("SUCCESS"))
        {
            newsItemArrayList.add(newsItem);
            realm.beginTransaction();
            realm.copyToRealm(newsItem);
            realm.commitTransaction();
            sharedPrefManager.setHasUserData(this,true);
            if(progressDialog!=null)
            if (progressDialog.isShowing())
                progressDialog.dismiss();

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
        }
        else{
            if(progressDialog!=null)
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
        }
    }
}
