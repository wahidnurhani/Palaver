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
import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;

import de.unidue.palaver.system.model.Message;
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
    private String serverTimeZone;

    public Communicator() {
        this.jsonBuilder = new JSONBuilder();
        this.parser = new Parser();
        TimeZone timeZone= TimeZone.getTimeZone("Europe/Berlin");
        int offset = 3600000/timeZone.getRawOffset();
        int dstOffset = timeZone.getDSTSavings()/3600000;
        int timezoneInt = offset+dstOffset;
        serverTimeZone = "+"+timezoneInt;
    }

    public boolean checkConnectivity(Context contex){
        ConnectivityManager connectivityManager = (ConnectivityManager) contex.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public String[] registerAndValidate(User user, String cmd) {
        String[] resultValue=new String[]{};
        try {
            JSONObject body = jsonBuilder.formatBodyUserDataToJSON(user.getUserName(), user.getPassword());

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
            JSONObject body = jsonBuilder.formatBodyUserDataToJSON(user.getUserName(), user.getPassword());

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

    public CommunicatorResult<Friend> addFriend(User user, String friendUserName) {
        CommunicatorResult<Friend> result = null;
        String cmd = StringValue.APICmd.ADD_FRIEND;
        try {
            JSONObject body = jsonBuilder.formatBodyAddOrRemoveFriendtToJSON(user.getUserName(), user.getPassword(), friendUserName);
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

            result = parser.addAndRemoveFriendReportParser(resultJSONString);

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

    public CommunicatorResult<Friend> removeFriend(User user, String friendUserName) {
        CommunicatorResult<Friend> result = null;
        String cmd = StringValue.APICmd.REMOVE_FRIEND;
        try {
            JSONObject body = jsonBuilder.formatBodyAddOrRemoveFriendtToJSON(user.getUserName(), user.getPassword(), friendUserName);
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

            result = parser.addAndRemoveFriendReportParser(resultJSONString);

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

            JSONObject body = jsonBuilder.formatBodyChangePasswordDataToJSON(user.getUserName(),
                    user.getPassword(),
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

            JSONObject body = jsonBuilder.formatBodyPushTokenDataToJSON(user.getUserName(),
                    user.getPassword(),
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


    public CommunicatorResult<Date> sendMessage(User user, Friend friend, Message message) {
        CommunicatorResult<Date> resultValue=null;
        try {
            String cmd = StringValue.APICmd.SEND_MESSAGE;
            JSONObject body = jsonBuilder.formatBodySendMessageToJSON(user.getUserName(),
                    user.getPassword(), friend.getUsername(),
                    message.getMessage());

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
            System.out.println("fresulakfdaif ---------------");
            System.out.println(resultJSONString);

            resultValue = parser.sendMessageReport(resultJSONString);

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

    public CommunicatorResult<Message> getMessage(User user, Friend friend) {
        CommunicatorResult<Message> result=null;
        String cmd = StringValue.APICmd.GET_MESSAGE;
        try {
            JSONObject body = jsonBuilder.formatBodyGetChatToJSON(user.getUserName(),
                    user.getPassword(), friend.getUsername());

            url = new URL(baseUrl+cmd);
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
            System.out.println(resultJSONString);

            result= parser.getChatDataParser(resultJSONString, "true", friend.getUsername());
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException | ParseException e) {
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
        return result;
    }
}
