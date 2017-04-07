package vardemin.com.yatranslate.di;


import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;
import vardemin.com.yatranslate.data.DataModule;
import vardemin.com.yatranslate.data.local.ILocalRepository;
import vardemin.com.yatranslate.data.remote.IRemoteRepository;

/**
 * YaTranslate Main Component
 */
@Component(modules = {ApplicationModule.class, NetModule.class, DataModule.class})
@Singleton
public interface YaTranslateComponent {
    Context context();
    ILocalRepository localDataRepository();
    IRemoteRepository remoteDataRepository();
    Retrofit retrofit();
}
