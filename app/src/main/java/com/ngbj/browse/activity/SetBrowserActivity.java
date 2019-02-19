package com.ngbj.browse.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.widget.TextView;

import com.ngbj.browse.R;
import com.ngbj.browse.util.SPHelper;
import com.socks.library.KLog;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/***
 * 判断有没有设置默认浏览器信息
 *  false -- 选择打开方式 -- 进行选择
 *  true -- 清除设置的浏览器 -- false -- 选择打开方式 -- 进行选择
 */
public class SetBrowserActivity extends CommonHeadActivity {

    @BindView(R.id.start_set)
    TextView start_set;

    boolean isSetOk = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setbrowser;
    }

    @Override
    protected void initDatas() {
        center_title.setText("设置默认浏览器");

    }




    @OnClick(R.id.start_set)
    public void start_set(){
        if(isSetOk){
            String packageName = getDefaultPackageName();
            startAppInfoForPackageName(this,packageName,1);
        }else{
            sendSetDefaultBrowserRequestIntent(this);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        isSetOk =  hasPreferredApplication(this);
        if(isSetOk){
            start_set.setText("清除默认设置");
        }else{
            start_set.setText("开始设置");
        }
    }

    /**
     * 通知设置浏览器请求
     */
    public static void sendSetDefaultBrowserRequestIntent(Context context) {
        try {
            Intent localIntent = new Intent();
            localIntent.setAction("android.intent.action.VIEW");
            localIntent.putExtra("set_browser_congratulation", "set_browser_congratulation");
            localIntent.addCategory("android.intent.category.DEFAULT");
            localIntent.setData(Uri.parse("http://www.niaowifi.com/"));
//            localIntent.setComponent(new ComponentName("android", "com.android.internal.app.ResolverActivity"));
            context.startActivity(localIntent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 启动packageName对应的App的清除默认浏览器设置界面
     */
    public static void startAppInfoForPackageName(Activity activity, String packageName, int requestCode) {
        Intent intent = new Intent();
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", packageName, null));
        activity.startActivityForResult(intent, requestCode);
    }


    PackageManager pg;
    ResolveInfo info;

    private String  getDefaultPackageName(){
        pg  = getPackageManager();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://www.niaowifi.com/"));
        info = pg.resolveActivity(intent,PackageManager.MATCH_DEFAULT_ONLY);
        KLog.d("info :" + info + "  pagName:" + info.activityInfo.packageName);
        return  info.activityInfo.packageName;
    }

    //检查是否已经设置了默认
    public final boolean hasPreferredApplication(final Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://www.niaowifi.com/"));
        PackageManager pm = context.getPackageManager();
        ResolveInfo info = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);

        // 找出手机当前安装的所有浏览器程序
        @SuppressLint("WrongConstant")
        List<ResolveInfo> resolveInfoList = pm
                .queryIntentActivities(intent,
                        PackageManager.GET_INTENT_FILTERS);
        KLog.d(resolveInfoList.size());
        for (int i = 0; i < resolveInfoList.size(); i++) {
            ActivityInfo activityInfo = resolveInfoList.get(i).activityInfo;
            String packageName = activityInfo.packageName;
            if(packageName.equals(info.activityInfo.packageName)){
                if(packageName.equals(getPackageName())){//如果是自己的话，存本地
                    SPHelper.put(context,"isDefaultBrowser",true);
                }else{
                    SPHelper.put(context,"isDefaultBrowser",false);
                }
                return true;
            }
        }


        return false;
    }


}
