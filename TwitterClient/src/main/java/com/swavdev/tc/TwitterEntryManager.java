package com.swavdev.tc;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import android.os.Handler;
import android.os.Message;
import android.sax.EndElementListener;
import android.util.Log;

import com.swavdev.tc.model.Entry;
import twitter4j.*;

abstract class TwitterEntryManager {
    private final ExecutorService mExecutorService;
    private final Twitter mTwitter;
    private static final String TAG = TwitterEntryManager.class.getName();

    /**
     * Constructor
     * @param twitter twitter object
     * @param executorService ExecutorService on which fetchEntry will be running
     */
    public TwitterEntryManager(Twitter twitter,  ExecutorService executorService) {
        mTwitter = twitter;
        mExecutorService = executorService;

    }

    protected List<Entry> doFetch(String screenName) {
        try {
            Paging paging = new Paging();
            paging.setCount(1);
            ResponseList<Status> responseList = mTwitter.getUserTimeline(screenName, paging);
            List<Entry> result = new ArrayList<Entry>();
            for (Status s : responseList) {
                Entry e = new Entry();
                e.setScreenName(s.getUser().getScreenName());
                e.setRealName(s.getUser().getName());
                e.setAvatarUrl(s.getUser().getProfileImageURL().toString());
                e.setLastTweet(s.getText());
                result.add(e);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * fetches twitter entry given screenName. Uses threads provided by ExecutorService
     * @param screenName twitter screenName
     */
    public void fetchEntry(final String screenName) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                Log.i(TAG, "handleMessage:" + message.toString());
                notifyAdapter((ArrayList<Entry>) message.obj);
            }
        };
        mExecutorService.submit(new Thread() {
            @Override
            public void run() {
                List<Entry> entries = doFetch(screenName);
                Message message = handler.obtainMessage(1, entries);
                handler.sendMessage(message);
            }

        });

    }

    /**
     * invoked when fetchEntry has finished download. Override this method to notify adapter about change
     * @param entries downloaded Entries
     */
    protected abstract void notifyAdapter(ArrayList<Entry> entries);
}
