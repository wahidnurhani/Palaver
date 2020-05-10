package de.unidue.palaver.system.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StackApiResponse {

    @SerializedName(StringValue.JSONKeyName.MSG_TYPE)
    @Expose
    private int messageType;

    @Expose
    @SerializedName(StringValue.JSONKeyName.INFO)
    private String info;

    public int getMessageType() {
        return messageType;
    }

    public String getInfo() {
        return info;
    }


    @Override
    public String toString() {
        return "StackApiResponse{" +
                "messageType=" + messageType +
                ", info='" + info + '\'' +
                '}';
    }
}
