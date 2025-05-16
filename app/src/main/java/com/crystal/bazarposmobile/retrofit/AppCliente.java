package com.crystal.bazarposmobile.retrofit;

import android.util.Log;

import com.crystal.bazarposmobile.common.Constantes;
import com.crystal.bazarposmobile.common.SPM;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppCliente {
    private static AppCliente instance = null;
    private ApiService apiService;
    private Retrofit retrofit;
    private String baseURL;

    public AppCliente() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .build();

        baseURL = SPM.getString(Constantes.API_POSSERVICE_URL);
        if(baseURL == null){
            baseURL = Constantes.API_POSSERVICE_URL;
        }

        retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    public static AppCliente getInstance(){

        if(instance == null) {
            instance = new AppCliente();
        }else{
            String urland = SPM.getString(Constantes.API_POSSERVICE_URL);
            String urlins = instance.retrofit.baseUrl().toString();
            if(!urlins.equals(urland)){
                instance = new AppCliente();
                Log.i("logcat","new: "+instance.retrofit.baseUrl()+" - "+ SPM.getString(Constantes.API_POSSERVICE_URL));
            }
        }
        return instance;
    }
    public ApiService getApiService() {
        return apiService;
    }
}
