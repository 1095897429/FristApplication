package com.ngbj.browse.adpter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ngbj.browse.R;
import com.ngbj.browse.bean.AdBean;
import com.ngbj.browse.bean.PicBean;
import com.ngbj.browse.event.History_CollectionEvent;
import com.ngbj.browse.util.StringUtils;
import com.socks.library.KLog;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;

public class DownAdapter extends BaseQuickAdapter<PicBean,BaseViewHolder> {

    private DownFileAdapter.OnItemClickListener mOnItemClickListener;

    public DownAdapter(List<PicBean> data) {
        super(R.layout.index_down_item,data);
    }


    public void setEditMode(int editMode) {
        mEditMode = editMode;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(DownFileAdapter.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
    public interface OnItemClickListener {
        void onItemClickListener(int pos,List<PicBean> myLiveList);
    }


    private static final int MYLIVE_MODE_CHECK = 0;
    int mEditMode = MYLIVE_MODE_CHECK;


    @Override
    protected void convert(final BaseViewHolder helper, final PicBean item) {

        helper.setText(R.id.name, item.getName())
                .setText(R.id.time,item.getTime());


        Glide.with(mContext)
                .load(item.getPath())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into((ImageView) helper.getView(R.id.imageView));

//        helper.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if(StringUtils.isFastClick()){
//                    return;
//                }
//
//                //调用系统图片查看器打开应用目录的图片
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                String  filePath = item.getPath();
//                Uri uri;
//                if (Build.VERSION.SDK_INT >= 24) {
//                    File file = new File(filePath);
//                    KLog.d("mine",file.length()+"");
//                    uri = FileProvider.getUriForFile(mContext,  "com.ngbj.browse.fileprovider", new File(filePath));
//                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//注意加上这句话
//                } else {
//                    uri = Uri.fromFile(new File(filePath));
//                }
//                intent.setDataAndType(uri, "image/*");
//                mContext.startActivity(intent);
//            }
//        });

        if (mEditMode == MYLIVE_MODE_CHECK) {
            helper.setVisible(R.id.mCheckBox,false);
        } else {
            helper.setVisible(R.id.mCheckBox,true);

            if (item.isSelect()) {
                helper.setImageResource(R.id.mCheckBox,R.mipmap.checked);
            } else {
                helper.setImageResource(R.id.mCheckBox,R.mipmap.unchecked);
            }
        }

        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClickListener(helper.getAdapterPosition(), mData);
            }
        });
    }

}
