package de.unidue.palaver.httpclient;

import de.unidue.palaver.model.StackApiResponseDate;
import de.unidue.palaver.model.StackApiResponseList;
import de.unidue.palaver.model.Message;
import de.unidue.palaver.model.User;
import de.unidue.palaver.model.StringValue;
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

    @POST(StringValue.APICmd.CHANGE_PASSWORD)
    Call<StackApiResponseList<String>> changePassword(@Body JSONBuilder.ChangePassWord body);

    @POST(StringValue.APICmd.PUSHTOKEN)
    Call<StackApiResponseList<String>> pushToken(@Body JSONBuilder.PushToken body);

    @POST(StringValue.APICmd.GET_MESSAGE_OFFSET)
    Call<StackApiResponseList<Message>> getMessageOffset(@Body JSONBuilder.GetMessageOffset body);

    @POST(StringValue.APICmd.REMOVE_FRIEND)
    Call<StackApiResponseList<String>> removeFriend(@Body JSONBuilder.UserAndFriend userAndFriend);
}
