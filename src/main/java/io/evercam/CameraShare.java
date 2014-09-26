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

public class CameraShare extends EvercamObject
{
    static String URL = API.URL + "shares";

    CameraShare(JSONObject shareJSONObject)
    {
        this.jsonObject = shareJSONObject;
    }

    /**
     *
     * @param cameraId The unique identifier of the camera to share
     * @param user Email address or user name of the user to share the camera with.
     * @param rights A comma separate list of the rights to be granted with the share.
     * @return CameraShare object if the camera successfully shared with the user.
     * @throws EvercamException
     */
    public static CameraShare create(String cameraId, String user, String rights) throws EvercamException
    {
        CameraShare cameraShare = null;
        Map<String, Object> fieldsMap = new HashMap<String, Object>();
        fieldsMap.put("email", user);
        fieldsMap.put("rights", rights);

        if (API.hasUserKeyPair())
        {
            try
            {
                HttpResponse<JsonNode> response =  Unirest.post(URL + "/cameras/" + cameraId + "?api_key=" + API.getUserKeyPair()[0] + "&api_id=" + API.getUserKeyPair()[1])
                        .header("accept", "application/json").fields(fieldsMap).asJson();
                if (response.getCode() == CODE_CREATE)
                {
                    JSONObject jsonObject = response.getBody().getObject().getJSONArray("shares").getJSONObject(0);
                    cameraShare = new CameraShare(jsonObject);
                }
                else if (response.getCode() == CODE_UNAUTHORISED || response.getCode() == CODE_FORBIDDEN)
                {
                    throw new EvercamException(EvercamException.MSG_INVALID_USER_KEY);
                }
                else
                {
                    //The HTTP error code could be 400, 409 etc.
                    ErrorResponse errorResponse = new ErrorResponse(response.getBody().getObject());
                    throw new EvercamException(errorResponse.getMessage());
                }
            } catch (UnirestException e)
            {
                throw new EvercamException(e);
            }
        }
        else
        {
            throw new EvercamException(EvercamException.MSG_USER_API_KEY_REQUIRED);
        }
        return cameraShare;
    }

    /**
     * Get details for a share for a specific camera and user.
     * @param cameraId The unique identifier for the camera in the share.
     * @param userId The unique identifier for the user the camera is shared with.
     * @return CameraShare object if the share exists, otherwise return null.
     * @throws EvercamException
     */
    public static CameraShare get(String cameraId, String userId) throws EvercamException
    {
        ArrayList<CameraShare> shareList = getSharesByUrl(URL + "?camera_id=" + cameraId + "&user_id=" + userId);
        if(shareList.size() > 0)
        {
            return shareList.get(0);
        }
        return null;
    }

    /**
     * Delete an existing camera share by specify camera id and share id.
     * @param shareId  The unique identifier of the share to be deleted.
     * @param cameraId  The unique identifier for a camera.
     * @return true if camera share get successfully deleted
     * @throws EvercamException with error message if failed to delete camera share
     */
    public static boolean deleteByShareId(String shareId, String cameraId) throws EvercamException
    {
        if (API.hasUserKeyPair())
        {
            if(shareId == null)
            {
                throw new EvercamException("Share id can not be null");
            }
            if(cameraId == null)
            {
                throw new EvercamException("Camera id can not be null");
            }
            try
            {
                HttpResponse<JsonNode> response = Unirest.delete(URL + "/cameras/" + cameraId).field("share_id",shareId)
                        .field("api_key",API.getUserKeyPair()[0]).field("api_id",API.getUserKeyPair()[1]).asJson();
                int statusCode = response.getCode();
                if(statusCode == CODE_OK)
                {
                    return true;
                }
                else
                {
                    ErrorResponse errorResponse = new ErrorResponse(response.getBody().getObject());
                    throw new EvercamException(errorResponse.getMessage());
                }
            } catch (UnirestException e)
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
     * Delete an existing camera share by specify camera id and user id.
     * @param userId  The unique identifier for the user the camera is shared with.
     * @param cameraId  The unique identifier for a camera.
     * @return true if camera share get successfully deleted
     * @throws EvercamException with error message if failed to delete camera share
     */
    public static boolean delete(String cameraId, String userId) throws EvercamException
    {
        if (API.hasUserKeyPair())
        {
            CameraShare cameraShare = get(cameraId,userId);
            if(deleteByShareId(String.valueOf(cameraShare.getId()), cameraId))
            {
                return true;
            }
        }
        else
        {
            throw new EvercamException(EvercamException.MSG_USER_API_KEY_REQUIRED);
        }
        return false;
    }

    public static ArrayList<CameraShare> getByUser(String userId) throws EvercamException
    {
        return getSharesByUrl(URL + "/users/" + userId);
    }

    public static ArrayList<CameraShare> getByCamera(String cameraId) throws EvercamException
    {
        return getSharesByUrl(URL + "/cameras/" + cameraId);
    }

    /**
     * Return the camera list owned by the specific user as a string
     * for requesting data for a specified set of cameras
     *
     * @param userId Evercam user unique identifier
     * @return a comma separated list of camera identifiers owned by this user
     * @throws EvercamException
     */
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
                HttpResponse<JsonNode> response = Unirest.get(url).fields(API.userKeyPairMap()).header("accept", "application/json").asJson();
                if (response.getCode() == CODE_OK)
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
                    ErrorResponse errorResponse = new ErrorResponse(response.getBody().getObject());
                    throw new EvercamException(errorResponse.getMessage());
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

    /**
     * @return Unique identifier for a camera share.
     * @throws EvercamException
     */
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

    /**
     * @return The unique identifier of the user who shared the camera.
     * @throws EvercamException
     */
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

    //TODO: Unit Test
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

    public Right getRights() throws EvercamException
    {
        try
        {
            String rightsString = jsonObject.getString("rights");
            return new Right(rightsString);
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }
    }
}
