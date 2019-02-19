package com.ngbj.browse.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Date:2018/8/9
 * author:zl
 * 备注：
 *      1.压缩是io操作，尽量异步操作
 */
public class BitmapHelper {


    /***
     * 通过指定的路径获取到Bitmap
     * @param imagePath
     * @return
     */
    public static Bitmap getBitmap(String imagePath){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inSampleSize = 1;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeFile(imagePath,options);
    }

    /***
     * 保存Bitmap到本地
     * @param bitmap
     * @param outPath 具体文件名的绝对路径
     */
    public static void storeBitmap(Bitmap bitmap,String outPath){
        try {
            FileOutputStream os = new FileOutputStream(outPath);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,os);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /***
     * 保存Bitmap到本地
     * @param bitmap
     * @param name  具体文件名的名称
     */
    public static void storeImage(Bitmap bitmap,String name){
        File appDir = new File(Environment.getExternalStorageDirectory().getPath());
        if(!appDir.exists()){
            appDir.mkdir();
        }
        String fileName = name + ".jpg";
        File file = new File(appDir,fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //保存到缓存中
    public static void storeImageCache(Bitmap bitmap, String name, Context context){
        File appDir = new File(SDCardHelper.getSDCardPrivateCacheDir(context));
        if(!appDir.exists()){
            appDir.mkdir();
        }
        String fileName = name + ".jpg";
        File file = new File(appDir,fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /***
     * 计算inSampleSize的值
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options , int reqWidth,int reqHeight){
        int width = options.outWidth;
        int height = options.outHeight;
        int inSampleSize = 1;
        //宽高的缩放比例
        int widthScale = Math.round((float)width / reqWidth);
        int heightScale = Math.round((float)height / reqHeight);
        // 得到图片的压缩比例
        if(width > height){
            inSampleSize = widthScale;
        }else{
            inSampleSize = heightScale;
        }
        return inSampleSize;
    }


    /***
     * 尺寸压缩
     * @param imagePath
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap compressBitmapBySize(String  imagePath,int reqWidth,int reqHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//只获取信息，不加载Bitmap到内存
        options.inPreferredConfig = Bitmap.Config.RGB_565;//降低图片从ARGB888到RGB565
        Bitmap bitmap;//获取图片信息
        options.inSampleSize = calculateInSampleSize(options,reqWidth,reqHeight);// 计算inSampleSize
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(imagePath,options);
        return bitmap;
    }


    /***
     * 质量压缩
     * @param bitmap
     * @return
     */
    public static Bitmap compressBitmapByQuality(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        int options = 90;
        while (baos.toByteArray().length / 1024 > 1024 ){//是否大于1M
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG,options,baos);
            options -= 10;
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(bais);
        return bitmap;
    }

    /***
     * 尺寸压缩 + 质量压缩
     * @param imagePath
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap compressBitmap(String  imagePath,int reqWidth,int reqHeight){
        Bitmap bitmap = compressBitmapBySize(imagePath,reqWidth,reqHeight);
        bitmap = compressBitmapByQuality(bitmap);
        return bitmap;
    }



}
