package com.swavdev.tc;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.IBinder;

/**
 * Base class for twitter activities which need to use TwitterService
 */
abstract class TwitterActivity extends Activity {

    private static final String TAG = TwitterActivity.class.getSimpleName();
    TwitterService mService;
    boolean mBound = false;
    Uri mResponseUri;

    /**
     * executed after binding to service is up
     */
    abstract protected void onBound();

    @Override
    protected void onStart() {
        super.onStart();
        bindTwitterService();
    }

    private void bindTwitterService() {
        Intent intent = new Intent(this, TwitterService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            TwitterService.LocalBinder binder = (TwitterService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
            onBound();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };


}
