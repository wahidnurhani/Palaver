package de.unidue.palaver.system.retrofit;

import com.google.gson.annotations.SerializedName;

import de.unidue.palaver.system.model.DateTime;

public class DataServerResponse extends ServerResponse {

    @SerializedName("Data")
    private DateTime dateTime;

    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "DataServerResponse{" +
                "dateTime=" + dateTime +
                '}';
    }
}
