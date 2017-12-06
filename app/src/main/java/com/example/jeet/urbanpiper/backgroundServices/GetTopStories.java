package com.example.jeet.urbanpiper.backgroundServices;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.jeet.urbanpiper.models.CommentsModel;
import com.example.jeet.urbanpiper.models.NewsItem;
import com.example.jeet.urbanpiper.utils.VolleySingleton;
import com.example.jeet.urbanpiper.interfaces.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import io.realm.RealmList;

/**
 * Created by jeet on 8/8/17.
 *
 * The stories and other details are being fetched using this volley class
 */

public class GetTopStories {

    private Context context;
    private String rtrnValue,url,id;
    private String Tag="GetNominee";
    private Volley.GetTopStoriesInterface getTopStoriesInterface;
    private Volley.GetTopStoriesDetail getTopStoriesDetail;
    private Volley.GetTopCommentsDetail getTopCommentsDetail;

    public GetTopStories(Context context){
        this.context=context;
        url="https://hacker-news.firebaseio.com/v0/topstories.json";
        try {
            getTopStoriesInterface=(Volley.GetTopStoriesInterface) context;
        }
        catch (ClassCastException e){
            throw new ClassCastException();
        }
    }

    public GetTopStories(Context context,String id){
        this.context=context;
        this.id=id;
        url="https://hacker-news.firebaseio.com/v0/item/"+id+".json";
        try {
            getTopStoriesDetail=(Volley.GetTopStoriesDetail) context;
        }
        catch (ClassCastException e){
            throw new ClassCastException();
        }

    }

    public GetTopStories(Context context,String id,String dummy){
        this.context=context;
        this.id=id;
        url="https://hacker-news.firebaseio.com/v0/item/"+id+".json";
        try {
            getTopCommentsDetail=(Volley.GetTopCommentsDetail) context;
        }
        catch (ClassCastException e){
            throw new ClassCastException();
        }

    }

    //fetches the list of stories
    public void getStoriesId()
    {
        final Map<String,String> params=new HashMap<String, String>();

        StringRequest storyRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response!=null){
                    try {
                        Log.d("Title",response);

                        JSONArray s=new JSONArray(response);

                        ArrayList<String> stories=new ArrayList<>();
                        for(int i=0;i<s.length();i++)
                        {
                            stories.add(s.getString(i));
                            Log.d("id",s.getString(i));
                        }

                        rtrnValue="SUCCESS";
                        getTopStoriesInterface.getTopStories(stories,rtrnValue);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else{
                    // response is null
                    rtrnValue="ERROR";
                    getTopStoriesInterface.getTopStories(null,rtrnValue);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    Log.e("Volley", "Error. HTTP Status Code:" + networkResponse.statusCode);
                }
                if (error instanceof TimeoutError) {
                    //AlertDialogs.show((Activity) context,"","Please try after some time. Slow net may be the problem");
                    getTopStoriesInterface.getTopStories(null,"HANDLED");
                    Toast.makeText(context, "Please try after some time. Slow net may be the problem", Toast.LENGTH_LONG).show();
                } else if (error instanceof NoConnectionError) {
                    //AlertDialogs.show((Activity) context,"",context.getString(R.string.no_internet_connection_text));
                    getTopStoriesInterface.getTopStories(null,"HANDLED");
                    Toast.makeText(context, "Check your network connection", Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    Log.e("Volley", "AuthFailureError");
                    getTopStoriesInterface.getTopStories(null,"ERROR");
                } else if (error instanceof ServerError) {
                    Log.e("Volley", "ServerError");
                    getTopStoriesInterface.getTopStories(null,"ERROR");
                } else if (error instanceof NetworkError) {
                    Log.e("Volley", "NetworkError");
                    getTopStoriesInterface.getTopStories(null,"ERROR");
                } else if (error instanceof ParseError) {
                    Log.e("Volley", "Parse Error: " + error.getMessage());
                    getTopStoriesInterface.getTopStories(null,"ERROR");
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> header=new HashMap<String, String>();
                header.put("accept","application/json");
                return header;
            }

            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }
        };
        VolleySingleton.getInstance(context).addToRequestQueue(storyRequest);
    }

