package de.unidue.palaver.httpclient;

import org.junit.Test;

import java.io.IOException;
import java.util.Date;

import de.unidue.palaver.model.StackApiResponseList;
import de.unidue.palaver.model.Friend;
import de.unidue.palaver.model.Message;
import de.unidue.palaver.model.StackApiResponseDate;
import de.unidue.palaver.model.User;
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

        Call<StackApiResponseList<String>> call= service.validate(new User("1991", "1991"));
        call.enqueue(new Callback<StackApiResponseList<String>>() {
            @Override
            public void onResponse(Call<StackApiResponseList<String>> call, Response<StackApiResponseList<String>> response) {
                assert response.body() != null;
                System.out.println(response.body().toString());
            }

            @Override
            public void onFailure(Call<StackApiResponseList<String>> call, Throwable t) {
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

        Call<StackApiResponseList<String>> call= service.getFriends(new User("test1991", "test1991"));
        Response<StackApiResponseList<String>> response = call.execute();

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

        User user = new User("gawang", "gawang");
        Friend friend = new Friend("bola");

        JSONBuilder.UserAndFriend userAndFriend = new JSONBuilder.UserAndFriend(user, friend);
        Call<StackApiResponseList<String>> call= service.addFriend(userAndFriend);
        Response<StackApiResponseList<String>> response = call.execute();

        assert response.body() != null;
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
        Call<StackApiResponseList<Message>> call= service.getMessage(body);
        Response<StackApiResponseList<Message>> response = call.execute();

        assert response.body() != null;
        System.out.println(response.body().getInfo());
        for(Message message : response.body().getDatas()){
            System.out.println(message.toString());
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
        Message message = new Message(friend.getUsername(),user.getUserName(),
                friend.getUsername(), "Hallot test retrofit", new Date());

        JSONBuilder.SendMessageBody body = new JSONBuilder.SendMessageBody(user, friend, message);
        Call<StackApiResponseDate> call= service.sendMessage(body);
        Response<StackApiResponseDate> response = call.execute();

        assert response.body() != null;
        System.out.println(response.body().getInfo());
        System.out.println(response.body().getDateTime().getDateTime());
    }

}
