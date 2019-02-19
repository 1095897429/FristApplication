package com.ngbj.browse.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.ngbj.browse.R;
import com.ngbj.browse.adpter.HomeFragmentAdapter;
import com.ngbj.browse.base.BaseActivity;
import com.ngbj.browse.db.DBManager;
import com.ngbj.browse.dialog.BottomAlertDialog;
import com.ngbj.browse.dialog.DeleteAlertDialog;
import com.ngbj.browse.event.CleanHistoryEvent;
import com.ngbj.browse.fragment.CollectionListFragment;
import com.ngbj.browse.fragment.HistoryListFragment;
import com.ngbj.browse.util.SPHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HistoryCollecteActivity extends BaseActivity {

    @BindView(R.id.tl_3)
    SegmentTabLayout mTabLayout_3;

    @BindView(R.id.viewPager)
    ViewPager viewPager;


    @BindView(R.id.delete)
    ImageView delete;

    String type;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_historylist;
    }

    @Override
    protected void initDatas() {
        type = getIntent().getStringExtra("type");
        SPHelper.put(this,"history_type",type);
        initData();
    }

    private void initData() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(HistoryListFragment.getInstance());
        fragments.add(CollectionListFragment.getInstance());
        List<String> list_Title = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            list_Title.add(i + "");
        }
        HomeFragmentAdapter pagerAdapter = new HomeFragmentAdapter(getSupportFragmentManager(),fragments,list_Title);
        viewPager.setAdapter(pagerAdapter);
        String [] titles = new String[]{"历史记录","收藏"};
        mTabLayout_3.setTabData(titles);
        mTabLayout_3.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout_3.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(0);

    }

    @OnClick(R.id.delete)
    public void delete(){
        DeleteAlertDialog deleteAlertDialog =  new DeleteAlertDialog(this).builder();

        final int position = viewPager.getCurrentItem();
        String name = "清空所有历史记录";
        if(0 == position){
            name = "清空所有历史记录";
        }else if(1 == position){
            name = "清空所有收藏记录";
        }
        deleteAlertDialog
                .setContextText(name)
                .setDeleteButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO 请求到后台
                        if(0 == position){
                            DBManager.getInstance(HistoryCollecteActivity.this).deleteAllHistoryData();
                        }else if(1 == position){
                            DBManager.getInstance(HistoryCollecteActivity.this).deleteBookMark();
                        }
                        //TODO 发送个事件到fragment中
                        EventBus.getDefault().post(new CleanHistoryEvent(position));
                    }
                });
        deleteAlertDialog.show();
    }


    @OnClick(R.id.back)
    public void back(){
        finish();
    }

}
