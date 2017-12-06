package com.example.jeet.urbanpiper.utils;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import com.example.jeet.urbanpiper.models.NewsItem;
import io.realm.Realm;
import io.realm.RealmResults;

/*
Custom Realm Controller class
 */
 
public class RealmController {
 
    private static RealmController instance;
    private final Realm realm;
 
    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }
 
    public static RealmController with(Fragment fragment) {
 
        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }
 
    public static RealmController with(Activity activity) {
 
        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }
 
    public static RealmController with(Application application) {
 
        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }
 
    public static RealmController getInstance() {
 
        return instance;
    }
 
    public Realm getRealm() {
 
        return realm;
    }
 
    //Refresh the realm istance
    public void refresh() {
 
        realm.refresh();
    }
 
    //clear all objects from Book.class
    public void clearAll() {
 
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
    }
 
    //find all objects in the Book.class
    public RealmResults<NewsItem> getNews() {
 
        return realm.where(NewsItem.class).findAllAsync();
    }
 
    //query a single item with the given id
    public NewsItem getBook(String id) {
 
        return realm.where(NewsItem.class).equalTo("id", id).findFirst();
    }
 
    //check if Book.class is empty
    public boolean hasBooks() {
 
        return !realm.where(NewsItem.class).findAll().isEmpty();
    }

}