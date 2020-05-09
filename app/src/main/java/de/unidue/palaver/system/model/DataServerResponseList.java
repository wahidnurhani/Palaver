package de.unidue.palaver.system.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DataServerResponseList<T> extends ServerResponse {

    @SerializedName(StringValue.JSONKeyName.DATA)
    @Expose
    private ArrayList<T> datas;

    public ArrayList<T> getDatas() {
        return datas;
    }

    public void setDatas(ArrayList<T> datas) {
        this.datas = datas;
    }

    @Override
    public String  toString() {
        return "DataServerResponseList{" +
                "datas=" + datas +
                '}';
    }
}
