package de.unidue.palaver.system.retrofit;

import java.io.IOException;
import java.util.TimeZone;

import de.unidue.palaver.system.engine.JSONBuilder;
import de.unidue.palaver.system.engine.PalaverEngine;
import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.model.Message;
import de.unidue.palaver.system.model.User;
import de.unidue.palaver.ui.ProgressDialog;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class NewCommunicator {
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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PalaverPostAPI.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(PalaverPostAPI.class);
    }

    public Response<DataServerResponseList<String>> authenticate(User user) throws IOException {
        Call<DataServerResponseList<String>> call= service.validate(user);
        return call.execute();
    }

    public Response<DataServerResponseList<String>> register(User user) throws IOException {
        Call<DataServerResponseList<String>> call= service.register(user);
        return call.execute();
    }

    public Response<DataServerResponseList<String>> fetchAllFriend(User user) throws IOException {
        Call<DataServerResponseList<String>> call= service.getFriends(user);
        return call.execute();
    }

    public Response<DataServerResponseList<String>> addFriend(User user, Friend friend) throws IOException {
        User.AndFriend userAndFriend = new User.AndFriend(
                user.getUserName(),
                user.getPassword(),
                friend.getUsername());

        Call<DataServerResponseList<String>> call = service.addFriend(userAndFriend);
        return call.execute();
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
