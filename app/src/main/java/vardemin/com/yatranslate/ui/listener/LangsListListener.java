package vardemin.com.yatranslate.ui.listener;

import vardemin.com.yatranslate.models.Lang;


public interface LangsListListener{
    /**
     * Handle Lang Choosing
     * @param lang chosen lang
     */
    void onLangChosen(Lang lang);
}
