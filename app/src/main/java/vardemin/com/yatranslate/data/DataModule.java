package vardemin.com.yatranslate.data;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import vardemin.com.yatranslate.data.local.ILocalRepository;
import vardemin.com.yatranslate.data.local.LocalDataRepository;
import vardemin.com.yatranslate.data.remote.IRemoteRepository;
import vardemin.com.yatranslate.data.remote.RemoteDataRepository;

@Module
public class DataModule {

    @Provides
    @Singleton
    public ILocalRepository provideLocalDataRepository(Context context) {
        return new LocalDataRepository(context);
    }

    @Provides
    @Singleton
    public IRemoteRepository provideRemoteDataRepository(Retrofit retrofit) {
        return new RemoteDataRepository(retrofit);
    }
}
