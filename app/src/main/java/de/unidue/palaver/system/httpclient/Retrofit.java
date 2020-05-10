package de.unidue.palaver.system.httpclient;

import java.io.IOException;

import de.unidue.palaver.system.model.StackApiResponse;
import de.unidue.palaver.system.model.StackApiResponseDate;
import de.unidue.palaver.system.model.StackApiResponseList;
import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.model.Message;
import de.unidue.palaver.system.model.User;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Retrofit {
    private PalaverPostAPI service;
    private Call<StackApiResponseList<String>> callListString;

    public Retrofit() {
        retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(PalaverPostAPI.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(PalaverPostAPI.class);
    }

    public Response<StackApiResponseList<String>> authenticate(User user)
            throws IOException {
        callListString = service.validate(user);
        return callListString.execute();
    }

    public Response<StackApiResponseList<String>> register(User user)
            throws IOException {
        callListString = service.register(user);
        return callListString.execute();
    }

    public Response<StackApiResponseList<String>> fetchAllFriend(User user)
            throws IOException {
        callListString = service.getFriends(user);
        return callListString.execute();
    }

    public Response<StackApiResponseList<String>> addFriend(User user, Friend friend)
            throws IOException {
        JSONBuilder.UserAndFriend userAndFriend = new JSONBuilder.UserAndFriend(user,friend);
        callListString = service.addFriend(userAndFriend);
        return callListString.execute();
    }

    public Response<StackApiResponseList<Message>> getMessage(JSONBuilder.UserAndRecipient body)
            throws IOException {
        Call<StackApiResponseList<Message>> call = service.getMessage(body);
        return call.execute();
    }

    public Response<StackApiResponseDate> sendMessage(JSONBuilder.SendMessageBody body)
            throws IOException {
        Call<StackApiResponseDate> call = service.sendMessage(body);
        return call.execute();
    }
}
