package hitcs.fghz.org.album;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_memory,container,false);



        GridView listView = (GridView) view.findViewById(R.id.memory_list);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,data);
        listView.setAdapter(arrayAdapter);
        return view;
    }
}
