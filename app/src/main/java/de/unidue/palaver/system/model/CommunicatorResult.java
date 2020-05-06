package de.unidue.palaver.system.model;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.Date;
import java.util.List;
import de.unidue.palaver.system.engine.communicator.Parser;

public class CommunicatorResult<T> {
    private static final String TAG = CommunicatorResult.class.getSimpleName();
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

    @RequiresApi(api = Build.VERSION_CODES.N)
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
                stringBuilder.append(" ]");
                Log.i(TAG, "test");
                return "Msg Type :"+responseValue+" , "+"message : "+message+" , data : "+stringBuilder.toString();
            }

            if(data.get(0) instanceof String){
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("[ ");
                for (T s : data){
                    stringBuilder.append((String)s).append(", ");
                }
                stringBuilder.append(" ]");
                Log.i(TAG, stringBuilder.toString());
                return "Msg Type :"+responseValue+" , "+"message : "+message+" , data : "+stringBuilder.toString();
            }

            if(data.get(0) instanceof Date){
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("[ ");
                for (T s : data){
                    Parser parser = new Parser();
                    String dateString = parser.dateToString((Date)s);
                    stringBuilder.append(dateString).append(", ");
                }
                stringBuilder.append(" ]");
                Log.i(TAG, stringBuilder.toString());
                return "Msg Type :"+responseValue+" , "+"message : "+message+" , data : "+stringBuilder.toString();
            }

            if (data.get(0) instanceof Message){
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("[ ");
                for (T chatItem : data){
                    stringBuilder.append(((Message) chatItem).getMessage()).append("  ").append(((Message) chatItem).getDate().toString()).append(", ");
                }
                stringBuilder.append(" ]");
                Log.i(TAG, stringBuilder.toString());
                return "Msg Type :"+responseValue+" , "+"message : "+message+" , data : "+stringBuilder.toString() ;
            }
        }

        Log.i(TAG, "Msg Type :"+responseValue+" , "+"message : "+message);
        return "Msg Type :"+responseValue+" , "+"message : "+message;
    }
}
