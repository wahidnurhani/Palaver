package de.unidue.palaver.engine;

import android.util.Log;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import de.unidue.palaver.Palaver;
import de.unidue.palaver.model.User;

public class Communicator {
    private static final String TAG=Communicator.class.getSimpleName();
    private Palaver palaver;

    private URL url;
    private HttpURLConnection urlConnection;
    private BufferedReader bufferedReader;
    private String resultJSONString = null;
    private JSONBuilder jsonBuilder=new JSONBuilder();
    private Parser parser= new Parser();

    public Communicator(Palaver palaver) {
        this.palaver = palaver;
    }

    public String[] registerAndValidate(User user, String cmd) {
        String[] resultValue=new String[]{};
        try {
            String baseUrl = "http://palaver.se.paluno.uni-due.de";
            url = new URL( baseUrl + cmd);

            JSONObject body = jsonBuilder.formatBodyUserDataToJSON(user.getUserData().getUserName(), user.getUserData().getPassword());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            OutputStream outputStream = urlConnection.getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream);
            printWriter.print(body.toString());

            printWriter.flush();
            printWriter.close();

            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line + "\n");
            }

            resultJSONString = stringBuffer.toString();

            String[] resultReportArray = parser.validateAndRegisterReportParser(resultJSONString);

            resultValue=resultReportArray;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return resultValue;
    }

    public void fetchAllContact() {

    }
}
