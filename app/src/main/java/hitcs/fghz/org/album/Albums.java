package hitcs.fghz.org.album;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by me on 16-12-19.
 */

public class Albums extends Fragment {
    private String content;
    private FragmentManager manager;
    private FragmentTransaction ft;

    public Albums() {
    }
    private String[] data = { "ALBUMS", "Banana", "Orange", "Watermelon",
            "Pear", "Grape", "Pineapple", "Strawberry", "Cherry", "Mango" };
    private List<AlbumItem> albumList = new ArrayList<AlbumItem>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_albums,container,false);
        initAlbums();
        GridView listView = (GridView) view.findViewById(R.id.album_list);
        manager = getFragmentManager();
        AlbumAdapter adapter = new AlbumAdapter(getActivity(), R.layout.album_item, albumList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                System.out.println(position+ " " +id);
                Photos myJDEditFragment = new Photos("qq");
                ft = manager.beginTransaction();
                ft.add(R.id.ly_content , myJDEditFragment);
                ft.setTransition(FragmentTransaction. TRANSIT_FRAGMENT_OPEN);
                ft.addToBackStack( null);
                ft.commit();
            }
        });
        return view;
    }


    private void initAlbums() {
        AlbumItem album;
        for (String s: data) {
             album = new AlbumItem(s, R.drawable.girl);
            albumList.add(album);
        }
        album = new AlbumItem("Add", R.drawable.add);
        albumList.add(album);

    }
}
class AlbumItem {
    private String name;
    private int imageId;
    public AlbumItem(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;

    }
    public String getName() {
        return name;
    }
    public int getImageId() {
        return imageId;
    }
}
class AlbumAdapter extends ArrayAdapter<AlbumItem> {
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