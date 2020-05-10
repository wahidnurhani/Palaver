package de.unidue.palaver.system.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class StackApiResponseList<T> extends StackApiResponse {

    @SerializedName(StringValue.JSONKeyName.DATA)
    @Expose
    private ArrayList<T> datas;

    public ArrayList<T> getDatas() {
        return datas;
    }

    @Override
    public String  toString() {
        return "StackApiResponseList{" +
                "datas=" + datas +
                '}';
    }
}
