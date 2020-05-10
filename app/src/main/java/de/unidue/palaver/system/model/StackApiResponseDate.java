package de.unidue.palaver.system.model;

import com.google.gson.annotations.SerializedName;

public class StackApiResponseDate extends StackApiResponse {

    @SerializedName("Data")
    private DataDateTime dataDateTime;

    public DataDateTime getDataDateTime() {
        return dataDateTime;
    }

    @Override
    public String toString() {
        return "StackApiResponseDate{" +
                "dataDateTime=" + dataDateTime +
                '}';
    }

    public class DataDateTime {

        @SerializedName("DateTime")
        private String dateTime;

        public String getDateTime() {
            return dateTime;
        }

        @Override
        public String toString() {
            return "DataDateTime{" +
                    "dateTime='" + dateTime + '\'' +
                    '}';
        }
    }
}
