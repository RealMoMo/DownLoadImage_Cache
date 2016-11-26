package momo.com.day25_test01.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Administrator on 2016/11/11 0011.
 */
public class SDcardUtils {

    //声明图片保存的文件夹
    public static  final String PATH ="/momo/";

    public static void saveBitmapToSDcard(String url, Bitmap bitmap) throws Exception {
        if(!isMounted()){
            Log.d("Tag","SDcard没有挂载好");
            return;
        }

        String path = getSDcardPath()+PATH;
        File parent = new File(path);

        //若保存的文件夹不存在，则创建
        if(!parent.exists()){
            parent.mkdirs();
        }
        Log.d("Tag","parent.getAbsolutePath())"+parent.getAbsolutePath());

        //保存图片

        String fileName = url.substring(url.lastIndexOf("/")+1);
        File targetFile = new File(parent,fileName);
        FileOutputStream out = new FileOutputStream(targetFile);

        bitmap.compress(Bitmap.CompressFormat.JPEG,100,out);

        out.close();


    }


    public static Bitmap getBitmapFromSDcard(String url)throws Exception{

        if(!isMounted()){
            Log.d("Tag","SDcard没有挂载好");
            return null;
        }

        String path = getSDcardPath()+PATH;
        String fileName = url.substring(url.lastIndexOf("/")+1);
        File targetFile = new File(path,fileName);

        //判断图片是否保存了
        if(!targetFile.exists()){
            Log.d("Tag","图片没有下载到SDcard");
            return null;
        }

        return BitmapFactory.decodeFile(targetFile.getAbsolutePath());
    }


    //sdcard是否挂载好
    public static boolean isMounted(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    //获取sdcard的根目录
    public static String getSDcardPath(){

        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

}
