package vardemin.com.yatranslate;


import android.app.Application;
import vardemin.com.yatranslate.data.DataModule;
import vardemin.com.yatranslate.di.ApplicationModule;
import vardemin.com.yatranslate.di.DaggerYaTranslateComponent;
import vardemin.com.yatranslate.di.NetModule;
import vardemin.com.yatranslate.di.YaTranslateComponent;
import vardemin.com.yatranslate.di.history.DaggerHistoryComponent;
import vardemin.com.yatranslate.di.history.HistoryComponent;
import vardemin.com.yatranslate.di.translate.DaggerTranslateComponent;
import vardemin.com.yatranslate.di.translate.TranslateComponent;
import vardemin.com.yatranslate.utils.Const;

public class App extends Application {

    private static YaTranslateComponent yaTranslateComponent;
    private static TranslateComponent translateComponent;
    private static HistoryComponent historyComponent;

    /**
     * Get YaTranslateComponent from App
     * @return YaTranslateComponent
     */
    public YaTranslateComponent getYaTranslateComponent() {
        return yaTranslateComponent;
    }

    /**
     * Get Translate Component
     * @return TranslateComponent
     */
    public TranslateComponent getTranslateComponent() {
        return translateComponent;
    }

    /**
     *
     * @return HistoryComponent
     */
    public HistoryComponent getHistoryComponent() {
        return historyComponent;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        yaTranslateComponent = DaggerYaTranslateComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .netModule(new NetModule(Const.BASE_URL, true))
                .dataModule(new DataModule()).build();
        translateComponent = DaggerTranslateComponent.builder()
                .yaTranslateComponent(yaTranslateComponent)
                .build();
        historyComponent = DaggerHistoryComponent.builder()
                .yaTranslateComponent(yaTranslateComponent)
                .build();

    }


}
