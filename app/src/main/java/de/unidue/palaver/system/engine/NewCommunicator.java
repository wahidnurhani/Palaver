package de.unidue.palaver.system.engine;

import android.app.Activity;
import android.content.Context;

import java.util.TimeZone;

import de.unidue.palaver.system.SessionManager;
import de.unidue.palaver.system.model.User;
import de.unidue.palaver.system.retrofit.DataServerResponse;
import de.unidue.palaver.system.retrofit.PalaverPostAPI;
import de.unidue.palaver.ui.ProgressDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class NewCommunicator {
    private Retrofit retrofit;
    private PalaverPostAPI service;
    private PalaverEngine palaverEngine;
    private ProgressDialog progressDialog;
    private String serverTimeZone;


    public NewCommunicator() {
        TimeZone timeZone= TimeZone.getTimeZone("Europe/Berlin");
        int offset = 3600000/timeZone.getRawOffset();
        int dstOffset = timeZone.getDSTSavings()/3600000;
        int timezoneInt = offset+dstOffset;
        serverTimeZone = "+"+timezoneInt;
        palaverEngine = PalaverEngine.getPalaverEngineInstance();
        retrofit = new Retrofit.Builder()
                .baseUrl(PalaverPostAPI.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(PalaverPostAPI.class);
    }

    public void authenticate(Context applicationContext, Activity activity, User user){
        Call<DataServerResponse<String>> call= service.validate(user);
        progressDialog = new ProgressDialog(activity);
        progressDialog.startDialog();
        call.enqueue(new Callback<DataServerResponse<String>>() {
            @Override
            public void onResponse(Call<DataServerResponse<String>> call, Response<DataServerResponse<String>> response) {
                if(response.body().getMessageType()==1){
                    SessionManager sessionManager = SessionManager.getSessionManagerInstance(applicationContext);
                    sessionManager.setUser(user);
                    sessionManager.startSession(user.getUserName(),
                            user.getPassword());
                    progressDialog.dismissDialog();
                    palaverEngine.hadleOpenSplashScreenActivityRequest(activity);
                } else {
                    progressDialog.dismissDialog();
                    palaverEngine.handleShowToastRequest(applicationContext, response.body().getInfo());
                }
            }

            @Override
            public void onFailure(Call<DataServerResponse<String>> call, Throwable t) {
                palaverEngine.handleShowToastRequest(applicationContext, "connection to server failed");
                progressDialog.dismissDialog();
            }
        });
    }

    public void register(Context applicationContext, Activity activity, User user) {
        Call<DataServerResponse<String>> call= service.register(user);
        progressDialog = new ProgressDialog(activity);
        progressDialog.startDialog();
        call.enqueue(new Callback<DataServerResponse<String>>() {
            @Override
            public void onResponse(Call<DataServerResponse<String>> call, Response<DataServerResponse<String>> response) {
                if(response.body().getMessageType()==1){
                    progressDialog.dismissDialog();
                    palaverEngine.handleShowToastRequest(applicationContext, response.body().getInfo());
                    palaverEngine.handleOpenLoginActivityRequest(activity);
                } else {
                    progressDialog.dismissDialog();
                    palaverEngine.handleShowToastRequest(applicationContext, response.body().getInfo());
                    palaverEngine.handleShowToastRequest(applicationContext, response.body().getInfo());
                }
            }

            @Override
            public void onFailure(Call<DataServerResponse<String>> call, Throwable t) {
                palaverEngine.handleShowToastRequest(applicationContext, "connection to server failed");
                progressDialog.dismissDialog();
            }
        });
    }
}
