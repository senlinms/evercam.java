package io.evercam;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import org.apache.http.HttpRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;


public class Camera extends EvercamObject {
    static String URL = API.URL + "cameras";

    Camera(JSONObject cameraJSONObject)
    {
        this.jsonObject = cameraJSONObject;
    }
//
//    public static Camera create(Map<String, Object> params) throws EvercamException
//    {
//        Camera camera;
//        try
//        {
//            HttpResponse<JsonNode> response = Unirest.post(URL).header("accept", "application/json").fields(params).asJson();
//            JSONObject userJSONObject = response.getBody().getObject().getJSONArray("cameras").getJSONObject(0);
//            camera = new Camera(userJSONObject);
//        }
//        catch (JSONException e)
//        {
//            throw new EvercamException(e);
//        }
//        catch (UnirestException e)
//        {
//            throw new EvercamException(e);
//        }
//        return camera;
//    }

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

    public ArrayList<String> getEndpoints()
    {
        ArrayList<String> endpoints = new ArrayList<String>();
        try
        {
            JSONArray endpointJSONArray = jsonObject.getJSONArray("endpoints");
            for(int i=0; i<endpointJSONArray.length(); i++)
            {
                endpoints.add(endpointJSONArray.getString(i));
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return endpoints;
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

    public String getOwner() throws EvercamException
    {
        try
        {
            return jsonObject.getString("owner");
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }
    }

    public boolean isPublic() throws EvercamException
    {
        try
        {
            return jsonObject.getBoolean("is_public");
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }
    }

    public Auth getAuth(String type) throws EvercamException
    {
        Auth auth;
        try
        {
            JSONObject authJSONObject = jsonObject.getJSONObject("auth").getJSONObject(type);
            auth = new Auth(type,authJSONObject);
        }
        catch (JSONException e)
        {
            throw new EvercamException(e);
        }
        return auth;
    }

    public String getSnapshotPath(String type) throws EvercamException
    {
        try
        {
            return jsonObject.getJSONObject("snapshots").getString(type);
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }
    }

    public InputStream getSnapshotStream() throws EvercamException
    {
        InputStream inputStream;
        String url = selectEndpoint() + getSnapshotPath("jpg") ;
        try
        {
            HttpResponse<String> response = Unirest.get(url).basicAuth(API.getAuth()[0],API.getAuth()[1]).asString();
            inputStream = response.getRawBody();

        } catch (UnirestException e)
        {
            throw new EvercamException(e);
        }
        return inputStream;
    }

    private String selectEndpoint() throws EvercamException
    {
        String snapshot = getSnapshotPath("jpg");

        for (String endpoint: getEndpoints())
        {
            String url = getFullURL(endpoint,snapshot);
            try
            {
                HttpResponse<String> response = Unirest.get(url).asString();
                if(response.getCode()!=400)
               {
                    return endpoint;
               }
                else
                {
                    throw new EvercamException("Endpoint not responding");
                }
            } catch (UnirestException e)
            {
                throw new EvercamException(e);
            }
        }
        return null;
    }

    private String getFullURL(String endpoint,String snapshot)
    {
        if(endpoint.endsWith("/") && snapshot.startsWith("/"))
        {
            endpoint = endpoint.substring(0,endpoint.lastIndexOf("/"));
        }
        return  endpoint + snapshot;
    }
}
