package com.ngbj.browse.fragment;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ngbj.browse.R;
import com.ngbj.browse.adpter.BookMarkAdapter;
import com.ngbj.browse.adpter.MyRecyclerAdapter;
import com.ngbj.browse.bean.BookMarkData;
import com.ngbj.browse.bean.HistoryData;
import com.ngbj.browse.db.DBManager;
import com.ngbj.browse.event.CleanHistoryEvent;
import com.ngbj.browse.util.SPHelper;
import com.ngbj.browse.view.CustomDecoration;
import com.socks.library.KLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Date:2018/8/20
 * author:zl
 * 备注：收藏
 */
public class CollectionListFragment extends BaseFragment {


    @BindView(R.id.recycleView)
    RecyclerView recyclerView;

    @BindView(R.id.empty_view)
    TextView empty_view;

    BookMarkAdapter myRecyclerAdapter;

    private List<BookMarkData> mList = new ArrayList<>();
     String myType;



    public static CollectionListFragment getInstance(){
        return new CollectionListFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_collectlist;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCleanHistoryEvent(CleanHistoryEvent event) {
        if(event.getIndex() == 1){
            if(mList != null && mList.size() != 0 ){
                mList.clear();
                myRecyclerAdapter.notifyDataSetChanged();
            }
            getActivity().finish();
        }
    }


    @Override
    protected void initVidget() {
        myType = (String) SPHelper.get(getActivity(),"history_type","1");
        EventBus.getDefault().register(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity() );
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new CustomDecoration(getActivity(),
                CustomDecoration.VERTICAL_LIST,R.drawable.divider,0));
        layoutManager.setOrientation(OrientationHelper. VERTICAL);
        //设置Adapter
//        for (int i = 1; i <= 5 ; i++) {
//            mList.add(i+" 历史记录");
//        }

        //获取数据库的数据
        List<BookMarkData> list =  DBManager.getInstance(getActivity()).queryBookMark();
        if(null != list && list.size() != 0){
            for (BookMarkData bookMarkData : list) {
                mList.add(bookMarkData);
            }
            myRecyclerAdapter = new BookMarkAdapter(getActivity(),mList,myType);
            recyclerView.setAdapter(myRecyclerAdapter);
        }else{
            recyclerView.setVisibility(View.GONE);
            empty_view.setVisibility(View.VISIBLE);
        }


    }
}
