package com.ngbj.browse.adpter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ngbj.browse.R;
import com.ngbj.browse.bean.NewsSaveMultiBean;
import com.ngbj.browse.util.StringUtils;

import java.util.List;

public class NewsMultiAdapter extends BaseMultiItemQuickAdapter<NewsSaveMultiBean,BaseViewHolder> {

    public NewsMultiAdapter(List<NewsSaveMultiBean> data) {
        super(data);
        addItemType(NewsSaveMultiBean.TYPE_ONE,R.layout.news_item);
        addItemType(NewsSaveMultiBean.TYPE_THREE,R.layout.news_3_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, NewsSaveMultiBean item) {
        int  type = item.getItemType();
        switch (type){
            case NewsSaveMultiBean.TYPE_ONE:
                helper.setText(R.id.title, item.getTitle())
                        .setText(R.id.author, item.getAuthor());
                helper.setText(R.id.time, StringUtils.longToDateStr(item.getPubtime()));

                Glide.with(mContext)
                        .load(item.getShow_img().get(0))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.mipmap.default_news)
                        .crossFade()
                        .into((ImageView) helper.getView(R.id.imageView));
                break;
            case NewsSaveMultiBean.TYPE_THREE:
                helper.setText(R.id.title, item.getTitle())
                        .setText(R.id.author, item.getAuthor());
                helper.setText(R.id.time, StringUtils.longToDateStr(item.getPubtime()));
                Glide.with(mContext)
                        .load(item.getShow_img().get(0))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.mipmap.default_news)
                        .crossFade()
                        .into((ImageView) helper.getView(R.id.imageView1));

                Glide.with(mContext)
                        .load(item.getShow_img().get(1))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.mipmap.default_news)
                        .crossFade()
                        .into((ImageView) helper.getView(R.id.imageView2));

                Glide.with(mContext)
                        .load(item.getShow_img().get(2))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.mipmap.default_news)
                        .crossFade()
                        .into((ImageView) helper.getView(R.id.imageView3));
                break;
        }


    }


}
