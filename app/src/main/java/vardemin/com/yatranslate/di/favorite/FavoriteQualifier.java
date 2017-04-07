package vardemin.com.yatranslate.di.favorite;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Qualifier for Favorites
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface FavoriteQualifier {
}
