package hitcs.fghz.org.album.adapter;

/**
 * 照片细节的适配器
 * Created by me on 16-12-21.
 */

import java.io.File;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import hitcs.fghz.org.album.R;

import static android.media.ThumbnailUtils.extractThumbnail;
import static hitcs.fghz.org.album.utils.ImagesScaner.getBitmap;

public class HorizontalScrollViewAdapter extends BaseAdapter
{

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Map> mDatas;
    private String url;

    public HorizontalScrollViewAdapter(Context context, List<Map> mDatas)
    {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
    }

    @Override
    public int getCount()
    {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position)
    {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Log.d("Horizontal: ", "position " + position);
        ViewHolder viewHolder = null;
        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(
                    R.layout.gallery_item, parent, false);
            viewHolder.mImg = (ImageView) convertView
                    .findViewById(R.id.id_index_gallery_item_image);


            convertView.setTag(viewHolder);
        } else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        try {
            url = (String) mDatas.get(position).get("_data");
            Bitmap bitmap = getBitmap(this.mContext,url);
            if (bitmap != null) {
                ;
            } else {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_4444;
                bitmap = BitmapFactory.decodeFile(url, options);
                bitmap = extractThumbnail(bitmap, 100, 100);
            }

            viewHolder.mImg.setImageBitmap(bitmap);
        } catch (Exception e) {
            viewHolder.mImg.setImageResource(R.drawable.none);
        }



        return convertView;
    }

    private class ViewHolder
    {
        ImageView mImg;
        TextView mText;
    }

}

