package momo.com.day25_test01;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import momo.com.day25_test01.Utils.BitmapUtils;

public class MainActivity extends AppCompatActivity {


    ImageView iv;
    TextView progress_tv;
    myReciver reciver;

    DownloadService service = new DownloadService();

    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupView();
        //注册本地广播
        reciver = new myReciver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.qf.week5.test");
        LocalBroadcastManager.getInstance(this).registerReceiver(reciver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //反注册本地广播
        LocalBroadcastManager.getInstance(this).unregisterReceiver(reciver);
    }

    private void setupView() {
        iv = (ImageView) findViewById(R.id.iv);
        progress_tv = (TextView) findViewById(R.id.progress_tv);
    }

    int i = 0;

    //点击start按钮，开启服务下载图片
    public void start(View view) {

        String http = "http://www.ytmfdw.com/image/img" + (i++ % 9 + 1) + ".jpg";
        Intent intent = new Intent(this, DownloadService.class);
        intent.putExtra("key", http);
        //开启服务
        startService(intent);

    }

    //广播
    class myReciver extends BroadcastReceiver {

        //接受广播
        @Override
        public void onReceive(Context context, Intent intent) {
            //进度广播
            int progress = intent.getIntExtra("progress", 0);
            if (progress != 0) {
                progress_tv.setText("downloading...:" + progress + "%");
            }



            //文件下载完成广播
            String name = intent.getStringExtra("name");
            if (name != null) {
                Log.d("Tag", "fileName:" + name);
                File file = new File(name);
                Uri uri = Uri.fromFile(file);
                iv.setImageURI(uri);
                bitmap = BitmapUtils.getSimplingBitmap(name);
                Log.d("Tag", "bitmap =null? :" + (bitmap == null ? true : false));
                iv.setImageBitmap(bitmap);

            }

//            bitmap = intent.getParcelableExtra("bitmap");
//            if(bitmap !=null){
//                iv.setImageBitmap(bitmap);
//                Log.d("Tag"," Bitmap:"+(bitmap==null?true:false));
//            }

            //文件已下载广播
            String exist = intent.getStringExtra("exist");
            if (exist != null) {
                progress_tv.setText(exist);
            }


        }
    }


}
