package vardemin.com.yatranslate.di.translate;

import dagger.Component;
import vardemin.com.yatranslate.di.YaTranslateComponent;
import vardemin.com.yatranslate.di.YaTranslateScope;
import vardemin.com.yatranslate.ui.fragment.TranslateFragment;

@Component(dependencies = YaTranslateComponent.class, modules = TranslateModule.class)
@YaTranslateScope
public interface TranslateComponent {
    void inject(TranslateFragment translateFragment);
}
