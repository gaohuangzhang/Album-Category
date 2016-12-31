package hitcs.fghz.org.album;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * 查看照片细节的activity
 * 点击照片后进入这个界面查看照片细节
 * Created by me on 16-12-20.
 */
import org.tensorflow.demo.TensorFlowImageClassifier;

import static hitcs.fghz.org.album.utils.ImagesScaner.*;
import hitcs.fghz.org.album.entity.PhotoItem;
import hitcs.fghz.org.album.view.MyHorizontalScrollView;
import hitcs.fghz.org.album.view.MyHorizontalScrollView.*;
import hitcs.fghz.org.album.adapter.HorizontalScrollViewAdapter;

public class PhotoDetailActivity extends Activity implements View.OnClickListener {

    // 初始化几个textview， 可以点击并且出发事件
    private TextView txt_back;
    private TextView txt_share;
    private TextView txt_love;
    private TextView txt_delete;

    // 自定义的布局， 实现下面缩略图，上面大图
    private MyHorizontalScrollView mHorizontalScrollView;
    // 适配器
    private HorizontalScrollViewAdapter mAdapter;
    private ImageView mImg;
    // 照片数组。照片在drawable文件夹中，名字为a.png ...
    private List<Map> mDatas ;

    int position_now = -1;
    String url = null;

    boolean init = false;

    TensorFlowImageClassifier classifier;
    PhotoDetailActivity() {
        this.classifier = null;
    }

    PhotoDetailActivity(TensorFlowImageClassifier classifier) {
        this.classifier = classifier;
    }

    // 重写创建活动的方法
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fg_detail);

        ActionBar actionBar = getActionBar();
        // 用于显示相应的属性
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        // 绑定textview按钮
        bindViews();

        initPhoto();


        // 下面设置下面缩略图上面大图。
        mDatas = getMediaImageInfo(this.getBaseContext());
        mImg = (ImageView) findViewById(R.id.id_content);

        mHorizontalScrollView = (MyHorizontalScrollView) findViewById(R.id.id_horizontalScrollView);
        mAdapter = new HorizontalScrollViewAdapter(this, mDatas);
        //添加滚动回调
        mHorizontalScrollView
                .setCurrentImageChangeListener(new CurrentImageChangeListener()
                {
                    @Override
                    public void onCurrentImgChanged(int position,
                                                    View viewIndicator)
                    {
                        if (!init) {
                            position = position_now;
                            init = true;
                        }
                        Log.d("PhotoDetail: ", "Image change to: " + position);
                        mImg.setImageURI(Uri.fromFile(new File((String)mDatas.get(position).get("_data"))));
                        viewIndicator.setBackgroundColor(Color.parseColor("#AA024DA4"));
//                        position_now = mHorizontalScrollView.get;
                    }
                });
        //添加点击回调
        mHorizontalScrollView.setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onClick(View view, int position)
            {
                mImg.setImageURI(Uri.fromFile(new File((String)mDatas.get(position).get("_data"))));
                view.setBackgroundColor(Color.parseColor("#AA024DA4"));
            }
        });
        //设置适配器
        mHorizontalScrollView.initDatas(mAdapter, position_now);
    }
    private void initPhoto() {
        Intent intent = getIntent();

        try {
            position_now = intent.getIntExtra("position", -1);
            url = intent.getStringExtra("url");
        } catch (Exception e) {
            Log.d("ERROR: ", "" + e);
        }
        Log.d("Test: ", "" + position_now + " " + url);
    }
    //UI组件初始化与事件绑定
    private void bindViews() {
        // 返回删除等按钮
        txt_back = (TextView) findViewById(R.id.back);
        txt_share = (TextView) findViewById(R.id.share);
        txt_love = (TextView) findViewById(R.id.love);
        txt_delete = (TextView) findViewById(R.id.delete);
        // 设置监听
        txt_back.setOnClickListener(this);
        txt_share.setOnClickListener(this);
        txt_love.setOnClickListener(this);
        txt_delete.setOnClickListener(this);
    }
    // 恢复点击状态为未点击状态
    private void setSelect() {
        txt_back.setSelected(false);
        txt_share.setSelected(false);
        txt_love.setSelected(false);
        txt_delete.setSelected(false);
    }
    /**
     * 生成动作栏上的菜单项目
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_for_detail, menu);
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
                this.finish();
                break;
            case R.id.action_about:
                Intent intent = new Intent(this, PhotoInfoActivity.class);
//                Log.d("Position", ""+position);
                intent.putExtra("position", position_now);
                intent.putExtra("url", (String)mDatas.get(position_now).get("_data"));
                startActivity(intent);
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    // 下面按钮（返回删除等）的点击动作
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // 返回
            case R.id.back:
                setSelect();
                txt_back.setSelected(true);
                System.out.println("1");
                PhotoDetailActivity.this.finish(); // 结束当前的activity， 返回上一个界面
                break;
            case R.id.share: // 分享
                setSelect();
                txt_share.setSelected(true);
                System.out.println("2");

                break;
            case R.id.love: // 喜爱
                setSelect();
                txt_love.setSelected(true);
                System.out.println("3");

                break;
            case R.id.delete: // 删除
                setSelect();
                txt_delete.setSelected(true);
                System.out.println("4");

                break;
        }
    }
}
