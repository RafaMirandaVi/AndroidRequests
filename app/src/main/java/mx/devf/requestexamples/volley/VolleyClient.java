package mx.devf.requestexamples.volley;


import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyClient extends Application {

    public static final String TAG = VolleyClient.class
            .getSimpleName();

    private static VolleyClient mInstance;

    private RequestQueue mRequestQueue;
    private static Context mCtx;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    private VolleyClient(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();

    }

    public static synchronized VolleyClient getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleyClient(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
