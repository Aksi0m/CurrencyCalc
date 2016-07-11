package aksiom.currencycalc.internals.di.components;

import com.google.gson.Gson;

import aksiom.currencycalc.internals.di.modules.AppModule;
import aksiom.currencycalc.internals.di.modules.DataModule;
import aksiom.currencycalc.internals.di.modules.NetworkModule;
import aksiom.currencycalc.internals.di.scope.PerApp;
import aksiom.currencycalc.networking.HnbAPI;
import aksiom.currencycalc.support.NetworkManager;
import dagger.Component;
import io.realm.Realm;

/**
 * Created by Aksiom on 6/29/2016.
 */
@PerApp
@Component(modules = {AppModule.class, NetworkModule.class, DataModule.class})
public interface AppComponent {

    Gson gson();

    Realm realm();

    HnbAPI hnbApi();

    NetworkManager networkManager();

}
