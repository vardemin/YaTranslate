package vardemin.com.yatranslate.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;
import vardemin.com.yatranslate.R;
import vardemin.com.yatranslate.models.HistoryItem;
import vardemin.com.yatranslate.models.Lang;
import vardemin.com.yatranslate.models.LangPair;
import vardemin.com.yatranslate.models.SupportedLangs;
import vardemin.com.yatranslate.models.TranslateItem;

/**
 * ILocalRepository implementation (Local M)
 * Manages HistoryItem, Langs models, preferences
 */
public class LocalDataRepository implements ILocalRepository {
    @NonNull
    private Context context;
    @NonNull
    private RealmConfiguration config;

    private static final String APP_PREFERENCES = "yatranslate_pref";
    private static final String APP_PREFERENCES_PRIMARY = "primary";
    private static final String APP_PREFERENCES_SECONDARY = "secondary";

    private  final String DEFAULT_PRIMARY_CODE;
    private  final String DEFAULT_PRIMARY_NAME;
    private  final String DEFAULT_SECONDARY_CODE;
    private  final String DEFAULT_SECONDARY_NAME;

    private SharedPreferences preferences;

    //Real-time results
    RealmResults<HistoryItem> historyItems;
    RealmResults<HistoryItem> favoriteItems;

    /**
     * Init config, defaults
     * @param context Any context
     */
    public LocalDataRepository(@NonNull Context context) {
        this.context = context;
        DEFAULT_PRIMARY_CODE = context.getString(R.string.default_primary_code);
        DEFAULT_PRIMARY_NAME = context.getString(R.string.default_primary_lang);
        DEFAULT_SECONDARY_CODE = context.getString(R.string.default_secondary_code);
        DEFAULT_SECONDARY_NAME = context.getString(R.string.default_secondary_lang);

        Realm.init(context);
        this.config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        preferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        Realm realm = Realm.getInstance(config);
        historyItems = realm.where(HistoryItem.class).equalTo("isFavorite", false).findAllSorted("date");
        favoriteItems = realm.where(HistoryItem.class).equalTo("isFavorite", true).findAllSorted("date");
    }


    @Override
    public void save(@NonNull String sourceText, @NonNull TranslateItem translateItem) {
        Realm realm = Realm.getInstance(config);
        final HistoryItem item = new HistoryItem(sourceText, translateItem.getTexts(), getLangPairFromDir(translateItem.getTranslatingLang()));
        if (realm.where(HistoryItem.class).equalTo("hashCode", item.getHashCode()).findFirst()==null)
        {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(item);
            realm.commitTransaction();
            realm.close();
        }
    }

    @Override
    public void save(@NonNull List<Lang> langs) {
        Realm realm = Realm.getInstance(config);
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(langs);
        realm.commitTransaction();
        realm.close();
    }

    @Override
    public void save(@NonNull HistoryItem historyItem, boolean isFavorite) {
        Realm realm = Realm.getInstance(config);
        realm.beginTransaction();
        historyItem.setFavorite(isFavorite);
        realm.commitTransaction();
        realm.close();
    }

    /**
     * save Lang to db
     * @param lang
     */
    public void save(@NonNull Lang lang) {
        Realm realm = Realm.getInstance(config);
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(lang);
        realm.commitTransaction();
        realm.close();
    }

    @Override
    public List<HistoryItem> fetchAllHistory(boolean isFavorite) {
        Realm realm = Realm.getInstance(config);
        if (isFavorite) {
            favoriteItems = realm.where(HistoryItem.class).equalTo("isFavorite", true).findAllSorted("date", Sort.DESCENDING);
            return favoriteItems;
        }
        else {
            historyItems = realm.where(HistoryItem.class).equalTo("isFavorite", false).findAllSorted("date", Sort.DESCENDING);
            return historyItems;
        }
    }

    @Override
    public HistoryItem fetchLast() {
        Realm realm = Realm.getInstance(config);
        try {
            return realm.where(HistoryItem.class).findAllSorted("date", Sort.DESCENDING).first();
        }
        catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    @Override
    public List<HistoryItem> searchHistory(String query, boolean isFavorite) {
        Realm realm = Realm.getInstance(config);
        if(isFavorite) {
            //searchquery_fav = query;
           // favoriteItems.where().contains("source", searchquery_hist);
            favoriteItems = realm.where(HistoryItem.class).equalTo("isFavorite", true).contains("source", query).findAllSorted("date", Sort.DESCENDING);
            return favoriteItems;
        }
        else {
            //searchquery_hist = query;
            historyItems = realm.where(HistoryItem.class).equalTo("isFavorite", false).contains("source", query).findAllSorted("date", Sort.DESCENDING);
            return historyItems;
        }
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

    /**
     * Get LangPair from dir(langpair) string
     * @param dir lang dir in format ('primary-secondary')
     * @return LangPair
     */
    ///SOLID ?? Репозиторий не ответственнен за создание сущности по строке. В утилс ?

    public LangPair getLangPairFromDir(String dir) {
        String[] codes = dir.split("-");
        Realm realm = Realm.getInstance(config);
        Lang primary = realm.where(Lang.class).equalTo("code", codes[0]).findFirst();
        Lang secondary = realm.where(Lang.class).equalTo("code", codes[1]).findFirst();
        if(primary!=null && secondary!=null)
            return new LangPair(primary, secondary);
        return null;
    }

    @Override
    public Lang getLang(String code) {
        Realm realm = Realm.getInstance(config);
        return realm.where(Lang.class).equalTo("code", code).findFirst();
    }

    @Override
    public List<Lang> getLangs() {
        Realm realm = Realm.getInstance(config);
        return realm.where(Lang.class).findAllSorted("name");
    }

    @Override
    public List<Lang> getLangs(String exceptLangCode) {
        Realm realm = Realm.getInstance(config);
        return realm.where(Lang.class).notEqualTo("code", exceptLangCode).findAllSorted("name");
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
        if(isFavorite)
            favoriteItems.addChangeListener(listener);
        else historyItems.addChangeListener(listener);
    }

    @Override
    public void removeChangeListener(boolean isFavorite) {
        if(isFavorite)
            favoriteItems.removeAllChangeListeners();
        else historyItems.removeAllChangeListeners();
    }

    @Override
    public void removeAllHistory() {
        Realm realm = Realm.getInstance(config);
        realm.beginTransaction();
        realm.where(HistoryItem.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();
    }

    @Override
    public void removeAllHistory(boolean isFavorite) {
        Realm realm = Realm.getInstance(config);
        realm.beginTransaction();
        if (isFavorite) {
            favoriteItems.deleteAllFromRealm();
        }
        else {
            historyItems.deleteAllFromRealm();
        }
        realm.commitTransaction();
    }
}
