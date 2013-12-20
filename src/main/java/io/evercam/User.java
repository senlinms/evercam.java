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

    private User(JSONObject userJSONObject)
    {
        this.userJSONObject = userJSONObject;
    }

    public static User create(Map<String, Object> params) throws EvercamException
    {
        User user;
        try
        {
            HttpResponse<JsonNode> response = Unirest.post(URL).header("accept", "application/json").fields(params).asJson();
            JSONObject userJSONObject = response.getBody().getObject().getJSONArray("vendors").getJSONObject(0);
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
            HttpResponse<JsonNode> response = Unirest.post(URL).header("accept", "application/json").asJson();
            JSONArray streamsJSONArray = response.getBody().getObject().getJSONArray("vendors");
            for(int vendorIndex = 0; vendorIndex < streamsJSONArray.length(); vendorIndex++)
            {
                JSONObject streamJSONObject =  streamsJSONArray.getJSONObject(vendorIndex);
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
