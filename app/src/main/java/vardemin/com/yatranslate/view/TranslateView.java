package vardemin.com.yatranslate.view;

import vardemin.com.yatranslate.models.HistoryItem;
import vardemin.com.yatranslate.models.LangPair;
import vardemin.com.yatranslate.models.TranslateItem;
import vardemin.com.yatranslate.view.BaseView;


public interface TranslateView extends BaseView{
    /**
     * Call for tranlating
     * @param text text to be translated
     */
    void callTranslate(String text);

    /**
     * Render translated
     * @param translating translated result
     */
    void onTranslated(TranslateItem translating);

    /**
     * Call for init
     */
    void callInit();

    /**
     * On Swap Langs Action
     */
    void callSwapLangs();

    /**
     * Load last translating from history
     * @param historyItem history item
     */
    void onLastTranslate(HistoryItem historyItem);
    void onLangPair(LangPair langPair);
}
