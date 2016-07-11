package aksiom.currencycalc.internals.di.modules;

import android.app.Application;

import com.google.gson.Gson;

import aksiom.currencycalc.internals.di.scope.PerApp;
import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

/**
 * Created by Aksiom on 6/29/2016.
 */
@Module
public class DataModule {

    private Application application;

    public DataModule(Application application) {
        this.application = application;
    }

    @Provides
    @PerApp
    Gson provideGson() {
        return new Gson();
    }

    @Provides
    @PerApp
    Realm provideRealm() {
        return Realm.getDefaultInstance();
    }

}
