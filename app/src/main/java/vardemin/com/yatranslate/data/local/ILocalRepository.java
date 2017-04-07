package vardemin.com.yatranslate.data.local;

import android.support.annotation.NonNull;

import java.util.List;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import vardemin.com.yatranslate.models.HistoryItem;
import vardemin.com.yatranslate.models.Lang;
import vardemin.com.yatranslate.models.LangPair;
import vardemin.com.yatranslate.models.SupportedLangs;
import vardemin.com.yatranslate.models.TranslateItem;

public interface ILocalRepository {
    /**
     * Save HistoryItem from translating
     * @param sourceText original text
     * @param translateItem TranslateItem instance
     */
    void save(@NonNull String sourceText, @NonNull TranslateItem translateItem);

    /**
     * Save/Update Langs List
     * @param langs List Langs
     */
    void save(@NonNull List<Lang> langs);

    /**
     * Update historyitem
     * @param historyItem current historyItem
     * @param isFavorite
     */
    void save(@NonNull HistoryItem historyItem, boolean isFavorite);

    /**
     * Get RealmResults list of HistroyItem in List
     * @param isFavorite filter by flag
     * @return List of HistroyItem
     */
    List<HistoryItem> fetchAllHistory(boolean isFavorite);

    /**
     * Get Last saved/updated HistoryItem
     * @return actual HistroyItem
     */
    HistoryItem fetchLast();

    /**
     * Search HistoryItems by string (by original text)
     * @param query search query
     * @param isFavorite filter flag
     * @return Found List (Realmresults) of HistroyItem
     */
    List<HistoryItem> searchHistory(String query, boolean isFavorite);

    /**
     * Get Actual LangPair
     * @return actural LangPair
     */
    LangPair getCurrentLangPair();

    /**
     * Set Lang as primary or secondary
     * @param code lang code
     * @param isPrimary flag to set
     */
    void setCurrentLang(String code, boolean isPrimary);

    /**
     * Set current LangPair
     * @param langPair lang pair to set
     */
    void setCurrentLangPair(LangPair langPair);

    /**
     * Get Lang by code
     * @param code lang code
     * @return Lang instance
     */
    Lang getLang(String code);

    /**
     * Get All Langs
     * @return langs
     */
    List<Lang> getLangs();

    /**
     * Get All Langs except
     * @param exceptLangCode excepting Lang code
     * @return Langs
     */
    List<Lang> getLangs(String exceptLangCode);

    /**
     * Get SupoortedLangs from FS file
     * @return SupportedLangs instance
     */
    SupportedLangs getDefaultSupportedLangs();

    /**
     * Set ChangeListener
     * @param listener RealmChangeListener
     * @param isFavorite falg isFavorite
     */
    void setChangeListener(RealmChangeListener<RealmResults<HistoryItem>> listener, boolean isFavorite);

    /**
     * Remove any ChangeListener
     * @param isFavorite flag isFavorite
     */
    void removeChangeListener(boolean isFavorite);

    /**
     * Remove all history
     */
    void removeAllHistory();

    /**
     * Remove all history filtered by flag
     * @param isFavorite flag is favorite
     */
    void removeAllHistory(boolean isFavorite);
}
