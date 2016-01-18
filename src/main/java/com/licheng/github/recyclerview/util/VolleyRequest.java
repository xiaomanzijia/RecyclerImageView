package com.licheng.github.recyclerview.util;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.licheng.github.recyclerview.RecyclerViewApplication;

import java.util.Map;

public class VolleyRequest
{
    public static StringRequest stringRequest;
    public static Context context;

    public static void RequestGet(Context context,String url, String tag, VolleyInterface vif)
    {

        RecyclerViewApplication.getHttpQueues().cancelAll(tag);
        stringRequest = new StringRequest(Request.Method.GET,url,vif.loadingListener(),vif.errorListener());
        stringRequest.setTag(tag);
        RecyclerViewApplication.getHttpQueues().add(stringRequest);
        // 不写也能执行
//        MyApplication.getHttpQueues().start();
    }

    public static void RequestPost(Context context,String url, String tag,final Map<String, String> params, VolleyInterface vif)
    {
//        ProgressDialog.createDialog(context);
//        ProgressDialog.setMessage("正在与服务器连接....");
        RecyclerViewApplication.getHttpQueues().cancelAll(tag);
        stringRequest = new StringRequest(Request.Method.POST,url,vif.loadingListener(),vif.errorListener())
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }

//            @Override
//            public Map<String, String> getHeaders() {
//                HashMap<String, String> headers = new HashMap<String, String>();
////                headers.put("Accept", "application/json");
//                headers.put("Content-Type", "application/json; charset=UTF-8");
//
//                return headers;
//            }
        };
        stringRequest.setTag(tag);
        RecyclerViewApplication.getHttpQueues().add(stringRequest);
        // 不写也能执行
//        MyApplication.getHttpQueues().start();
    }
}
