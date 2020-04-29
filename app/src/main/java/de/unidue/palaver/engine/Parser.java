package de.unidue.palaver.engine;

import org.json.JSONException;
import org.json.JSONObject;

public class Parser {

    public Parser() {
    }

    public String[] validateAndRegisterReportParser(String result){
        try {
            JSONObject jsonObject = new JSONObject(result);
            int msgType = jsonObject.getInt("MsgType");
            String info = jsonObject.getString("Info");
            String data;
            if(jsonObject.get("Data") instanceof String){
                data = jsonObject.getString("Data");
            } else {
                data = null;
            }



            return new String[]{msgType+"",info, data};
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
