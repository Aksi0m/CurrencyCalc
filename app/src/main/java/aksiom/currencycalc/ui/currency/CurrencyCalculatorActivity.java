package aksiom.currencycalc.ui.currency;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import aksiom.currencycalc.R;
import aksiom.currencycalc.adapters.CurrencyRecyclerAdapter;
import aksiom.currencycalc.internals.di.components.AppComponent;
import aksiom.currencycalc.internals.di.components.DaggerCurrencyComponent;
import aksiom.currencycalc.internals.mvp.presenters.CurrencyCalculatorPresenter;
import aksiom.currencycalc.internals.mvp.views.CurrencyCalculatorView;
import aksiom.currencycalc.realm.Currency;
import aksiom.currencycalc.ui.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;

public class CurrencyCalculatorActivity extends BaseActivity implements CurrencyCalculatorView {

    private static final String TAG = "CurrencyCalculatorActivity";

    @Inject
    CurrencyCalculatorPresenter presenter;

    @BindView(R.id.pb)
    ProgressBar pb;

    @BindView(R.id.tv_no_data)
    TextView tvNoData;

    @BindView(R.id.cv_calculator)
    CardView cvCalculator;

    @BindView(R.id.rv_currency)
    RecyclerView rvCurrency;

    @BindView(R.id.et_number_2)
    EditText etNumber2;

    @BindView(R.id.et_number)
    EditText etNumber1;

    @BindView(R.id.tv_covert_from_currency_code)
    TextView tvCurrencyCode2;

    @BindView(R.id.tv_covert_to_currency_code)
    TextView tvCurrencyCode1;

    private CurrencyRecyclerAdapter adapter;
    private GridLayoutManager manager = new GridLayoutManager(this, 2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_calculator);
        ButterKnife.bind(this);
        setTitle(getString(R.string.kuna_converter));
        presenter.setView(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.loadData();
    }

    @Override
    protected void setupComponent(AppComponent appComponent) {
        DaggerCurrencyComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    public void showLoading() {
        pb.setVisibility(View.VISIBLE);
        tvNoData.setVisibility(View.INVISIBLE);
        rvCurrency.setVisibility(View.INVISIBLE);
        cvCalculator.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showError(String error) {
        Snackbar.make(findViewById(android.R.id.content), error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showContent() {
        pb.setVisibility(View.INVISIBLE);
        tvNoData.setVisibility(View.INVISIBLE);
        rvCurrency.setVisibility(View.VISIBLE);
        cvCalculator.setVisibility(View.VISIBLE);
        sendToConvert(etNumber1.getText().toString());
    }

    @Override
    public void showEmpty() {
        pb.setVisibility(View.INVISIBLE);
        tvNoData.setVisibility(View.VISIBLE);
        cvCalculator.setVisibility(View.INVISIBLE);
        rvCurrency.setVisibility(View.INVISIBLE);
    }

    @Override
    public void populateList(List<Currency> data) {
        adapter = new CurrencyRecyclerAdapter(data, CurrencyRecyclerAdapter.SIMPLE, true);
        rvCurrency.setLayoutManager(manager);
        rvCurrency.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnClickListener(new CurrencyRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                presenter.selectCurrency(position);
                sendToConvert(etNumber1.getText().toString());
            }
        });
    }

    @OnClick(R.id.ib_swap)
    void onSwapCurrenciesClick(){
        presenter.swapCurrencies();
    }

    @Override
    public void swapCurrencies() {
        String cc1 = tvCurrencyCode1.getText().toString();
        String cc2 = tvCurrencyCode2.getText().toString();
        tvCurrencyCode1.setText(cc2);
        tvCurrencyCode2.setText(cc1);
        String number1 = etNumber1.getText().toString();
        String number2 = etNumber2.getText().toString();
        etNumber1.setText(number2);
        etNumber2.setText(number1);
    }

    @Override
    public void changeCurrency(String currencyCode) {
        if(presenter.isConverted()) tvCurrencyCode1.setText(currencyCode);
        else tvCurrencyCode2.setText(currencyCode);
    }

    @OnTextChanged(value = R.id.et_number, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void onTextChanged(CharSequence text) {
        sendToConvert(text.toString());
    }

    @OnFocusChange(R.id.et_number)
    void onFocusChanged(boolean focused) {
        if(!focused){
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE); imm.hideSoftInputFromWindow(etNumber1.getWindowToken(), 0);
        }
    }

    @Override
    public void showConverted(double result) {
        etNumber2.setText(String.valueOf(result));
    }

    private void sendToConvert(String number) {
        presenter.convert((number.trim().equals("") ? 0D : Double.valueOf(number.trim())));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destroy();
        presenter = null;
    }
}
