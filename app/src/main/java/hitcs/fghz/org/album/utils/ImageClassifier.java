package hitcs.fghz.org.album.utils;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import org.tensorflow.demo.Classifier;
import org.tensorflow.demo.TensorFlowImageClassifier;

import java.io.IOException;
import java.util.List;

/**
 * Created by me on 16-12-29.
 */

public class ImageClassifier {

    // These are the settings for the original v1 Inception model. If you want to
    // use a model that's been produced from the TensorFlow for Poets codelab,
    // you'll need to set IMAGE_SIZE = 299, IMAGE_MEAN = 128, IMAGE_STD = 128,
    // INPUT_NAME = "Mul:0", and OUTPUT_NAME = "final_result:0".
    // You'll also need to update the MODEL_FILE and LABEL_FILE paths to point to
    // the ones you produced.

    private static final int NUM_CLASSES = 14;
    private static final int INPUT_SIZE = 299;
    private static final int IMAGE_MEAN = 128;
    private static final float IMAGE_STD = 128;
    private static final String INPUT_NAME = "Mul:0";
    private static final String OUTPUT_NAME = "final_result:0";
    private static final String MODEL_FILE = "file:///android_asset/stripped_graph.pb";
    private static final String LABEL_FILE = "file:///android_asset/retrained_labels.txt";

    private TensorFlowImageClassifier classifier;

    public boolean initTensorflow(AssetManager assetManager) {

        classifier = new TensorFlowImageClassifier();
        try {
            classifier.initializeTensorFlow(
                    assetManager, MODEL_FILE, LABEL_FILE, NUM_CLASSES, INPUT_SIZE, IMAGE_MEAN, IMAGE_STD,
                    INPUT_NAME, OUTPUT_NAME);
            return true;
        } catch (final IOException e) {
            ;
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public  List<Classifier.Recognition> recognizeImage(Bitmap bitmap) {
        return classifier.recognizeImage(bitmap);
    }
}
