package vardemin.com.yatranslate.events;

import vardemin.com.yatranslate.models.HistoryItem;

/**
 * On Translate Event
 */
public class OnTranslateMsg {
    private final HistoryItem historyItem;

    /**
     * Init Translate Event message
     * @param historyItem saved translated object as history item
     */
    public OnTranslateMsg(HistoryItem historyItem) {
        this.historyItem = historyItem;
    }

    /**
     * Get translated obj as HistoryItem
     * @return history item
     */
    public HistoryItem getHistoryItem() {
        return historyItem;
    }
}