    //fetches individual stories
    public void getStoriesDetail()
    {
        final Map<String,String> params=new HashMap<String, String>();

        StringRequest storyRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response!=null){
                    try {
                        Log.d("resp",response);

                        JSONObject news=new JSONObject(response);
                        NewsItem newsItem=new NewsItem();

                        String by="";
                        if(news.has("by"))
                        by=news.getString("by");
                        String descendents="";
                        if(news.has("descendants"))
                        descendents=news.getString("descendants");
                        String id=news.getString("id");
                        String score="";
                        if(news.has("score"))
                        score=news.getString("score");
                        String time=news.getString("time");
                        String timestamp=getDateCurrentTimeZone(Long.parseLong(time));
                        String formatted_time=getDateCurrentFormatted(Long.parseLong(time));
                        String title=news.getString("title");
                        String type=news.getString("type");
                        String url="";
                        if(news.has("url"))
                        url=news.getString("url");
                        String kids_string="";
                        JSONArray kids=new JSONArray();
                        RealmList<String> comments=new RealmList<>();

                        if(news.has("kids"))
                        {
                            kids_string=news.getString("kids");
                            kids=new JSONArray(kids_string);
                            for(int i=0;i<kids.length();i++)
                            {
                                comments.add(kids.getString(i));
                            }
                        }


                        newsItem.setBy(by);
                        newsItem.setDescendents(descendents);
                        newsItem.setId(id);
                        newsItem.setScore(score);
                        newsItem.setTime(timestamp);
                        newsItem.setTitle(title);
                        newsItem.setType(type);
                        newsItem.setUrl(url);
                        newsItem.setFormatted_time(formatted_time);
                        newsItem.setComments(comments);

                        rtrnValue="SUCCESS";
                        getTopStoriesDetail.getTopStoriesDetail(newsItem,rtrnValue);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else{
                    // response is null
                    rtrnValue="ERROR";
                    getTopStoriesDetail.getTopStoriesDetail(null,rtrnValue);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    Log.e("Volley", "Error. HTTP Status Code:" + networkResponse.statusCode);
                }
                if (error instanceof TimeoutError) {
                    //AlertDialogs.show((Activity) context,"","Please try after some time. Slow net may be the problem");
                    getTopStoriesDetail.getTopStoriesDetail(null,"HANDLED");
                    Toast.makeText(context, "Please try after some time. Slow net may be the problem", Toast.LENGTH_LONG).show();
                } else if (error instanceof NoConnectionError) {
                    //AlertDialogs.show((Activity) context,"",context.getString(R.string.no_internet_connection_text));
                    getTopStoriesDetail.getTopStoriesDetail(null,"HANDLED");
                    Toast.makeText(context, "Check your network connection", Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    Log.e("Volley", "AuthFailureError");
                    getTopStoriesDetail.getTopStoriesDetail(null,"ERROR");
                } else if (error instanceof ServerError) {
                    Log.e("Volley", "ServerError");
                    getTopStoriesDetail.getTopStoriesDetail(null,"ERROR");
                } else if (error instanceof NetworkError) {
                    Log.e("Volley", "NetworkError");
                    getTopStoriesDetail.getTopStoriesDetail(null,"ERROR");
                } else if (error instanceof ParseError) {
                    Log.e("Volley", "Parse Error: " + error.getMessage());
                    getTopStoriesDetail.getTopStoriesDetail(null,"ERROR");
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> header=new HashMap<String, String>();
                header.put("accept","application/json");
                return header;
            }

            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }
        };
        VolleySingleton.getInstance(context).addToRequestQueue(storyRequest);
    }

