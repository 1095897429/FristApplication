package com.ngbj.browse.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;


/**
 * Created by weixiaopan on 2018/2/23.
 */

public class MediaUtils {
    private MediaUtils() {
    }

    private static final String IMAGE_TYPE = "image/*";

    /**
     * PopupMenu打开本地相册.
     */
    public static boolean launchSys(Activity activity, int actResultCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                IMAGE_TYPE);
        try {
            activity.startActivityForResult(intent, actResultCode);

        } catch (android.content.ActivityNotFoundException e) {

            return true;
        }

        return false;
    }

    /**
     * 打开其他的一文件浏览器,如果没有本地相册的话
     */
    public static boolean launch3partyBrowser(Activity activity, int requestCode) {
        //ToastUtils.show("没有相册软件，运行文件浏览器");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT); // "android.intent.action.GET_CONTENT"
        intent.setType(IMAGE_TYPE); // 查看类型 String IMAGE_UNSPECIFIED =
        // "image/*";
        Intent wrapperIntent = Intent.createChooser(intent, null);
        try {
            activity.startActivityForResult(wrapperIntent, requestCode);
        } catch (android.content.ActivityNotFoundException e1) {
            return true;
        }
        return false;
    }

    /**
     * 这个是找不到相关的图片浏览器,或者相册
     */
    public static boolean launchFinally(Activity activity) {
        ToastUtil.showShort(activity, "您的系统没有文件浏览器或则相册支持,请安装！");
        return false;
    }

    public static void editPicture(Activity activity, Uri uri, File outputFile, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);// 去黑边
        intent.putExtra("scaleUpIfNeeded", true);// 去黑边
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);//输出是X方向的比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);//输出X方向的像素
        intent.putExtra("outputY", 200);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputFile));
        intent.putExtra("return-data", false);//设置为不返回数据
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 选择图片后，获取图片的路径
     *
     * @param data
     */
    public static String handleSelectedPhoto(Activity activity,Intent data) {
            String picPath = null;
            if (data == null) {
                ToastUtil.showShort(activity, "选择图片文件出错");
                return null;
            }
            Uri photoUri = data.getData();
            if (photoUri == null) {
                ToastUtil.showShort(activity, "选择图片文件出错");
                return null;
            }

        String[] pojo = {MediaStore.Images.Media.DATA};
        ContentResolver cr = activity.getContentResolver();
        Cursor cursor = cr.query(photoUri, pojo,null, null, null);
            if(cursor !=null){
            int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
            cursor.moveToFirst();
             picPath = cursor.getString(columnIndex);
            cursor.close();
        }
            if(picPath !=null&&(picPath.endsWith(".png")||picPath.endsWith(".PNG")||picPath.endsWith(".jpg")||picPath.endsWith(".JPG"))) {
            return picPath;
        }else{
                ToastUtil.showShort(activity, "选择图片文件出错");
            return null;
        }
    }
}
