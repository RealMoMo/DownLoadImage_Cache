package momo.com.day25_test01.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * 对图片二次采样
 */
public class BitmapUtils {


    public static Bitmap getSimplingBitmap(String filePath) {

        BitmapFactory.Options ops = new BitmapFactory.Options();

        ops.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(filePath,ops);

        int width = ops.outWidth;
        int height = ops.outHeight;
        Log.d("Tag","width:"+width);
        Log.d("Tag","height:"+height);

        //在界面上显示下载完的图片，对图片做二次采样处理，使宽高变成原来的一半
        int scal = 2;

        ops.inJustDecodeBounds =false;
        ops.inSampleSize =scal;

        ops.inPreferredConfig = Bitmap.Config.ALPHA_8;


        return BitmapFactory.decodeFile(filePath,ops);
    }

}
