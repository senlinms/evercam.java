package io.evercam;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CameraShare extends EvercamObject
{
    static String URL = API.URL + "shares";

    CameraShare(JSONObject shareJSONObject)
    {
        this.jsonObject = shareJSONObject;
    }

    public static ArrayList<CameraShare> getByUser(String userId) throws EvercamException
    {
        return getSharesByUrl(URL + "/user/" + userId);
    }

    public static ArrayList<CameraShare> getByCamera(String cameraId) throws EvercamException
    {
        return getSharesByUrl(URL + "/camera/" + cameraId);
    }

    public static String getNameSetString(String userId) throws EvercamException
    {
        ArrayList<CameraShare> cameraShares = getByUser(userId);
        String nameSetString = "";
        for (int count = 0; count < cameraShares.size(); count++)
        {
            nameSetString += cameraShares.get(count).getCameraId() + ",";
        }
        return nameSetString;
    }

    private static ArrayList<CameraShare> getSharesByUrl(String url) throws EvercamException
    {
        ArrayList<CameraShare> cameraShares = new ArrayList<CameraShare>();
        if (API.hasUserKeyPair())
        {
            try
            {
                HttpResponse<JsonNode> response = Unirest.get(url + "?api_key=" + API.getUserKeyPair()[0] + "&api_id=" + API.getUserKeyPair()[1]).header("accept", "application/json").asJson();
                if(response.getCode() == CODE_OK)
                {
                    JSONArray sharesJSONArray = response.getBody().getObject().getJSONArray("shares");
                    for (int count = 0; count < sharesJSONArray.length(); count++)
                    {
                        JSONObject shareJSONObject = sharesJSONArray.getJSONObject(count);
                        cameraShares.add(new CameraShare(shareJSONObject));
                    }
                }
                else if (response.getCode() == CODE_SERVER_ERROR)
                {
                    throw new EvercamException(EvercamException.MSG_SERVER_ERROR);
                }
                else
                {
                    throw new EvercamException(response.getBody().toString());
                }
            } catch (UnirestException e)
            {
                throw new EvercamException(e);
            } catch (JSONException e)
            {
                throw new EvercamException(e);
            }
        }
        else
        {
            throw new EvercamException(EvercamException.MSG_USER_API_KEY_REQUIRED);
        }
        return cameraShares;
    }

    public int getId() throws EvercamException
    {
        try
        {
            return jsonObject.getInt("id");
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }
    }

    public String getCameraId() throws EvercamException
    {
        try
        {
            return jsonObject.getString("camera_id");
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }
    }

    public String getSharerId() throws EvercamException
    {
        try
        {
            return jsonObject.getString("sharer_id");
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }
    }

    public String getUserId() throws EvercamException
    {
        try
        {
            return jsonObject.getString("user_id");
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }
    }

    public String getUserEmail() throws EvercamException
    {
        try
        {
            return jsonObject.getString("email");
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }
    }

    public String getKind() throws EvercamException
    {
        try
        {
            return jsonObject.getString("kind");
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }
    }

    public String getRights() throws EvercamException
    {
        try
        {
            return jsonObject.getString("rights");
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }
    }
}
