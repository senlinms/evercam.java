package io.evercam;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


public class Camera extends EvercamObject {
    static String URL = API.URL + "cameras";

    Camera(JSONObject cameraJSONObject)
    {
        this.jsonObject = cameraJSONObject;
    }

    public static Camera create(Map<String, Object> params) throws EvercamException
    {
        Camera camera;
        try
        {
            HttpResponse<JsonNode> response = Unirest.post(URL).header("accept", "application/json").fields(params).asJson();
            JSONObject userJSONObject = response.getBody().getObject().getJSONArray("cameras").getJSONObject(0);
            camera = new Camera(userJSONObject);
        }
        catch (JSONException e)
        {
            throw new EvercamException(e);
        }
        catch (UnirestException e)
        {
            throw new EvercamException(e);
        }
        return camera;
    }

    public static Camera getById(String cameraId) throws EvercamException
    {
        Camera camera;
        try
        {
            HttpResponse<JsonNode> response = Unirest.get(URL + '/' + cameraId).header("accept", "application/json").asJson();
            JSONObject userJSONObject = response.getBody().getObject().getJSONArray("cameras").getJSONObject(0);
            camera = new Camera(userJSONObject);
        }
        catch (JSONException e)
        {
            throw new EvercamException(e);
        }
        catch (UnirestException e)
        {
            throw new EvercamException(e);
        }
        return camera;
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
}
