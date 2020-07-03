package de.unidue.palaver.httpclient;

import java.io.IOException;

import de.unidue.palaver.model.Friend;
import de.unidue.palaver.model.Message;
import de.unidue.palaver.model.StackApiResponseDate;
import de.unidue.palaver.model.StackApiResponseList;
import de.unidue.palaver.model.User;
import retrofit2.Response;

public interface IHttpClient {
    Response<StackApiResponseList<String>> authenticate(User user)
            throws IOException;

    Response<StackApiResponseList<String>> register(User user)
            throws IOException;

    Response<StackApiResponseList<String>> fetchAllFriend(User user)
            throws IOException;

    Response<StackApiResponseList<String>> addFriend(User user, Friend friend)
            throws IOException;

    Response<StackApiResponseList<String>> removeFriend(User user, Friend friend)
            throws IOException;

    Response<StackApiResponseList<Message>> getMessage(JSONBuilder.UserAndRecipient body)
            throws IOException;

    Response<StackApiResponseList<Message>> getMessageOffset(JSONBuilder.GetMessageOffset body)
            throws IOException;

    Response<StackApiResponseDate> sendMessage(JSONBuilder.SendMessageBody body)
            throws IOException;

    Response<StackApiResponseList<String>> changePassword(JSONBuilder.ChangePassWord body)
            throws IOException;

    Response<StackApiResponseList<String>> pushToken(JSONBuilder.PushToken body)
            throws IOException;
}
