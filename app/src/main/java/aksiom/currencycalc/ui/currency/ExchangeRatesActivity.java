package aksiom.currencycalc.ui.currency;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.joda.time.LocalDate;

import java.util.List;

import javax.inject.Inject;

import aksiom.currencycalc.R;
import aksiom.currencycalc.adapters.CurrencyRecyclerAdapter;
import aksiom.currencycalc.internals.di.components.AppComponent;
import aksiom.currencycalc.internals.di.components.DaggerCurrencyComponent;
import aksiom.currencycalc.internals.mvp.presenters.ExchangeRatesPresenter;
import aksiom.currencycalc.internals.mvp.views.ExchangeRatesView;
import aksiom.currencycalc.realm.Currency;
import aksiom.currencycalc.ui.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ExchangeRatesActivity extends BaseActivity implements ExchangeRatesView {

    @Inject
    ExchangeRatesPresenter presenter;

    @BindView(R.id.pb)
    ProgressBar pb;

    @BindView(R.id.tv_no_data)
    TextView tvNoData;

    @BindView(R.id.rv_exchange_rates)
    RecyclerView rvExchangeRates;

    private CurrencyRecyclerAdapter adapter;
    private LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_rates);
        ButterKnife.bind(this);
        presenter.setView(this);
        setTitle(getString(R.string.exchange_rate_title) + LocalDate.now().toString());
    }


    @Override
    protected void setupComponent(AppComponent appComponent) {
        DaggerCurrencyComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.loadData();
    }

    @Override
    public void populateList(List<Currency> data) {
        adapter = new CurrencyRecyclerAdapter(data, CurrencyRecyclerAdapter.DETAIL);
        rvExchangeRates.setLayoutManager(manager);
        rvExchangeRates.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showLoading() {
        pb.setVisibility(View.VISIBLE);
        tvNoData.setVisibility(View.INVISIBLE);
        rvExchangeRates.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showError(String error) {
        Snackbar.make(findViewById(android.R.id.content), error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showContent() {
        pb.setVisibility(View.INVISIBLE);
        tvNoData.setVisibility(View.INVISIBLE);
        rvExchangeRates.setVisibility(View.VISIBLE);
    }

    @Override
    public void showEmpty() {
        pb.setVisibility(View.INVISIBLE);
        tvNoData.setVisibility(View.VISIBLE);
        rvExchangeRates.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destroy();
        presenter = null;
    }
}
