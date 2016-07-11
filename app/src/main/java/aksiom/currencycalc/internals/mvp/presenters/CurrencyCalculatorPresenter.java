package aksiom.currencycalc.internals.mvp.presenters;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import aksiom.currencycalc.internals.mvp.MVP;
import aksiom.currencycalc.internals.mvp.views.CurrencyCalculatorView;
import aksiom.currencycalc.realm.Currency;
import aksiom.currencycalc.internals.mvp.models.CurrencyModel;

/**
 * Created by Aksiom on 6/30/2016.
 */
public class CurrencyCalculatorPresenter extends MVP.Presenter<CurrencyCalculatorView> {

    private CurrencyModel currencyModel;
    private List<Currency> currencies = new ArrayList<>();
    private int posOfCurrency = 0;
    private DecimalFormat df = new DecimalFormat("#.00");
    private boolean fromKuna = true;

    @Inject
    public CurrencyCalculatorPresenter(CurrencyModel currencyModel) {
        this.currencyModel = currencyModel;
    }

    public void loadData(){
        getView().showLoading();
        currencyModel.getExchangeRatesData(new MVP.Model.OnDataLoaded<List<Currency>>() {
            @Override
            public void onSuccess(List<Currency> data) {
                currencies.clear();
                currencies.addAll(data);
                getView().showContent();
                getView().populateList(data);
                getView().changeCurrency(currencies.get(posOfCurrency).getCurrencyCode());
            }

            @Override
            public void onFail(String error) {
                getView().showEmpty();
                getView().showError(error);
            }
        });
    }

    public void swapCurrencies() {
        fromKuna = !fromKuna;
        getView().swapCurrencies();
    }

    public void selectCurrency(int position) {
        posOfCurrency = position;
        getView().changeCurrency(currencies.get(posOfCurrency).getCurrencyCode());
    }

    public void convert(Double moneyzz) {
        if(currencies.isEmpty()) return;
        if(fromKuna){
            //(Moneyzz/MedianRate)*unitValue
            getView().showConverted(Double.parseDouble(df.format((moneyzz/
                    Double.valueOf(currencies.get(posOfCurrency).getMedianRate())*
                    currencies.get(posOfCurrency).getUnitValue()))));
        }else{
            //(Moneyzz*MedianRate)/unitValue
            getView().showConverted(Double.parseDouble(df.format((moneyzz*
                    Double.valueOf(currencies.get(posOfCurrency).getMedianRate())/
                    currencies.get(posOfCurrency).getUnitValue()))));
        }
    }

    public boolean isConverted() {
        return fromKuna;
    }

    @Override
    public void destroy() {
        currencies.clear();
        posOfCurrency = 0;
        fromKuna = true;
    }
}
