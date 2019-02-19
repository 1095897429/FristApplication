package com.ngbj.browse.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.support.annotation.RequiresApi;

import com.socks.library.KLog;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * anthor:Administrator
 * create on:2018/8.9
 * des:SD帮助类
 *  内部存储：关注data/data目录下，比如data/data/包名/files  data/data/包名/cache等等
 *  外部存储：关注storage/sdcard目录下，在sdcard目录下又分为
 *               公有目录【九大类，比如Download DICM等等】
 *               私有目录【Android目录下】 storage/sdcard/Android/data/包名/files
 *
 *   操作：包含包名的都是调用Context里的方法
 *          比如说：内部存储 -- context.getFileDir()
 *                  外部私有 -- context.getExternalCacheDir()
 *                  外部公有 -- Environment.getExternalStoragePublicDirectory()
 */
public class SDCardHelper {

    //判断SD卡是否被挂载
    public static boolean isSDCardMounted(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    //获取SD卡的根目录 一般为：storage/sdcard/0
    public static String getSDCardBaseDir(){
        KLog.d("根目录 " + Environment.getExternalStorageDirectory().getAbsolutePath());
        if(isSDCardMounted()){
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return null;
    }

    //获取SD卡公有目录的路径
    public static String getSDCardPublicDir(String type){
        return Environment.getExternalStoragePublicDirectory(type).toString();
    }

    // 获取SD卡私有Cache目录的路径
    public static String getSDCardPrivateCacheDir(Context context) {
        KLog.d("路径：" + context.getExternalCacheDir().getAbsolutePath());
        return context.getExternalCacheDir().getAbsolutePath();
//        KLog.d("路径2：" +  getSDCardPublicDir("DIRECTORY_DCIM"));
//        return  getSDCardPublicDir("DIRECTORY_DCIM");
    }

    //获取SD卡的剩余空间大小
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static long getSDCardFreeSize(){
        if(isSDCardMounted()){
            StatFs fs = new StatFs(getSDCardBaseDir());
            long count = fs.getFreeBlocksLong();
            long size = fs.getBlockSizeLong();
            return count * size / 1024 / 1024;
        }
        return 0;
    }

    //往SD卡公有目录下保存文件 DownLoad ， DICM等等
    public static boolean saveFileToSDCardPublicDir(byte [] data,String type,String fileName){
        BufferedOutputStream bos = null;
        if(isSDCardMounted()){
            File file = Environment.getExternalStoragePublicDirectory(type);
            try {
                bos = new BufferedOutputStream(new FileOutputStream(new File(file,fileName)));
                bos.write(data);
                bos.flush();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }




    //往SD卡自定义目录下保存文件
    public static boolean saveFiletoSDCardCustomDir(byte[] data,String dir,String fileName){
        BufferedOutputStream bao = null;
        if(isSDCardMounted()){
            File file = new File(getSDCardBaseDir() + File.separator + dir);
            if(!file.exists()){
                file.mkdirs();//创建自定义目录
            }
            try {
                bao = new BufferedOutputStream(new FileOutputStream(new File(file,fileName)));
                bao.write(data);
                bao.flush();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                try {
                    bao.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    //往SD卡私有Files目录下保存文件
    public static boolean saveFiletoSDCardPrivateDir(byte [] data, String dir, String fileName, Context context){
        BufferedOutputStream bos = null;
        if(isSDCardMounted()){
            File file = context.getExternalFilesDir(dir);
            try {
                bos = new BufferedOutputStream(new FileOutputStream(new File(file,fileName)));
                bos.write(data);
                bos.flush();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    // 往SD卡的私有Cache目录下保存文件
    public static boolean saveFileToSDCardPrivateCacheDir(byte[] data, String fileName, Context context) {
        BufferedOutputStream bos = null;
        if (isSDCardMounted()) {
            File file = context.getExternalCacheDir();
            try {
                bos = new BufferedOutputStream(new FileOutputStream(new File(file, fileName)));
                bos.write(data);
                bos.flush();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    // 保存bitmap图片到SDCard的私有Cache目录
    public static boolean saveBitmapToSDCardPrivateCacheDir(Bitmap bitmap,String fileName,Context context){
        BufferedOutputStream bos = null;
        if(isSDCardMounted()){
            // 获取私有的Cache缓存目录
            File file = context.getExternalCacheDir();
            try {
                bos = new BufferedOutputStream(new FileOutputStream(new File(file, fileName)));
                if(fileName != null && ( fileName.contains(".png") || fileName.contains(".PNG"))){
                    bitmap.compress(Bitmap.CompressFormat.PNG,80,bos);//保留图片80%的质量
                }else if(fileName != null && ( fileName.contains(".jpg"))){
                    bitmap.compress(Bitmap.CompressFormat.JPEG,80,bos);
                }
                bos.flush();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    //从SD卡获取文件
    public static byte[] loadFileFromSDCard(String fileDir){
        BufferedInputStream bis = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        if(isSDCardMounted()){
            try {
                bis = new BufferedInputStream(new FileInputStream(new File(fileDir)));
                int c ;
                byte [] buffer = new byte[1024 * 8];
                while ((c = bis.read(buffer)) != -1){
                    bos.write(buffer, 0,c);//将buffer数据写到流中
                    bos.flush();
                }
                return bos.toByteArray();
            } catch (Exception e) {
                e.printStackTrace();
            }finally {

                try {
                    bos.close();
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    //从SD卡中寻找指定目录下的文件，返回Bitmap
    public Bitmap loadBitmapFromSDCard(String filePath){
        byte[] data = loadFileFromSDCard(filePath);
        if(data != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
            if(bitmap != null)
                return bitmap;
        }
        return null;
    }

    // 从sdcard中删除文件
    public static boolean removeFileFromSDCard(String filePath){
        File file = new File(filePath);
        if(file.exists()){
                file.delete();
                return true;
        }else{
            return false;
        }
    }




}
