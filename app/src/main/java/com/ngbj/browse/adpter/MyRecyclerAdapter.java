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
import com.ngbj.browse.bean.HistoryData;
import com.ngbj.browse.event.History_CollectionEvent;
import com.ngbj.browse.fragment.CustomerFragment;
import com.ngbj.browse.fragment.HistoryListFragment;
import com.ngbj.browse.fragment.MyFragment;
import com.ngbj.browse.util.StringUtils;
import com.socks.library.KLog;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import static com.ngbj.browse.fragment.BaseFragment.getParentFragmentToType;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {

    private List<HistoryData> dataList;
    private Context mContext;
    private LayoutInflater layoutInflater;
    private String type;

    public MyRecyclerAdapter(Context context, List<HistoryData> dataList,String type) {
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
        HistoryData historyData = dataList.get(position);
        //新闻取标题  搜索取关键字
        holder.tv_title.setText(historyData.getTitle());
        holder.text_link.setText(historyData.getVisit_link());

        //历史记录的点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(StringUtils.isFastClick()){
                    KLog.d("点击的太快啦");
                    return;
                }

                KLog.d("type " + type);

                String link = dataList.get(position).getVisit_link();
                if(!TextUtils.isEmpty(link)){
                    EventBus.getDefault().post(new History_CollectionEvent(link,type));
                    ((Activity)mContext).finish();
                }

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
