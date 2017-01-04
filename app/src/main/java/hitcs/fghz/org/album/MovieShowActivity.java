package hitcs.fghz.org.album;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import hitcs.fghz.org.album.entity.PhotoItem;

import static hitcs.fghz.org.album.utils.ImagesScaner.getAlbumPhotos;
import static hitcs.fghz.org.album.utils.ImagesScaner.getMediaImageInfo;

/**
 * Created by me on 17-1-4.
 */

public class MovieShowActivity extends AppCompatActivity {
    private String type;
    private List<Map> result;
    private int count = 0;
    private ImageView iv;
    private List<PhotoItem> photoList = new ArrayList<PhotoItem>();
    private Handler myHandler = new Handler()
    {
        @Override
        //重写handleMessage方法,根据msg中what的值判断是否执行后续操作
        public void handleMessage(Message msg) {
            switch (msg.what)
            {
                case 0x24:
                    iv.setImageURI(Uri.fromFile(new File((String) photoList.get(count % photoList.size()).getImageId())));
                    break;

                case 0x123:
                    break;
            }
        }
    };
    private TimerTask  task= new TimerTask() {
        @Override
        public void run() {
            count++;
            myHandler.sendEmptyMessage(0x24);
            Log.d("MainActivity",count + "");
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ui界面最上边的动作栏
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        setContentView(R.layout.movie_show);

        getMessage();
        initPhoto();
        Timer timer = new Timer();
        timer.schedule(task, 100, 2000);
        iv = (ImageView) findViewById(R.id.movie_image);
        iv.setImageURI(Uri.fromFile(new File((String) photoList.get(0).getImageId())));

    }
    @Override
    protected void onResume() {
        /**
         * 设置为横屏
         */
        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();
    }

    private void playMusic() {

    }
    protected void getMessage() {
        Intent intent = getIntent();
        try {

            type = intent.getStringExtra("type");
        } catch (Exception e) {
            Log.d("ERROR: ", "" + e);
        }
        Log.d("Info: ", "" + " " + type);
    }

    // 初始化照片数组
    private void initPhoto() {
        PhotoItem photo;
        if (type == null) {
            final List<Map> mediaImageInfo;
            mediaImageInfo = getMediaImageInfo(MovieShowActivity.this);
            for (Map<String, String> map : mediaImageInfo) {
                // in this map, the key of url is _data
                String url = map.get("_data");
                if (url != null) {
                    photo = new PhotoItem(url);
                    photoList.add(photo);
                }
            }
        } else {
            this.result = getAlbumPhotos(MovieShowActivity.this, this.type);
            for (Map<String, String> map : result) {
                // in this map, the key of url is _data
                String url = map.get("url");
                if (url != null) {
                    photo = new PhotoItem(url);
                    photoList.add(photo);
                }
            }
        }
    }
}
