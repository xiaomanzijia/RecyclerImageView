package com.licheng.github.recyclerview.util;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.GsonBuilder;

public abstract class VolleyInterface<T>
{
    public Response.Listener<String> listener;
    public Response.ErrorListener errorListener;
    private Class<T> clazz;                 //获取结果后将json转成bean
    private String tag;                         //获取数据后将结果打印出来

    public abstract void onMySuccess(T result);
    public abstract  void onMyError(VolleyError error);

    /**
     *
     * @param clazz 为String则不进行gson转换，否则将结果转为相应的bean
     * @param tag   为空则不打印log.
     */
    public VolleyInterface (Class<T> clazz, String tag)
    {
        this.clazz = clazz;
        this.tag = tag;
    }

    public Response.Listener loadingListener()
    {
        listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                ProgressDialog.Dismiss();
//                BaseBean baseBean  = new GsonBuilder().create().fromJson(response, BaseBean.class);

                String responseBody = "{\n" +
                        "\"beautylist\":" + response +"}";

                if (!"".equals(tag)) {
                    Log.d(tag, response);               //如果tag为空的话就不打印log
                }

//                if ("成功".equals(baseBean.getStatus())){

                    try{
                        if ("String".equals(clazz.getSimpleName())) {               //如果传入的class是String，就不用再解析了
                            onMySuccess((T) response);
                        } else {
                            T t  = new GsonBuilder().create().fromJson(responseBody, clazz);
                            onMySuccess(t);                  //只对成功请求处理，失败则打印失败原因
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

//                } else {
//                }

            }
        };
        return listener;
    }

    public Response.ErrorListener errorListener()
    {
        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                ProgressDialog.Dismiss();
                onMyError(error);
            }
        };
        return errorListener;
    }
}
