package vardemin.com.yatranslate.view;

public interface BaseView {
    /**
     * Handle loading
     * @param state start(true)/stop(false)
     */
    void onLoading(boolean state);

    /**
     * View error
     * @param msg error msg
     */
    void onError(String msg);
}
