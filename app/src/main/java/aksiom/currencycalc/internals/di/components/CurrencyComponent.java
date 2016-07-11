package aksiom.currencycalc.internals.di.components;

import aksiom.currencycalc.internals.di.modules.CurrencyModule;
import aksiom.currencycalc.internals.di.scope.PerCurrency;
import aksiom.currencycalc.internals.mvp.models.CurrencyModel;
import aksiom.currencycalc.ui.currency.CurrencyCalculatorActivity;
import aksiom.currencycalc.ui.currency.CurrencyStatsActivity;
import aksiom.currencycalc.ui.currency.ExchangeRatesActivity;
import dagger.Component;

/**
 * Created by Aksiom on 6/29/2016.
 */
@PerCurrency
@Component(dependencies = AppComponent.class, modules = CurrencyModule.class)
public interface CurrencyComponent {
    void inject(CurrencyCalculatorActivity currencyCalculatorActivity);
    void inject(ExchangeRatesActivity exchangeRatesActivity);
    void inject(CurrencyStatsActivity currencyStatsActivity);

    CurrencyModel currencyModel();
}
