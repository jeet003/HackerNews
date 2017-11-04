package com.example.jeet.urbanpiper.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by jeet on 10/28/2017.
 */

public class NewsItem  extends RealmObject implements Parcelable {
    @PrimaryKey
    private String id;

    private String descendents,by,score,time,title,type,url,formatted_time;
    private RealmList<String> comments;

    public NewsItem(){

    }

    protected NewsItem(Parcel in)
    {
        by=in.readString();
        descendents=in.readString();
        id=in.readString();
        score=in.readString();
        time=in.readString();
        title=in.readString();
        type=in.readString();
        url=in.readString();
        formatted_time=in.readString();
        this.comments=new RealmList<>();
        this.comments.addAll(in.createStringArrayList());
    }

    public void setBy(String by)
    {
        this.by=by;
    }
    public String getBy()
    {
        return by;
    }

    public String getDescendents() {
        return descendents;
    }

    public void setDescendents(String descendents) {
        this.descendents = descendents;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFormatted_time() {
        return formatted_time;
    }

    public void setFormatted_time(String formatted_time) {
        this.formatted_time = formatted_time;
    }

    public RealmList<String> getComments() {
        return comments;
    }

    public void setComments(RealmList<String> comments) {

        this.comments = new RealmList<>();
        this.comments.addAll(comments);
    }

    public static final Creator<NewsItem> CREATOR = new Creator<NewsItem>() {
        @Override
        public NewsItem createFromParcel(Parcel in) {
            return new NewsItem(in);
        }

        @Override
        public NewsItem[] newArray(int size) {
            return new NewsItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(by);
        dest.writeString(descendents);
        dest.writeString(id);
        dest.writeString(score);
        dest.writeString(time);
        dest.writeString(title);
        dest.writeString(type);
        dest.writeString(url);
        dest.writeString(formatted_time);
        dest.writeStringList(this.comments);
    }
}
