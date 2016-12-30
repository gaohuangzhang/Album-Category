package hitcs.fghz.org.album;

/**
 * Created by me on 16-12-28.
 */



import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.tensorflow.demo.Classifier;


public class CameraActivity extends Activity {
    /** Called when the activity is first created. */

    private static final int INPUT_SIZE = 299;
    private ImageClassifier imageClassifier;
    private TextView mResultText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);


        // test1 load tensorflow
        imageClassifier = new ImageClassifier();
        imageClassifier.initTensorflow(getAssets());

        // test1 end


        Button button = (Button)findViewById(R.id.b01);
        button.setText("选择图片");
        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
		        /* 开启Pictures画面Type设定为image */
                intent.setType("image/*");
		        /* 使用Intent.ACTION_GET_CONTENT这个Action */
                intent.setAction(Intent.ACTION_GET_CONTENT);
		        /* 取得相片后返回本画面 */
                startActivityForResult(intent, 1);
            }

        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Log.e("uri", uri.toString());
            ContentResolver cr = this.getContentResolver();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                dealPics(bitmap);
            } catch (FileNotFoundException e) {
                Log.e("Exception", e.getMessage(),e);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void dealPics(Bitmap bitmap) {
        ImageView imageView = (ImageView) findViewById(R.id.iv01);
				/* 将Bitmap设定到ImageView */

        int width = bitmap.getWidth();

        int height = bitmap.getHeight();
        System.out.println(width + "&&" + height);
        float scaleWidth = ((float)INPUT_SIZE) / width;

        float scaleHeight = ((float) INPUT_SIZE) / height;
        Matrix matrix = new Matrix();

        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbm = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

        imageView.setImageBitmap(newbm);
        final List<Classifier.Recognition> results = imageClassifier.recognizeImage(newbm);
        for (final Classifier.Recognition result : results) {
            System.out.println("Result: " + result.getTitle());
        }
        mResultText = (TextView)findViewById(R.id.t01);
        mResultText.setText("Detected = " + results.get(0).getTitle());

        System.out.println(newbm.getWidth() + "&&" + newbm.getHeight());


    }
}