package aksiom.currencycalc.internals.mvp.presenters;

import java.util.List;

import javax.inject.Inject;

import aksiom.currencycalc.internals.mvp.MVP;
import aksiom.currencycalc.internals.mvp.views.ExchangeRatesView;
import aksiom.currencycalc.realm.Currency;
import aksiom.currencycalc.internals.mvp.models.CurrencyModel;

/**
 * Created by Aksiom on 6/29/2016.
 */
public class ExchangeRatesPresenter extends MVP.Presenter<ExchangeRatesView> {

    private CurrencyModel currencyModel;

    @Inject public ExchangeRatesPresenter(CurrencyModel currencyModel) {
        this.currencyModel = currencyModel;
    }

    public void loadData(){
        getView().showLoading();
        currencyModel.getExchangeRatesData(new MVP.Model.OnDataLoaded<List<Currency>>() {
            @Override
            public void onSuccess(List<Currency> data) {
                getView().showContent();
                getView().populateList(data);
            }

            @Override
            public void onFail(String error) {
                getView().showEmpty();
                getView().showError(error);
            }
        });
    }

    @Override
    public void destroy() {
        //do cleanup if needed
    }
}
