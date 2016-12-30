package hitcs.fghz.org.album;
/**
 * 相片界面的fregment
 * 这个就是进入app之后的第一个界面
 * Created by me on 16-12-19.
 */
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 导入相片的元素（一个照片）
import hitcs.fghz.org.album.entity.PhotoItem;
// 照片元素的适配器， 对于gridview需要适配器将数据传递给布局显示
import hitcs.fghz.org.album.adapter.PhotoAdapter;


public class Photos extends Fragment {

    // 所有照片  或者  某个相册
    private String type;
    private List<PhotoItem> photoList = new ArrayList<PhotoItem>();

    // 声明一个gridview
    GridView gridView;
    public Photos(String type) {

        this.type = type;
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
                Intent intent = new Intent(getActivity(), PhotoDetailActivity.class);
                startActivity(intent);

            }
        });
        return view;

    }
    // 初始化照片数组
    private void initPhoto() {
        final List<Map> mediaImageInfo;
        mediaImageInfo = getMediaImageInfo(getContext());

        PhotoItem photo;

        for (Map<String, String> map : mediaImageInfo) {
            photo = new PhotoItem(map.get("_data"));
            System.out.println(map.get("_data"));
            photoList.add(photo);
        }

        // 用drawable文件夹中的search.p
        // 在数组中放100张上述图片
        // 后续实现应该放入具体的照片
//        for (int i = 0; i != 100; ++i) {
//            photo = new PhotoItem(R.drawable.search);
//            photoList.add(photo);
//        }

    }
    /**
     * 异步扫描SD卡多媒体文件,不阻塞当前线程
     *
     * @param ctx
     * @param file 扫描的指定文件
     */
    public static void scanMediaAsync(Context ctx, File file) {
        Intent scanner = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanner.setData(Uri.fromFile(file));
        ctx.sendBroadcast(scanner);
    }
    public static List<Map> getMediaImageInfo(Context ctx) {
        //可以手动指定获取的列
        String[] columns = new String[]{
                MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID, MediaStore.Images.Media.TITLE, MediaStore.Images.Media.DISPLAY_NAME
                , MediaStore.Images.Media.LATITUDE, MediaStore.Images.Media.LONGITUDE, MediaStore.Images.Media.DATE_TAKEN};
        ContentResolver contentResolver = ctx.getContentResolver();
       Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;//外部存储的SD卡的访问Uri
//        MediaStore.Images.Media.INTERNAL_CONTENT_URI
        Cursor cursor = contentResolver.query(uri, null, null, null, null);//获取全部列
//        Cursor cursor = contentResolver.query(uri, columns, null, null, null);//获取指定列
        if (cursor != null) {
            Map<String, String> item = null;
            List<Map> result = new ArrayList<>();
            while (cursor.moveToNext()) {
                String[] columnNames = cursor.getColumnNames();
                item = new HashMap<>();
                for (String colnmnName : columnNames) {
                    int columnIndex = cursor.getColumnIndex(colnmnName);
                    String columnValue = cursor.getString(columnIndex);
                    item.put(colnmnName, columnValue);
                }
                result.add(item);
                Log.d("debug", "getMediaImageInfo() item=" + item);
            }
            Log.d("debug", "getMediaImageInfo() size=" + result.size() + ", result=" + result);
            return result;
        }

        return null;
    }
}


