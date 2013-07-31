package com.swavdev.tc.model;

public class Entry {
    String mScreenName;
    String mRealName;
    String mLastTweet;
    String mAvatarUrl;
    String mCachedAvatarUrl;

    public Entry() {
    }

    public Entry(String screenName, String realname, String lastTweet, String avatarUrl) {
        mScreenName = screenName;
        mRealName = realname;
        mLastTweet = lastTweet;
        mAvatarUrl = avatarUrl;

    }

    public Entry(String screenName, String realname, String lastTweet, String avatarUrl, String cachedAvatarUrl) {
        mScreenName = screenName;
        mRealName = realname;
        mLastTweet = lastTweet;
        mAvatarUrl = avatarUrl;
        mCachedAvatarUrl = cachedAvatarUrl;

    }

    public String getCachedAvatarUrl() {
        return mCachedAvatarUrl;
    }

    public void setCachedAvatarUrl(String cachedAvatarUrl) {
        mCachedAvatarUrl = cachedAvatarUrl;
    }

    public String getScreenName() {
        return mScreenName;
    }

    public void setScreenName(String screenName) {
        this.mScreenName = screenName;
    }

    public String getRealName() {
        return mRealName;
    }

    public void setRealName(String realName) {
        this.mRealName = realName;
    }

    public String getLastTweet() {
        return mLastTweet;
    }

    public void setLastTweet(String lastTweet) {
        this.mLastTweet = lastTweet;
    }

    public String getAvatarUrl() {
        return mAvatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.mAvatarUrl = avatarUrl;
    }

}
