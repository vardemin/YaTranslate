package vardemin.com.yatranslate.events;


import vardemin.com.yatranslate.models.Lang;

/**
 * Change Language Event
 */
public class ChangeLanguageMsg {
    private final boolean isPrimary;
    private final Lang lang;

    /**
     * Event constructor
     * @param isPrimary is primary Lang ? else secondary
     * @param lang Lang instance
     */
    public ChangeLanguageMsg(boolean isPrimary, Lang lang) {
        this.isPrimary = isPrimary;
        this.lang = lang;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public Lang getLang() {
        return lang;
    }
}
