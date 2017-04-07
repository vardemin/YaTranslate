package vardemin.com.yatranslate.models;


import android.text.TextUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * History Item for RealmDB
 * source - source text
 * translated - translated variants
 * lang - Lang Pair
 */
public class HistoryItem extends RealmObject {
    /**
     * HashCode for soruce+translated. PrimaryKey
     */
    @PrimaryKey
    private int hashCode;
    /**
     * Original Text
     */
    private String source;

    /**
     * Translated Text in format ( variant1 \n variant2 \n ... )
     */
    private String translated;

    /**
     * Primary Lang reference
     */
    private Lang primary;

    /**
     * Secondary Lang reference
     */
    private Lang secondary;

    /**
     * Is Favorite Flag
     */
    private boolean isFavorite;

    /**
     * Date created/updated
     */
    private Date date;

    /**
     * Default init constructor (favorite=false; new Date)
     */
    public HistoryItem() {}

    /**
     * Create HistoryItem from Translated Response
     * @param sourceText source text
     * @param translated TranslatedItem
     */
    public HistoryItem(String sourceText, List<String> translated, LangPair langPair) {
        this.source = sourceText;
        this.translated = TextUtils.join("\n", translated);
        this.primary = langPair.getPrimary();
        this.secondary = langPair.getSecondary();
        this.isFavorite = false;
        this.date = new Date();
        this.hashCode = this.source.concat(this.translated).hashCode();
    }


    /**
     * Get date string
     * @return date in dd/MM/yyyy HH:mm:ss
     */
    public String getDateStr() {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return df.format(date);
    }

    /*****************GETTERS/SETTERS***********************/
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSourceText() {
        return source;
    }

    public void setSourceText(String source) {
        this.source = source;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public Lang getPrimary() {
        return primary;
    }

    public void setPrimary(Lang primary) {
        this.primary = primary;
    }

    public Lang getSecondary() {
        return secondary;
    }

    public void setSecondary(Lang secondary) {
        this.secondary = secondary;
    }

    public String getTranslated() {
        return translated;
    }

    public void setTranslated(String translated) {
        this.translated = translated;
    }
    /**************************************************************/

    /**
     * Get String representation of Langs (primary-secondary)
     * @return langs pair string (format: 'primary-secondary' )
     */
    public String getLang() {
        return primary.getCode().concat("-".concat(secondary.getCode()));
    }

    public LangPair getLangPair() {
        return new LangPair(primary, secondary);
    }

    /**
     * Override equals
     * @param object comparing object
     * @return if instance of HistoryItem and if source, translated text same
     */
    @Override
    public boolean equals(Object object) {
        if (object instanceof HistoryItem)
        {
            HistoryItem item = (HistoryItem) object;
            return item.getSourceText().equals(getSourceText()) && item.getTranslated().equals(getTranslated());
        }
        return false;
    }


    public int getHashCode() {
        return hashCode;
    }
}
