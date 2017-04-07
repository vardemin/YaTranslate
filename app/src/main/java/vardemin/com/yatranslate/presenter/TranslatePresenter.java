package vardemin.com.yatranslate.presenter;

import android.support.annotation.NonNull;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import vardemin.com.yatranslate.data.local.ILocalRepository;
import vardemin.com.yatranslate.data.remote.IRemoteRepository;
import vardemin.com.yatranslate.events.ChangeHistoryMsg;
import vardemin.com.yatranslate.events.ChangeLanguageMsg;
import vardemin.com.yatranslate.events.OnTranslateMsg;
import vardemin.com.yatranslate.models.HistoryItem;
import vardemin.com.yatranslate.models.LangPair;
import vardemin.com.yatranslate.models.SupportedLangs;
import vardemin.com.yatranslate.models.TranslateItem;
import vardemin.com.yatranslate.presenter.Presenter;
import vardemin.com.yatranslate.view.TranslateView;

public class TranslatePresenter implements Presenter<TranslateView> {

    private TranslateView translateView;

    private IRemoteRepository remoteRepository;
    private ILocalRepository localRepository;

    private final CompositeDisposable composite; //rx2 CompositeSubscription

    private SupportedLangs supportedLangs;
    private final String api_ui;

    /**
     * Translate presenter
     * @param ui locale ui
     */
    public TranslatePresenter(String ui, IRemoteRepository remoteRepository, ILocalRepository localRepository) {
        this.remoteRepository = remoteRepository;
        this.localRepository = localRepository;
        this.api_ui = ui;
        composite = new CompositeDisposable();
        loadSupportLangs();

        EventBus.getDefault().register(this);
    }

    @Override
    public void attachView(TranslateView translateView) {
        this.translateView = translateView;
    }


    @Override
    public void detachView() {
        this.translateView = null;
        //Unsubscribe all
        composite.clear();
    }

    /**
     * Called from view
     */
    public void initView() {
        HistoryItem lastItem = localRepository.fetchLast();
        if (lastItem!=null)
            EventBus.getDefault().post(new OnTranslateMsg(lastItem));
        else
            translateView.onLangPair(localRepository.getCurrentLangPair());
    }

    /**
     * Load Support Langs from API
     * @param ui locale ui
     */
    private void loadSupportLangs(String ui) {
        remoteRepository.getLangs(ui)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleSupportedLangsResponse,this::handleError);
    }

    /**
     * Load default Support Langs
     */
    private void loadSupportLangs() {
        handleSupportedLangsResponse(localRepository.getDefaultSupportedLangs());
    }

    /**
     * Handle suppoted langs observer result
     * @param supportedLangs supported langs
     */
    private void handleSupportedLangsResponse(SupportedLangs supportedLangs) {
        this.supportedLangs = supportedLangs;
        localRepository.save(supportedLangs.getLangList());
    }

    /**
     * Handle translation observer result
     * @param origin origin text
     * @param translated TranslateItem instance
     */
    private void handleTranlsated(String origin, TranslateItem translated) {
        localRepository.save(origin, translated);
        EventBus.getDefault().post(new ChangeHistoryMsg(localRepository.fetchLast(), false));
        if (isAttached())
            translateView.onTranslated(translated);
    }

    /**
     * Translate call
     * @param text origin text
     */
    public void translate(@NonNull String text) {
        handleLoading(true);
        composite.clear();
        remoteRepository.translate(text, localRepository.getCurrentLangPair().getDir())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TranslateItem>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        composite.add(d);
                    }

                    @Override
                    public void onNext(TranslateItem translateItem) {
                        handleLoading(false);
                        handleTranlsated(text, translateItem);
                    }

                    @Override
                    public void onError(Throwable e) {
                        handleError(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * Swap Current Langs
     */
    public void onSwapLangs(){
        LangPair pair = localRepository.getCurrentLangPair();
        localRepository.setCurrentLang(pair.getSecondary().getCode(), true);
        localRepository.setCurrentLang(pair.getPrimary().getCode(), false);
        if(isAttached())
            translateView.onLangPair(pair.swap());
    }

    /**
     * Handle Presenter Error
     * @param error throwable error
     */
    private void handleError(Throwable error){
        if(isAttached())
            translateView.onError(error.getLocalizedMessage());
    }

    /**
     * Loading handler
     * @param state start(true)/finish(false) loading
     */
    private void handleLoading(boolean state) {
        if (isAttached())
            translateView.onLoading(state);
    }

    /**
     * Handle ChangeLanguage event
     * @param msg ChangeLanguageMsg object
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChangeLang(ChangeLanguageMsg msg){
        localRepository.setCurrentLang(msg.getLang().getCode(), msg.isPrimary());
        if(isAttached())
            translateView.onLangPair(localRepository.getCurrentLangPair());
    }

    /**
     * Handle OnTranslate event
     * @param msg OnTranslateMsg object
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTranslateMsg(OnTranslateMsg msg) {
        localRepository.setCurrentLangPair(msg.getHistoryItem().getLangPair());
        if (isAttached())
            translateView.onLastTranslate(msg.getHistoryItem());
    }

    @Override
    public boolean isAttached() {
        return translateView!=null;
    }
}
