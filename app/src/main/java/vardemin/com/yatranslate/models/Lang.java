package vardemin.com.yatranslate.models;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Lang realm model
 */
public class Lang extends RealmObject {
    /**
     * Lang code in format: "en" or "ru"... see API.
     * Primary Key, Required
     */
    @PrimaryKey
    @Required
    private String code;

    /**
     * Localized human-friendly String representation of Lang name.
     * Required
     */
    @Required
    private String name;

    /**
     * Default constructor
     */
    public Lang(){}

    /**
     * Lang constructor
     * @param code Lang id (in format "en", "ru"...)
     * @param name Human localized representation of language name
     */
    public Lang(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /************GETTERS/SETTERS**************************/
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    /******************************************************/
}
