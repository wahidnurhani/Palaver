package de.unidue.palaver.engine;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import de.unidue.palaver.Palaver;
import de.unidue.palaver.StringValue;
import de.unidue.palaver.database.PalaverDBManager;
import de.unidue.palaver.model.Friend;
import de.unidue.palaver.model.User;

public class Communicator {
    private static final String TAG=Communicator.class.getSimpleName();

    private URL url;
    private String baseUrl = StringValue.System.BASE_URL;
    private HttpURLConnection urlConnection;
    private BufferedReader bufferedReader;
    private String resultJSONString = null;
    private JSONBuilder jsonBuilder;
    private Parser parser;

    Communicator() {
        this.jsonBuilder = new JSONBuilder();
        this.parser = new Parser();
    }

    public boolean checkConnectivity(Context contex){
        ConnectivityManager connectivityManager = (ConnectivityManager) contex.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public String[] registerAndValidate(User user, String cmd) {
        String[] resultValue=new String[]{};
        try {
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
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }

            resultJSONString = stringBuilder.toString();

            resultValue= parser.validateAndRegisterReportParser(resultJSONString);

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


    public String[] fetchFriends(User user) {
        String[] resultValue=new String[]{};
        String cmd = StringValue.APICmd.GET_ALL_FRIENDS;

        try {
            JSONObject body = jsonBuilder.formatBodyUserDataToJSON(user.getUserData().getUserName(), user.getUserData().getPassword());

            url = new URL(baseUrl + cmd);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type","application/json");
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
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line=bufferedReader.readLine())!=null){
                stringBuilder.append(line).append("\n");
            }

            resultJSONString = stringBuilder.toString();
            resultValue = parser.fetchFriendFeedback(resultJSONString);
            Friend[] resultContacts= parser.getFriendParser(resultJSONString);

            if(resultContacts.length>0){
                PalaverDBManager palaverDBManager = Palaver.getInstance().getPalaverDBManager();
                if(palaverDBManager.deleteAllContact()){
                    for(int i=0; i<resultContacts.length;i++){
                        palaverDBManager.insertContact(resultContacts[i]);
                    }
                }
            }

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if ( urlConnection!= null){
                urlConnection.disconnect();
            }
            if (bufferedReader!=null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return resultValue;
    }

    public String[] addContact(User user, String friendUserName) {
        String[] resultValue = new String[]{};
        String cmd = StringValue.APICmd.ADD_FRIEND;
        try {
            JSONObject body = jsonBuilder.formatBodyAddOrRemoveFriendtToJSON(user.getUserData().getUserName(), user.getUserData().getPassword(), friendUserName);
            url = new URL(baseUrl+cmd);
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
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }

            resultJSONString = stringBuilder.toString();
            String[] resultReportArray = parser.addContactReportParser(resultJSONString);

            PalaverDBManager palaverDBManager = Palaver.getInstance().getPalaverDBManager();
            if (resultReportArray[0].equals("1")) {
                Friend newFriend = new Friend(friendUserName);
                palaverDBManager.insertContact(newFriend);
            }
            resultValue = resultReportArray;

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
}
