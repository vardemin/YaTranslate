package vardemin.com.yatranslate.di;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    @NonNull
    private final Context context;

    public ApplicationModule(@NonNull Context context) {
        this.context = context;
    }

    /**
     * Provide Application context
     * @return context
     */
    @Provides
    @Singleton
    Context provideContext() {
        return this.context;
    }


}
