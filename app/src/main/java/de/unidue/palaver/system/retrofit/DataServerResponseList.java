package de.unidue.palaver.system.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import de.unidue.palaver.system.values.StringValue;

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
