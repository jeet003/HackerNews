package com.example.jeet.urbanpiper.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.jeet.urbanpiper.adapter.ViewPagerAdapter;
import com.example.jeet.urbanpiper.backgroundServices.GetTopStories;
import com.example.jeet.urbanpiper.fragments.NewsDetailComments;
import com.example.jeet.urbanpiper.fragments.NewsDetailWebView;
import com.example.jeet.urbanpiper.interfaces.FragmentCommunicator;
import com.example.jeet.urbanpiper.interfaces.Volley;
import com.example.jeet.urbanpiper.models.CommentsModel;
import com.example.jeet.urbanpiper.models.NewsItem;
import com.example.jeet.urbanpiper.R;
import com.example.jeet.urbanpiper.utils.SharedPrefManager;

import java.util.ArrayList;

import io.realm.RealmList;

/**
 * Created by jeet on 10/28/17.
 *
 * Activity for displaying the detailed news along with their comments and webview to render the url
 */

public class NewsDetail extends AppCompatActivity implements Volley.GetTopCommentsDetail,FragmentCommunicator.FragmentComments {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TextView title,url,time;
    private static final int SECOND_ACTIVITY_RESULT_CODE=0;
    private SharedPrefManager sharedPreferences;
    private ProgressDialog progressDialog;
    private RealmList<String> stringRealmList;
    private String url_string,descendants;
    private ArrayList<CommentsModel> commentsModelArrayList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail);

        // initialize views
        init();

        if (stringRealmList.size() != 0) {
            progressDialog = ProgressDialog.show(this, getResources().getString(R.string.loader_title), getResources().getString(R.string.loader_body_news_detail));
            for (String id : stringRealmList) {
                GetTopStories getTopStories = new GetTopStories(this, id, "");
                getTopStories.getCommentsDetail();
            }
        }else{
            setUpViewPager(viewPager);
        }

        // set up toolbar
        setUpActionBar();


    }

    private void setUpViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getFragmentManager());
        viewPagerAdapter.addFragment(new NewsDetailComments(),descendants+" Comments");
        viewPagerAdapter.addFragment(new NewsDetailWebView(), " Article");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void init() {

        toolbar = (Toolbar) findViewById(R.id.toolbar_newsdetail);
        viewPager = (ViewPager) findViewById(R.id.view_pager_profile);
        tabLayout = (TabLayout) findViewById(R.id.profile_tab);
        sharedPreferences=new SharedPrefManager(this);
        commentsModelArrayList=new ArrayList<>();
        title=(TextView) findViewById(R.id.title_news_detail);
        url=(TextView) findViewById(R.id.url_news_detail);
        time=(TextView) findViewById(R.id.time_news_detail);
        Intent i=getIntent();
        String name=i.getStringExtra("name");
        NewsItem newsItem=i.getParcelableExtra("model");
        title.setText(newsItem.getTitle());
        url.setText(newsItem.getUrl());
        time.setText(newsItem.getFormatted_time()+" @ "+newsItem.getBy());
        stringRealmList=newsItem.getComments();
        url_string=newsItem.getUrl();
        descendants=newsItem.getDescendents();


    }

    private void setUpActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setBackgroundDrawable(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //Getting List of Comments
    @Override
    public void getTopCommentsDetail(CommentsModel commentsModel, String rtenValue) {

        if (rtenValue.equals("SUCCESS"))
        {
            commentsModelArrayList.add(commentsModel);

            if(commentsModelArrayList.size()==stringRealmList.size())
            {
                if(progressDialog.isShowing())
                    progressDialog.dismiss();
                setUpViewPager(viewPager);
            }
        }else{
            if(progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }

    @Override
    public ArrayList<CommentsModel> returnComments() {
        return commentsModelArrayList;
    }

    @Override
    public String returnUrl() {
        return url_string;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch(id)
        {
            case android.R.id.home:
                onBackPressed();
                break;


        }
        return super.onOptionsItemSelected(item);
    }

}

