package vardemin.com.yatranslate.events;


import vardemin.com.yatranslate.models.HistoryItem;

public class ChangeHistoryMsg {
    private final HistoryItem historyItem;
    private final boolean isFavorite;

    public ChangeHistoryMsg(HistoryItem historyItem, boolean isFavorite) {
        this.historyItem = historyItem;
        this.isFavorite = isFavorite;
    }

    public HistoryItem getHistoryItem() {
        return historyItem;
    }

    public boolean isFavorite() {
        return isFavorite;
    }
}
