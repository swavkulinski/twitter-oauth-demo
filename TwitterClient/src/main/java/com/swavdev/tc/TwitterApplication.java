package com.swavdev.tc;

import android.app.Application;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * Persists RequestToken and AccessToken during authorisation process
 */
public class TwitterApplication extends Application {
    private RequestToken mRequest;
    private AccessToken mAccess;

    public RequestToken getRequest() {
        return mRequest;
    }

    public AccessToken getAccess() {
        return mAccess;
    }

    public void setRequest(RequestToken request) {
        mRequest = request;
    }

    public void setAccess(AccessToken access) {
        mAccess = access;
    }
}
