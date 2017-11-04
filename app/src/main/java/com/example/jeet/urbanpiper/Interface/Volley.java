package com.example.jeet.urbanpiper.Interface;

import com.example.jeet.urbanpiper.Models.CommentsModel;
import com.example.jeet.urbanpiper.Models.NewsItem;

import java.lang.ref.SoftReference;
import java.util.ArrayList;

/**
 * Created by jeet on 10/26/2017.
 */

public interface Volley {
    interface GetTopStoriesInterface{
        void getTopStories(ArrayList<String> topStories, String rtrnValue);
    }
    interface GetTopStoriesDetail{
        void getTopStoriesDetail(NewsItem newsItem,String rtrnValue);
    }
    interface GetTopCommentsDetail{
        void getTopCommentsDetail(CommentsModel commentsModel,String rtenValue);
    }
}
