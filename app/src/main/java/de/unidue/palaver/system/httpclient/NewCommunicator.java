package de.unidue.palaver.system.httpclient;

import java.io.IOException;

import de.unidue.palaver.system.model.DataServerResponse;
import de.unidue.palaver.system.model.DataServerResponseList;
import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.model.Message;
import de.unidue.palaver.system.model.User;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class NewCommunicator {
    private PalaverPostAPI service;
    private Retrofit retrofit;
    private Call<DataServerResponseList<String>> callListString;

    public NewCommunicator() {
        retrofit = new Retrofit.Builder()
                .baseUrl(PalaverPostAPI.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(PalaverPostAPI.class);
    }

    public Response<DataServerResponseList<String>> authenticate(User user) throws IOException {
        callListString = service.validate(user);
        return callListString.execute();
    }

    public Response<DataServerResponseList<String>> register(User user) throws IOException {
        callListString = service.register(user);
        return callListString.execute();
    }

    public Response<DataServerResponseList<String>> fetchAllFriend(User user) throws IOException {
        callListString = service.getFriends(user);
        return callListString.execute();
    }

    public Response<DataServerResponseList<String>> addFriend(User user, Friend friend) throws IOException {
        JSONBuilder.UserAndFriend userAndFriend = new JSONBuilder.UserAndFriend(user,friend);

        callListString = service.addFriend(userAndFriend);
        return callListString.execute();
    }

    public Response<DataServerResponseList<Message>> getMessage(JSONBuilder.UserAndRecipient body) throws IOException {
        Call<DataServerResponseList<Message>> call = service.getMessage(body);
        return call.execute();
    }

    public Response<DataServerResponse> sendMessage(JSONBuilder.SendMessageBody body) throws IOException {
        Call<DataServerResponse> call = service.sendMessage(body);
        return call.execute();
    }
}
