package hitcs.fghz.org.album;


import java.util.List;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
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

public class PhotoDetail extends Activity implements View.OnClickListener {

    private TextView txt_back;
    private TextView txt_share;
    private TextView txt_love;
    private TextView txt_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("HERE");
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.fg_detail);
        bindViews();
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
class HorizontalScrollViewAdapter
{

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Integer> mDatas;

    public HorizontalScrollViewAdapter(Context context, List<Integer> mDatas)
    {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
    }

    public int getCount()
    {
        return mDatas.size();
    }

    public Object getItem(int position)
    {
        return mDatas.get(position);
    }

    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder = null;
        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(
                    R.layout.fg_detail, parent, false);
            viewHolder.mImg = (ImageView) convertView
                    .findViewById(R.id.id_content);
//            viewHolder.mText = (TextView) convertView
//                    .findViewById(R.id.);

            convertView.setTag(viewHolder);
        } else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mImg.setImageResource(mDatas.get(position));
        viewHolder.mText.setText("some info ");

        return convertView;
    }

    private class ViewHolder
    {
        ImageView mImg;
        TextView mText;
    }

}