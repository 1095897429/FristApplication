package com.ngbj.browse.view;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ngbj.browse.R;
import com.ngbj.browse.bean.LoginBean;
import com.ngbj.browse.util.FileUtils;
import com.ngbj.browse.util.MediaUtils;
import com.ngbj.browse.util.SDCardHelper;
import com.ngbj.browse.util.SDCardUtils;
import com.socks.library.KLog;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static com.ngbj.browse.util.SDCardHelper.getSDCardBaseDir;


/**
 * Created by Administrator on 2015/3/4.
 */
public class AvatarWindow {
    private ViewGroup layout;
    private View avatarWindow;
    private Activity activity;
    public static final int REQUEST_IMAGE_CODE = 0x00;
    public static final int REQUEST_CAMERA_CODE = 0x01;
    public static final int REQUEST_EDIT_PIC = 0x02;
    private boolean isShowing;
    private static final String IMAGE_TYPE = "image/*";
    private int mBottomPadding = 0;
    private CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public interface CallBack{
        void uploadSuccess(LoginBean headPhotoBean);//上传
        void upTakePicSuccess();//带看
    }

    public AvatarWindow(Activity activity, ViewGroup layout) {
        this.activity = activity;
        this.layout = layout;
        initAvatarWindow();
    }

    private void initAvatarWindow() {
        avatarWindow = LayoutInflater.from(activity).inflate(R.layout.window_modify_avatar, null);
        ClickListener clickListener = new ClickListener();
        avatarWindow.findViewById(R.id.capture_txt).setOnClickListener(clickListener);
        avatarWindow.findViewById(R.id.gallery_txt).setOnClickListener(clickListener);
        avatarWindow.findViewById(R.id.cancel_txt).setOnClickListener(clickListener);
        avatarWindow.findViewById(R.id.dismiss_view).setOnClickListener(clickListener);
    }


    public void showAvatarWindow() {
        if (isShowing) {
            return;
        }
        if (avatarWindow == null) {
            initAvatarWindow();
        }
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        //主播招募h5页面 需要设置一个padding
        if(mBottomPadding != 0) {
            avatarWindow.setPadding(0, 0, 0, mBottomPadding);
        }
        layout.addView(avatarWindow, params);
        isShowing = true;
    }

    public void setBottomPadding(int padding) {
        mBottomPadding = padding;
    }

    public boolean isShowing() {
        return isShowing;
    }


    //上传头像 -- 我的界面里
    public void uploadPhoto() {
        //TODO editFile 指向这个文件 getName获取文件的名称 || 返回一个文件对象
        File editFile = getOutputEditImageFile();
//        RetrofitUtils.getInstance().uploadHeadPhoto(editFile.getName(), getOutputEditImageFile(), new BaseCallBack<HeadPhotoBean>() {
//            @Override
//            public void OnSuccess(HeadPhotoBean headPhotoBean) {
//                Toasty.normal(activity,"上传头像成功").show();
//                if(callBack!=null){
//                    callBack.uploadSuccess(headPhotoBean);
//                }
//            }
//
//            @Override
//            public void onFailure(int code, String msg) {
//                if(!TextUtils.isEmpty(msg)){
//                    Toasty.normal(activity,msg).show();
//                }
//            }
//        });
    }


