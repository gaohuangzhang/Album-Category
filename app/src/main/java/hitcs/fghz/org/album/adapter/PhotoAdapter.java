package hitcs.fghz.org.album.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

import hitcs.fghz.org.album.R;
import hitcs.fghz.org.album.entity.PhotoItem;

/**
 * Created by me on 16-12-21.
 */

public class PhotoAdapter extends ArrayAdapter<PhotoItem> {
    private int resourceId;
    public PhotoAdapter(Context context, int textViewResourceId,
                        List<PhotoItem> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PhotoItem photo= getItem(position); // 获取当前项的Fruit实例
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        ImageView photoImage = (ImageView) view.findViewById(R.id.photo_small);
        photoImage.setImageResource(photo.getImageId());
        return view;
    }
}
