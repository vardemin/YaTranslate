package vardemin.com.yatranslate.view;

import java.util.List;

import vardemin.com.yatranslate.models.HistoryItem;
import vardemin.com.yatranslate.view.BaseView;

public interface HistoryView extends BaseView {
    /**
     * Init view with data for adapter
     * @param historyItemList init List
     */
    void init(List<HistoryItem> historyItemList);

    /**
     * Load new data to be rendered
     * @param historyItems new dataList
     */
    void loadList(List<HistoryItem> historyItems);

    /**
     * Called on removeAll action
     */
    void clear();

    /**
     * Search by query
     * @param str str query
     */
    void search(String str);

    /**
     * Refresh view
     */
    void refresh();
}
