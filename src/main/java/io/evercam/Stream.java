package io.evercam;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


public class Stream extends EvercamObject {
    static String URL = API.URL + "streams";

    Stream(JSONObject streamJSONObject)
    {
        this.jsonObject = streamJSONObject;
    }

    public static Stream create(Map<String, Object> params) throws EvercamException
    {
        Stream stream;
        try
        {
            HttpResponse<JsonNode> response = Unirest.post(URL).header("accept", "application/json").fields(params).asJson();
            JSONObject userJSONObject = response.getBody().getObject().getJSONArray("streams").getJSONObject(0);
            stream = new Stream(userJSONObject);
        }
        catch (JSONException e)
        {
            throw new EvercamException(e);
        }
        catch (UnirestException e)
        {
            throw new EvercamException(e);
        }
        return stream;
    }

    public static Stream getById(String streamId) throws EvercamException
    {
        Stream stream;
        try
        {
            HttpResponse<JsonNode> response = Unirest.get(URL + '/' + streamId).header("accept", "application/json").asJson();
            JSONObject userJSONObject = response.getBody().getObject().getJSONArray("streams").getJSONObject(0);
            stream = new Stream(userJSONObject);
        }
        catch (JSONException e)
        {
            throw new EvercamException(e);
        }
        catch (UnirestException e)
        {
            throw new EvercamException(e);
        }
        return stream;
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
