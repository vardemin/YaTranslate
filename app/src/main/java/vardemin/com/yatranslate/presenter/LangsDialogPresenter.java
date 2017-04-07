package vardemin.com.yatranslate.presenter;


import org.greenrobot.eventbus.EventBus;

import java.util.List;

import vardemin.com.yatranslate.App;
import vardemin.com.yatranslate.data.local.ILocalRepository;
import vardemin.com.yatranslate.events.ChangeLanguageMsg;
import vardemin.com.yatranslate.models.Lang;
import vardemin.com.yatranslate.models.LangPair;

import vardemin.com.yatranslate.ui.fragment.LangsDialog;
import vardemin.com.yatranslate.view.LangsDialogView;

public class LangsDialogPresenter implements Presenter<LangsDialogView>{

    private LangsDialogView dialogView;

    private ILocalRepository localRepository;
    private LangPair currentLangPair;
    private final boolean isPrimary;

    /**
     * Init LangDialogPresenter
     * @param isPrimary flag choosing for
     * @param localRepository local repo
     */
    public LangsDialogPresenter(boolean isPrimary, ILocalRepository localRepository) {
        this.localRepository = localRepository;
        this.isPrimary = isPrimary;

    }
    @Override
    public void attachView(LangsDialogView dialogView) {
        this.dialogView = dialogView;
    }

    /**
     * Init View
     */
    public void initView() {
        dialogView.onLoading(true);
    }

    @Override
    public void detachView() {
        this.dialogView = null;
    }

    @Override
    public boolean isAttached() {
        return dialogView!=null;
    }

    /**
     * Get Langs except current selected lang
     * @return langs list
     */
    public List<Lang> getLangsExcept() {
        this.currentLangPair = localRepository.getCurrentLangPair();
        return localRepository.getLangs(isPrimary?currentLangPair.getSecondary().getCode():currentLangPair.getPrimary().getCode());
    }

    /**
     * Handle Lang Choosing event
     * @param lang chosen lang
     */
    public void onLangChosen(Lang lang) {
        EventBus.getDefault().post(new ChangeLanguageMsg(isPrimary, lang));
        dialogView.close();
    }
}
