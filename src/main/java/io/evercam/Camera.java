package io.evercam;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.http.utils.Base64Coder;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class Camera extends EvercamObject
{
    static String URL = API.URL + "cameras";

    Camera(JSONObject cameraJSONObject)
    {
        this.jsonObject = cameraJSONObject;
    }

    public static Camera create(CameraDetail cameraDetail) throws EvercamException
    {
        Camera camera = null;
        if (API.isAuth())
        {
            try
            {
                JSONObject cameraJSONObject = buildPatchJSONObject(cameraDetail);
                DefaultHttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(URL);
                post.setHeader("Content-type", "application/json");
                post.setHeader("Accept", "application/json");
                post.setEntity(new StringEntity(cameraJSONObject.toString()));
                String encoding = Base64Coder.encodeString(API.getAuth()[0] + ":" + API.getAuth()[1]);
                post.setHeader("Authorization", "Basic " + encoding);
                org.apache.http.HttpResponse response = client.execute(post);
                String result = EntityUtils.toString(response.getEntity());
                if (response.getStatusLine().getStatusCode() == CODE_UNAUTHORISED)
                {
                    throw new EvercamException("Invalid auth");
                }
                else if (response.getStatusLine().getStatusCode() == CODE_ERROR)
                {
                    JsonNode jsonNode = new JsonNode(result);
                    String message = jsonNode.getObject().getString("message");
                    throw new EvercamException(message);
                }
                else if (response.getStatusLine().getStatusCode() == CODE_CREATE)
                {
                    JsonNode jsonNode = new JsonNode(result);
                    JSONObject jsonObject = jsonNode.getObject().getJSONArray("cameras").getJSONObject(0);
                    return new Camera(jsonObject);
                }
                else if (response.getStatusLine().getStatusCode() == CODE_SERVER_ERROR)
                {
                    throw new EvercamException("Internal server error");
                }
            } catch (JSONException e)
            {
                throw new EvercamException(e);
            } catch (ClientProtocolException e)
            {
                e.printStackTrace();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            throw new EvercamException("Auth is necessary for creating camera");
        }
        return camera;
    }

    public static Camera patch(CameraDetail cameraDetail) throws EvercamException
    {
        Camera camera = null;
        if (API.isAuth())
        {
            try
            {
                JSONObject cameraJSONObject = buildPatchJSONObject(cameraDetail);
                DefaultHttpClient client = new DefaultHttpClient();
                HttpPatch patch = new HttpPatch(URL + '/' + cameraDetail.id);
                patch.setHeader("Content-type", "application/json");
                patch.setHeader("Accept", "application/json");
                patch.setEntity(new StringEntity(cameraJSONObject.toString()));
                String encoding = Base64Coder.encodeString(API.getAuth()[0] + ":" + API.getAuth()[1]);
                patch.setHeader("Authorization", "Basic " + encoding);
                org.apache.http.HttpResponse response = client.execute(patch);
                String result = EntityUtils.toString(response.getEntity());
                if (response.getStatusLine().getStatusCode() == CODE_UNAUTHORISED)
                {
                    throw new EvercamException("Invalid auth");
                }
                else if (response.getStatusLine().getStatusCode() == CODE_ERROR)
                {
                    JsonNode jsonNode = new JsonNode(result);
                    String message = jsonNode.getObject().getString("message");
                    throw new EvercamException(message);
                }
                else if (response.getStatusLine().getStatusCode() == CODE_OK)
                {
                    JsonNode jsonNode = new JsonNode(result);
                    JSONObject jsonObject = jsonNode.getObject().getJSONArray("cameras").getJSONObject(0);
                    return new Camera(jsonObject);
                }
                else if (response.getStatusLine().getStatusCode() == CODE_SERVER_ERROR)
                {
                    throw new EvercamException("Internal server error");
                }
            } catch (JSONException e)
            {
                throw new EvercamException(e);
            } catch (ClientProtocolException e)
            {
                e.printStackTrace();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            throw new EvercamException("Auth is necessary for patching camera");
        }
        return camera;
    }

    public static Camera getById(String cameraId) throws EvercamException
    {
        Camera camera;
        try
        {
            HttpResponse<JsonNode> response;
            if (API.isAuth())
            {
                response = Unirest.get(URL + '/' + cameraId).header("accept", "application/json").basicAuth(API.getAuth()[0], API.getAuth()[1]).asJson();
            }
            else
            {
                response = Unirest.get(URL + '/' + cameraId).header("accept", "application/json").asJson();
            }
            JSONObject userJSONObject = response.getBody().getObject().getJSONArray("cameras").getJSONObject(0);
            camera = new Camera(userJSONObject);
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        } catch (UnirestException e)
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
            for (int count = 0; count < endpointJSONArray.length(); count++)
            {
                endpoints.add(endpointJSONArray.getString(count));
            }
        } catch (JSONException e)
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
            auth = new Auth(type, authJSONObject);
        } catch (JSONException e)
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

    public String getMacAddress() throws EvercamException
    {
        try
        {
            return jsonObject.getString("mac_address");
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
        if (endpoint != null)
        {
            String url = endpoint + getSnapshotPath("jpg");
            try
            {
                HttpResponse<String> response = Unirest.get(url).basicAuth(getAuth(Auth.TYPE_BASIC).getUsername(), getAuth(Auth.TYPE_BASIC).getPassword()).asString();
                inputStream = response.getRawBody();
            } catch (UnirestException e)
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

        for (String endpoint : getEndpoints())
        {
            String url = getFullURL(endpoint, snapshot);
            try
            {
                HttpResponse<String> response = Unirest.get(url).asString();
                if (response.getCode() != 400)
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

    private String getFullURL(String endpoint, String snapshot)
    {
        if (endpoint.endsWith("/") && snapshot.startsWith("/"))
        {
            endpoint = endpoint.substring(0, endpoint.lastIndexOf("/"));
        }
        return endpoint + snapshot;
    }

    private static JSONObject buildPatchJSONObject(CameraDetail cameraDetail) throws JSONException
    {
        JSONObject cameraJSONObject = new JSONObject();
        JSONObject authJSONObject = new JSONObject();
        JSONObject basicJSONObject = new JSONObject();
        JSONObject snapshotJSONObject = new JSONObject();
        JSONArray endpointArray = new JSONArray();
        cameraJSONObject.put("id", cameraDetail.id);
        if (cameraDetail.endpoints != null)
        {
            for (int count = 0; count < cameraDetail.endpoints.length; count++)
            {
                endpointArray.put(count, cameraDetail.endpoints[count]);
            }
            cameraJSONObject.put("endpoints", endpointArray);
        }
        if (cameraDetail.snapshotJPG != null)
        {
            snapshotJSONObject.put("jpg", cameraDetail.snapshotJPG);
            cameraJSONObject.put("snapshots", snapshotJSONObject);
        }
        if (cameraDetail.isPublic != null)
        {
            cameraJSONObject.put("is_public", cameraDetail.isPublic);
        }
        if (cameraDetail.basicAuth != null)
        {
            basicJSONObject.put("username", cameraDetail.basicAuth[0]);
            basicJSONObject.put("password", cameraDetail.basicAuth[1]);
            authJSONObject.put("basic", basicJSONObject);
            cameraJSONObject.put("auth", authJSONObject);
        }
        if (cameraDetail.name != null)
        {
            cameraJSONObject.put("name", cameraDetail.name);
        }
        if (cameraDetail.model != null)
        {
            cameraJSONObject.put("model", cameraDetail.model);
        }
        if (cameraDetail.vendor != null)
        {
            cameraJSONObject.put("vendor", cameraDetail.vendor);
        }
        if (cameraDetail.timezone != null)
        {
            cameraJSONObject.put("timezone", cameraDetail.timezone);
        }
        if (cameraDetail.macAddress != null)
        {
            cameraJSONObject.put("mac_address", cameraDetail.macAddress);
        }

        return cameraJSONObject;
    }
}
