package com.ngbj.browse.adpter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ngbj.browse.R;
import com.ngbj.browse.bean.BookMarkData;
import com.ngbj.browse.bean.HistoryData;
import com.ngbj.browse.event.History_CollectionEvent;
import com.ngbj.browse.util.StringUtils;
import com.socks.library.KLog;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class BookMarkAdapter extends RecyclerView.Adapter<BookMarkAdapter.MyViewHolder> {

    private List<BookMarkData> dataList;
    private Context mContext;
    private LayoutInflater layoutInflater;
    private String type;

    public BookMarkAdapter(Context context, List<BookMarkData> dataList, String type) {
        this.dataList = dataList;
        mContext = context;
        layoutInflater = LayoutInflater.from(context);
        this.type = type;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_left, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        BookMarkData bookMarkData = dataList.get(position);
        //新闻取标题  搜索取关键字
        holder.tv_title.setText(bookMarkData.getTitle());
        holder.text_link.setText(bookMarkData.getVisit_link());

        //历史记录的点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(StringUtils.isFastClick()){
                    KLog.d("点击的太快啦");
                    return;
                }
                String link = dataList.get(position).getVisit_link();
                if(!TextUtils.isEmpty(link)){
                    EventBus.getDefault().post(new History_CollectionEvent(link,type));
                    ((Activity)mContext).finish();
                }

//                KLog.d("点击了第" + (position + 1) + "条条目");
//                Intent intent = new Intent(mContext, WebViewActivity.class);
//                intent.putExtra("url",dataList.get(position).getVisit_link());
//                intent.putExtra("type","0");
//                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_title;
        private TextView text_link;

        MyViewHolder(View itemView) {
            super(itemView);
            tv_title =  itemView.findViewById(R.id.text);
            text_link =  itemView.findViewById(R.id.text_link);
        }
    }

}
