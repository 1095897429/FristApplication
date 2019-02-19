package com.ngbj.browse.activity;


import android.app.DownloadManager;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ngbj.browse.R;
import com.ngbj.browse.adpter.DownAdapter;
import com.ngbj.browse.adpter.DownFileAdapter;
import com.ngbj.browse.adpter.MyRecyclerAdapter;
import com.ngbj.browse.bean.HistoryData;
import com.ngbj.browse.bean.PicBean;
import com.ngbj.browse.dialog.DeleteAlertDialog;
import com.ngbj.browse.util.SDCardHelper;
import com.ngbj.browse.view.CustomDecoration;
import com.socks.library.KLog;

import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class DownPicActivity extends CommonHeadActivity {

    @BindView(R.id.delete_txt)
    TextView delete_txt;


    @BindView(R.id.recycleView)
    RecyclerView recyclerView;

    @BindView(R.id.empty_view)
    TextView empty_view;

    DownAdapter downAdapter;

    List<String> pathList = new ArrayList<>();
    private List<PicBean> mList = new ArrayList<>();
    PicBean picBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_download_pic;
    }

    @Override
    protected void initDatas() {
        center_title.setText("图片下载管理");
        initRecycle();
        getPathList();
    }

    private boolean editorStatus = false;
    private void initEvent() {
        downAdapter.setOnItemClickListener(new DownFileAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int pos, List<PicBean> myLiveList) {
                if(editorStatus){
                    PicBean picBean = myLiveList.get(pos);
                    boolean isSelect = picBean.isSelect();
                    if(!isSelect){
                        picBean.setSelect(true);
                        index++;
                    }else {
                        picBean.setSelect(false);
                        index--;
                    }
                    downAdapter.notifyDataSetChanged();
                }
            }
        });
    }


    public void initRecycle(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this );
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new CustomDecoration(this,
                CustomDecoration.VERTICAL_LIST,R.drawable.divider,0));
        layoutManager.setOrientation(OrientationHelper. VERTICAL);
    }


    private List<String> getPathList() {
        String dir = SDCardHelper.getSDCardBaseDir() + "/SmallBrowse/Pic";
        File parentFile = new File(dir);
        KLog.d("是否是文件夹：" + parentFile.isDirectory());
        if(parentFile.isDirectory()){
            File [] files = parentFile.listFiles();
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if(file.exists()){
                    picBean = new PicBean();
                    picBean.setName(file.getName());
                    picBean.setPath(file.getAbsolutePath());
                    long time = file.lastModified();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String result = formatter.format(time);
                    picBean.setTime(result);
                    mList.add(picBean);
                    pathList.add(file.getAbsolutePath());
                }
            }
        }

        if(pathList.size() > 0){
            downAdapter = new DownAdapter(mList);
            recyclerView.setAdapter(downAdapter);
            initEvent();
        }else{
            recyclerView.setVisibility(View.GONE);
            empty_view.setVisibility(View.VISIBLE);
        }

        return pathList;
    }


    private int mEditMode = MYLIVE_MODE_CHECK;//当前状态
    private static final int MYLIVE_MODE_CHECK = 0;
    private static final int MYLIVE_MODE_EDIT = 1;
    int index;
    @OnClick(R.id.delete)
    public void delete(){
        if(pathList.size()!= 0){
            mEditMode = mEditMode == MYLIVE_MODE_CHECK ? MYLIVE_MODE_EDIT:MYLIVE_MODE_CHECK;
            if(mEditMode == MYLIVE_MODE_EDIT ){
                delete_txt.setVisibility(View.VISIBLE);
                editorStatus = true;
            }else{
                delete_txt.setVisibility(View.GONE);
                editorStatus = false;
            }

            downAdapter.setEditMode(mEditMode);
        }

    }


    @OnClick(R.id.delete_txt)
    public void deleteTxt(){
        if(index != 0){
            DeleteAlertDialog deleteAlertDialog =  new DeleteAlertDialog(this).builder();
            String name = "删除选中的记录";
            deleteAlertDialog
                    .setContextText(name)
                    .setDeleteButton(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            List<PicBean> list = new ArrayList<>();
                            for (PicBean picBean: mList ) {
                                if(picBean.isSelect()){
                                    SDCardHelper.removeFileFromSDCard(picBean.getPath());//删除
                                    continue;
                                }
                                list.add(picBean);
                            }
                            KLog.d(list.size());

                            //删除操作
                            delete_txt.setVisibility(View.GONE);
                            mEditMode = MYLIVE_MODE_CHECK;
                            //重新设置
                            if(list.size() != 0){
                                downAdapter.setNewData(list);
                            }else{
                                recyclerView.setVisibility(View.GONE);
                                empty_view.setVisibility(View.VISIBLE);
                            }

                        }
                    });
            deleteAlertDialog.show();
        }


    }




}
