package vardemin.com.yatranslate.di.history;

import dagger.Component;
import vardemin.com.yatranslate.di.YaTranslateComponent;
import vardemin.com.yatranslate.di.YaTranslateScope;
import vardemin.com.yatranslate.ui.fragment.FavoriteFragment;
import vardemin.com.yatranslate.ui.fragment.HistoryFragment;

@Component(dependencies = YaTranslateComponent.class, modules = HistoryModule.class)
@YaTranslateScope
public interface HistoryComponent {
    void inject(HistoryFragment historyFragment);
    void inject(FavoriteFragment favoriteFragment);
}
