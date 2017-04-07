package vardemin.com.yatranslate;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import vardemin.com.yatranslate.data.local.ILocalRepository;
import vardemin.com.yatranslate.models.HistoryItem;
import vardemin.com.yatranslate.models.Lang;
import vardemin.com.yatranslate.models.LangPair;
import vardemin.com.yatranslate.models.SupportedLangs;
import vardemin.com.yatranslate.models.TranslateItem;



public class TestLocalRepo implements ILocalRepository {
    private List<HistoryItem> historyItems = new ArrayList<>();
    private List<Lang> langList = new ArrayList<>();
    private Context context;

    private static final String APP_PREFERENCES = "yatranslate_pref";
    private static final String APP_PREFERENCES_PRIMARY = "primary";
    private static final String APP_PREFERENCES_SECONDARY = "secondary";

    private  final String DEFAULT_PRIMARY_CODE;
    private  final String DEFAULT_PRIMARY_NAME;
    private  final String DEFAULT_SECONDARY_CODE;
    private  final String DEFAULT_SECONDARY_NAME;

    private SharedPreferences preferences;

    private RealmChangeListener<RealmResults<HistoryItem>> favoriteListener;
    private RealmChangeListener<RealmResults<HistoryItem>> historyListener;

    public TestLocalRepo(@NonNull Context context) {
        this.context = context;
        DEFAULT_PRIMARY_CODE = context.getString(R.string.default_primary_code);
        DEFAULT_PRIMARY_NAME = context.getString(R.string.default_primary_lang);
        DEFAULT_SECONDARY_CODE = context.getString(R.string.default_secondary_code);
        DEFAULT_SECONDARY_NAME = context.getString(R.string.default_secondary_lang);
        preferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }


    @Override
    public void save(@NonNull String sourceText, @NonNull TranslateItem translateItem) {
        historyItems.add(new HistoryItem(sourceText,translateItem.getTexts(),getCurrentLangPair()));
    }

    @Override
    public void save(@NonNull List<Lang> langs) {
        langList.clear();
        langList.addAll(langs);
    }

    @Override
    public void save(@NonNull HistoryItem historyItem, boolean isFavorite) {
        HistoryItem item = historyItems.get(historyItems.indexOf(historyItem));
        item.setFavorite(isFavorite);
    }

    public void save(Lang lang){
        langList.add(lang);
    }

    @Override
    public List<HistoryItem> fetchAllHistory(boolean isFavorite) {
        List<HistoryItem> filterdList = new ArrayList<>();
        for (HistoryItem item: historyItems) {
            if (item.isFavorite() == isFavorite) {
                filterdList.add(item);
            }
        }
        return filterdList;
    }

    @Override
    public HistoryItem fetchLast() {
        return historyItems.get(historyItems.size()-1);
    }

    @Override
    public List<HistoryItem> searchHistory(String query, boolean isFavorite) {
        List<HistoryItem> searchList = new ArrayList<>();
        for (HistoryItem item: historyItems){
            if (item.isFavorite() == isFavorite) {
                if (item.getSourceText().contains(query))
                    searchList.add(item);
            }
        }
        return searchList;
    }

    @Override
    public LangPair getCurrentLangPair() {
        Lang primary, secondary;

        if(preferences.contains(APP_PREFERENCES_PRIMARY))
            primary = getLang(preferences.getString(APP_PREFERENCES_PRIMARY, context.getString(R.string.default_primary_code)));
        else {
            primary = new Lang(DEFAULT_PRIMARY_CODE, DEFAULT_PRIMARY_NAME);
            save(primary);
        }

        if (preferences.contains(APP_PREFERENCES_SECONDARY))
            secondary = getLang(preferences.getString(APP_PREFERENCES_SECONDARY, context.getString(R.string.default_secondary_code)));
        else{
            secondary = new Lang(DEFAULT_SECONDARY_CODE, DEFAULT_SECONDARY_NAME);
            save(secondary);
        }
        return new LangPair(primary, secondary);
    }

    @Override
    public void setCurrentLang(String code, boolean isPrimary) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(isPrimary?APP_PREFERENCES_PRIMARY:APP_PREFERENCES_SECONDARY, code);
        editor.apply();
    }

    @Override
    public void setCurrentLangPair(LangPair langPair) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(APP_PREFERENCES_PRIMARY, langPair.getPrimary().getCode());
        editor.putString(APP_PREFERENCES_SECONDARY, langPair.getSecondary().getCode());
        editor.apply();
    }

    @Override
    public Lang getLang(String code) {
        for (Lang lng: langList ){
            if (lng.getCode().equals(code)) {
                return lng;
            }
        }
        return null;
    }

    @Override
    public List<Lang> getLangs() {
        return langList;
    }

    @Override
    public List<Lang> getLangs(String exceptLangCode) {
        List<Lang> langs = new ArrayList<>();
        for (Lang lang: langList) {
            if (!lang.getCode().equals(exceptLangCode))
                langs.add(lang);
        }
        return langs;
    }

    @Override
    public SupportedLangs getDefaultSupportedLangs() {
        InputStream is = context.getResources().openRawResource(R.raw.langs);
        byte[] buffer = null;
        int size = 0;
        try {
            size = is.available();
            buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            Gson gson = new GsonBuilder().create();
            return gson.fromJson(json, SupportedLangs.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void setChangeListener(RealmChangeListener<RealmResults<HistoryItem>> listener, boolean isFavorite) {
        if (isFavorite)
            this.favoriteListener = listener;
        else this.historyListener = listener;
    }

    @Override
    public void removeChangeListener(boolean isFavorite) {
        if (isFavorite)
            this.favoriteListener = null;
        else this.historyListener = null;
    }

    @Override
    public void removeAllHistory() {
        historyItems.clear();
    }

    @Override
    public void removeAllHistory(boolean isFavorite) {
        for(HistoryItem item : historyItems ){
            if(item.isFavorite())
                historyItems.remove(item);
        }
    }
}
