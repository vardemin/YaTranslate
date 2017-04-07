package vardemin.com.yatranslate.di.history;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Qualifier for selecting DI for History (not favorite)
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface HistoryQualifier {
}
