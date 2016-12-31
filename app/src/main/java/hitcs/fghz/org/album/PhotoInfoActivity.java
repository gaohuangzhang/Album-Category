package hitcs.fghz.org.album;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.tensorflow.demo.Classifier;
import org.tensorflow.demo.TensorFlowImageClassifier;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static android.media.ThumbnailUtils.extractThumbnail;

/**
 * Created by me on 16-12-31.
 */

public class PhotoInfoActivity extends Activity {

    private String url;
    private int position_now;

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

        dealImage();

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
    private void dealImage() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_4444;
        final Bitmap bitmap = BitmapFactory.decodeFile(url, options);
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void run() {
                do_tensorflow(bitmap);
            }
        }).start();

    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void do_tensorflow(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = ((float) Config.INPUT_SIZE) / width;
        float scaleHeight = ((float) Config.INPUT_SIZE) / height;
        Matrix matrix = new Matrix();

        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbm = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);


        final List<Classifier.Recognition> results = Config.classifier.recognizeImage(newbm);
        Log.d("Result", String.valueOf(results));
        for (final Classifier.Recognition result : results) {
            System.out.println("Result: " + result.getTitle());
        }
    }
}
