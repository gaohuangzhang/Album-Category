package hitcs.fghz.org.album.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import hitcs.fghz.org.album.R;
import hitcs.fghz.org.album.entity.AlbumItem;

/**
 * 相册的适配器
 * Created by me on 16-12-21.
 */

public class AlbumAdapter extends ArrayAdapter<AlbumItem> {
    private int resourceId;
    public AlbumAdapter(Context context, int textViewResourceId,
                        List<AlbumItem> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AlbumItem album = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        ImageView albumImage = (ImageView) view.findViewById(R.id.album_image);
        TextView albumName = (TextView) view.findViewById(R.id.album_name);
        albumImage.setImageResource(album.getImageId());
        albumName.setText(album.getName());
        return view;
    }
}