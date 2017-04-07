package vardemin.com.yatranslate.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Support Languages API response model
 * dirs = String - pair of Lang codes (example: "en-ru")
 * langs = localized Map<String, String> of Lang model (code-name)
 *
 * JSON structure:
 * {
 *     dirs: [
 *          "en-ru",
 *          "en-ua"
 *     ],
 *     langs: {
 *         "en": English,
 *         "ru": Russian
 *     }
 * }
 */
public class SupportedLangs {
    /**
     * Dirs List
     */
    @SerializedName("dirs")
    private List<String> dirs;

    /**
     * Langs map (code: name)
     */
    @SerializedName("langs")
    private HashMap<String,String> langs;


    /***********************GETTERS/SETTERS************************/
    public HashMap<String,String> getLangs() {
        return langs;
    }

    public void setLangs(HashMap<String, String> langs) {
        this.langs = langs;
    }

    public List<String> getDirs() {
        return dirs;
    }

    public void setDirs(List<String> dirs) {
        this.dirs = dirs;
    }
    
    public String getLang(String key) {
        return langs.get(key);
    }
    /**************************************************************/

    /**
     * Get List of Lang from result Map
     * @return list of Langs
     */
    public List<Lang> getLangList() {
        List<Lang> langList = new ArrayList<>();
        for (Map.Entry<String, String> entry: langs.entrySet()) {
            langList.add(new Lang(entry.getKey(), entry.getValue()));
        }
        return langList;
    }

    /**
     * Make dir String pair representation of two Langs
     * @param primary primary Lang
     * @param secondary secondary Lang
     * @return String pair (example: "en-ru")
     */
    public String getDir (Lang primary, Lang secondary) {
        String dir = primary.getCode().concat("-".concat(secondary.getCode()));
        if(dirs.contains(dir))
            return dir;
        return null;
    }
}
