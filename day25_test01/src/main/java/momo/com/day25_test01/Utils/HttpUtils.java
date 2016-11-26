package momo.com.day25_test01.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/11/11 0011.
 */
public class HttpUtils {

    public static final int DOWNLOAD_START = 0x100;
    public static final int UDDATE_PROGRESS = 0x101;
    public static final int DOWNLOAD_FINISH = 0x102;
    public static final int EXIST_FILE = 0x103;


    //联网下载图片
    public static Bitmap downloadByHttp(String http, Handler handler) throws Exception {

        if (handler == null) {
            Log.d("Tag", "handler为Null");
            return null;
        }

        Bitmap bitmap;
        //获取缓存/SDcard有木有该图片
        bitmap = CacheUtils.getInstance().getBitmapFromCache(http);

        //判断缓存/SDcard有木有该图片
        if (bitmap != null) {
            //若有，发已有该图片的消息。并直接返回Bitmap，不需下载
            Message msg = Message.obtain();
            msg.what = EXIST_FILE;
            String name = SDcardUtils.getSDcardPath() + SDcardUtils.PATH;
            Bundle bundle = new Bundle();
            bundle.putString("name", name + http.substring(http.lastIndexOf("/") + 1));
            msg.setData(bundle);
            handler.sendMessage(msg);
            return bitmap;
        }

        //若缓存/SDcard木有该图片，开始下载。并发开始下载的消息
        Message msg = Message.obtain();
        msg.what = DOWNLOAD_START;
        msg.obj = http;
        handler.sendMessage(msg);


        //联网下载图片

        HttpURLConnection conn = null;
        URL url = new URL(http);
        conn = (HttpURLConnection) url.openConnection();
        InputStream in = conn.getInputStream();

        //读写图片
        int totalLen = conn.getContentLength();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];
        int temp = 0;
        int currentLen = 0;
        while ((temp = in.read(buffer)) != -1) {
            out.write(buffer, 0, temp);
            currentLen += temp;

            //发更新进度消息
            int per = (int) (currentLen * 100f / totalLen);
            Message msg1 = Message.obtain();
            msg1.what = UDDATE_PROGRESS;
            msg1.arg1 = per;
            handler.sendMessage(msg1);
        }
        //刷流
        out.flush();

        bitmap = BitmapFactory.decodeByteArray(out.toByteArray(), 0, out.toByteArray().length);


        //下载完，保存到缓存与SDcard中
        CacheUtils.getInstance().putBitmapToCache(http, bitmap);

        //发下载完成的消息
        Message msg2 = Message.obtain();
        msg2.what = DOWNLOAD_FINISH;
        String name = SDcardUtils.getSDcardPath() + SDcardUtils.PATH;
        Log.d("Tag", "name:" + name);

        //方式1：传文件名路径
        Bundle bundle = new Bundle();
        bundle.putString("name", name + http.substring(http.lastIndexOf("/") + 1));
        msg2.setData(bundle);

        //方式2：传Bitmap
//        msg2.obj =bitmap;

        handler.sendMessage(msg2);

        //关流
        out.close();
        in.close();
        if (conn != null) {
            conn.disconnect();
        }

        Log.d("Tag", "联网下载图片：" + http);

        return bitmap;
    }


}
