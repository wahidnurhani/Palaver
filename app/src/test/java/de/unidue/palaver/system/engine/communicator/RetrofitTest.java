package de.unidue.palaver.system.engine.communicator;

import org.junit.Test;

import de.unidue.palaver.system.model.User;
import de.unidue.palaver.system.retrofit.DataServerResponse;
import de.unidue.palaver.system.retrofit.PalaverPostAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitTest {

    @Test
    public void retrofitTest(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PalaverPostAPI.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PalaverPostAPI service = retrofit.create(PalaverPostAPI.class);

        Call<DataServerResponse<String>> call= service.validate(new User("1991", "1991"));
        call.enqueue(new Callback<DataServerResponse<String>>() {
            @Override
            public void onResponse(Call<DataServerResponse<String>> call, Response<DataServerResponse<String>> response) {
                System.out.println(response.body().toString());
            }

            @Override
            public void onFailure(Call<DataServerResponse<String>> call, Throwable t) {
                System.out.println("failur");
            }
        });
    }

}
