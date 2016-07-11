package aksiom.currencycalc.internals.mvp.presenters;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

import aksiom.currencycalc.internals.mvp.MVP;
import aksiom.currencycalc.internals.mvp.models.CurrencyModel;
import aksiom.currencycalc.internals.mvp.views.CurrencyStatsView;
import aksiom.currencycalc.realm.Currency;
import aksiom.currencycalc.support.DateTimeManager;

/**
 * Created by Aksiom on 7/1/2016.
 */
public class CurrencyStatsPresenter extends MVP.Presenter<CurrencyStatsView> {

    private CurrencyModel currencyModel;
    private TreeMap<LocalDate,List<Currency>> currenciesPerDay = new TreeMap<>(new LocalDateComparator());
    private int lastNumberOfDays;
    private int selectedCurrency = 0;
    private static String error = "";

    class LocalDateComparator implements Comparator<LocalDate> {
        public int compare(LocalDate o1,LocalDate o2)
        {
            return o1.compareTo(o2);
        }
    }

    @Inject
    public CurrencyStatsPresenter(CurrencyModel currencyModel) {
        this.currencyModel = currencyModel;
    }

    public void loadData(int lastNumberOfDays){
        getView().showLoading();
        currenciesPerDay.clear();
        CurrencyStatsPresenter.error = "";
        this.lastNumberOfDays = lastNumberOfDays;
        currencyModel.getExchangeRatesData(new MVP.Model.OnDataLoaded<List<Currency>>() {
            @Override
            public void onSuccess(List<Currency> data) {
                if(CurrencyStatsPresenter.error.equals("")){
                    if(currenciesPerDay.isEmpty()) getView().populateList(data);
                    currenciesPerDay.put(LocalDate.fromDateFields(data.get(0).getDownloadDate()), data);
                    prepareChartData();
                }
            }

            @Override
            public void onFail(String error) {
                if(CurrencyStatsPresenter.error.equals("")) {
                    getView().showEmpty();
                    getView().showError(error);
                    CurrencyStatsPresenter.error = error;
                }
            }
        }, lastNumberOfDays);
    }

    private void prepareChartData(){
        if(currenciesPerDay.size() != lastNumberOfDays){
            return;
        }

        List<Entry> entries= new ArrayList<>();
        ArrayList<String> x = new ArrayList<>();

        int i = 0;
        for (Map.Entry<LocalDate,List<Currency>> entry : currenciesPerDay.entrySet()) {
            LocalDate key = entry.getKey();
            x.add(DateTimeManager.parseFromDate(key, DateTimeManager.CHART_DATE));
            entries.add(new Entry(Float.valueOf(entry.getValue().get(selectedCurrency).getMedianRate()), i++));
        }

        LineDataSet currencySet = new LineDataSet(entries, "Date");
        currencySet.setColor(Color.parseColor("#4DB6AC"));
        currencySet.setCircleColor(Color.parseColor("#4DB6AC"));
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(currencySet);
        LineData data = new LineData(x, dataSets);
        getView().showContent();
        getView().loadChartData(data);
    }


    public void initChart(LineChart lcCurrency) {
        lcCurrency.getLegend().setEnabled(false);
        lcCurrency.setDrawGridBackground(false);
        lcCurrency.setDescription("");
        lcCurrency.setTouchEnabled(true);
        lcCurrency.setDragEnabled(true);
        lcCurrency.setScaleEnabled(true);
        lcCurrency.setPinchZoom(true);

        XAxis xAxis = lcCurrency.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setSpaceBetweenLabels(2);

        YAxis rightAxis = lcCurrency.getAxisRight();
        rightAxis.setAxisMinValue(0f);
        rightAxis.setEnabled(false);
    }

    public void selectCurrency(int position) {
        this.selectedCurrency = position;
        prepareChartData();
    }

    @Override
    public void destroy() {
        CurrencyStatsPresenter.error = "";
        currenciesPerDay.clear();
        selectedCurrency = 0;
    }
}
