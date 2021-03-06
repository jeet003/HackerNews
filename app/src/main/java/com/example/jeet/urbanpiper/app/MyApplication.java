package com.example.jeet.urbanpiper.app;

/**
 * Created by jeet on 10/29/2017.
 */

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/*
Application class to initialize realm
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {

        super.onCreate();
        Realm.init(getApplicationContext());
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

    }
}