package de.unidue.palaver.system.model;

import com.google.gson.annotations.SerializedName;

public class DataServerResponse extends ServerResponse {

    @SerializedName("Data")
    private DataDateTime dataDateTime;

    public DataDateTime getDataDateTime() {
        return dataDateTime;
    }

    public void setDataDateTime(DataDateTime dataDateTime) {
        this.dataDateTime = dataDateTime;
    }

    @Override
    public String toString() {
        return "DataServerResponse{" +
                "dataDateTime=" + dataDateTime +
                '}';
    }
}
