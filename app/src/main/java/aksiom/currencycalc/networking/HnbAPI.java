package aksiom.currencycalc.networking;

import java.util.List;

import aksiom.currencycalc.realm.Currency;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Aksiom on 6/29/2016.
 */
public interface HnbAPI {

    String BASE_URL = "http://hnbex.eu/api/v1/";

    @GET("rates/daily/")
    Observable<List<Currency>> listExchangeRates(@Query("date") String date);

}
