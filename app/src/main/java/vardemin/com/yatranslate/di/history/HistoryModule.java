package vardemin.com.yatranslate.di.history;

import dagger.Module;
import dagger.Provides;
import vardemin.com.yatranslate.data.local.ILocalRepository;
import vardemin.com.yatranslate.di.YaTranslateScope;
import vardemin.com.yatranslate.di.favorite.FavoriteQualifier;
import vardemin.com.yatranslate.presenter.HistoryPresenter;

@Module
public class HistoryModule {
    /**
     * Provide not favorite HistoryPresenter
     * @param localRepository local repo
     * @return history presenter
     */
    @Provides
    @YaTranslateScope
    @HistoryQualifier
    public HistoryPresenter provideHistoryPresenter(ILocalRepository localRepository) {
        return new HistoryPresenter(false, localRepository);
    }

    /**
     * Provide favorite HistoryPresenter
     * @param localRepository local repo
     * @return history presenter
     */
    @Provides
    @YaTranslateScope
    @FavoriteQualifier
    public HistoryPresenter provideFavoriteHistoryPresenter(ILocalRepository localRepository) {
        return new HistoryPresenter(true, localRepository);
    }
}
