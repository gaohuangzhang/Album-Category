package hitcs.fghz.org.album;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 查看照片细节的activity
 * 点击照片后进入这个界面查看照片细节
 * Created by me on 16-12-20.
 */

import hitcs.fghz.org.album.MyHorizontalScrollView.*;
import hitcs.fghz.org.album.adapter.HorizontalScrollViewAdapter;

public class PhotoDetail extends Activity implements View.OnClickListener {

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
    private List<Integer> mDatas = new ArrayList<Integer>(Arrays.asList(
            R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d,
            R.drawable.e, R.drawable.f, R.drawable.g, R.drawable.h,
            R.drawable.l));

    // 重写创建活动的方法
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fg_detail);
        // 绑定textview按钮
        bindViews();
        // 下面设置下面缩略图上面大图。
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
                        mImg.setImageResource(mDatas.get(position));
                        viewIndicator.setBackgroundColor(Color.parseColor("#AA024DA4"));
                    }
                });
        //添加点击回调
        mHorizontalScrollView.setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onClick(View view, int position)
            {
                mImg.setImageResource(mDatas.get(position));
                view.setBackgroundColor(Color.parseColor("#AA024DA4"));
            }
        });
        //设置适配器
        mHorizontalScrollView.initDatas(mAdapter);
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
    // 下面按钮（返回删除等）的点击动作
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // 返回
            case R.id.back:
                setSelect();
                txt_back.setSelected(true);
                System.out.println("1");
                PhotoDetail.this.finish(); // 结束当前的activity， 返回上一个界面
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
