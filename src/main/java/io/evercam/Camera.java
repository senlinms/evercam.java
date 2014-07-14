package io.evercam;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.io.IOUtils;
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

    /**
     * Create a new camera owned by the authenticating user
     *
     * @param cameraDetail Evercam camera detail object that produced by CameraBuilder
     * @return Evercam camera object.
     * @throws EvercamException If camera is not successfully created.
     * @see CameraDetail
     * @see CameraBuilder
     */
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
                    String message = jsonNode.getObject().toString();
                    throw new EvercamException(message);
                }
                else if (statusCode == CODE_CREATE)
                {
                    JsonNode jsonNode = new JsonNode(result);
                    JSONObject jsonObject = jsonNode.getObject().getJSONArray("cameras").getJSONObject(0);
                    camera = new Camera(jsonObject);
                }
                else if (statusCode == CODE_SERVER_ERROR)
                {
                    throw new EvercamException(EvercamException.MSG_SERVER_ERROR);
                }
                else if (statusCode == CODE_CONFLICT)
                {
                    JsonNode jsonNode = new JsonNode(result);
                    String message = jsonNode.getObject().getString("message");
                    throw new EvercamException(message);
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

    /**
     * Delete a camera from Evercam along with any stored media.
     *
     * @param cameraId the unique identifier of the camera
     * @return If the camera delete is successful, return true, otherwise return false.
     * @throws EvercamException If camera not exists or user unauthorized
     */
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
    }

    /**
     * Updates full or partial data for an existing camera
     *
     * @param cameraDetail Evercam camera detail object that produced by PatchCameraBuilder
     * @return Evercam camera object.
     * @throws EvercamException If user unauthorized or error occurred with Evercam
     * @see CameraDetail
     * @see PatchCameraBuilder
     */
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
                    camera = new Camera(jsonObject);
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

    /**
     * Retrieve a camera from Evercam by camera unique identifier
     *
     * @param cameraId the camera's unique identifier with Evercam
     * @return Evercam camera object with all data of this camera
     * @throws EvercamException If user unauthorized or error occurred with Evercam
     */
    public static Camera getById(String cameraId) throws EvercamException
    {
        ArrayList<Camera> cameraArrayList = getByUrl(URL + '/' + cameraId);
        return cameraArrayList.isEmpty() ? null : cameraArrayList.get(0);
    }

    /**
     * Returns data for a specified set of cameras.
     * The ultimate intention would be to expand this functionality to be a more general search.
     * The current implementation is as a basic absolute match list capability.
     *
     * @param idSetString comma separated list of camera identifiers for the cameras being queried.
     * @return the list of specified set of cameras
     * @throws EvercamException If user unauthorized or error occurred with Evercam
     */
    public static ArrayList<Camera> getByIdSet(String idSetString) throws EvercamException
    {
        return getByUrl(URL + "?ids=" + idSetString);
    }

    /**
     * Request camera live snapshot from Evercam.
     *
     * @param cameraId the camera's unique identifier
     * @return the stream of camera live image
     * @throws EvercamException
     */
    public static InputStream getSnapshotByCameraId(String cameraId) throws EvercamException
    {
        InputStream inputStream;
        if (API.hasUserKeyPair())
        {
            try
            {
                HttpResponse response = Unirest.get(URL + "/" + cameraId + "/snapshot.jpg").fields(API.userKeyPairMap()).asBinary();
                inputStream = response.getRawBody();
            } catch (UnirestException e)
            {
                throw new EvercamException(e);
            }
        }
        else
        {
            throw new EvercamException(EvercamException.MSG_USER_API_KEY_REQUIRED);
        }
        return inputStream;
    }

    public boolean hasCredentials() throws EvercamException
    {
        return jsonObject.toString().contains("\"cam_username\":") && jsonObject.toString().contains("\"cam_password\":");
    }

    /**
     * Return external host name of the camera, return an empty string if
     * the host name does not exist.
     *
     * @return external host name of the camera
     * @throws EvercamException
     */
    public String getExternalHost() throws EvercamException
    {
        ExternalObject externalObject = getExternalObject();
        if (externalObject != null)
        {
            return externalObject.getHost();
        }
        return "";
    }

    /**
     * Return internal host name of the camera, return an empty string if
     * the host name does not exist.
     *
     * @return internal host name of the camera
     * @throws EvercamException
     */
    public String getInternalHost() throws EvercamException
    {
        InternalObject internalObject = getInternalObject();
        if (internalObject != null)
        {
            return internalObject.getHost();
        }
        return "";
    }

    /**
     * Return external HTTP port number of the camera,
     * return 0 if no internal HTTP port associated with this camera.
     * @return external HTTP port of the camera
     * @throws EvercamException
     */
    public int getExternalHttpPort() throws EvercamException
    {
        ExternalObject externalObject = getExternalObject();
        if (externalObject != null)
        {
            return externalObject.getHttp().getPort();
        }
        return 0;
    }

    /**
     * Return internal HTTP port number of the camera,
     * return 0 if no internal HTTP port associated with this camera.
     * @return internal HTTP port of the camera
     * @throws EvercamException
     */
    public int getInternalHttpPort() throws EvercamException
    {
        InternalObject internalObject = getInternalObject();
        if (internalObject != null)
        {
            return internalObject.getHttp().getPort();
        }
        return 0;
    }

    /**
     * Return external RTSP port number of the camera,
     * return 0 if no external RTSP port associated with this camera.
     * @return external RTSP port of the camera
     * @throws EvercamException
     */
    public int getExternalRtspPort() throws EvercamException
    {
        ExternalObject externalObject = getExternalObject();
        if (externalObject != null)
        {
            return externalObject.getRtsp().getPort();
        }
        return 0;
    }

    /**
     * Return internal RTSP port number of the camera,
     * return 0 if no internal RTSP port associated with this camera.
     * @return internal RTSP port of the camera
     * @throws EvercamException
     */
    public int getInternalRtspPort() throws EvercamException
    {
        InternalObject internalObject = getInternalObject();
        if (internalObject != null)
        {
            return internalObject.getRtsp().getPort();
        }
        return 0;
    }

