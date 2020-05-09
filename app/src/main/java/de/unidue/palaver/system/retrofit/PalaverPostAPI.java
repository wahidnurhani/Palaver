package de.unidue.palaver.system.retrofit;

import de.unidue.palaver.system.engine.JSONBuilder;
import de.unidue.palaver.system.model.Message;
import de.unidue.palaver.system.model.User;
import de.unidue.palaver.system.values.StringValue;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PalaverPostAPI {

    String BASE_URL = StringValue.APICmd.BASE_URL;

    @POST(StringValue.APICmd.GET_ALL_FRIENDS)
    Call<DataServerResponseList<String>> getFriends(@Body User body);

    @POST(StringValue.APICmd.VALIDATE)
    Call<DataServerResponseList<String>> validate(@Body User body);

    @POST(StringValue.APICmd.REGISTER)
    Call<DataServerResponseList<String>> register(@Body User body);

    @POST(StringValue.APICmd.ADD_FRIEND)
    Call<DataServerResponseList<String>> addFriend(@Body User.AndFriend body);

    @POST(StringValue.APICmd.GET_MESSAGE)
    Call<DataServerResponseList<Message>> getMessage(@Body JSONBuilder.UserAndRecipient body);

    @POST(StringValue.APICmd.SEND_MESSAGE)
    Call<DataServerResponse> sendMessage(@Body JSONBuilder.SendMessageBody body);
}
