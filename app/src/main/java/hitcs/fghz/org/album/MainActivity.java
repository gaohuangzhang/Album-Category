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
 * Created by Coder-pig on 2015/8/28 0028.
 */
public class MainActivity extends Activity implements View.OnClickListener{

    //UI Object
    private TextView txt_topbar;
    private TextView txt_channel;
    private TextView txt_message;
    private TextView txt_better;
    private TextView txt_setting;
    private FrameLayout ly_content;

    //Fragment Object
    private Memory memory;
    private Photos photos;
    private Albums albums;
    private FragmentManager fManager;

    private String last_click = "PHOTO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);


        setContentView(R.layout.activity_main);
        fManager = getFragmentManager();
        bindViews();
        txt_channel.performClick();   //模拟一次点击，既进去后选择第一项
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case android.R.id.home:
                System.out.println("title");
                getFragmentManager().popBackStack();
                break;
            case R.id.action_search:
               System.out.println("search");
                break;
            case R.id.action_camera:
                System.out.println("Camera");

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //UI组件初始化与事件绑定
    private void bindViews() {
        //txt_topbar = (TextView) findViewById(R.id.txt_topbar);
        txt_channel = (TextView) findViewById(R.id.all_photos);

        txt_better = (TextView) findViewById(R.id.memory);
        txt_setting = (TextView) findViewById(R.id.all_albums);
        ly_content = (FrameLayout) findViewById(R.id.ly_content);

        txt_channel.setOnClickListener(this);

        txt_better.setOnClickListener(this);
        txt_setting.setOnClickListener(this);
    }

    //重置所有文本的选中状态
    private void setSelected(){
        txt_channel.setSelected(false);
        txt_better.setSelected(false);
        txt_setting.setSelected(false);
    }

    //隐藏所有Fragment
    private void hideAllFragment(FragmentTransaction fragmentTransaction){
        if(photos != null)fragmentTransaction.hide(photos);
        if(memory != null)fragmentTransaction.hide(memory);
        if(albums != null)fragmentTransaction.hide(albums);
    }


    @Override
    public void onClick(View v) {

        FragmentTransaction fTransaction = fManager.beginTransaction();
        hideAllFragment(fTransaction);
        switch (v.getId()){
            case R.id.all_photos:
                setSelected();
                txt_channel.setSelected(true);
                getFragmentManager().popBackStack();
                if(photos == null){
                    photos = new Photos("All Photos Here");
                    fTransaction.add(R.id.ly_content,photos);
                }else{
                    fTransaction.show(photos);
                }

                break;
            case R.id.memory:
                setSelected();
                txt_better.setSelected(true);
                getFragmentManager().popBackStack();
                if(memory == null){
                    memory = new Memory("Take A Photo");
                    fTransaction.add(R.id.ly_content, memory);
                }else{
                    fTransaction.show(memory);
                }
                break;
            case R.id.all_albums:
                setSelected();
                txt_setting.setSelected(true);
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
