package com.ngbj.browse.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ngbj.browse.R;
import com.ngbj.browse.bean.AdBean;
import com.ngbj.browse.bean.ProductListBean;
import com.socks.library.KLog;

import java.util.List;

/***
 * 首页GridView加载数据Adapter
 */
public class IndexGridViewAdapter extends BaseAdapter{
    private List<AdBean> listData;
    private LayoutInflater inflater;
    private Context context;
    private int mIndex;//页数下标 表示第几页 从0开始
    private int mPageSize;//每页显示最大数量

    public IndexGridViewAdapter(Context context, List<AdBean> listData, int mIndex, int mPagerSize) {
        this.context = context;
        this.listData = listData;
        this.mIndex = mIndex;
        this.mPageSize = mPagerSize;
        inflater = LayoutInflater.from(context);
    }


    //逻辑是：每个VP的界面都会创建一个GridView实例，那么这个getCount返回的数据也会不同
    //所有的数据不变 --- 如果不够每页的最大值，那么久显示剩下的数据
    @Override
    public int getCount() {
        return listData.size() > (mIndex + 1) * mPageSize ? mPageSize: (listData.size() - mIndex * mPageSize) ;
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position + mIndex *  mPageSize);
    }

    @Override
    public long getItemId(int position) {
        return position + mIndex *  mPageSize;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;
        if(null == convertView){
            convertView = inflater.inflate(R.layout.index_tag2_item_girditem,parent,false);
            holder = new ViewHolder();
            holder.proName = convertView.findViewById(R.id.proName);
            holder.imgUrl = convertView.findViewById(R.id.imgUrl);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }


        //设置数据
        final  int pos = position + mIndex * mPageSize;//重新确定position（因为拿到的是总的数据源，数据源是分页加载到每页的GridView上的，为了确保能正确的点对不同页上的item）
        AdBean bean = listData.get(pos);
        holder.proName.setText(bean.getTitle());

        if(bean.getResId() != 0){
            holder.imgUrl.setImageResource(bean.getResId());
        }else{
            Glide.with(context)
                    .load(bean.getImg_url())
                    .into(holder.imgUrl);
        }

        return convertView;
    }

    class ViewHolder{
        private TextView proName;
        private ImageView imgUrl;
    }






















}
