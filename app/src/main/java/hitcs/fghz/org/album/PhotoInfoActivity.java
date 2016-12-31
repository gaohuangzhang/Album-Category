package hitcs.fghz.org.album;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.tensorflow.demo.TensorFlowImageClassifier;

import java.io.File;
import java.io.IOException;

/**
 * Created by me on 16-12-31.
 */

public class PhotoInfoActivity extends Activity {

    String url;
    int position_now;

    PhotoInfoActivity() {

    }

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
        setContentView(R.layout.photo_info);
        // get intent information
        getMessage();

        initTensorflow();

        ImageView iv = (ImageView) findViewById(R.id.photo_target);
        iv.setImageURI(Uri.fromFile(new File(url)));
//        TextView tv = (TextView) findViewById()
    }
    protected void getMessage() {
        Intent intent = getIntent();

        try {
            position_now = intent.getIntExtra("position", -1);
            url = intent.getStringExtra("url");
        } catch (Exception e) {
            Log.d("ERROR: ", "" + e);
        }
        Log.d("Info: ", "" + position_now + " " + url);
    }
    private void initTensorflow() {
        if (Config.classifier == null) {
            Config.classifier = new TensorFlowImageClassifier();
            try {
                Config.classifier.initializeTensorFlow(
                        getAssets(), Config.MODEL_FILE, Config.LABEL_FILE, Config.NUM_CLASSES, Config.INPUT_SIZE, Config.IMAGE_MEAN, Config.IMAGE_STD,
                        Config.INPUT_NAME, Config.OUTPUT_NAME);
            } catch (final IOException e) {
                ;
            }
        }
    }
}
