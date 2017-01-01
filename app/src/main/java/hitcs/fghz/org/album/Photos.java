package hitcs.fghz.org.album;
/**
 * 相片界面的fregment
 * 这个就是进入app之后的第一个界面
 * Created by me on 16-12-19.
 */
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import org.tensorflow.demo.TensorFlowImageClassifier;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// 导入相片的元素（一个照片）
import hitcs.fghz.org.album.entity.PhotoItem;
// 照片元素的适配器， 对于gridview需要适配器将数据传递给布局显示
import hitcs.fghz.org.album.adapter.PhotoAdapter;
import static hitcs.fghz.org.album.utils.ImagesScaner.getMediaImageInfo;

public class Photos extends Fragment {
    // 所有照片  或者  某个相册
    private String type;
    private List<PhotoItem> photoList = new ArrayList<PhotoItem>();
    // 声明一个gridview
    GridView gridView;
    private int start_index, end_index;
    private boolean isInit = false;
    private boolean scoll = false;


    public Photos() {


    }
    // 重写创建fregement方法
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fg_photos,container,false);
        gridView = (GridView) view.findViewById(R.id.gridView1);
        // 获得照片数据， 具体方法在下面
        initPhoto();
        // 获得gridview
        gridView = (GridView) view.findViewById(R.id.gridView1);
        // 讲相片元素与相片数组用适配器组合
        PhotoAdapter adapter = new PhotoAdapter(getActivity(), R.layout.photo_item, photoList);
        gridView.setAdapter(adapter);
        if (!scoll) {
            ;
        } else {
            ;
        }

        // 设定点击事件，当点击某一个相片，返回照片在list的位置
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                System.out.println(position+ " " +id);
//                // 进入查看相片细节的activity， 注意这个是activity不是fregment
                Intent intent = new Intent(getActivity(), PhotoDetailActivity.class);
                Log.d("Position", ""+position);
                intent.putExtra("position", position);
                intent.putExtra("url", photoList.get(position).getImageId());
                startActivity(intent);
            }
        });

        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
                    scoll = false;
                    Log.d("Scroll", "no");
                }else{
                    scoll = true;
                    Log.d("Scroll", "yes");
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        return view;
    }
    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        initPhoto();
    }
    // 初始化照片数组
    private void initPhoto() {
        final List<Map> mediaImageInfo;
        mediaImageInfo = getMediaImageInfo(getContext());
        PhotoItem photo;
        for (Map<String, String> map : mediaImageInfo) {
            String thum = map.get("_data");
            if (thum != null) {
                photo = new PhotoItem(thum);
                System.out.println(map.get("_data"));
                photoList.add(photo);
            }
        }
    }

}


