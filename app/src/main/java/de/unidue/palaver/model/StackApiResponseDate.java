package de.unidue.palaver.model;

import com.google.gson.annotations.SerializedName;

public class StackApiResponseDate extends StackApiResponse {

    @SerializedName("Data")
    private DateTime dateTime;

    public DateTime getDateTime() {
        return dateTime;
    }

    @Override
    public String toString() {
        return "StackApiResponseDate{" +
                "dateTime=" + dateTime +
                '}';
    }

    public class DateTime {

        @SerializedName("DateTime")
        private String dateTime;

        public String getDateTime() {
            return dateTime;
        }

        @Override
        public String toString() {
            return "DateTime{" +
                    "dateTime='" + dateTime + '\'' +
                    '}';
        }
    }
}
