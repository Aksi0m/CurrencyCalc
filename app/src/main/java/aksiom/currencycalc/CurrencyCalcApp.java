package aksiom.currencycalc;

import android.app.Application;
import android.util.Log;

import net.danlew.android.joda.JodaTimeAndroid;

import aksiom.currencycalc.internals.di.components.AppComponent;
import aksiom.currencycalc.internals.di.components.DaggerAppComponent;
import aksiom.currencycalc.internals.di.modules.DataModule;
import aksiom.currencycalc.internals.di.modules.NetworkModule;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Aksiom on 6/29/2016.
 */
public class CurrencyCalcApp extends Application {

    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("INIT", "initInjector() called with: " + System.currentTimeMillis());
        initInjector();
        Log.d("INIT", "initRealmConfiguration() called with: " + System.currentTimeMillis());
        initRealmConfiguration();
        Log.d("INIT", "JodaTimeAndroid() called with: " + System.currentTimeMillis());
        JodaTimeAndroid.init(this);
        Log.d("INIT", "DONE: " + System.currentTimeMillis());
    }

    /**
     * Initialise the injector and create the app graph
     */
    private void initInjector() {
        appComponent = DaggerAppComponent.builder()
                .networkModule(new NetworkModule(this))
                .dataModule(new DataModule(this))
                .build();
    }

    /**
     * Initialise the realm configuration
     */
    private void initRealmConfiguration(){
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }


    /**
     *
     * @return the AppComponent instance
     */
    public static AppComponent getAppComponent() {
        return appComponent;
    }
}
