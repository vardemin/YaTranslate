package vardemin.com.yatranslate.events;


/**
 * On Navigate Event
 */
public class NavigateMsg {

    private final int tabPosition;

    /**
     * Init Event message
     * @param tabPosition Position needed to be rendered
     */
    public NavigateMsg(int tabPosition) {
        this.tabPosition = tabPosition;
    }

    /**
     * Position needed to be rendered
     * @return tab position
     */
    public int getTabPosition() {
        return tabPosition;
    }
}
