package io.evercam;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class User {

    private static String URL = API.URL + "users";
    private JSONObject userJSONObject;

    public String getCountry() throws JSONException {
        return userJSONObject.getString("country");
    }

    public String getId() throws JSONException  {
        return userJSONObject.getString("id");
    }

    public String getEmail() throws JSONException  {
        return userJSONObject.getString("email");
    }

    public String getLastname() throws JSONException {
        return userJSONObject.getString("lastname");
    }


    public String getForename() throws JSONException {
        return userJSONObject.getString("forename");
    }

    User(JSONObject userJSONObject)
    {
        this.userJSONObject = userJSONObject;
    }

    public static User create(Map<String, Object> params) throws EvercamException
    {
        User user;
        try
        {
            HttpResponse<JsonNode> response = Unirest.post(URL).header("accept", "application/json").fields(params).field("parameter", "value").asJson();
            JSONObject userJSONObject = response.getBody().getObject().getJSONArray("users").getJSONObject(0);
            user = new User(userJSONObject);
        }
        catch (JSONException e)
        {
            throw new EvercamException(e);
        }
        catch (UnirestException e)
        {
            throw new EvercamException(e);
        }
        return user;
    }

    public static ArrayList<Stream> getStreams(String userId) throws EvercamException
    {
        ArrayList<Stream> streamList = new ArrayList<Stream>();
        try
        {
            HttpResponse<JsonNode> response = Unirest.get(URL + "/" + userId + "/streams").header("accept", "application/json").asJson();
            JSONArray streamsJSONArray = response.getBody().getObject().getJSONArray("streams");
            for(int i = 0; i < streamsJSONArray.length(); i++)
            {
                JSONObject streamJSONObject =  streamsJSONArray.getJSONObject(i);
                streamList.add(new Stream(streamJSONObject));
            }
        }
        catch (JSONException e)
        {
            throw new EvercamException(e);
        }
        catch (UnirestException e)
        {
            throw new EvercamException(e);
        }
        return streamList;
    }
}
