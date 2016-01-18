package com.licheng.github.recyclerview;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.GsonBuilder;
import com.licheng.github.recyclerview.adapter.HomeAdapter;
import com.licheng.github.recyclerview.divider.DividerGridItemDecoration;
import com.licheng.github.recyclerview.model.Beauty;
import com.licheng.github.recyclerview.model.BeautyList;
import com.licheng.github.recyclerview.model.FrescoRecycler;
import com.licheng.github.recyclerview.util.ConfigConstants;
import com.licheng.github.recyclerview.util.VolleyInterface;
import com.licheng.github.recyclerview.util.VolleyRequest;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<FrescoRecycler> mDatas;
    private HomeAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化一定要再setContentView前
        Fresco.initialize(this, ConfigConstants.getImagePipelineConfig(this));
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });




        recyclerView = (RecyclerView) findViewById(R.id.recyleview);

        getImagesOkhttp();

        recyclerView.setAdapter(adapter = new HomeAdapter(mDatas,MainActivity.this));
        //设置布局管理器
//        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this,3));
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        //添加自定义分割线
        recyclerView.addItemDecoration(new DividerGridItemDecoration(MainActivity.this));
        //添加动画 系统默认动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter.setListener(new HomeAdapter.onItenClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, position + " click",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(MainActivity.this, position + " long click",
                        Toast.LENGTH_SHORT).show();
                //长按删除图片
                adapter.remove(position);
            }
        });

    }

    //从网络读取图片 okhttp
    public void getImagesOkhttp(){
        mDatas = new ArrayList<>();

        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
        //创建一个Request
        final Request request = new Request.Builder()
                .url("http://www.diandidaxue.com:8080/apiServer.do?opcode=getBeauty&pageNum=1&numPerPage=100")
                .build();
        //new call
        Call call = mOkHttpClient.newCall(request);
        //请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("beauty","fail");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                final String htmlStr =  response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("beauty",htmlStr);
                        String responseBody = "{" +
                                "\"beautylist\":" + htmlStr +"}";
                        Log.i("beautylist",responseBody);
                        BeautyList list = new GsonBuilder().create().fromJson(responseBody,BeautyList.class);
                        int size = list.getList().size();
                        Log.i("listsize",size + "");
                        for (int i = 0; i < size; i++) {
                            Beauty beauty = list.getList().get(i);
                            Log.i("forlist",beauty.getDescription()+" "+beauty.getUrl());
                            Uri uri = Uri.parse(beauty.getUrl());
                            mDatas.add(new FrescoRecycler(beauty.getDescription(),uri));
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private void getImagesVolley() {
        String url = "http://www.diandidaxue.com:8080/apiServer.do?opcode=getBeauty&pageNum=1&numPerPage=10";
        VolleyRequest.RequestGet(this, url, "", new VolleyInterface<BeautyList>(BeautyList.class, "GetLatLon-->") {
            @Override
            public void onMySuccess(BeautyList result) {
                if(0 != result.getList().size()){
                    for (int i = 0; i < result.getList().size(); i++) {
                        Beauty beauty = result.getList().get(i);
                        Log.i("beautyinfo:",beauty.getDescription()+" "+beauty.getUrl());
                    }
                }
            }

            @Override
            public void onMyError(VolleyError error) {
                Log.i("beautyinfo","error");
                RecyclerViewApplication.getInstance().showTextToast("网络错误");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            adapter.add(2);
            return true;
        }
        if(id == R.id.action_delete){
            adapter.remove(2);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
