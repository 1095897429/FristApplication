package com.ngbj.browse.adpter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ngbj.browse.R;
import com.ngbj.browse.bean.AdBean;

import java.util.List;

public class ADAdapter extends BaseQuickAdapter<AdBean,BaseViewHolder> {

    public ADAdapter(List<AdBean> data) {
        super(R.layout.index_menu_item,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AdBean item) {

        helper.setText(R.id.proName, item.getTitle());


        Glide.with(mContext)
                .load(item.getImg_url())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into((ImageView) helper.getView(R.id.imgUrl));


    }
}
