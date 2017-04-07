package vardemin.com.yatranslate;


import com.google.gson.GsonBuilder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Arrays;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import vardemin.com.yatranslate.data.local.ILocalRepository;
import vardemin.com.yatranslate.data.remote.IRemoteRepository;
import vardemin.com.yatranslate.data.remote.RemoteDataRepository;
import vardemin.com.yatranslate.presenter.HistoryPresenter;
import vardemin.com.yatranslate.presenter.TranslatePresenter;
import vardemin.com.yatranslate.models.HistoryItem;

import static org.junit.Assert.*;
import static vardemin.com.yatranslate.utils.Const.BASE_URL;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, application = App.class)
public class MainTest {
    HistoryPresenter historyPresenter;
    TranslatePresenter translatePresenter;
    ILocalRepository localRepository;
    IRemoteRepository remoteRepository;


    @Before
    public void setup() {
        localRepository = new TestLocalRepo(RuntimeEnvironment.application);
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .client(new OkHttpClient.Builder().build())
                .build();
        remoteRepository = new RemoteDataRepository(retrofit);
        historyPresenter = new HistoryPresenter(false, localRepository);
        translatePresenter = new TranslatePresenter("en", remoteRepository, localRepository);

    }

    @Test
    public void checkLanguage() {
        assertEquals("English",localRepository.getLang("en").getName());
    }

    @Test
    public void checkAfterTranslateAndHistoryChange() throws InterruptedException {
        localRepository.save("hello", remoteRepository.translate("hello", localRepository.getCurrentLangPair().getDir()).blockingFirst());
        assertEquals(new HistoryItem("hello", Arrays.asList("привет"), localRepository.getCurrentLangPair()), localRepository.fetchLast());
        historyPresenter.onChangeHistoryItem(localRepository.fetchLast());
        assertEquals(true, localRepository.fetchLast().isFavorite());
    }
    @Test
    public void checkAfterTranslateSwapLangs() {
        localRepository.save("hello", remoteRepository.translate("hello", localRepository.getCurrentLangPair().getDir()).blockingFirst());
        assertEquals(new HistoryItem("hello", Arrays.asList("привет"), localRepository.getCurrentLangPair()), localRepository.fetchLast());
        translatePresenter.onSwapLangs();
        localRepository.save("код", remoteRepository.translate("код", localRepository.getCurrentLangPair().getDir()).blockingFirst());
        assertEquals(new HistoryItem("код", Arrays.asList("code"), localRepository.getCurrentLangPair()), localRepository.fetchLast());
    }

}
