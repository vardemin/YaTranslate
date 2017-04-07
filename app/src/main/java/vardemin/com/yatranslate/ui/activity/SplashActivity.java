package vardemin.com.yatranslate.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import vardemin.com.yatranslate.R;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 1000;

    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        handler = new Handler();
        startDelayed();
    }
    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopCurrent();
    }

    /**
     * Stop countdown
     */
    private void stopCurrent() {
        if (runnable!=null) {
            handler.removeCallbacks(runnable);
            runnable=null;
        }
    }

    /**
     * Start countdown
     */
    private void startDelayed() {
        stopCurrent();
        runnable = () -> {
            Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        };
        handler.postDelayed(runnable, SPLASH_DISPLAY_LENGTH);
    }

}
