package hitcs.fghz.org.album;
/**
 * Created by me on 16-12-19.
 */
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import hitcs.fghz.org.album.entity.PhotoItem;
import hitcs.fghz.org.album.adapter.PhotoAdapter;


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

        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                System.out.println(position+ " " +id);
                Intent intent = new Intent(getActivity(), PhotoDetail.class);
                startActivity(intent);

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


