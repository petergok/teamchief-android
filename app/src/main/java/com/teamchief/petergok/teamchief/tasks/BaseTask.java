package com.teamchief.petergok.teamchief.tasks;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

/**
 * Created by Peter on 2015-01-10.
 */
public abstract class BaseTask extends AsyncTask<Void, Void, String> {
    public static final String SUCCESS_RESULT = "Request Successful";
    private long mDelay;
    private boolean mUseRetry;

    public BaseTask(long delay, boolean useRetry) {
        mDelay = delay;
        mUseRetry = useRetry;
    }

    public void retry() {
        Handler handler = new Handler();
        handler.postDelayed(getRetryRunnable(), getNewDelay());
    }

    public long getNewDelay() {
        return mDelay * 2;
    }

    public abstract Runnable getRetryRunnable();

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (mUseRetry && !result.equals(SUCCESS_RESULT)) {
            retry();
        }
        Log.d("RESPONSE", result);
    }
}
