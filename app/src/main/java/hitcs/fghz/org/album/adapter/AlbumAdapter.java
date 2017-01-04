package hitcs.fghz.org.album.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import hitcs.fghz.org.album.R;
import hitcs.fghz.org.album.entity.AlbumItem;

import static android.media.ThumbnailUtils.extractThumbnail;
import static hitcs.fghz.org.album.utils.ImagesScaner.getBitmap;

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
        Bitmap bitmap = getBitmap(getContext(),album.getImageId());
        if (bitmap != null) {
            ;
        } else {
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_4444;
                bitmap = BitmapFactory.decodeFile(album.getImageId(), options);
                bitmap = extractThumbnail(bitmap,180 , 180);
            } catch (Exception e) {
                Log.d("Error: " , " " + e);
            }
        }
        albumImage.setImageBitmap(bitmap);
        albumName.setText(album.getName());
        return view;
    }
}