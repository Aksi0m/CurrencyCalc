package aksiom.currencycalc.internals.di.modules;

import android.app.Application;

import aksiom.currencycalc.internals.di.scope.PerApp;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Aksiom on 6/29/2016.
 */
@Module
public class AppModule {

    Application mApplication;

    public AppModule(Application application) {
        mApplication = application;
    }

    @Provides
    @PerApp
    Application providesApplication() {
        return mApplication;
    }

}
