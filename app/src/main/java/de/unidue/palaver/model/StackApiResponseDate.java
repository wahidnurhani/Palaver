package de.unidue.palaver.model;

import com.google.gson.annotations.SerializedName;

public class StackApiResponseDate extends StackApiResponse {

    @SerializedName(StringValue.JSONKeyName.DATA)
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

        @SerializedName(StringValue.JSONKeyName.DATE_TIME)
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
