package vardemin.com.yatranslate.ui.listener;


import vardemin.com.yatranslate.models.HistoryItem;

public interface HistoryListListener {
    /**
     * Handle Translate Action
     * @param historyItem item to be translated
     */
    void onActionTranslate(HistoryItem historyItem);

    /**
     * Handle history change Action
     * @param historyItem item to be changed
     */
    void onActionHistory(HistoryItem historyItem);
}
