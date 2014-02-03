package io.evercam;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User extends EvercamObject
{

    private static String URL = API.URL + "users";

    public String getCountry() throws JSONException
    {
        return jsonObject.getString("country");
    }

    public String getId() throws JSONException
    {
        return jsonObject.getString("id");
    }

    public String getEmail() throws JSONException
    {
        return jsonObject.getString("email");
    }

    public String getLastname() throws JSONException
    {
        return jsonObject.getString("lastname");
    }


    public String getForename() throws JSONException
    {
        return jsonObject.getString("forename");
    }

    User(JSONObject userJSONObject)
    {
        this.jsonObject = userJSONObject;
    }

    public static User create(UserDetail userDetail) throws EvercamException
    {
        User user = null;
        Map<String, Object> userMap = new HashMap<String, Object>();
        userMap.put("forename", userDetail.getFirstname());
        userMap.put("lastname", userDetail.getLastname());
        userMap.put("email", userDetail.getEmail());
        userMap.put("username", userDetail.getUsername());
        userMap.put("country", userDetail.getCountrycode());
        try
        {
            HttpResponse<JsonNode> response = Unirest.post(URL).header("accept", "application/json").fields(userMap).field("parameter", "value").asJson();
            if(response.getCode() == CODE_CREATE)
            {
                JSONObject userJSONObject = response.getBody().getObject().getJSONArray("users").getJSONObject(0);
                user = new User(userJSONObject);
            }
            else if(response.getCode() == CODE_ERROR)
            {
                String message = response.getBody().getObject().getJSONArray("message").toString();
                throw new EvercamException(message);
            }
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        } catch (UnirestException e)
        {
            throw new EvercamException(e);
        }
        return user;
    }

    public static ArrayList<Camera> getCameras(String userId) throws EvercamException
    {
        ArrayList<Camera> cameraList = new ArrayList<Camera>();
        try
        {
            HttpResponse<JsonNode> response;
            if (API.isAuth())
            {
                response = Unirest.get(URL + "/" + userId + "/cameras").header("accept", "application/json").basicAuth(API.getAuth()[0], API.getAuth()[1]).asJson();
            }
            else
            {
                response = Unirest.get(URL + "/" + userId + "/cameras").header("accept", "application/json").asJson();
            }
            JSONArray camerasJSONArray = response.getBody().getObject().getJSONArray("cameras");
            for (int i = 0; i < camerasJSONArray.length(); i++)
            {
                JSONObject cameraJSONObject = camerasJSONArray.getJSONObject(i);
                cameraList.add(new Camera(cameraJSONObject));
            }
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        } catch (UnirestException e)
        {
            throw new EvercamException(e);
        }
        return cameraList;
    }
}
