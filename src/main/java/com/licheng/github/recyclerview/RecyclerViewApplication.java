package com.licheng.github.recyclerview;

import android.app.Application;
import android.view.Gravity;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by licheng on 10/11/15.
 */
public class RecyclerViewApplication extends Application {
    private static RecyclerViewApplication mApplication;
    private static RequestQueue queue;

    public synchronized static RecyclerViewApplication getInstance() {
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        queue = Volley.newRequestQueue(getApplicationContext());
    }

    public static RequestQueue getHttpQueues(){
        return queue;
    }

    public void showTextToast(String msg) {

        Toast toast = null;

        if (toast == null) {
            toast = Toast.makeText(getApplicationContext(), msg,
                    Toast.LENGTH_SHORT);

        } else {

            toast.setText(msg);
        }

        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
