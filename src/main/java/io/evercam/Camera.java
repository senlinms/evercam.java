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
                JSONObject cameraJSONObject = buildCameraJSONObject(cameraDetail);
                DefaultHttpClient c = new DefaultHttpClient();
                HttpPost post = new HttpPost(URL);
                post.setHeader("Content-type", "application/json");
                post.setHeader("Accept", "application/json");
                post.setEntity(new StringEntity(cameraJSONObject.toString()));
                String encoding = Base64Coder.encodeString(API.getAuth()[0] + ":" + API.getAuth()[1]);
                post.setHeader("Authorization", "Basic " + encoding);
                org.apache.http.HttpResponse r = c.execute(post);
                String result = EntityUtils.toString(r.getEntity());
                if (r.getStatusLine().getStatusCode() == CODE_UNAUTHORISED)
                {
                    throw new EvercamException("Invalid auth");
                }
                else if (r.getStatusLine().getStatusCode() == CODE_ERROR)
                {
                    JsonNode jsonNode = new JsonNode(result);
                    String message = jsonNode.getObject().getString("message");
                    throw new EvercamException(message);
                }
                else if (r.getStatusLine().getStatusCode() == CODE_CREATE)
                {
                    JsonNode jsonNode = new JsonNode(result);
                    JSONObject jsonObject = jsonNode.getObject().getJSONArray("cameras").getJSONObject(0);
                    return new Camera(jsonObject);
                }
                else if (r.getStatusLine().getStatusCode() == CODE_SERVER_ERROR)
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

    public static Camera patch(PatchCamera patchCamera) throws EvercamException
    {
        Camera camera = null;
        if (API.isAuth())
        {
            try
            {
                JSONObject cameraJSONObject = buildPatchJSONObject(patchCamera);
                System.out.print(cameraJSONObject.toString());
                DefaultHttpClient c = new DefaultHttpClient();
                HttpPatch patch = new HttpPatch(URL + '/' + patchCamera.id);
                patch.setHeader("Content-type", "application/json");
                patch.setHeader("Accept", "application/json");
                patch.setEntity(new StringEntity(cameraJSONObject.toString()));
                String encoding = Base64Coder.encodeString(API.getAuth()[0] + ":" + API.getAuth()[1]);
                patch.setHeader("Authorization", "Basic " + encoding);
                org.apache.http.HttpResponse r = c.execute(patch);
                String result = EntityUtils.toString(r.getEntity());
                if (r.getStatusLine().getStatusCode() == CODE_UNAUTHORISED)
                {
                    throw new EvercamException("Invalid auth");
                }
                else if (r.getStatusLine().getStatusCode() == CODE_ERROR)
                {
                    JsonNode jsonNode = new JsonNode(result);
                    String message = jsonNode.getObject().getString("message");
                    throw new EvercamException(message);
                }
                else if (r.getStatusLine().getStatusCode() == CODE_OK)
                {
                    JsonNode jsonNode = new JsonNode(result);
                    JSONObject jsonObject = jsonNode.getObject().getJSONArray("cameras").getJSONObject(0);
                    return new Camera(jsonObject);
                }
                else if (r.getStatusLine().getStatusCode() == CODE_SERVER_ERROR)
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
            for (int i = 0; i < endpointJSONArray.length(); i++)
            {
                endpoints.add(endpointJSONArray.getString(i));
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

    private static JSONObject buildCameraJSONObject(CameraDetail cameraDetail) throws JSONException, EvercamException
    {
        JSONObject cameraJSONObject = new JSONObject();
        JSONObject authJSONObject = new JSONObject();
        JSONObject basicJSONObject = new JSONObject();
        JSONObject snapshotJSONObject = new JSONObject();
        JSONArray endpointArray = new JSONArray();
        for(int i=0; i<cameraDetail.getEndpoints().length;i++)
        {
            endpointArray.put(i,cameraDetail.getEndpoints()[i]);
        }
        snapshotJSONObject.put("jpg", cameraDetail.getSnapshotJPG());
        basicJSONObject.put("username", cameraDetail.getBasicAuth()[0]);
        basicJSONObject.put("password", cameraDetail.getBasicAuth()[1]);
        authJSONObject.put("basic", basicJSONObject);
        cameraJSONObject.put("id", cameraDetail.getId());
        cameraJSONObject.put("name", cameraDetail.getName());
        if (cameraDetail.getModel() != null)
        {
            cameraJSONObject.put("model", cameraDetail.getModel());
        }
        if (cameraDetail.getVendor() != null)
        {
            cameraJSONObject.put("vendor", cameraDetail.getVendor());
        }
        if (cameraDetail.getTimezone() != null)
        {
            cameraJSONObject.put("timezone", cameraDetail.getTimezone());
        }
        if (cameraDetail.getMacAddress() != null)
        {
            cameraJSONObject.put("mac_address", cameraDetail.getMacAddress());
        }
        cameraJSONObject.put("is_public", cameraDetail.isPublic());
        cameraJSONObject.put("snapshots", snapshotJSONObject);
        cameraJSONObject.put("endpoints", endpointArray);
        cameraJSONObject.put("auth", authJSONObject);

        return cameraJSONObject;
    }

    private static JSONObject buildPatchJSONObject(PatchCamera patchCamera) throws JSONException
    {
        JSONObject cameraJSONObject = new JSONObject();
        JSONObject authJSONObject = new JSONObject();
        JSONObject basicJSONObject = new JSONObject();
        JSONObject snapshotJSONObject = new JSONObject();
        JSONArray endpointArray = new JSONArray();
        if(patchCamera.endpoints != null)
        {
        for(int i=0; i<patchCamera.endpoints.length;i++)
        {
            endpointArray.put(i,patchCamera.endpoints[i]);
        }
            cameraJSONObject.put("endpoints", endpointArray);
        }
        if(patchCamera.snapshotJPG != null)
        {
            snapshotJSONObject.put("jpg", patchCamera.snapshotJPG);
            cameraJSONObject.put("snapshots", snapshotJSONObject);
        }
        if(patchCamera.isPublic != null)
        {
            cameraJSONObject.put("is_public", patchCamera.isPublic);
        }
        if(patchCamera.basicAuth != null)
        {
            basicJSONObject.put("username", patchCamera.basicAuth[0]);
            basicJSONObject.put("password", patchCamera.basicAuth[1]);
            authJSONObject.put("basic", basicJSONObject);
            cameraJSONObject.put("auth", authJSONObject);
        }
        cameraJSONObject.put("id", patchCamera.id);
        if(patchCamera.name != null)
        {
        cameraJSONObject.put("name", patchCamera.name);
        }
        if (patchCamera.model != null)
        {
            cameraJSONObject.put("model", patchCamera.model);
        }
        if (patchCamera.vendor != null)
        {
            cameraJSONObject.put("vendor", patchCamera.vendor);
        }
        if (patchCamera.timezone != null)
        {
            cameraJSONObject.put("timezone", patchCamera.timezone);
        }
        if (patchCamera.macAddress != null)
        {
            cameraJSONObject.put("mac_address", patchCamera.macAddress);
        }

        return cameraJSONObject;
    }
}
