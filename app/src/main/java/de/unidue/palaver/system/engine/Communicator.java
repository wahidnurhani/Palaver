package de.unidue.palaver.system.engine;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONException;
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
import java.util.Date;

import de.unidue.palaver.system.model.ChatItem;
import de.unidue.palaver.system.model.CommunicatorResult;
import de.unidue.palaver.system.resource.StringValue;
import de.unidue.palaver.system.model.Friend;
import de.unidue.palaver.system.model.User;


public class Communicator {

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

    String[] registerAndValidate(User user, String cmd) {
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


    public CommunicatorResult<Friend> fetchFriends(User user) {
        CommunicatorResult<Friend> result;

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
            result = parser.getFriendParser(resultJSONString);

            return result;

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
        return null;
    }

    public CommunicatorResult<Friend> addContact(User user, String friendUserName) {
        CommunicatorResult<Friend> result = null;
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

            result = parser.addContactReportParser(resultJSONString);

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
        return result;
    }


    public CommunicatorResult<String> changePassword(User user, String newPassword) {
        CommunicatorResult<String> result = null;
        String cmd = StringValue.APICmd.CHANGE_PASSWORD;
        try {

            JSONObject body = jsonBuilder.formatBodyChangePasswordDataToJSON(user.getUserData().getUserName(),
                    user.getUserData().getPassword(),
                    newPassword);


            url = new URL(baseUrl + cmd);
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

            result = parser.changePasswordResultParser(resultJSONString, newPassword);

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
        return result;
    }

    public CommunicatorResult<String> pushToken(User user, String token) {
        CommunicatorResult<String> result = null;
        String cmd = StringValue.APICmd.PUSHTOKEN;
        try {

            JSONObject body = jsonBuilder.formatBodyPushTokenDataToJSON(user.getUserData().getUserName(),
                    user.getUserData().getPassword(),
                    token);

            url = new URL(baseUrl + cmd);
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

            result = parser.pushTokenParser(resultJSONString);

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
        return result;
    }


    public CommunicatorResult<Date> sendMessage(User user, Friend friend, ChatItem chatItem) {
        CommunicatorResult<Date> resultValue;
        try {
            String cmd = StringValue.APICmd.SEND_MESSAGE;
            JSONObject body = jsonBuilder.formatBodySendMessageToJSON(user.getUserData().getUserName(),
                    user.getUserData().getPassword(), friend.getUsername(),
                    chatItem.getMessage());

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

            resultValue = parser.sendMessageReport(resultJSONString);

            return resultValue;

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
        return null;
    }
}
