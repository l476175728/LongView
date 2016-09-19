package com.example.liuhailong.longviewsample.adaper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.liuhailong.longviewsample.R;
import com.example.liuhailong.longviewsample.bean.ResultBean;
import com.lidroid.xutils.BitmapUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by liuhailong on 16/6/29.
 */
public class MyGridViewAdapter extends BaseAdapter {

  //  private final RequestQueue mRequestque;
    private Context mContext;
    private List<ResultBean.Photo> photos;

    private LayoutInflater mInflater;



//	final LruCache<String, Bitmap> mImageCache = new LruCache<String, Bitmap>(
//			20);
//	ImageLoader.ImageCache imageCache = new ImageLoader.ImageCache() {
//		@Override
//		public void putBitmap(String key, Bitmap value) {
//			mImageCache.put(key, value);
//		}
//
//		@Override
//		public Bitmap getBitmap(String key) {
//			return mImageCache.get(key);
//		}
//	};
//    private final ImageLoader mImageLoader;
    private final int width;
    private final BitmapUtils bitmapUtils;

    public MyGridViewAdapter(Context mContext, List<ResultBean.Photo> photos) {

        mInflater= LayoutInflater.from(mContext);
        this.mContext=mContext;
        this.photos=photos;

        width = mContext.getResources().getDisplayMetrics().widthPixels;
       // mRequestque = Volley.newRequestQueue(mContext);
       // mImageLoader = new ImageLoader(mRequestque, imageCache);
        bitmapUtils = new BitmapUtils(mContext);
        bitmapUtils.configDefaultLoadFailedImage(android.R.drawable.ic_menu_rotate);
        bitmapUtils.configDefaultLoadFailedImage(android.R.drawable.ic_delete);
        bitmapUtils.configDiskCacheEnabled(true);
        bitmapUtils.configThreadPoolSize(5);

    }

    @Override
    public int getCount() {
        return photos==null?0:photos.size();
    }

    @Override
    public Object getItem(int position) {
        return photos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

       View view=mInflater.inflate(R.layout.gridview_item,null);

       ImageView imageView= (ImageView) view.findViewById(R.id.show_imageview);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(width/3, width/3));

//        ImageLoader.ImageListener listener = ImageLoader
//					.getImageListener(imageView, android.R.drawable.ic_menu_rotate,
//							android.R.drawable.ic_delete);
//			mImageLoader.get(getURLEncoded(photos.get(position).getFilePath()), listener);
        bitmapUtils.display(imageView,getURLEncoded(photos.get(position).getFilePath()));
        return view;
    }


    public static String getURLEncoded(String str) {
        String result = "";
        String filename = str.substring(str.lastIndexOf('/') + 1);
        String prePath = str.substring(0, str.lastIndexOf('/') + 1);
        try {
            result = URLEncoder.encode(filename, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "http://112.33.7.21:8081/Telemedicine/" + prePath + result;
    }
}
