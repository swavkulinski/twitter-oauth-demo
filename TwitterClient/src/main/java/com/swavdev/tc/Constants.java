package com.swavdev.tc;

public class Constants {

    /**
     * application consumer key as set on Twitter dev account
     */
    public static final String CONSUMER_KEY = BuildConfig.CONSUMER_KEY;

    /**
     * application consumer secret as set on Twitter dev account
     */
    public static final String CONSUMER_SECRET = BuildConfig.CONSUMER_SECRET;


    /**
     * parameter name for OAuth authorisation AccessToken
     */
    public static final String ACCESS_TOKEN = "accessToken";

    /**
     * parameter name for 0Auth authorisation AccessToken secret
     */
    public static final String ACCESS_SECRET = "accessSecret";

    /**
     * SharedPreference key for userId
     */
    public static final String USER_ID = "userId";


    /**
     * url which will be invoked after web authorisation is successful, callback protocol has to be registered in AuthActivity intent filter
     */
    public static final String CALLBACK_URL = "callback://twitter";
}
