package hitcs.fghz.org.album;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

/**
 * i plan use this activity as the first activity
 * when open the app, you can see this page
 * and the app can deal somethings at background
 * Created by me on 17-1-1.
 */

public class WelcomActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }
}
