package de.unidue.palaver.system.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.unidue.palaver.system.values.StringValue;

public class BaseServerResponse {

    @SerializedName(StringValue.JSONKeyName.MSG_TYPE)
    @Expose
    private int messageType;

    @Expose
    @SerializedName(StringValue.JSONKeyName.INFO)
    private String info;

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "BaseServerResponse{" +
                "messageType=" + messageType +
                ", info='" + info + '\'' +
                '}';
    }
}
