package vardemin.com.yatranslate.presenter;

import vardemin.com.yatranslate.view.BaseView;

public interface Presenter<T extends BaseView> {
    /**
     * Attach View to Presenter
     * @param baseView view
     */
    void attachView(T baseView);

    /**
     * Detach View
     */
    void detachView();

    /**
     * Check attached
     * @return is Attached
     */
    boolean isAttached();
}
