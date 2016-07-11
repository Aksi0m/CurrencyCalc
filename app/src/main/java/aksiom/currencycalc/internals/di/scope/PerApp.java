package aksiom.currencycalc.internals.di.scope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by Aksiom on 6/29/2016.
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerApp {
}
