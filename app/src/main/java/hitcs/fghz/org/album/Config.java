package hitcs.fghz.org.album;

import org.tensorflow.demo.TensorFlowImageClassifier;

import hitcs.fghz.org.album.dao.MyDatabaseHelper;


/**
 * Created by me on 16-12-31.
 */

public class Config {
    // for tensorflow
    public static final int NUM_CLASSES = 14;
    public static final int INPUT_SIZE = 299;
    public static final int IMAGE_MEAN = 128;
    public static final float IMAGE_STD = 128;
    public static final String INPUT_NAME = "Mul:0";
    public static final String OUTPUT_NAME = "final_result:0";
    public static final String MODEL_FILE = "file:///android_asset/stripped_graph.pb";
    public static final String LABEL_FILE = "file:///android_asset/retrained_labels.txt";
    // once the 'tfpb' had been load, the appliction needn't to reload it again when another thread need tf
    public static TensorFlowImageClassifier classifier;
    // location to save image
    public static String location = "/sdcard/TfAlbum/";
    // database
    public static MyDatabaseHelper dbHelper;
    public static int dbversion = 5;

    public static boolean workdone = false;
}