    //fetches the comments
    public void getCommentsDetail()
    {
        final Map<String,String> params=new HashMap<String, String>();

        StringRequest storyRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response!=null){
                    try {
                        Log.d("resp",response);

                        JSONObject news=new JSONObject(response);
                        CommentsModel newsItem=new CommentsModel();

                        String by="";
                        if(news.has("by"))
                            by=news.getString("by");

                        String id=news.getString("id");

                        String time="",formatted_time="";
                        if(news.has("time")) {
                            time = news.getString("time");
                            formatted_time = getDateCurrentFormattedDetail(Long.parseLong(time));
                        }
                        String text="";
                        if(news.has("text"))
                        text=news.getString("text");
                        String type="";
                        if(news.has("type"))
                        type=news.getString("type");



                        newsItem.setBy(by);
                        newsItem.setId(id);
                        newsItem.setTime(formatted_time);
                        newsItem.setType(type);
                        newsItem.setText(text);

                        rtrnValue="SUCCESS";
                        getTopCommentsDetail.getTopCommentsDetail(newsItem,rtrnValue);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else{
                    // response is null
                    rtrnValue="ERROR";
                    getTopCommentsDetail.getTopCommentsDetail(null,rtrnValue);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    Log.e("Volley", "Error. HTTP Status Code:" + networkResponse.statusCode);
                }
                if (error instanceof TimeoutError) {
                    //AlertDialogs.show((Activity) context,"","Please try after some time. Slow net may be the problem");
                    getTopCommentsDetail.getTopCommentsDetail(null,"HANDLED");
                    Toast.makeText(context, "Please try after some time. Slow net may be the problem", Toast.LENGTH_LONG).show();
                } else if (error instanceof NoConnectionError) {
                    //AlertDialogs.show((Activity) context,"",context.getString(R.string.no_internet_connection_text));
                    getTopCommentsDetail.getTopCommentsDetail(null,"HANDLED");
                    Toast.makeText(context, "Check your network connection", Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    Log.e("Volley", "AuthFailureError");
                    getTopCommentsDetail.getTopCommentsDetail(null,"ERROR");
                } else if (error instanceof ServerError) {
                    Log.e("Volley", "ServerError");
                    getTopCommentsDetail.getTopCommentsDetail(null,"ERROR");
                } else if (error instanceof NetworkError) {
                    Log.e("Volley", "NetworkError");
                    getTopCommentsDetail.getTopCommentsDetail(null,"ERROR");
                } else if (error instanceof ParseError) {
                    Log.e("Volley", "Parse Error: " + error.getMessage());
                    getTopCommentsDetail.getTopCommentsDetail(null,"ERROR");
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> header=new HashMap<String, String>();
                header.put("accept","application/json");
                return header;
            }

            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }
        };
        VolleySingleton.getInstance(context).addToRequestQueue(storyRequest);
    }



    private String getDateCurrentTimeZone(long timestamp) {
        try {

            Date date = new Date(timestamp*1000L); // *1000 is to convert seconds to milliseconds
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"); // the format of your date
            sdf.setTimeZone(TimeZone.getDefault()); // give a timezone reference for formating (see comment at the bottom
            String formattedDate = sdf.format(date);
            Date date1=new Date();
            long diff = date1.getTime() - date.getTime();
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);
            String days="",hours="",mint="";
            if(diffDays==0)
                days="";
            else
                days=String.valueOf(diffDays)+" days ";
            if(diffHours==0)
                hours="";
            else
                hours=String.valueOf(diffHours)+" hours ";
            if(diffMinutes==0)
                mint="";
            else
                mint=String.valueOf(diffMinutes)+" minutes ";

            return (days+hours+mint+" ago");
        }catch (Exception e) {
        }
        return "";
    }

    private String getDateCurrentFormatted(long timestamp) {
        try {

            Date date = new Date(timestamp*1000L); // *1000 is to convert seconds to milliseconds
            //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"); // the format of your date
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM,yyyy");
            sdf.setTimeZone(TimeZone.getDefault()); // give a timezone reference for formating (see comment at the bottom
            String formattedDate = sdf.format(date);
            return formattedDate;
        }catch (Exception e) {
        }
        return "";
    }

    private String getDateCurrentFormattedDetail(long timestamp) {
        try {

            Date date = new Date(timestamp*1000L); // *1000 is to convert seconds to milliseconds
            //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"); // the format of your date
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM,yyyy HH:mm");
            sdf.setTimeZone(TimeZone.getDefault()); // give a timezone reference for formating (see comment at the bottom
            String formattedDate = sdf.format(date);
            return formattedDate;
        }catch (Exception e) {
        }
        return "";
    }

}
