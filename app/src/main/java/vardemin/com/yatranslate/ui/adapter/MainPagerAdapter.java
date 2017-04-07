package vardemin.com.yatranslate.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import vardemin.com.yatranslate.R;
import vardemin.com.yatranslate.ui.fragment.FavoriteFragment;
import vardemin.com.yatranslate.ui.fragment.HistoryFragment;
import vardemin.com.yatranslate.ui.fragment.TranslateFragment;

/**
 * Main Fragment Pager Adapter
 */
public class MainPagerAdapter extends FragmentPagerAdapter {

    private static int NUM_ITEMS = 3;

    private final int[] titles = {R.string.tab_tr, R.string.tab_hs, R.string.tab_fv};
    private final int[] icons = {R.drawable.ic_action_translate, R.drawable.ic_action_history_false, R.drawable.ic_action_history_true};

    private Context context;

    /**
     * Main Pager Adapter constructor
     * @param fragmentManager fragment manager
     * @param context context
     */
    public MainPagerAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new TranslateFragment();
            case 1: return new HistoryFragment();
            case 2: return new FavoriteFragment();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Drawable image = context.getResources().getDrawable(icons[position]);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        SpannableString sb = new SpannableString("   " + context.getString(titles[position]));
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }

}