//    public String getJpgUrl() throws EvercamException
//    {
//        try
//        {
//            return jsonObject.getString("jpg_url");
//        } catch (JSONException e)
//        {
//            return "";
//        }
//    }
//
//    public String getRtspUrl() throws EvercamException
//    {
//        try
//        {
//            return jsonObject.getString("rtsp_url");
//        } catch (JSONException e)
//        {
//            return "";
//        }
//    }

    public String getCameraUsername() throws EvercamException
    {
        try
        {
            return jsonObject.getString("cam_username");
        } catch (JSONException e)
        {
            return "";
        }
    }

    public String getCameraPassword() throws EvercamException
    {
        try
        {
            return jsonObject.getString("cam_password");
        } catch (JSONException e)
        {
            return "";
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
            return "";
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

    public boolean isDiscoverable() throws EvercamException
    {
        try
        {
            return jsonObject.getBoolean("discoverable");
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

    public String getVendorId() throws EvercamException
    {
        try
        {
            return jsonObject.getString("vendor_id");
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }
    }

    public String getVendorName() throws EvercamException
    {
        try
        {
            return jsonObject.getString("vendor_name");
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
            return "";
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

    /**
     * Return internal full jpg URL of this camera that can be
     * used to request a camera live image. Return an empty string
     * if the URL does not exist.
     *
     * @return full internal snapshot URL (jpg) of this camera.
     * @throws EvercamException
     */
    public String getInternalJpgUrl() throws EvercamException
    {
        InternalObject internalObject = getInternalObject();
        if (internalObject != null)
        {
            return internalObject.getHttp().getJpgUrl();
        }
        return "";
    }

    /**
     * Return external full jpg URL of this camera that can be
     * used to request a camera live image. Return an empty string
     * if the URL does not exist.
     *
     * @return full external snapshot URL (jpg) of this camera.
     * @throws EvercamException
     */
    public String getExternalJpgUrl() throws EvercamException
    {
        ExternalObject externalObject = getExternalObject();
        if (externalObject != null)
        {
            return externalObject.getHttp().getJpgUrl();
        }
        return "";
    }

    /**
     * Return internal RTSP stream (H264) URL of this camera for
     * video playing. Return an empty string if the URL does not exist.
     *
     * @return full internal stream URL (h264) of this camera.
     * @throws EvercamException
     */
    public String getInternalH264Url() throws EvercamException
    {
        InternalObject internalObject = getInternalObject();
        if (internalObject != null)
        {
            return internalObject.getRtsp().getH264Url();
        }
        return "";
    }

    /**
     * Return internal RTSP stream (H264) URL of this camera for
     * video playing. Return an empty string if the URL does not exist.
     *
     * @return full internal stream URL (h264) of this camera.
     * @throws EvercamException
     */
    public String getExternalH264Url() throws EvercamException
    {
        ExternalObject externalObject = getExternalObject();
        if (externalObject != null)
        {
            return externalObject.getRtsp().getH264Url();
        }
        return "";
    }

    /**
     * Return the internal full stream URL (H264) with username and password
     * for the basic authentication of media player.
     *
     * @return the H264 URL with credential.
     * @throws EvercamException
     */
    public String getInternalH264UrlWithCredential() throws EvercamException
    {
        return replaceUrlWithCredential(getInternalH264Url(), RTSP_PREFIX);
    }

    /**
     * Return the external full stream URL (H264) with username and password
     * for the basic authentication of media player.
     *
     * @return the H264 URL with credential.
     * @throws EvercamException
     */
    public String getExternalH264UrlWithCredential() throws EvercamException
    {
        return replaceUrlWithCredential(getExternalH264Url(), RTSP_PREFIX);
    }

    /**
     * @param url    the full URL without credentials.
     * @param prefix the prefix of the URL.
     * @return the full URL with credentials as prefix://username:password@host
     * @throws EvercamException
     */
    private String replaceUrlWithCredential(String url, String prefix) throws EvercamException
    {
        if (!url.isEmpty() && url.startsWith(prefix))
        {
            return url.replace(prefix, prefix + getCameraUsername() + ":" + getCameraPassword() + "@");
        }
        else
        {
            return "";
        }
    }
    //
    //    public String getShortJpgUrl() throws EvercamException
    //    {
    //        return getShortUrls().getJpgUrl();
    //    }
    //
    //    public String getDynamicDnsJpgUrl() throws EvercamException
    //    {
    //        return getDynamicDnsUrls().getJpgUrl();
    //    }
    //
    //    public String getDynamicDnsRtspUrl() throws EvercamException
    //    {
    //        return getDynamicDnsUrls().getRtspUrl();
    //    }

    public static InputStream getStreamFromUrl(String url, String username, String password) throws EvercamException
    {
        InputStream inputStream;
        try
        {
            HttpResponse response = Unirest.get(url).basicAuth(username, password).asBinary();
            inputStream = response.getRawBody();
        } catch (UnirestException e)
        {
            throw new EvercamException(e);
        }
        return inputStream;
    }

    public InputStream getSnapshotFromEvercam() throws EvercamException
    {
        return getSnapshotByCameraId(getId());
    }

    private boolean isValidUrl(String url)
    {
        try
        {
            HttpResponse response = Unirest.get(url).asBinary();
            if (response.getCode() != CODE_ERROR)
            {
                return true;
            }
        } catch (UnirestException e)
        {
            //do nothing
        }
        return false;
    }

    public ArrayList<String> getEndpoints()
    {
        ArrayList<String> endpointsArray = new ArrayList<String>();
        try
        {
            if (getInternalHost() != null && !getInternalHost().equals("null"))
            {
                String internalUrl = HTTP_PREFIX + getInternalHost() + ":" + getInternalHttpPort();
                endpointsArray.add(internalUrl);
            }
            if (getExternalHost() != null && !getExternalHost().equals("null"))
            {
                String externalUrl = HTTP_PREFIX + getExternalHost() + ":" + getExternalHttpPort();
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
        Snapshot snapshot = null;
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
                HttpResponse<JsonNode> response = Unirest.get(URL + "/" + cameraId + "/snapshots").fields(API.userKeyPairMap()).header("accept", "application/json").asJson();
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
        if (API.hasUserKeyPair())
        {
            try
            {
                if (withData)
                {
                    response = Unirest.get(URL + "/" + cameraId + "/snapshots/latest" + "?with_data=true").fields(API.userKeyPairMap()).header("accept", "application/json").asJson();
                }
                else
                {
                    response = Unirest.get(URL + "/" + cameraId + "/snapshots/latest").fields(API.userKeyPairMap()).header("accept", "application/json").asJson();
                }
                if (response.getCode() == CODE_OK)
                {
                    JSONArray snapshotJsonArray = response.getBody().getObject().getJSONArray("snapshots");
                    if (snapshotJsonArray.length() != 0)
                    {
                        JSONObject snapshotJsonObject = snapshotJsonArray.getJSONObject(0);
                        snapshot = new Snapshot(snapshotJsonObject);
                    }
                    else
                    {
                        throw new EvercamException("No snapshot saved for camera:" + cameraId);
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
        return snapshot;
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
        if (cameraDetail.rtspUrl != null)
        {
            cameraJSONObject.put("rtsp_url", cameraDetail.rtspUrl);
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

        //For testing location data only:
        //   cameraJSONObject.put("location", getLocationJsonObject());

        return cameraJSONObject;
    }

    private static JSONObject getLocationJsonObject()
    {
        JSONObject locationJsonObject = new JSONObject();
        locationJsonObject.put("lng", -122.086966);
        locationJsonObject.put("lat", 37.377166);
        return locationJsonObject;
    }

    //    /**
    //     * Return the 'internal' information of this camera,
    //     * could return null if it's a shared camera with no internal details.
    //     */
    //    private InternalFullUrl getInternalFullUrls() throws EvercamException
    //    {
    //        try
    //        {
    //            JSONObject fullUrlJsonObject = getJsonObjectByString("internal");
    //            return new InternalFullUrl(fullUrlJsonObject);
    //        } catch (EvercamException e)
    //        {
    //            return null;
    //        }
    //    }

    /**
     * Return the 'internal' object of this camera.
     * Return null if it's a shared camera with no internal details.
     */
    private InternalObject getInternalObject() throws EvercamException
    {
        try
        {
            JSONObject internalJsonObject = getJsonObjectByString("internal");
            return new InternalObject(internalJsonObject);
        } catch (EvercamException e)
        {
            return null;
        }
    }

    /**
     * Return the 'external' object of this camera.
     * Return null if it's a shared camera with no external details.
     */
    private ExternalObject getExternalObject()
    {
        try
        {
            JSONObject externalJsonObject = getJsonObjectByString("external");
            return new ExternalObject(externalJsonObject);
        } catch (EvercamException e)
        {
            return null;
        }
    }

    private static ArrayList<Camera> getByUrl(String url) throws EvercamException
    {
        ArrayList<Camera> cameraList = new ArrayList<Camera>();
        try
        {
            HttpResponse<JsonNode> response;
            if (API.hasUserKeyPair())
            {
                response = Unirest.get(url).fields(API.userKeyPairMap()).header("accept", "application/json").asJson();
            }
            else
            {
                response = Unirest.get(url).header("accept", "application/json").asJson();
            }
            if (response.getCode() == CODE_OK)
            {
                JSONArray cameraJsonArray = response.getBody().getObject().getJSONArray("cameras");
                for (int count = 0; count < cameraJsonArray.length(); count++)
                {
                    cameraList.add(new Camera(cameraJsonArray.getJSONObject(count)));
                }
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
        return cameraList;
    }
}
