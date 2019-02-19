package com.ngbj.browse.adpter;


import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ngbj.browse.R;
import com.ngbj.browse.bean.AdBean;

import java.util.List;


public class Index_Cool_Adapter extends BaseQuickAdapter<AdBean,BaseViewHolder> {

    public Index_Cool_Adapter(List<AdBean> data) {
        super(R.layout.index_cool_item,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AdBean item) {
        helper.setText(R.id.proName,  item.getTitle() );

        if(item.getResId() != 0){
            ImageView imageView =  helper.getView(R.id.imgUrl);
            imageView.setImageResource(item.getResId());
        }else{
            Glide.with(mContext)
                    .load(item.getImg_url())
                    .into((ImageView) helper.getView(R.id.imgUrl));
        }
    }
}