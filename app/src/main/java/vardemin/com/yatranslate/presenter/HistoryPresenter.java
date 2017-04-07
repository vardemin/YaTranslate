package vardemin.com.yatranslate.presenter;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import vardemin.com.yatranslate.data.local.ILocalRepository;
import vardemin.com.yatranslate.events.ChangeHistoryMsg;
import vardemin.com.yatranslate.events.NavigateMsg;
import vardemin.com.yatranslate.events.OnTranslateMsg;
import vardemin.com.yatranslate.models.HistoryItem;
import vardemin.com.yatranslate.presenter.Presenter;
import vardemin.com.yatranslate.view.HistoryView;

public class HistoryPresenter implements Presenter<HistoryView>, RealmChangeListener<RealmResults<HistoryItem>>{
    /**
     * History View
     */
    private HistoryView historyView;
    /**
     * Local Repo
     */
    private ILocalRepository localRepository;
    /**
     * Is Favorite? flag
     */
    private final boolean isFavorite;

    public HistoryPresenter(boolean isFavorite, ILocalRepository localRepository) {
        this.localRepository = localRepository;
        this.isFavorite = isFavorite;
        this.localRepository.setChangeListener(this, isFavorite);
        EventBus.getDefault().register(this);
    }

    /**
     * Search history by query
     * @param query str query
     */
    public void searchHistory(String query) {
        if (query.length()>0) {
            List<HistoryItem> historyItemList = localRepository.searchHistory(query, isFavorite);
            if (isAttached())
                historyView.loadList(historyItemList);
        }
        else this.historyView.loadList(localRepository.fetchAllHistory(isFavorite));
    }

    /**
     * Called on
     * @param msg
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChangeHistoryEvent(ChangeHistoryMsg msg) {
        if(historyView!=null)
            historyView.refresh();
    }

    /**
     * Called when history item selected
     * @param historyItem selected historyitem
     */
    public void onNavigateTranslate(HistoryItem historyItem) {
        EventBus.getDefault().post(new NavigateMsg(0));
        EventBus.getDefault().post(new OnTranslateMsg(historyItem));
    }

    /**
     * Called when changin historyitem (flag isFavorite)
     * @param historyItem item to be updated
     */
    public void onChangeHistoryItem(HistoryItem historyItem){
        localRepository.save(historyItem, !historyItem.isFavorite());
        EventBus.getDefault().post(new ChangeHistoryMsg(historyItem, historyItem.isFavorite()));
    }

    @Override
    public void attachView(HistoryView historyView) {
        this.historyView = historyView;
        this.historyView.init(localRepository.fetchAllHistory(isFavorite));
    }

    @Override
    public void detachView() {
        this.historyView = null;
    }

    @Override
    public boolean isAttached() {
        return historyView!=null;
    }


    @Override
    public void onChange(RealmResults<HistoryItem> element) {
        if (isAttached())
            historyView.refresh();
    }

    /**
     * On RemoveAll action. Remove rendered data from db
     */
    public void removeAll() {
        localRepository.removeAllHistory(isFavorite);
        if (isAttached())
            historyView.refresh();
    }
}
