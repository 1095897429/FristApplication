package com.ngbj.browse.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ngbj.browse.R;
import com.ngbj.browse.bean.MenuBean;

import java.util.List;

/***
 * 菜单的适配数据
 */

public class HomeMenuAdapter extends BaseAdapter {
    private List<MenuBean> listData;
    private LayoutInflater inflater;
    private Context context;

    public HomeMenuAdapter(Context context,List<MenuBean> listData) {
        this.context = context;
        this.listData = listData;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;
        if(null == convertView){
            convertView = inflater.inflate(R.layout.index_menu_item,parent,false);
            holder = new ViewHolder();
            holder.proName = convertView.findViewById(R.id.proName);
            holder.imgUrl = convertView.findViewById(R.id.imgUrl);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        //设置数据
        MenuBean bean = listData.get(position);
        holder.proName.setText(bean.getMenuName());
        holder.imgUrl.setImageResource(bean.getImgUrl());
        //设置监听
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                KLog.d(listData.get(position).getMenuName() + " ");
//                switch (position){
//                    case 0:
//                        context.startActivity(new Intent(context, HistoryCollecteActivity.class));
//                        break;
//                    case 6:
//                        context.startActivity(new Intent(context, SettingActivity.class));
//                        break;
//
//                    case 8:
//                        new ShareAlertDialog(context).builder().show();
//                        break;
//                }
//            }
//        });
        return convertView;
    }


    class ViewHolder{
        private TextView proName;
        private ImageView imgUrl;
    }


}
