package hitcs.fghz.org.album.adapter;

import java.util.List;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;


import hitcs.fghz.org.album.R;
import hitcs.fghz.org.album.entity.PhotoItem;

import static android.media.ThumbnailUtils.extractThumbnail;
import static hitcs.fghz.org.album.utils.ImagesScaner.getBitmap;

/**
 * 照片栏目的适配器
 * Created by me on 16-12-21.
 */
public class PhotoAdapter extends ArrayAdapter<PhotoItem> {
    private int resourceId;
    LruCache mImageCache;
    LayoutInflater mInflater;
    List<PhotoItem> mImageList;
    boolean mBusy = false;

    public PhotoAdapter(Context context, int textViewResourceId,
                        List<PhotoItem> objects) {
        super(context, textViewResourceId, objects);
        mInflater = LayoutInflater.from(context);
        final int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        final int maxSize = 1024 * 1024 * memClass / 8;
        mImageCache = new LruCache(maxSize) {
            protected int sizeOf(String key, Bitmap value) {
                // TODO 自动生成的方法存根
                return value.getByteCount();
            }
        };
        mImageList = objects;
        resourceId = textViewResourceId;
    }
    @Override
    public int getCount() {
        // TODO 自动生成的方法存根
        return mImageList.size();
    }

    @Override
    public PhotoItem getItem(int position) {
        // TODO 自动生成的方法存根
        return mImageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO 自动生成的方法存根
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 获取当前项的item实例
        final PhotoItem photo= getItem(position);
        // 保存当前信息
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resourceId, null);
            holder = new ViewHolder();
            holder.iv_thumbnail = (ImageView) convertView.findViewById(R.id.photo_small);
            holder.thumbnail_url = photo.getImageId();
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            if (!holder.thumbnail_url.equals(photo.getImageId())) {
                holder.iv_thumbnail.setImageResource(R.drawable.loading);
            }
        }
        if (!isBusy()) {
            final String imgUrl = photo.getImageId();
            Bitmap bmp = (Bitmap) mImageCache.get(imgUrl);
            if (bmp != null) {
                holder.iv_thumbnail.setImageBitmap(bmp);
            } else {
                loadThumBitmap(imgUrl, photo);
                bmp = (Bitmap) mImageCache.get(imgUrl);
                holder.iv_thumbnail.setImageBitmap(bmp);
            }
        }
        return convertView;
    }
    private void loadThumBitmap(final String url, PhotoItem photo) {
        Bitmap bitmap = getBitmap(getContext(),photo.getImageId());
        if (bitmap != null) {
            ;
        } else {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_4444;
            bitmap = BitmapFactory.decodeFile(photo.getImageId(), options);
            bitmap = extractThumbnail(bitmap, 320, 320);
        }
        mImageCache.put(url, bitmap);
        notifyDataSetChanged();
    }
    @Override
    public int getItemViewType(int position) {
        // TODO 自动生成的方法存根
        return super.getItemViewType(position);
    }
    @Override
    public int getViewTypeCount() {
        // TODO 自动生成的方法存根
        return super.getViewTypeCount();
    }
    //用来保存各个控件的引用
    static class ViewHolder {
        ImageView iv_thumbnail;
        String thumbnail_url;
    }
    public boolean isBusy() {
        return mBusy;
    }

    public void setBusy(boolean busy) {
        this.mBusy = busy;
    }

}
