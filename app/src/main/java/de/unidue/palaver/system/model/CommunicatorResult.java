package de.unidue.palaver.system.model;

import androidx.annotation.NonNull;

import java.util.List;

public class CommunicatorResult<T> {
    private int responseValue;
    private String message;
    private List<T> data;

    public CommunicatorResult(int responseValue, String message, List<T> data) {
        this.responseValue = responseValue;
        this.message = message;
        this.data = data;
    }

    public int getResponseValue() {
        return responseValue;
    }

    public String getMessage() {
        return message;
    }

    public List<T> getData() {
        return data;
    }

    @NonNull
    @Override
    public String toString() {
        if(data!=null){
            if (data.get(0) instanceof Friend){
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("[ ");
                for (T friend : data){
                    stringBuilder.append(((Friend)friend).getUsername()).append(", ");
                }
                stringBuilder.append(" @ignoreLast]");
                return "Msg Type :"+responseValue+" , "+"message : "+message+" , data : "+stringBuilder.toString();
            }

            if(data.get(0) instanceof String){
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("[ ");
                for (T s : data){
                    stringBuilder.append((String)s).append(", ");
                }
                stringBuilder.append(" @ignoreLast]");
                return "Msg Type :"+responseValue+" , "+"message : "+message+" , data : "+stringBuilder.toString();
            }
        }
        return "Msg Type :"+responseValue+" , "+"message : "+message;
    }
}
