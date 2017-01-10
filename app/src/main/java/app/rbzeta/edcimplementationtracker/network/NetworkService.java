package app.rbzeta.edcimplementationtracker.network;

import android.support.v4.util.LruCache;

import java.util.concurrent.TimeUnit;

import app.rbzeta.edcimplementationtracker.application.AppConfig;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Robyn on 12/30/2016.
 */

public class NetworkService {
    private static final String BASE_URL = AppConfig.BASE_URL;
    private NetworkApi mNetworkAPI;
    private OkHttpClient okHttpClient;
    private LruCache<Class<?>, Observable<?>> apiObservables;

    public NetworkService() {
        this(BASE_URL);
    }

    private NetworkService(String baseUrl) {
        okHttpClient = buildClient();
        apiObservables = new LruCache<>(10);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        mNetworkAPI = retrofit.create(NetworkApi.class);

    }

    public NetworkApi getNetworkAPI() {
        return mNetworkAPI;
    }

    private OkHttpClient buildClient() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.addInterceptor(chain -> {
            //Response response = chain.proceed(chain.request());

            return chain.proceed(chain.request());
        });

        builder.addInterceptor(chain -> {

            Request request = chain.request().newBuilder().addHeader("Accept", "application/json").build();
            return chain.proceed(request);
        });

        builder.connectTimeout(1, TimeUnit.MINUTES);

        return builder.build();
    }

    public void clearCache() {
        apiObservables.evictAll();
    }

    public Observable<?> getPreparedObservable(Observable<?> unPreparedObservable,
                                               Class<?> clazz,
                                               boolean cacheObservable,
                                               boolean useCache) {

        Observable<?> preparedObservable = null;

        if (useCache)
            preparedObservable = apiObservables.get(clazz);

        if (preparedObservable != null)
            return preparedObservable;

        preparedObservable = unPreparedObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        if (cacheObservable) {
            preparedObservable = preparedObservable.cache();
            apiObservables.put(clazz, preparedObservable);
        }

        return preparedObservable;
    }
}
