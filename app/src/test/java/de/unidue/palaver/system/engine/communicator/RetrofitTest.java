package de.unidue.palaver.system.engine.communicator;

import org.junit.Test;

import java.io.IOException;
import java.util.Date;

import de.unidue.palaver.system.engine.JSONBuilder;
import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.model.Message;
import de.unidue.palaver.system.model.User;
import de.unidue.palaver.system.retrofit.DataServerResponseList;
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
    public void retrofitLoginTest(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PalaverPostAPI.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PalaverPostAPI service = retrofit.create(PalaverPostAPI.class);

        Call<DataServerResponseList<String>> call= service.validate(new User("1991", "1991"));
        call.enqueue(new Callback<DataServerResponseList<String>>() {
            @Override
            public void onResponse(Call<DataServerResponseList<String>> call, Response<DataServerResponseList<String>> response) {
                System.out.println(response.body().toString());
            }

            @Override
            public void onFailure(Call<DataServerResponseList<String>> call, Throwable t) {
                System.out.println("failur");
            }
        });
    }

    @Test
    public void retrovitFetchFriendTest() throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PalaverPostAPI.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PalaverPostAPI service = retrofit.create(PalaverPostAPI.class);

        Call<DataServerResponseList<String>> call= service.getFriends(new User("test1991", "test1991"));
        Response<DataServerResponseList<String>> response = call.execute();

        System.out.println(response.body().getInfo());

        if(response.body().getMessageType()==1){
            for(String str: response.body().getDatas()){
                System.out.println(str);
            }
        }

    }

    @Test
    public void retrovitAddFriend() throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PalaverPostAPI.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PalaverPostAPI service = retrofit.create(PalaverPostAPI.class);

        User.AndFriend userAndFriend = new User.AndFriend("gawang", "gawang", "bola");
        Call<DataServerResponseList<String>> call= service.addFriend(userAndFriend);
        Response<DataServerResponseList<String>> response = call.execute();

        System.out.println(response.body().getInfo());

    }

    @Test
    public void retrofitGetAllChat() throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PalaverPostAPI.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PalaverPostAPI service = retrofit.create(PalaverPostAPI.class);
        User user = new User("test1991", "test1991");
        Friend friend = new Friend("test1992");
        JSONBuilder.UserAndRecipient body = new JSONBuilder.UserAndRecipient(user, friend);
        Call<DataServerResponseList<Message>> call= service.getMessage(body);
        Response<DataServerResponseList<Message>> response = call.execute();
        System.out.println(response.body().getInfo());

        if(response!=null){
            for(Message message : response.body().getDatas()){
                System.out.println(message.toString());
            }
        }
    }

    @Test
    public void retrofitSendMessageTest() throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PalaverPostAPI.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PalaverPostAPI service = retrofit.create(PalaverPostAPI.class);
        User user = new User("test1991", "test1991");
        Friend friend = new Friend("test1992");
        Message message = new Message(user.getUserName(),
                friend.getUsername(), "Hallot test retrofit", new Date());

        JSONBuilder.SendMessageBody body = new JSONBuilder.SendMessageBody(user, friend, message);
        Call<DataServerResponse> call= service.sendMessage(body);
        Response<DataServerResponse> response = call.execute();
        System.out.println(response.body().getInfo());

        if(response!=null){
            System.out.println(response.body().getDateTime().getDateTime());
        }
    }

}
