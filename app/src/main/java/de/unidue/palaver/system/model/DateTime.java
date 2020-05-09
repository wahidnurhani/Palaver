package de.unidue.palaver.system.model;

import com.google.gson.annotations.SerializedName;

public class DateTime<T> {

    @SerializedName("DateTime")
    private String dateTime;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "DateTime{" +
                "dateTime='" + dateTime + '\'' +
                '}';
    }
}
