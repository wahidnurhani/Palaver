package de.unidue.palaver.httpclient;

import java.io.IOException;

import de.unidue.palaver.model.StackApiResponseDate;
import de.unidue.palaver.model.StackApiResponseList;
import de.unidue.palaver.model.Friend;
import de.unidue.palaver.model.Message;
import de.unidue.palaver.model.User;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitHttpClient implements IHttpClient{
    private PalaverPostAPI service;
    private Call<StackApiResponseList<String>> callListString;

    public RetrofitHttpClient() {
        retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(PalaverPostAPI.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(PalaverPostAPI.class);
    }

    @Override
    public Response<StackApiResponseList<String>> authenticate(User user)
            throws IOException {
        callListString = service.validate(user);
        return callListString.execute();
    }

    @Override
    public Response<StackApiResponseList<String>> register(User user)
            throws IOException {
        callListString = service.register(user);
        return callListString.execute();
    }

    @Override
    public Response<StackApiResponseList<String>> fetchAllFriend(User user)
            throws IOException {
        callListString = service.getFriends(user);
        return callListString.execute();
    }

    @Override
    public Response<StackApiResponseList<String>> addFriend(User user, Friend friend)
            throws IOException {
        JSONBuilder.UserAndFriend userAndFriend = new JSONBuilder.UserAndFriend(user,friend);
        callListString = service.addFriend(userAndFriend);
        return callListString.execute();
    }

    @Override
    public Response<StackApiResponseList<String>> removeFriend(User user, Friend friend)
            throws IOException {
        JSONBuilder.UserAndFriend userAndFriend = new JSONBuilder.UserAndFriend(user,friend);
        callListString = service.removeFriend(userAndFriend);
        return callListString.execute();
    }

    @Override
    public Response<StackApiResponseList<Message>> getMessage(JSONBuilder.UserAndRecipient body)
            throws IOException {
        Call<StackApiResponseList<Message>> call = service.getMessage(body);
        return call.execute();
    }

    @Override
    public Response<StackApiResponseList<Message>> getMessageOffset(JSONBuilder.GetMessageOffset body)
            throws IOException {
        Call<StackApiResponseList<Message>> call = service.getMessageOffset(body);
        return call.execute();
    }

    @Override
    public Response<StackApiResponseDate> sendMessage(JSONBuilder.SendMessageBody body)
            throws IOException {
        Call<StackApiResponseDate> call = service.sendMessage(body);
        return call.execute();
    }

    @Override
    public Response<StackApiResponseList<String>> changePassword(JSONBuilder.ChangePassWord body)
            throws IOException {
        Call<StackApiResponseList<String>> call = service.changePassword(body);
        return call.execute();
    }

    @Override
    public Response<StackApiResponseList<String>> pushToken(JSONBuilder.PushToken body)
            throws IOException {
        Call<StackApiResponseList<String>> call = service.pushToken(body);
        return call.execute();
    }

}
