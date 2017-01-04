package hitcs.fghz.org.album;

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
import java.util.Map;

import hitcs.fghz.org.album.entity.AlbumItem;
import hitcs.fghz.org.album.entity.MemoryItem;
import hitcs.fghz.org.album.adapter.MemoryAdapter;

import static hitcs.fghz.org.album.utils.ImagesScaner.getAlbumInfo;

/**
 * 回忆 栏的fregment的具体定义
 */

public class Memory extends Fragment {
    // 同photos.java

    private List<Map<String, String>> result;
    public Memory() {

    }
    private String[] data = { "Apple", "Banana", "Orange", "Watermelon",
            "Pear", "Grape", "Pineapple", "Strawberry", "Cherry", "Mango" };
    private List<MemoryItem> memoryList = new ArrayList<MemoryItem>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_memory,container,false);
        GridView gridView = (GridView) view.findViewById(R.id.memory_list);
        initMemory();

        MemoryAdapter adapter = new MemoryAdapter(getActivity(), R.layout.memory_item, memoryList);

        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                System.out.println(position+ " " +id);
                Intent intent = new Intent(getActivity(), PhotoDetailActivity.class);
                startActivity(intent);
            }
        });
        return view;

    }
    private void initMemory() {
        MemoryItem memory;
        result = getAlbumInfo(getContext());
//        for (int i = 0; i != 20; ++i) {
//            memory = new MemoryItem(R.drawable.flower);
//            memoryList.add(memory);
//        }
        for (Map<String, String> s: result) {
            memory = new MemoryItem(s.get("show_image"));
//            album = new AlbumItem(s.get("album_name"), s.get("show_image"));
            memoryList.add(memory);
        }
    }
}

