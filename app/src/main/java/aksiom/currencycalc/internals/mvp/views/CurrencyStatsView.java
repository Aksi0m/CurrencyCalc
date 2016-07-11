package aksiom.currencycalc.internals.mvp.views;

import com.github.mikephil.charting.data.LineData;

import java.util.List;

import aksiom.currencycalc.internals.mvp.MVP;
import aksiom.currencycalc.realm.Currency;

/**
 * Created by Aksiom on 7/1/2016.
 */
public interface CurrencyStatsView extends MVP.View{

    void populateList(List<Currency> data);

    void loadChartData(LineData data);
}
