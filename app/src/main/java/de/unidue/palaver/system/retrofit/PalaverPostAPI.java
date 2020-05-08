package de.unidue.palaver.system.retrofit;

import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.model.User;
import de.unidue.palaver.system.values.StringValue;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PalaverPostAPI {

    String BASE_URL = StringValue.APICmd.BASE_URL;

    @POST(StringValue.APICmd.GET_ALL_FRIENDS)
    Call<DataServerResponse<Friend>> getFriends(@Body User body);

    @POST(StringValue.APICmd.VALIDATE)
    Call<DataServerResponse<String>> validate(@Body User body);

}
