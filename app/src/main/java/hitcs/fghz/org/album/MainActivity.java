package hitcs.fghz.org.album;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * 主活动
 * 进入相册的界面
 * Created by dongchang @ 20161218
 */

/**
 * 全部布局介绍：
 *
 * activity_main.xml: 主布局，进入app后的第一个布局，除了动作栏和选项卡之外，中间是一个fregment
 *
 * album_item: 相册的单个元素的定义
 *
 * fg_ablums: 相册的fregment定义
 *
 * fg_detail: 照片细节，即查看一张具体的照片
 *
 * fg_memory： 回忆的fregment定义
 *
 * fg_photos：显示全部照片的fregment
 *
 * gallery_item :照片细节中的下面缩略图单个的定义
 *
 * memory_item: 回忆的单个元素的定义
 *
 * photo_item: 照片fregment中的单个元素的定义
 */

/**
 * res 文件夹
 * drawable：
 * menu：上面动作栏的显示内容，如搜索等
 * mipmap：图标资源，如返回删除分享图标
 *
 */
public class MainActivity extends Activity implements View.OnClickListener{

    //UI Object
    private TextView txt_photos;
    private TextView txt_memory;
    private TextView txt_albums;

    //Fragment Object
    private Memory memory;
    private Photos photos;
    private Albums albums;
    private FragmentManager fManager;

    // camera
    private Uri contentUri;
    private File newFile;



    private String last_click = "PHOTO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ui界面最上边的动作栏
        ActionBar actionBar = getActionBar();
        // 用于显示相应的属性
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        // 生成布局
        setContentView(R.layout.activity_main);
        // 申请相机权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        fManager = getFragmentManager();
        // 绑定事件
        bindViews();
        //模拟一次点击，既进去后选择第一项
        txt_photos.performClick();
    }

    /**
     * 生成动作栏上的菜单项目
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 监听菜单栏目的动作，当按下不同的按钮执行相应的动作
     *
     * @param item
     * @return
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case android.R.id.home:
                // 返回
                System.out.println("title");
                getFragmentManager().popBackStack();
                break;
            case R.id.action_search:
                // 搜索
               System.out.println("search");
                break;
            case R.id.action_camera:
                // 拍照
                startCamera();
                break;
                // 语音

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 打开相机获取图片
     */
    private void startCamera() {
        File imagePath = new File(Environment.getExternalStorageDirectory(), "images");
        if (!imagePath.exists()) imagePath.mkdirs();
        newFile = new File(imagePath, "default_image.jpg");

        //第二参数是在manifest.xml定义 provider的authorities属性
        contentUri = FileProvider.getUriForFile(this, "hitcs.fghz.org.album.fileprovider", newFile);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //兼容版本处理，因为 intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION) 只在5.0以上的版本有效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ClipData clip =
                    ClipData.newUri(getContentResolver(), "A photo", contentUri);
            intent.setClipData(clip);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            List<ResolveInfo> resInfoList =
                    getPackageManager()
                            .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                grantUriPermission(packageName, contentUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        startActivityForResult(intent, 1000);
    }
    // 接受拍照的结果

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            ContentResolver contentProvider = getContentResolver();
            ParcelFileDescriptor mInputPFD;
            try {
                //获取contentProvider图片
                mInputPFD = contentProvider.openFileDescriptor(contentUri, "r");
                FileDescriptor fileDescriptor = mInputPFD.getFileDescriptor();
                System.out.println("here");
               // mImageView.setImageBitmap(BitmapFactory.decodeFileDescriptor(fileDescriptor));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }


    //UI组件初始化与事件绑定
    private void bindViews() {
        // 定位textview
        txt_photos = (TextView) findViewById(R.id.all_photos);
        txt_memory = (TextView) findViewById(R.id.memory);
        txt_albums = (TextView) findViewById(R.id.all_albums);
        // 对其设置监听动作
        txt_photos.setOnClickListener(this);
        txt_memory.setOnClickListener(this);
        txt_albums.setOnClickListener(this);
    }

    //重置所有文本的选中状态为未点击状态
    private void setSelected(){
        txt_photos.setSelected(false);
        txt_memory.setSelected(false);
        txt_albums.setSelected(false);
    }

    //隐藏所有Fragment
    private void hideAllFragment(FragmentTransaction fragmentTransaction){
        if(photos != null)fragmentTransaction.hide(photos);
        if(memory != null)fragmentTransaction.hide(memory);
        if(albums != null)fragmentTransaction.hide(albums);
    }

    /**
     * 监听textview的按钮事件
     *
     * @param v
     */

    @Override
    public void onClick(View v) {

        FragmentTransaction fTransaction = fManager.beginTransaction();
        hideAllFragment(fTransaction);
        switch (v.getId()){
            // 照片
            case R.id.all_photos:
                setSelected();
                txt_photos.setSelected(true);
                // 暂时使用弹出堆栈，以避免从相簿进入相册无法返回
                // 可以使用其他方法，这个方法不好，下面相同
                getFragmentManager().popBackStack();
                if(photos == null){
                    photos = new Photos("All Photos Here");
                    fTransaction.add(R.id.ly_content,photos);
                }else{
                    fTransaction.show(photos);
                }

                break;
            // 回忆
            case R.id.memory:
                setSelected();
                txt_memory.setSelected(true);
                getFragmentManager().popBackStack();
                if(memory == null){
                    memory = new Memory("Take A Photo");
                    fTransaction.add(R.id.ly_content, memory);
                }else{
                    fTransaction.show(memory);
                }
                break;
            // 相册
            case R.id.all_albums:
                setSelected();
                txt_albums.setSelected(true);
                getFragmentManager().popBackStack();
                if(albums == null){
                    albums = new Albums();
                    fTransaction.add(R.id.ly_content,albums);
                }else{
                    fTransaction.show(albums);
                }
                break;
        }
        fTransaction.commit();
    }
}
