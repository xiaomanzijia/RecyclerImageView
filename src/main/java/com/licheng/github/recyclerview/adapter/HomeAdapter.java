package com.licheng.github.recyclerview.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.licheng.github.recyclerview.R;
import com.licheng.github.recyclerview.model.FrescoRecycler;

import java.util.List;

/**
 * Created by licheng on 18/1/16.
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHodler> {
    private Context mContext;
    private List<FrescoRecycler> mDatas;

    public HomeAdapter(List<FrescoRecycler> mDatas, Context mContext) {
        this.mDatas = mDatas;
        this.mContext = mContext;
    }

    private onItenClickListener listener;

    public onItenClickListener getListener() {
        return listener;
    }

    public void setListener(onItenClickListener listener) {
        this.listener = listener;
    }

    //实现点击
    public interface onItenClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view,int position);
    }


    @Override
    public MyViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHodler hodler = new MyViewHodler(LayoutInflater.from(mContext)
                .inflate(R.layout.recyleview_item_layout,parent,false));
        return hodler;
    }

    @Override
    public void onBindViewHolder(final MyViewHodler holder, int position) {
        //给textView设置一个随机高度，实现瀑布流 实际情况可测量子view的高度
//            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.textView.getLayoutParams();
//            params.height = (int) (100+(Math.random()*300));
//            holder.textView.setLayoutParams(params);


//            GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(getResources());
//
//            GenericDraweeHierarchy hierarchy = builder.setFadeDuration(1000)
//                    //如果四次retry还没加载出图片，显示fail.png
//                    .setFailureImage(getResources().getDrawable(R.drawable.fail))
//                    .setRetryImage(getResources().getDrawable(R.drawable.rectry))
//                    .build();
//            holder.imagedrawee.setHierarchy(hierarchy);
//
//
        //设置图片的地址以及对图片的控制
        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setUri(mDatas.get(position).getUri())
                .setTapToRetryEnabled(true)
                .setOldController(holder.imagedrawee.getController())
                .build();
        holder.imagedrawee.setAspectRatio(0.86f);
        holder.imagedrawee.setController(draweeController);

        holder.textView.setText(mDatas.get(position).getText());

        //点击事件回调
        if(listener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getAdapterPosition();
                    listener.onItemClick(holder.itemView,pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getAdapterPosition();
                    listener.onItemLongClick(holder.itemView,pos);
                    return false;
                }
            });
        }

    }



    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class MyViewHodler extends RecyclerView.ViewHolder{
        TextView textView;
        SimpleDraweeView imagedrawee;

        public MyViewHodler(View itemView) {
            super(itemView);
            //注意textView来自itemView 不然会报NULL
            textView = (TextView) itemView.findViewById(R.id.textRecyleview);
            imagedrawee = (SimpleDraweeView) itemView.findViewById(R.id.user_avator);
        }
    }

    @Override
    public void onViewRecycled(MyViewHodler holder) {
        super.onViewRecycled(holder);
    }


    //添加图片
    public void add(int position){
        mDatas.add(position,new FrescoRecycler("添加美女", Uri.parse("http://www.diandidaxue.com:8080/images/beauty/20160117105655.jpg")));
        notifyItemInserted(position);
    }

    //删除图片
    public void remove(int position){
        mDatas.remove(position);
        notifyItemRemoved(position);
    }
}