    //Bitmap -- 文件
    public void saveBitmapFile(Bitmap bitmap) {
        File file = getYaSuoOutputCaptureImageFile();//将要保存图片的路径或文件
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //2018.8.8晚 上传多个文件时，文件名方式 Bitmap -- 文件
    public File saveBitmapFile(Bitmap bitmap,String fileName) {
        File file = new File(getFeedBackFile() , fileName + ".jpg" );//将要保存图片的路径或文件
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    //2018.8.9 从sdcard中删除文件
    public  boolean removeFileFromSDCard(String filePath){
        File file = new File(filePath);
        if(file.exists()){
            file.delete();
            return true;
        }else{
            return false;
        }
    }




    public File getFeedBackFile() {
        return activity.getExternalCacheDir();
//          return new File(SDCardUtils.getAppStoreImageFileDir());
    }


    //TODO 7.25 网络图片 -- 保存的地址
    public void saveBitmapFileFromNet(Bitmap bitmap) {
        File file = getWangluoOutputImageFile();//将要保存图片的路径或文件
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //TODO 8.6 网络图片实名认证 -- 保存的地址
    public void saveBitmapFileFromVerifiedNet(Bitmap bitmap) {
        File file = getWangluoOutputVerifiedImageFile();//将要保存图片的路径或文件
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//    private DefaultStringCallback getUploadPhotoCallBack() {
//        return new DefaultStringCallback() {
//            @Override
//            public void onSuccess(String data) {
//                super.onSuccess(data);
//                ToastUtils.show("上传头像成功");
//                EventBus.getDefault().post(new UpdateAvatarEvent(activity.getClass().getName()));
//                progressDialog.dismiss();
//            }
//
//            @Override
//            public void onFailure(String errorCode, String msg) {
//                super.onFailure(errorCode, msg);
//                if (!TextUtils.isEmpty(msg)) {
//                    ToastUtils.show(msg);
//                }
//                progressDialog.dismiss();
//
//            }
//        };
//    }

    private class ClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.capture_txt:
                    takePicture();
                    dismiss();
                    break;
                case R.id.gallery_txt:
                    choosePictureFromGallery();
                    dismiss();
                    break;
                case R.id.cancel_txt:
                case R.id.dismiss_view:
                    dismiss();
                    break;
            }
        }
    }

    public void dismiss() {
        if (layout != null && isShowing) {
            layout.removeView(avatarWindow);
            isShowing = false;
        }
    }


    private void choosePictureFromGallery() {
        if (MediaUtils.launchSys(activity, REQUEST_IMAGE_CODE)
                && MediaUtils.launch3partyBrowser(activity, REQUEST_IMAGE_CODE)
                && MediaUtils.launchFinally(activity)) ;
    }

    private void takePicture() {
        //TODO 调用相机的Intent -- 并指定图片保存的文件 和 Uri
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, FileUtils.getUriForFile(activity,getOutputCaptureImageFileNew()));
        activity.startActivityForResult(intentFromCapture, REQUEST_CAMERA_CODE);


//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        ContentValues contentValues = new ContentValues(1);
//        contentValues.put(MediaStore.Images.Media.DATA, getOutputCaptureImageFile().getAbsolutePath());
//        Uri uri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
//        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//        activity.startActivityForResult(intent, REQUEST_CAMERA_CODE);
    }

    /**
     *  TODO 获取拍照的图片文件 --- 重要
     *
     * @return
     */
    public File getOutputCaptureImageFile() {

        return new File(SDCardUtils.getAppStoreImageFileDir()+ "phone"+".png");
    }


    public File getOutputCustomerUploadImageFile() {

        return new File(SDCardUtils.getAppStoreImageFileDir()+ "phone2" +".png");
    }

    /**
     * TODO 获取拍照的图片文件 -- 先删，后建 -- 不适于获取File，用上面的
     *
     * @return
     */
    public File getOutputCaptureImageFileNew() {
        File outputImage = new File(SDCardUtils.getAppStoreImageFileDir()+ "phone3"+".png");
        if(outputImage.exists()){
            outputImage.delete();
        }
        try {
            outputImage.createNewFile();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return outputImage;
    }


    /**
     * 获取编辑之后的图片文件
     *
     * @return
     */
    public File getOutputEditImageFile() {
//        return new File(SDCardUtils.getAppStoreImageFileDir() +"head_edit.png");
//        return new File(getSDCardBaseDir(),"headedit.png");
        return  new File(SDCardHelper.getSDCardPrivateCacheDir(activity),"headedit.png");
    }


    /**
     * 获取压缩之后的图片文件
     */
    public File getYaSuoOutputCaptureImageFile() {

        return new File(SDCardUtils.getAppStoreImageFileDir()+ "zl"+".png");
    }

    /**
     * 获取下载apk之后的文件
     */
    public File getDownOutputCaptureImageFile() {

        return new File(SDCardUtils.getAppStoreImageFileDir());
    }


    /**
     * 获取网络之后的图片文件
     */
    public File getWangluoOutputImageFile() {

        return new File(SDCardUtils.getAppStoreImageFileDir()+ "network"+".png");
    }


    /**
     * 获取网络之后实名认证的图片文件
     */
    public File getWangluoOutputVerifiedImageFile() {

        return new File(SDCardUtils.getAppStoreImageFileDir()+ "verified"+".png");
    }


    public Bitmap getBitmapFromUri(Uri uri) {
        try {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //TODO 头像 + 拍照 -- 裁剪到一个File中，上传时，获取裁剪File即可
    public void editPicture(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
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
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getOutputEditImageFile()));
        intent.putExtra("return-data", true);//设置为不返回数据
        activity.startActivityForResult(intent, REQUEST_EDIT_PIC);
    }


    public static boolean saveBitmap2file(Bitmap bmp, String filename)
    {
        Bitmap.CompressFormat format = Bitmap.CompressFormat.PNG;
        int quality = 100;
        OutputStream stream = null;
        try
        {
            stream = new FileOutputStream(filename);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        return bmp.compress(format, quality, stream);
    }



}
