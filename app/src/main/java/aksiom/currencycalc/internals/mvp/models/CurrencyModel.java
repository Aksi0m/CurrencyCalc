package aksiom.currencycalc.internals.mvp.models;

import android.util.Log;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import aksiom.currencycalc.internals.mvp.MVP;
import aksiom.currencycalc.networking.HnbAPI;
import aksiom.currencycalc.realm.Currency;
import aksiom.currencycalc.support.DateTimeManager;
import aksiom.currencycalc.support.NetworkManager;
import io.realm.Realm;
import io.realm.RealmResults;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Aksiom on 6/29/2016.
 */
public class CurrencyModel implements MVP.Model {

    private static final String TAG = CurrencyModel.class.getSimpleName();

    private HnbAPI hnbAPI;
    private Realm realm;
    List<Currency> currencyCached = new ArrayList<>();
    private NetworkManager networkManager;

    public CurrencyModel(HnbAPI hnbAPI, Realm realm, NetworkManager networkManager) {
        this.hnbAPI = hnbAPI;
        this.realm = realm;
        this.networkManager = networkManager;
    }

    /**
     * Get exchange rates for today
     *
     * @param onDataLoaded
     */
    public void getExchangeRatesData(final OnDataLoaded<List<Currency>> onDataLoaded) {
        getExchangeRatesData(onDataLoaded, 1);
    }

    /**
     * * Get exchange rates for a specific date
     *
     * @param onDataLoaded
     * @param history
     */
    public void getExchangeRatesData(final OnDataLoaded<List<Currency>> onDataLoaded, int history) {
        if (networkManager.isConnected()) {
            for (LocalDate date = LocalDate.now(); date.isAfter(LocalDate.now().minusDays(history));
                 date = date.minusDays(1)) {

                String dateQuery = DateTimeManager.parseFromDate(date, DateTimeManager.HNB_DATE);
                final LocalDate finalDate = date;
                hnbAPI.listExchangeRates(dateQuery)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<List<Currency>>() {
                                       @Override
                                       public void onCompleted() {}

                                       @Override
                                       public void onError(Throwable e) {
                                           Log.d(TAG, "onError() called with: " + "e = [" + e + "]");
                                           currencyCached.clear();
                                           currencyCached = checkIfCached(finalDate.toDate());
                                           if (currencyCached.isEmpty()) {
                                               onDataLoaded.onFail(e.toString());
                                           } else {
                                               onDataLoaded.onSuccess(currencyCached);
                                           }
                                       }

                                       @Override
                                       public void onNext(List<Currency> currencies) {
                                           Log.d(TAG, "onNext() called with: " + "currencies = ["
                                                   + currencies + "]");
                                           cache(currencies, finalDate.toDate());
                                           onDataLoaded.onSuccess(currencies);
                                       }
                                   }
                        );
            }
        } else {
            for (LocalDate date = LocalDate.now(); date.isAfter(LocalDate.now().minusDays(history));
                 date = date.minusDays(1)) {

                currencyCached.clear();
                currencyCached = checkIfCached(date.toDate());
                if (currencyCached.isEmpty()) {
                    onDataLoaded.onFail("No cached data and not Internet");
                    break;
                } else {
                    onDataLoaded.onSuccess(currencyCached);
                }
            }
        }
    }

    private RealmResults<Currency> checkIfCached(Date date) {
        return realm.where(Currency.class)
                .equalTo("downloadDate", date)
                .findAll();
    }

    @Override
    public void cache(List<?> data, Date date) {
        List<Currency> currencies = new ArrayList<>((Collection<? extends Currency>) data);
        realm.beginTransaction();
        for (Currency currency : currencies) {
            currency.setDownloadDate(date);
            realm.copyToRealm(currency);
        }
        realm.commitTransaction();
    }

    @Override
    public void clearCache() {
        try {
            realm.delete(Currency.class);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
}
