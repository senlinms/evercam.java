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

    public String getCountry() throws EvercamException
    {
        try
        {
            return jsonObject.getString("country");
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }
    }

    public String getId() throws EvercamException
    {
        try
        {
            return jsonObject.getString("id");
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }
    }

    public String getEmail() throws EvercamException
    {
        try
        {
            return jsonObject.getString("email");
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }
    }

    public String getLastname() throws EvercamException
    {
        try
        {
            return jsonObject.getString("lastname");
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }
    }


    public String getForename() throws EvercamException
    {
        try
        {
            return jsonObject.getString("forename");
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }
    }

    public String getUsername() throws EvercamException
    {
        try
        {
            return jsonObject.getString("username");
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }
    }

    User(JSONObject userJSONObject)
    {
        this.jsonObject = userJSONObject;
    }

    public User(String id) throws EvercamException
    {
        if (API.hasDeveloperKeyPair())
        {
            if (API.isAuth())
            {
                try
                {
                    HttpResponse<JsonNode> response = Unirest.get(URL + "/" + id + '/' + "?app_key=" + API.getDeveloperKeyPair()[0] + "&app_id=" + API.getDeveloperKeyPair()[1]).header("accept", "application/json").basicAuth(API.getAuth()[0], API.getAuth()[1]).asJson();
                    if (response.getCode() == CODE_OK)
                    {
                        JSONObject userJSONObject = response.getBody().getObject().getJSONArray("users").getJSONObject(0);
                        this.jsonObject = userJSONObject;
                    }
                    else if (response.getCode() == CODE_UNAUTHORISED)
                    {
                        throw new EvercamException(EvercamException.MSG_INVALID_AUTH);
                    }
                } catch (UnirestException e)
                {
                    throw new EvercamException(e);
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                throw new EvercamException("Authentication required to access user info");
            }
        }
        else
        {
            throw new EvercamException(EvercamException.MSG_API_KEY_REQUIRED);
        }
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

        if (API.hasDeveloperKeyPair())
        {
            try
            {
                HttpResponse<JsonNode> response = Unirest.post(URL + '/' + "?app_key=" + API.getDeveloperKeyPair()[0] + "&app_id=" + API.getDeveloperKeyPair()[1]).header("accept", "application/json").fields(userMap).field("parameter", "value").asJson();
                if (response.getCode() == CODE_CREATE)
                {
                    JSONObject userJSONObject = response.getBody().getObject().getJSONArray("users").getJSONObject(0);
                    user = new User(userJSONObject);
                }
                else if (response.getCode() == CODE_ERROR)
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
        }
        else
        {
            throw new EvercamException(EvercamException.MSG_API_KEY_REQUIRED);
        }
        return user;
    }

    public static ArrayList<Camera> getCameras(String userId) throws EvercamException
    {
        ArrayList<Camera> cameraList = new ArrayList<Camera>();
        if (API.hasDeveloperKeyPair())
        {
            try
            {
                HttpResponse<JsonNode> response;
                if (API.isAuth())
                {
                    response = Unirest.get(URL + "/" + userId + "/cameras" + '/' + "?app_key=" + API.getDeveloperKeyPair()[0] + "&app_id=" + API.getDeveloperKeyPair()[1]).header("accept", "application/json").basicAuth(API.getAuth()[0], API.getAuth()[1]).asJson();
                }
                else
                {
                    response = Unirest.get(URL + "/" + userId + "/cameras" + '/' + "?app_key=" + API.getDeveloperKeyPair()[0] + "&app_id=" + API.getDeveloperKeyPair()[1]).header("accept", "application/json").asJson();
                }
                JSONArray camerasJSONArray = response.getBody().getObject().getJSONArray("cameras");
                for (int count = 0; count < camerasJSONArray.length(); count++)
                {
                    JSONObject cameraJSONObject = camerasJSONArray.getJSONObject(count);
                    cameraList.add(new Camera(cameraJSONObject));
                }
            } catch (JSONException e)
            {
                throw new EvercamException(e);
            } catch (UnirestException e)
            {
                throw new EvercamException(e);
            }
        }
        else
        {
            throw new EvercamException(EvercamException.MSG_API_KEY_REQUIRED);
        }
        return cameraList;
    }
}
