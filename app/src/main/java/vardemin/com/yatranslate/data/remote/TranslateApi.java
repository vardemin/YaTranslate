package vardemin.com.yatranslate.data.remote;


import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;
import retrofit2.http.Query;
import vardemin.com.yatranslate.models.SupportedLangs;
import vardemin.com.yatranslate.models.TranslateItem;
import vardemin.com.yatranslate.utils.NetworkUtil;

import static  vardemin.com.yatranslate.utils.Const.*;

/**
 * YaTranslate Api interface
 */
public interface TranslateApi {
    @POST("translate")
    Observable<TranslateItem> translate(@Query("key") String key, @Query("lang") String lang, @Query("text") String text);

    @POST("getLangs")
    Observable<SupportedLangs> getLangs(@Query("key") String key, @Query("ui") String ui);

    class Factory {
        /**
         * Factory pattern implementation
         * @param context caching context
         * @param cached use cache?
         * @return TranslateApi implementation
         */
        public static TranslateApi create (Context context, boolean cached) {

            OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
            builder.readTimeout(15, TimeUnit.SECONDS);
            builder.connectTimeout(10, TimeUnit.SECONDS);
            builder.writeTimeout(10, TimeUnit.SECONDS);

            if (cached) {
                Cache cache = new Cache(context.getCacheDir(), 10 * 1024 * 1024);
                builder.cache(cache);
                builder.addInterceptor(chain -> {
                    Request request = chain.request();
                    if (NetworkUtil.isNetworkConnected(context)) {
                        request = request.newBuilder().header("Cache-Control", "public, max-age=" + 60).build();
                    } else {
                        request = request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build();
                    }
                    return chain.proceed(request);
                });
            }


            OkHttpClient client = builder.build();

            Gson gson = new GsonBuilder().create();

            Retrofit retrofit =
                    new Retrofit.Builder().baseUrl(BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create(gson)).build();

            return retrofit.create(TranslateApi.class);
        }

    }
}
