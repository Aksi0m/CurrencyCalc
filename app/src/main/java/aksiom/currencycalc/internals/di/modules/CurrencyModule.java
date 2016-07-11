package aksiom.currencycalc.internals.di.modules;

import aksiom.currencycalc.internals.di.scope.PerCurrency;
import aksiom.currencycalc.networking.HnbAPI;
import aksiom.currencycalc.internals.mvp.models.CurrencyModel;
import aksiom.currencycalc.support.NetworkManager;
import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

/**
 * Created by Aksiom on 6/29/2016.
 */
@Module
public class CurrencyModule {

    @Provides @PerCurrency
    CurrencyModel provideCurrencyModel(HnbAPI hnbAPI, Realm realm, NetworkManager networkManager){
        return new CurrencyModel(hnbAPI, realm, networkManager);
    }
}
