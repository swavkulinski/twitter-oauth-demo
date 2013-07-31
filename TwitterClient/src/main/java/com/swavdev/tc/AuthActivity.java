package com.swavdev.tc;

import android.os.Handler;
import android.os.Message;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


/**
 * Initialises authentication process using TwitterService
 */
public class AuthActivity extends TwitterActivity {

    private static final String TAG = "AuthActivity";


    /**
     * @{inheritDoc}
     */
    @Override
    protected void onBound() {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                if (message.what != TwitterService.ACQUIRE_ACCESS_FAILURE) {
                    startMainActivity();
                }
            }
        };
        mService.acquireAccess(mResponseUri, handler);

    }

    /**
     * Starts main activity
     */
    private void startMainActivity() {
        Log.i(TAG, "startFirstActivity()");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle bundle) {
        Log.i(TAG, "onCreate()");
        System.setProperty("http.keepAlive", "false");

        super.onCreate(bundle);
        if (getIntent().getData() != null) {
            mResponseUri = getIntent().getData();

        } else {
            setContentView(R.layout.auth_view);
            Button submitButton = (Button) findViewById(R.id.submit);
            submitButton.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (mBound) {
                        mService.requestAccess();
                    }
                }
            });

        }

    }


}
