package vardemin.com.yatranslate.data.remote;


import android.support.annotation.NonNull;

import io.reactivex.Observable;
import vardemin.com.yatranslate.models.SupportedLangs;
import vardemin.com.yatranslate.models.TranslateItem;

public interface IRemoteRepository {
    Observable<TranslateItem> translate(@NonNull String text, @NonNull String langPair);
    Observable<SupportedLangs> getLangs(@NonNull String ui);
}
