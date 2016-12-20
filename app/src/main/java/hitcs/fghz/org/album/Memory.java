package hitcs.fghz.org.album;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jay on 2015/8/28 0028.
 */
public class Memory extends Fragment {

    private String content;
    public Memory(String content) {
        this.content = content;
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

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,numbers);
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
    private void initMemory() {
        MemoryItem memory;
        for (int i = 0; i != 20; ++i) {
            memory = new MemoryItem(R.drawable.girl);
            memoryList.add(memory);
        }
    }
}
class MemoryItem {
    private int imageId;
    public MemoryItem(int imageId) {
        this.imageId = imageId;
    }
    public int getImageId() {
        return imageId;
    }
}
class MemoryAdapter extends ArrayAdapter<MemoryItem> {
    private int resourceId;
    public MemoryAdapter(Context context, int textViewResourceId,
                        List<MemoryItem> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MemoryItem photo= getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        ImageView memoryImage = (ImageView) view.findViewById(R.id.memory_photo);
        memoryImage.setImageResource(photo.getImageId());
        return view;
    }
}