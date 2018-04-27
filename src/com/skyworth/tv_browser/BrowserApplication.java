package com.skyworth.tv_browser;

import org.litepal.LitePalApplication;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import android.app.Application;

public class BrowserApplication extends Application
{
    @Override
    public void onCreate() {
        super.onCreate();
        LitePalApplication.initialize(getApplicationContext());
    }
    
    public static synchronized void post(final Runnable r) {

        Observable
                .create(new Observable.OnSubscribe<Object>() {
                    @Override
                    public void call(Subscriber<? super Object> subscriber) {
                        r.run();
                        subscriber.onCompleted();
                    }
                }).subscribeOn(Schedulers.newThread()).subscribe();
    }
}
