package de.unidue.palaver.system.httpclient;

import de.unidue.palaver.system.model.StackApiResponseDate;
import de.unidue.palaver.system.model.StackApiResponseList;
import de.unidue.palaver.system.model.Message;
import de.unidue.palaver.system.model.User;
import de.unidue.palaver.system.model.StringValue;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PalaverPostAPI {

    String BASE_URL = StringValue.APICmd.BASE_URL;

    @POST(StringValue.APICmd.GET_ALL_FRIENDS)
    Call<StackApiResponseList<String>> getFriends(@Body User body);

    @POST(StringValue.APICmd.VALIDATE)
    Call<StackApiResponseList<String>> validate(@Body User body);

    @POST(StringValue.APICmd.REGISTER)
    Call<StackApiResponseList<String>> register(@Body User body);

    @POST(StringValue.APICmd.ADD_FRIEND)
    Call<StackApiResponseList<String>> addFriend(@Body JSONBuilder.UserAndFriend body);

    @POST(StringValue.APICmd.GET_MESSAGE)
    Call<StackApiResponseList<Message>> getMessage(@Body JSONBuilder.UserAndRecipient body);

    @POST(StringValue.APICmd.SEND_MESSAGE)
    Call<StackApiResponseDate> sendMessage(@Body JSONBuilder.SendMessageBody body);
}
