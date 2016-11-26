package momo.com.day25_test01;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;

import momo.com.day25_test01.Utils.HttpUtils;

/**
 * Created by Administrator on 2016/11/11 0011.
 */
public class DownloadService extends IntentService {

    public DownloadService() {
        super("momo");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HttpUtils.DOWNLOAD_START: {
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(DownloadService.this);
                    builder.setTicker("开始下载")
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("开始下载图片");

                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    manager.notify(66, builder.build());
                }
                break;
                case HttpUtils.UDDATE_PROGRESS: {
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(DownloadService.this);
                    builder.setTicker("正在下载")
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("正在下载图片");

                    builder.setProgress(100, msg.arg1, false);

                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    manager.notify(66, builder.build());

                    //还要发本地广播，让Activity的文本框显示进度
                    Intent intent = new Intent("com.qf.week5.test");
                    intent.putExtra("progress", msg.arg1);
                    LocalBroadcastManager.getInstance(DownloadService.this).sendBroadcast(intent);


                }
                break;
                case HttpUtils.DOWNLOAD_FINISH: {
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(DownloadService.this);
                    builder.setTicker("下载完成")
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("图片下载完成");

                    //点击通知，跳转到Activity
//                    Intent intentToActivity = new Intent(DownloadService.this,MainActivity.class);
//                    PendingIntent pendingIntent = PendingIntent.getActivity(DownloadService.this,
//                            1,
//                            intentToActivity,
//                            PendingIntent.FLAG_UPDATE_CURRENT);
//
//                    builder.setContentIntent(pendingIntent);
                    builder.setAutoCancel(true);


                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    manager.notify(66, builder.build());

                    //还要发本地广播，让Activity的显示图片
                    Intent intent = new Intent("com.qf.week5.test");
                    //对应发消息的方式1：intent传文件名
                    Bundle bundle = msg.getData();
                    String name = bundle.getString("name");
                    intent.putExtra("name", name);

                    //对应发消息的方式2：intent传Bitmap(Bitmap已实现Parcelable接口)，可直接传
//                    Bitmap bitmap = (Bitmap) msg.obj;
//                    intent.putExtra("bitmap",bitmap);
                    LocalBroadcastManager.getInstance(DownloadService.this).sendBroadcast(intent);


                }
                break;
                case HttpUtils.EXIST_FILE: {
                    //已有该图片,发广播
                    Intent intent = new Intent("com.qf.week5.test");
                    intent.putExtra("exist", "本地已有该图片");
                    Bundle bundle = msg.getData();
                    String name = bundle.getString("name");
                    intent.putExtra("name", name);
                    LocalBroadcastManager.getInstance(DownloadService.this).sendBroadcast(intent);
                }
                break;
            }
        }
    };

    @Override
    protected void onHandleIntent(Intent intent) {

        String http = intent.getStringExtra("key");
        try {
            //下载图片
            HttpUtils.downloadByHttp(http, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
