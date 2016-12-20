package hitcs.fghz.org.album;
/**
 * Created by me on 16-12-19.
 */


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jay on 2015/8/28 0028.
 */
public class Photos extends Fragment {

    private String content;
    private List<PhotoItem> photoList = new ArrayList<PhotoItem>();

    GridView gridView;
    public Photos(String content) {
        this.content = content;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fg_photos,container,false);
        initPhoto();
        gridView = (GridView) view.findViewById(R.id.gridView1);
        PhotoAdapter adapter = new PhotoAdapter(getActivity(), R.layout.photo_item, photoList);

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,numbers);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                System.out.println(position+ " " +id);
                Intent intent = new Intent(getActivity(), PhotoDetail.class);
                startActivity(intent);

               // it.putExtra("title",txtTitle.getText());
             //   startActivity(it);

            }
        });
        return view;

    }
    private void initPhoto() {
        PhotoItem photo;
        for (int i = 0; i != 100; ++i) {
            photo = new PhotoItem(R.drawable.search);
            photoList.add(photo);
        }
    }
}
class PhotoItem {
    private int imageId;
    public PhotoItem(int imageId) {
        this.imageId = imageId;
    }
    public int getImageId() {
        return imageId;
    }
}
class PhotoAdapter extends ArrayAdapter<PhotoItem> {
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
