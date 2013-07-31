package com.swavdev.tc;

import java.util.ArrayList;
import java.util.concurrent.Executors;

import android.os.Handler;
import android.util.Log;
import android.widget.ListView;
import com.swavdev.tc.model.Entry;
import android.os.Bundle;
import android.view.Menu;

/**
 * Shows last tweets from users specified in resources
 */
public class MainActivity extends TwitterActivity {

    ListView mListView;
    SimpleTwitterAdapter mAdapter;
    private static final String TAG = MainActivity.class.getSimpleName();
    final ArrayList<Entry> mList = new ArrayList<Entry>();

    TwitterEntryManager mTwitterEntryManager;

    /**
     * {@inheritDoc}
     * instantiates SimpleTwitterAdapter and assigns it to the ListView
     * instantiates TwitterEntryManager
     * starts fetching tweets
     */
    @Override
    protected void onBound() {
        Log.i(TAG,"onBound");

        mService.acquireAccess(null,new Handler());
        mAdapter = new SimpleTwitterAdapter(this, mList);
        final SimpleTwitterAdapter adapter = new SimpleTwitterAdapter(this, new ArrayList<Entry>());
        mListView.setAdapter(adapter);
        mTwitterEntryManager = new TwitterEntryManager(
                mService.getTwitter(),
                Executors.newFixedThreadPool(5)
        ) {

            @Override
            public void notifyAdapter(ArrayList<Entry> entries) {
                if (entries == null) {
                    Log.e(TAG, "Entry is null");
                } else {
                    adapter.addAll(entries);
                    adapter.notifyDataSetChanged();
                }

            }
        };

        bootstrap();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mListView = (ListView) findViewById(R.id.mListView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity, menu);
        return true;
    }

    /**
     * invokes fetching tweets from users specified in R.array.feeds list
     */
    private void bootstrap() {
        String[] list = getResources().getStringArray(R.array.feeds);
        for (int i = 0; i < list.length; i++) {
            mTwitterEntryManager.fetchEntry(list[i]);
        }
    }

}
