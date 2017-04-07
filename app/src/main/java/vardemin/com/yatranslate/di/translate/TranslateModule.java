package vardemin.com.yatranslate.di.translate;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import vardemin.com.yatranslate.R;
import vardemin.com.yatranslate.data.local.ILocalRepository;
import vardemin.com.yatranslate.data.remote.IRemoteRepository;
import vardemin.com.yatranslate.di.YaTranslateScope;
import vardemin.com.yatranslate.presenter.TranslatePresenter;

@Module
public class TranslateModule {
    /**
     * Provide Translate Presenter
     * @param context init context
     * @param remoteRepository remote repo instance
     * @param localRepository local repo instance
     * @return Translate Presenter
     */
    @Provides
    @YaTranslateScope
    public TranslatePresenter provideTranslatePresenter(Context context, IRemoteRepository remoteRepository, ILocalRepository localRepository) {
        return new TranslatePresenter(context.getString(R.string.api_ui), remoteRepository, localRepository);
    }
}
