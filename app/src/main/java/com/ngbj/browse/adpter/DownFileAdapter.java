package com.ngbj.browse.adpter;


import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ngbj.browse.R;
import com.ngbj.browse.bean.PicBean;

import java.util.List;

public class DownFileAdapter extends BaseQuickAdapter<PicBean,BaseViewHolder> {


    private OnItemClickListener mOnItemClickListener;

    public DownFileAdapter(List<PicBean> data) {
        super(R.layout.index_down_file_item,data);
    }



    public void setEditMode(int editMode) {
        mEditMode = editMode;
        notifyDataSetChanged();
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
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
