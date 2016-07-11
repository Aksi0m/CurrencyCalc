package aksiom.currencycalc.internals.di.modules;

import android.content.Context;

import aksiom.currencycalc.internals.di.scope.PerApp;
import aksiom.currencycalc.networking.HnbAPI;
import aksiom.currencycalc.support.NetworkManager;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Aksiom on 6/29/2016.
 */
@Module
public class NetworkModule {

    private Context context;

    public NetworkModule(Context context) {
        this.context = context;
    }

    @Provides @PerApp public NetworkManager networkManager(){
        return new NetworkManager(context);
    }

    @Provides @PerApp public HnbAPI provideHnbAPI(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HnbAPI.BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(HnbAPI.class);
    }
}
