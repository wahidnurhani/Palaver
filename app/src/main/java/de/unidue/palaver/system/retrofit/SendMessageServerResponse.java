package de.unidue.palaver.system.retrofit;

public class SendMessageServerResponse extends BaseServerResponse {

    private SendMessageServerResponseData responseData;

    public SendMessageServerResponseData getResponseData() {
        return responseData;
    }

    public void setResponseData(SendMessageServerResponseData responseData) {
        this.responseData = responseData;
    }

    @Override
    public String toString() {
        return "SendMessageServerResponse{" +
                "responseData=" + responseData +
                '}';
    }
}
