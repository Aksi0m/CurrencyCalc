package aksiom.currencycalc.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import aksiom.currencycalc.CurrencyCalcApp;
import aksiom.currencycalc.R;
import aksiom.currencycalc.internals.di.components.AppComponent;
import aksiom.currencycalc.ui.currency.CurrencyCalculatorActivity;
import aksiom.currencycalc.ui.currency.CurrencyStatsActivity;
import aksiom.currencycalc.ui.currency.ExchangeRatesActivity;

public abstract class BaseActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        markCurrentNavItem();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        drawerLayout = (DrawerLayout)getLayoutInflater()
                .inflate(R.layout.activity_base, null);
        FrameLayout contentView = (FrameLayout) drawerLayout.findViewById(R.id.activity_content);
        navigationView = (NavigationView) drawerLayout.findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, null, R.string.app_name, R.string.app_name);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getLayoutInflater().inflate(layoutResID, contentView, true);
        super.setContentView(drawerLayout);
        setupComponent(CurrencyCalcApp.getAppComponent());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                if(drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawers();
                }else{
                    drawerLayout.openDrawer(GravityCompat.START);
                }

                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.currency_calc:
                if(this instanceof CurrencyCalculatorActivity) break;
                startActivity(new Intent(this, CurrencyCalculatorActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                break;
            case R.id.exchange_rate:
                if(this instanceof ExchangeRatesActivity) break;
                startActivity(new Intent(this, ExchangeRatesActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                break;
            case R.id.currency_compare:
                if(this instanceof CurrencyStatsActivity) break;
                startActivity(new Intent(this, CurrencyStatsActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                break;
        }
        item.setChecked(true);
        drawerLayout.closeDrawers();
        return true;
    }

    /**
     * Marks the item inside the NavigationView that represents the current location
     * in the applications navigation
     */
    private void markCurrentNavItem() {
        for (int i = 0; i < navigationView.getMenu().size(); i++) {
            navigationView.getMenu().getItem(i).setChecked(false);
        }
        if(this instanceof CurrencyCalculatorActivity){
            navigationView.getMenu().findItem(R.id.currency_calc).setChecked(true);
        }else if(this instanceof ExchangeRatesActivity){
            navigationView.getMenu().findItem(R.id.exchange_rate).setChecked(true);
        }else if(this instanceof CurrencyStatsActivity){
            navigationView.getMenu().findItem(R.id.currency_compare).setChecked(true);
        }
    }

    protected abstract void setupComponent(AppComponent appComponent);
}
