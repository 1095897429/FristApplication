package com.ngbj.browse.activity;

import android.widget.TextView;

import com.ngbj.browse.R;
import com.ngbj.browse.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public abstract class CommonHeadActivity extends BaseActivity {

    @BindView(R.id.center_title)
    TextView center_title;

   @OnClick(R.id.back)
    public void back(){
       finish();
   }
}
