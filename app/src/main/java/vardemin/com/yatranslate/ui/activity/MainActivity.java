package vardemin.com.yatranslate.ui.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ToxicBakery.viewpager.transforms.RotateUpTransformer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import vardemin.com.yatranslate.R;
import vardemin.com.yatranslate.events.NavigateMsg;
import vardemin.com.yatranslate.ui.adapter.MainPagerAdapter;

public class MainActivity extends AppCompatActivity {
    /**
     * Tab position field
     */
    public static String POSITION = "POSITION";

    /**
     * AppCompat Tab layout
     */
    TabLayout tabLayout;
    /**
     * View Pager
     */
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.viewpager_main);
        viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(),
                MainActivity.this));

        tabLayout = (TabLayout) findViewById(R.id.tabs_main);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setPageTransformer(true, new RotateUpTransformer());
    }

    @Override
    protected void onStart(){
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNavigateMsg(NavigateMsg msg) {
        viewPager.setCurrentItem(msg.getTabPosition(),true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION, tabLayout.getSelectedTabPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        viewPager.setCurrentItem(savedInstanceState.getInt(POSITION));
    }

    @Override
    public void onBackPressed(){
        if(viewPager.getCurrentItem()==0) {
            super.onBackPressed();
        }
        else {
            viewPager.setCurrentItem(0,true);
        }
    }
}
