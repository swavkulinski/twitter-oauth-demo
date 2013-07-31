package com.swavdev.tc;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import oauth.signpost.OAuth;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by swav on 30/07/13.
 */
public class TwitterService extends Service {

    private static final String TAG = TwitterService.class.getSimpleName();
    private final IBinder mBinder = new LocalBinder();
    private final ExecutorService mExecutorService = Executors.newFixedThreadPool(5);

    public static final int ACQUIRE_ACCESS_FAILURE = 0;
    public static final int ACQUIRE_ACCESS_REMOTE = 1;
    public static final int ACQUIRE_ACCESS_LOCAL = 2;



    Twitter mTwitter;

    /**
     * second step of authorisation. Requires request to be present otherwise it will attempt to read already
     * existing AccessToken from SharedPreferences
     *
     * @param responseUri response Uri from first step of authorisation should start with callback://twitter followed by oauth parameters
     * @throws TwitterException
     */
    public void acquireAccess(final Uri responseUri,final Handler handler) {
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                int success = ACQUIRE_ACCESS_FAILURE;
                try{
                RequestToken request = getRequestToken();
                AccessToken persistedAccessToken = retrieveAccessToken();
                if (persistedAccessToken == null) {
                    if (request != null && responseUri != null) {
                        String verifier = responseUri.getQueryParameter(OAuth.OAUTH_VERIFIER);
                        setAccessToken(mTwitter.getOAuthAccessToken(request, verifier));
                        persistAccessToken(getTwitterApplication().getAccess());
                        success = ACQUIRE_ACCESS_REMOTE;
                    }
                } else {
                    setAccessToken(persistedAccessToken);
                    mTwitter.setOAuthAccessToken(persistedAccessToken);
                    success = ACQUIRE_ACCESS_LOCAL;
                }
                } catch (TwitterException e) {
                    Log.e(TAG,e.getMessage());
                }
                handler.sendEmptyMessage(success);
            }
        });

    }



    /**
     * first step of authorisation which makes call to twitter oauth login page
     */
    public void requestAccess() {

        mExecutorService.submit(new Thread() {
            @Override
            public void run() {
                try {
                    setRequestToken(mTwitter.getOAuthRequestToken(Constants.CALLBACK_URL));
                    RequestToken request = getRequestToken();
                    Log.i(TAG, "token:" + request.getToken());
                    Log.i(TAG, "secret" + request.getTokenSecret());
                    Intent intent = new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(request.getAuthorizationURL())
                    );
                    intent.setFlags(
                            intent.getFlags()
                            |Intent.FLAG_ACTIVITY_NEW_TASK
                            |Intent.FLAG_ACTIVITY_NO_HISTORY
                            |Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
                            |Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);


                    startActivity(intent);
                } catch (TwitterException e) {
                    Log.e(TAG, e.getMessage());
                }
            }

        });

    }

    /**
     * {@inheritDoc}
     * sets twitter intance and sets consumer key and secret
     */
    @Override
    public void onCreate() {
        mTwitter = new TwitterFactory().getInstance();
        mTwitter.setOAuthConsumer(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
    }

    public class LocalBinder extends Binder {
        TwitterService getService() {
            return TwitterService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy()");
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    /**
     * convenience getter for AccessToken stored in Application
     *
     * @return current AccessToken
     */
    private AccessToken getAccessToken() {
        return getTwitterApplication().getAccess();
    }

    /**
     * convenience getter for RequestToken stored in Application
     *
     * @return current RequestToken
     */
    private RequestToken getRequestToken() {
        return getTwitterApplication().getRequest();
    }

    /**
     * sets AccessToken within Application
     *
     * @param token
     */
    private void setAccessToken(AccessToken token) {
        getTwitterApplication().setAccess(token);
    }

    /**
     * sets RequestToken within Application
     *
     * @param request
     */
    private void setRequestToken(RequestToken request) {
        getTwitterApplication().setRequest(request);
    }

    private TwitterApplication getTwitterApplication() {
        return (TwitterApplication) getApplication();
    }

    public Twitter getTwitter() {
        return mTwitter;
    }

    /**
     * persists AccessToken using SharedPreferences
     *
     * @param accessToken
     */
    private void persistAccessToken(AccessToken accessToken) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(Constants.ACCESS_TOKEN, accessToken.getToken());
        editor.putString(Constants.ACCESS_SECRET, accessToken.getTokenSecret());
        editor.putLong(Constants.USER_ID, accessToken.getUserId());
        editor.commit();
    }

    /**
     * clears AccessToken from preferences
     */
    private void clearAccessToken() {
        SharedPreferences.Editor editor = getEditor();
        editor.clear();
        editor.commit();
    }

    /**
     * reads AccessToken from SharedPreferences
     *
     * @return
     */
    private AccessToken retrieveAccessToken() {
        SharedPreferences sharedPreferences = getSharedPreferences(getTwitterApplication().getPackageName(), Context.MODE_PRIVATE);

        String tokenString = sharedPreferences.getString(Constants.ACCESS_TOKEN, null);
        String tokenSecretString = sharedPreferences.getString(Constants.ACCESS_SECRET, null);
        long userId = sharedPreferences.getLong(Constants.USER_ID, -1);

        if (tokenString == null || tokenSecretString == null || userId == -1) {
            return null;
        } else {
            return new AccessToken(tokenString, tokenSecretString);
        }
    }

    /**
     * convenience getter for SharedPreferences.Editor
     *
     * @return
     */
    private SharedPreferences.Editor getEditor() {
        SharedPreferences sharedPreferences = getSharedPreferences(getTwitterApplication().getPackageName(), Context.MODE_PRIVATE);
        return sharedPreferences.edit();
    }

}
