package hitcs.fghz.org.album;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * Created by me on 16-12-20.
 */

import hitcs.fghz.org.album.MyHorizontalScrollView.*;
import hitcs.fghz.org.album.adapter.HorizontalScrollViewAdapter;

public class PhotoDetail extends Activity implements View.OnClickListener {

    private TextView txt_back;
    private TextView txt_share;
    private TextView txt_love;
    private TextView txt_delete;

    private MyHorizontalScrollView mHorizontalScrollView;
    private HorizontalScrollViewAdapter mAdapter;
    private ImageView mImg;
    private List<Integer> mDatas = new ArrayList<Integer>(Arrays.asList(
            R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d,
            R.drawable.e, R.drawable.f, R.drawable.g, R.drawable.h,
            R.drawable.l));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("HERE");


        setContentView(R.layout.fg_detail);

        bindViews();

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
        txt_back = (TextView) findViewById(R.id.back);
        txt_share = (TextView) findViewById(R.id.share);
        txt_love = (TextView) findViewById(R.id.love);
        txt_delete = (TextView) findViewById(R.id.delete);

        txt_back.setOnClickListener(this);
        txt_share.setOnClickListener(this);
        txt_love.setOnClickListener(this);
        txt_delete.setOnClickListener(this);
    }
    private void setSelect() {
        txt_back.setSelected(false);
        txt_share.setSelected(false);
        txt_love.setSelected(false);
        txt_delete.setSelected(false);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                setSelect();
                txt_back.setSelected(true);
                System.out.println("1");
                PhotoDetail.this.finish();
                break;
            case R.id.share:
                setSelect();
                txt_share.setSelected(true);
                System.out.println("2");

                break;
            case R.id.love:
                setSelect();
                txt_love.setSelected(true);
                System.out.println("3");

                break;
            case R.id.delete:
                setSelect();
                txt_delete.setSelected(true);
                System.out.println("4");

                break;
        }
    }
}
