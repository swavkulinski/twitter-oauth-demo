package com.swavdev.tc;

import java.util.ArrayList;
import java.util.concurrent.Executors;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
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
    private static final String SEARCH_FIELD_EXTRA = MainActivity.class.getCanonicalName() + "SEARCH_FIELD_EXTRA";
    private String mLastSearch;

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
        mListView.setAdapter(null);
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
                    adapter.clear();
                    adapter.addAll(entries);
                    adapter.notifyDataSetChanged();
                }

            }
        };
        if(!TextUtils.isEmpty(mLastSearch)){
            mTwitterEntryManager.fetchEntry(mLastSearch);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle bundle){
        bundle.putString(SEARCH_FIELD_EXTRA,mLastSearch);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            mLastSearch = savedInstanceState.getString(SEARCH_FIELD_EXTRA);
        }
        setContentView(R.layout.main_activity);
        mListView = (ListView) findViewById(R.id.mListView);
        SearchView searchView = (SearchView) getLayoutInflater().inflate(R.layout.search_view, null);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.startsWith("@")){
                    query = query.substring(1);
                }
                mLastSearch = query;
                mTwitterEntryManager.fetchEntry(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mListView.addHeaderView(searchView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity, menu);
        return true;
    }


}
