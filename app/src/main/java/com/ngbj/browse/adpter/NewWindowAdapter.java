package com.ngbj.browse.adpter;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ngbj.browse.R;
import com.ngbj.browse.bean.NewWindowBean;
import com.ngbj.browse.bean.NewsBean;
import com.ngbj.browse.util.BitmapHelper;
import com.ngbj.browse.util.SDCardHelper;
import com.ngbj.browse.util.StringUtils;
import com.socks.library.KLog;

import java.util.List;

public class NewWindowAdapter extends BaseQuickAdapter<NewWindowBean,BaseViewHolder> {

    public NewWindowAdapter(List<NewWindowBean> data) {
        super(R.layout.new_window_item,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NewWindowBean item) {

        helper.addOnClickListener(R.id.delete_window);    //给图标添加点击事件
        Bitmap bitmap = null;
        KLog.d(bitmap);

        String name = StringUtils.getFragmentName(item.getType());
        bitmap =   BitmapHelper.getBitmap(SDCardHelper.getSDCardPrivateCacheDir(mContext) + "/"+ name + ".jpg");

        helper.setImageBitmap(R.id.imageView,bitmap);

    }
}
