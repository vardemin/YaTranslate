package vardemin.com.yatranslate.data.remote;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import vardemin.com.yatranslate.models.SupportedLangs;
import vardemin.com.yatranslate.models.TranslateItem;
import static vardemin.com.yatranslate.utils.Const.YA_TL_KEY;

public class RemoteDataRepository implements IRemoteRepository {
    /**
     * Retrofit instance
     */
    private Retrofit retrofit;

    public RemoteDataRepository(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    /**
     * Request translate
     * @param text original text
     * @param langPair langpair
     * @return Observable TranslateItem
     */
    public Observable<TranslateItem> translate(@NonNull String text, @NonNull String langPair) {
        return retrofit.create(TranslateApi.class).translate(YA_TL_KEY, langPair, text);
    }

    /**
     * Request Langs
     * @param ui ui localization
     * @return Observable SupportedLangs
     */
    public Observable<SupportedLangs> getLangs(@NonNull String ui) {
        return retrofit.create(TranslateApi.class).getLangs(YA_TL_KEY, ui);
    }

}
