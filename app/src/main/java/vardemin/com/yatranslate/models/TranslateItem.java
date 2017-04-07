package vardemin.com.yatranslate.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Translating response model
 * lang - Lang Pair
 * text[] - translated variants
 *
 *  JSON structure:
 *  {
 *      lang : str,
 *      text : [
 *          str1,
 *          str2
 *      ]
 *  }
 */
public class TranslateItem {
    /**
     * Lang pair in string
     */
    @SerializedName("lang")
    private String lang;

    /**
     * List of translating variants
     */
    @SerializedName("text")
    private List<String> text;

    public TranslateItem(String lang, List<String> text) {
        this.lang = lang;
        this.text = text;
    }

    /*****************GETTERS/SETTERS*******************/
    public String getTranslatingLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public List<String> getTexts() {
        return text;
    }

    public void setTexts(List<String> text) {
        this.text.clear();
        this.text.addAll(text);
    }
    /******************************************************/
}
