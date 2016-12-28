package hitcs.fghz.org.album;
/**
 * 相片界面的fregment
 * 这个就是进入app之后的第一个界面
 * Created by me on 16-12-19.
 */
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

// 导入相片的元素（一个照片）
import hitcs.fghz.org.album.entity.PhotoItem;
// 照片元素的适配器， 对于gridview需要适配器将数据传递给布局显示
import hitcs.fghz.org.album.adapter.PhotoAdapter;


public class Photos extends Fragment {

    private String content;
    private List<PhotoItem> photoList = new ArrayList<PhotoItem>();

    // 声明一个gridview
    GridView gridView;
    // 一个构造方法 现在没啥用
    public Photos(String content) {
        this.content = content;
    }

    // 重写创建fregement方法
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fg_photos,container,false);
        // 获得照片数据， 具体方法在下面
        initPhoto();
        // 获得gridview
        gridView = (GridView) view.findViewById(R.id.gridView1);
        // 讲相片元素与相片数组用适配器组合
        PhotoAdapter adapter = new PhotoAdapter(getActivity(), R.layout.photo_item, photoList);
        gridView.setAdapter(adapter);
        // 设定点击事件，当点击某一个相片，返回照片在list的位置
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                System.out.println(position+ " " +id);
                // 进入查看相片细节的activity， 注意这个是activity不是fregment
                Intent intent = new Intent(getActivity(), PhotoDetail.class);
                startActivity(intent);

            }
        });
        return view;

    }
    // 初始化照片数组
    private void initPhoto() {
        PhotoItem photo;
        // 用drawable文件夹中的search.png
        // 在数组中放100张上述图片
        // 后续实现应该放入具体的照片
        /**
         * Environment.getExternalStorageState()获取路径是否成功
         */

        /*if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String path=Environment.getExternalStorageDirectory().getPath();
        }
        String path=Environment.getExternalStorageDirectory()+"autophoto";
        File file=new File(path);
        if (!file.exists()) {
            file.mkdir();
        }*/
        for (int i = 0; i != 100; ++i) {
            photo = new PhotoItem(R.drawable.search);
            photoList.add(photo);
        }
    }
}


