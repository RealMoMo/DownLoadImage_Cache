package momo.com.day25_test01.Utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Created by Administrator on 2016/11/11 0011.
 */
public class CacheUtils {

    static CacheUtils cacheUtils;

    static LruCache<String,Bitmap> lruCache;

    //单例模式
    private  CacheUtils(){
        long maxMemory = Runtime.getRuntime().maxMemory();
        lruCache = new LruCache<String,Bitmap>((int) (maxMemory/8)){

            @Override
            protected int sizeOf(String key, Bitmap value) {
               int count = value.getByteCount();
                return count;
            }
        };

    }

    public static CacheUtils getInstance(){
        if(cacheUtils==null){
            cacheUtils = new CacheUtils();
        }
        return  cacheUtils;
    }


    public void putBitmapToCache(String url,Bitmap bitmap){
        //缓存的内存里
        lruCache.put(url,bitmap);
        //还保存到SDcard里
        try {
            SDcardUtils.saveBitmapToSDcard(url,bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public Bitmap getBitmapFromCache(String url){
        //先从缓存中取
        Bitmap bitmap =   lruCache.get(url);
        //若没有，再到SDcard中取
        if(bitmap == null){
            try {
                bitmap = SDcardUtils.getBitmapFromSDcard(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return bitmap;
    }

}
