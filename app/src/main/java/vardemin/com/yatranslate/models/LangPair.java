package vardemin.com.yatranslate.models;

/**
 * Lang Pair
 */
public class LangPair {

    private final Lang primary;
    private final Lang secondary;

    /**
     * LangPair from Langs constructor
     * @param primary primary Lang
     * @param secondary secondary Lang
     */
    public LangPair(Lang primary, Lang secondary) {
        this.primary = primary;
        this.secondary = secondary;
    }

    /**
     * Check if pair contains lang
     * @param lang lang to check
     * @return is contain lang
     */
    public boolean contains(String lang) {
        return primary.getName().equals(lang) || secondary.getName().equals(lang);
    }

    public Lang getPrimary() {
        return primary;
    }
    public Lang getSecondary() {
        return secondary;
    }

    /**
     * Get String Lang Dir in format: ('primary-secondary')
     * @return String ('primary-secondary')
     */
    public String getDir() {
        return primary.getCode().concat("-".concat(secondary.getCode()));
    }

    /**
     * Swap langs and return new LangPair
     * @return new LangPair
     */
    public LangPair swap() {
        return new LangPair(secondary, primary);
    }

}
