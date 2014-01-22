package io.evercam;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
            HttpResponse<JsonNode> response;
            if(API.isAuth())
            {
                response = Unirest.get(URL + '/' + cameraId).header("accept", "application/json").basicAuth(API.getAuth()[0],API.getAuth()[1]).asJson();
            }
            else
            {
                response = Unirest.get(URL + '/' + cameraId).header("accept", "application/json").asJson();
            }
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

    public String getName() throws EvercamException
    {
        try
        {
            return jsonObject.getString("name");
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }
    }

    public String getVendor() throws EvercamException
    {
        try
        {
            return jsonObject.getString("vendor");
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }
    }

    public String getTimezone() throws EvercamException
    {
        try
        {
            return jsonObject.getString("timezone");
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }
    }

    public String getModel() throws EvercamException
    {
        try
        {
            return jsonObject.getString("model");
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }
    }

    public String isOnline() throws EvercamException
    {
        try
        {
            return jsonObject.getString("is_online");
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }
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
        String endpoint = selectEndpoint();
        if(endpoint != null)
        {
            String url = endpoint + getSnapshotPath("jpg");
            try
            {
                HttpResponse<String> response = Unirest.get(url).basicAuth(getAuth(Auth.TYPE_BASIC).getUsername(),getAuth(Auth.TYPE_BASIC).getPassword()).asString();
                inputStream = response.getRawBody();
            }
            catch (UnirestException e)
            {
                throw new EvercamException(e);
            }
        }
        else
        {
            throw new EvercamException("Endpoint not available");
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
