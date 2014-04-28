package io.evercam;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
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
        if (API.hasUserKeyPair())
        {
            try
            {
                JSONObject cameraJSONObject = buildJSONObject(cameraDetail);
                DefaultHttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(URL);
                post.setHeader("Content-type", "application/json");
                post.setHeader("Accept", "application/json");
                post.setEntity(new StringEntity(cameraJSONObject.toString()));
                org.apache.http.HttpResponse response = client.execute(post);
                String result = EntityUtils.toString(response.getEntity());
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == CODE_UNAUTHORISED)
                {
                    throw new EvercamException(EvercamException.MSG_INVALID_AUTH);
                }
                else if (statusCode == CODE_ERROR)
                {
                    JsonNode jsonNode = new JsonNode(result);
                    String message = jsonNode.getObject().getString("message");
                    throw new EvercamException(message);
                }
                else if (statusCode == CODE_CREATE)
                {
                    JsonNode jsonNode = new JsonNode(result);
                    JSONObject jsonObject = jsonNode.getObject().getJSONArray("cameras").getJSONObject(0);
                    return new Camera(jsonObject);
                }
                else if (statusCode == CODE_SERVER_ERROR)
                {
                    throw new EvercamException(EvercamException.MSG_SERVER_ERROR);
                }
            } catch (JSONException e)
            {
                throw new EvercamException(e);
            } catch (ClientProtocolException e)
            {
                throw new EvercamException(e);
            } catch (IOException e)
            {
                throw new EvercamException(e);
            }
        }
        else
        {
            throw new EvercamException(EvercamException.MSG_USER_API_KEY_REQUIRED);
        }
        return camera;
    }

    public static boolean delete(String cameraId) throws EvercamException
    {
        if (API.hasUserKeyPair())
        {
            try
            {
                DefaultHttpClient client = new DefaultHttpClient();
                HttpDelete delete = new HttpDelete(URL + '/' + cameraId + '/' + "?api_key=" + API.getUserKeyPair()[0] + "&api_id=" + API.getUserKeyPair()[1]);
                delete.setHeader("Content-type", "application/json");
                delete.setHeader("Accept", "application/json");
                org.apache.http.HttpResponse response = client.execute(delete);
                String result = EntityUtils.toString(response.getEntity());
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == CODE_OK)
                {
                    return true;
                }
                else if (statusCode == CODE_UNAUTHORISED)
                {
                    throw new EvercamException(EvercamException.MSG_INVALID_USER_KEY);
                }
                else if (statusCode == CODE_NOT_FOUND)
                {
                    throw new EvercamException(result);
                }
                else if (statusCode == CODE_SERVER_ERROR)
                {
                    throw new EvercamException(EvercamException.MSG_SERVER_ERROR);
                }
                else
                {
                    return false;
                }
            }  catch (ClientProtocolException e)
            {
                throw new EvercamException(e);
            } catch (IOException e)
            {
                throw new EvercamException(e);
            }
        }
        else
        {
            throw new EvercamException(EvercamException.MSG_USER_API_KEY_REQUIRED);
        }
    }

    public static Camera patch(CameraDetail cameraDetail) throws EvercamException
    {
        Camera camera = null;
        if (API.hasUserKeyPair())
        {
            try
            {
                JSONObject cameraJSONObject = buildJSONObject(cameraDetail);
                DefaultHttpClient client = new DefaultHttpClient();
                HttpPatch patch = new HttpPatch(URL + '/' + cameraDetail.id);
                patch.setHeader("Content-type", "application/json");
                patch.setHeader("Accept", "application/json");
                patch.setEntity(new StringEntity(cameraJSONObject.toString()));
                org.apache.http.HttpResponse response = client.execute(patch);
                String result = EntityUtils.toString(response.getEntity());
                if (response.getStatusLine().getStatusCode() == CODE_UNAUTHORISED)
                {
                    throw new EvercamException(EvercamException.MSG_INVALID_AUTH);
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
                    throw new EvercamException(EvercamException.MSG_SERVER_ERROR);
                }
            } catch (JSONException e)
            {
                throw new EvercamException(e);
            } catch (ClientProtocolException e)
            {
                throw new EvercamException(e);
            } catch (IOException e)
            {
                throw new EvercamException(e);
            }
        }
        else
        {
            throw new EvercamException(EvercamException.MSG_USER_API_KEY_REQUIRED);
        }
        return camera;
    }

    public static Camera getById(String cameraId) throws EvercamException
    {
        Camera camera = null;
        try
        {
            HttpResponse<JsonNode> response;
            if (API.hasUserKeyPair())
            {
                response = Unirest.get(URL + '/' + cameraId + '/' + "?api_key=" + API.getUserKeyPair()[0] + "&api_id=" + API.getUserKeyPair()[1]).header("accept", "application/json").asJson();
            }
            else
            {
                response = Unirest.get(URL + '/' + cameraId).header("accept", "application/json").asJson();
            }
            if (response.getCode() == CODE_OK)
            {
                JSONObject userJSONObject = response.getBody().getObject().getJSONArray("cameras").getJSONObject(0);
                camera = new Camera(userJSONObject);
            }
            else if (response.getCode() == CODE_UNAUTHORISED)
            {
                throw new EvercamException(EvercamException.MSG_INVALID_USER_KEY);
            }
            else
            {
                throw new EvercamException(response.getBody().toString());
            }
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        } catch (UnirestException e)
        {
            throw new EvercamException(e);
        }
        return camera;
    }

    public String getExternalHost() throws EvercamException
    {
        try
        {
            return jsonObject.getString("external_host");
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }
    }

    public int getExternalHttpPort() throws EvercamException
    {
        try
        {
            return jsonObject.getInt("external_http_port");
        } catch (JSONException e)
        {
            return 0;
        }
    }

    public int getExternalRtspPort() throws EvercamException
    {
        try
        {
            return jsonObject.getInt("external_rtsp_port");
        } catch (JSONException e)
        {
            return 0;
        }
    }

    public String getInternalHost() throws EvercamException
    {
        try
        {
            return jsonObject.getString("internal_host");
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }
    }

    public int getInternalHttpPort() throws EvercamException
    {
        try
        {
            return jsonObject.getInt("internal_http_port");
        } catch (JSONException e)
        {
            return 0;
        }
    }

    public int getInternalRtspPort() throws EvercamException
    {
        try
        {
            return jsonObject.getInt("internal_rtsp_port");
        } catch (JSONException e)
        {
            return 0;
        }
    }

    public String getJpgUrl() throws EvercamException
    {
        try
        {
            return jsonObject.getString("jpg_url");
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }
    }

    public String getCameraUsername() throws EvercamException
    {
        try
        {
            return jsonObject.getString("cam_username");
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }
    }

    public String getCameraPassword() throws EvercamException
    {
        try
        {
            return jsonObject.getString("cam_password");
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

    public boolean isOnline() throws EvercamException
    {
        try
        {
            return jsonObject.getBoolean("is_online");
        } catch (JSONException e)
        {
            return false;
        }
    }

    public String getInternalJpgUrl() throws EvercamException
    {
        return getFullUrls().getInternalJpgUrl();
    }

    public String getExternalJpgUrl() throws EvercamException
    {
        return getFullUrls().getExternalJpgUrl();
    }

    public String getInternalRtspUrl() throws EvercamException
    {
        return getFullUrls().getInternalRtspUrl();
    }

    public String getExternalRtspUrl() throws EvercamException
    {
        return getFullUrls().getExternalRtspUrl();
    }

    public String getShortJpgUrl() throws EvercamException
    {
        return getFullUrls().getShortJpgUrl();
    }

    //FIXME: tests for this method
    public InputStream getSnapshotImage() throws EvercamException
    {
        InputStream inputStream;
        String endpoint = selectEndpoint();
        if (endpoint != null)
        {
            String url = getFullURL(endpoint, getJpgUrl());
            try
            {
                HttpResponse response = Unirest.get(url).basicAuth(getCameraUsername(), getCameraPassword()).asBinary();
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

    public ArrayList<String> getEndpoints()
    {
        ArrayList<String> endpointsArray = new ArrayList<String>();
        try
        {
            if (getInternalHost() != null && !getInternalHost().equals("null"))
            {
                String internalUrl = "http://" + getInternalHost() + ":" + getInternalHttpPort();
                endpointsArray.add(internalUrl);
            }
            if (getExternalHost() != null && !getExternalHost().equals("null"))
            {
                String externalUrl = "http://" + getExternalHost() + ":" + getExternalHttpPort();
                endpointsArray.add(externalUrl);
            }
        } catch (EvercamException e)
        {
            e.printStackTrace();
        }
        return endpointsArray;
    }

    public static Snapshot archiveSnapshot(String cameraId, String notes) throws EvercamException
    {
        Snapshot snapshot;
        if (API.hasUserKeyPair())
        {
            try
            {
                HttpResponse<JsonNode> response;
                if (notes == null)
                {
                    response = Unirest.post(URL + '/' + cameraId + "/" + "snapshots").fields(API.userKeyPairMap()).asJson();
                }
                else
                {
                    response = Unirest.post(URL + '/' + cameraId + "/" + "snapshots").fields(API.userKeyPairMap()).field("notes", notes).asJson();
                }
                if (response.getCode() == CODE_CREATE)
                {
                    JSONObject snapshotJsonObject = response.getBody().getObject().getJSONArray("snapshots").getJSONObject(0);
                    snapshot = new Snapshot(snapshotJsonObject);
                }
                else if (response.getCode() == CODE_NOT_FOUND)
                {
                    throw new EvercamException("camera does not exist");
                }
                else if (response.getCode() == CODE_ERROR)
                {
                    throw new EvercamException("camera is offline");
                }
                else if (response.getCode() == CODE_SERVER_ERROR)
                {
                    throw new EvercamException(EvercamException.MSG_SERVER_ERROR);
                }
                else
                {
                    throw new EvercamException(response.getCode() + response.getBody().toString());
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
        return snapshot;
    }

    public static ArrayList<Snapshot> getArchivedSnapshots(String cameraId) throws EvercamException
    {
        ArrayList<Snapshot> snapshots = new ArrayList<Snapshot>();
        if (API.hasUserKeyPair())
        {
            try
            {
                HttpResponse<JsonNode> response = Unirest.get(URL + "/" + cameraId + "/snapshots" + "?api_key=" + API.getUserKeyPair()[0] + "&api_id=" + API.getUserKeyPair()[1]).header("accept", "application/json").asJson();
                if (response.getCode() == CODE_OK)
                {
                    JSONArray snapshotJsonArray = response.getBody().getObject().getJSONArray("snapshots");
                    for (int count = 0; count < snapshotJsonArray.length(); count++)
                    {
                        JSONObject snapshotJsonObject = snapshotJsonArray.getJSONObject(count);
                        snapshots.add(new Snapshot(snapshotJsonObject));
                    }
                }
                else if (response.getCode() == CODE_UNAUTHORISED || response.getCode() == CODE_FORBIDDEN)
                {
                    throw new EvercamException(EvercamException.MSG_INVALID_USER_KEY);
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
        return snapshots;
    }

    public static Snapshot getLatestArchivedSnapshot(String cameraId, boolean withData) throws EvercamException
    {
        Snapshot snapshot;
        HttpResponse<JsonNode> response;
        if(API.hasUserKeyPair())
        {
        try
        {
            if(withData)
            {
                response = Unirest.get(URL + "/" + cameraId + "/snapshots/latest" + "?with_data=true&api_key=" + API.getUserKeyPair()[0] + "&api_id=" + API.getUserKeyPair()[1]).header("accept", "application/json").asJson();
            }
            else
            {
                response = Unirest.get(URL + "/" + cameraId + "/snapshots/latest" + "?api_key=" + API.getUserKeyPair()[0] + "&api_id=" + API.getUserKeyPair()[1]).header("accept", "application/json").asJson();
            }
            if(response.getCode() == CODE_OK)
            {
                JSONObject snapshotJsonObject = response.getBody().getObject().getJSONArray("snapshots").getJSONObject(0);
                snapshot = new Snapshot(snapshotJsonObject);
            }
            else if (response.getCode() == CODE_UNAUTHORISED || response.getCode() == CODE_FORBIDDEN)
            {
                throw new EvercamException(EvercamException.MSG_INVALID_USER_KEY);
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
        return snapshot;
    }

    private String selectEndpoint() throws EvercamException
    {
        String snapshot = getJpgUrl();

        for (String endpoint : getEndpoints())
        {
            String url = getFullURL(endpoint, snapshot);
            try
            {
                HttpResponse<String> response = Unirest.get(url).asString();
                if (response.getCode() != CODE_ERROR)
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

    private String getFullURL(String endpoint, String relativePath)
    {
        if (endpoint.endsWith("/") && relativePath.startsWith("/"))
        {
            endpoint = endpoint.substring(0, endpoint.lastIndexOf("/"));
        }
        else if (!(endpoint.endsWith("/") || relativePath.startsWith("/")))
        {
            endpoint += '/';
        }
        return endpoint + relativePath;
    }

    private static JSONObject buildJSONObject(CameraDetail cameraDetail) throws JSONException
    {
        JSONObject cameraJSONObject = new JSONObject();
        cameraJSONObject.put("api_key", API.getUserKeyPair()[0]);
        cameraJSONObject.put("api_id", API.getUserKeyPair()[1]);
        cameraJSONObject.put("id", cameraDetail.id);
        if (cameraDetail.internalHost != null)
        {
            cameraJSONObject.put("internal_host", cameraDetail.internalHost);
        }
        if (cameraDetail.internalHttpPort != 0)
        {
            cameraJSONObject.put("internal_http_port", cameraDetail.internalHttpPort);
        }
        if (cameraDetail.internalRtspPort != 0)
        {
            cameraJSONObject.put("internal_rtsp_port", cameraDetail.internalRtspPort);
        }
        if (cameraDetail.externalHost != null)
        {
            cameraJSONObject.put("external_host", cameraDetail.externalHost);
        }
        if (cameraDetail.externalHttpPort != 0)
        {
            cameraJSONObject.put("external_http_port", cameraDetail.externalHttpPort);
        }
        if (cameraDetail.externalRtspPort != 0)
        {
            cameraJSONObject.put("external_rtsp_port", cameraDetail.externalRtspPort);
        }
        if (cameraDetail.jpgUrl != null)
        {
            cameraJSONObject.put("jpg_url", cameraDetail.jpgUrl);
        }
        if (cameraDetail.isPublic != null)
        {
            cameraJSONObject.put("is_public", cameraDetail.isPublic);
        }
        if (cameraDetail.cameraUsername != null)
        {
            cameraJSONObject.put("cam_username", cameraDetail.cameraUsername);
        }
        if (cameraDetail.cameraPassword != null)
        {
            cameraJSONObject.put("cam_password", cameraDetail.cameraPassword);
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

    private FullUrls getFullUrls() throws EvercamException
    {
        FullUrls fullUrls;
        try
        {
            JSONObject fullUrlJsonObject = jsonObject.getJSONObject("extra_urls");
            fullUrls = new FullUrls(fullUrlJsonObject);
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }
        return  fullUrls;
    }
}
