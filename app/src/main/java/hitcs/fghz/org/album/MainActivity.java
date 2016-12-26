package hitcs.fghz.org.album;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

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
                System.out.println("Camera");
                break;
            // 语音

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
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
