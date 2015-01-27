package io.evercam;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Snapshot extends EvercamObject
{
    private String timezone = "";
    static String URL = API.URL + "cameras";

    Snapshot(JSONObject snapshotJsonObject, String timezone)
    {
        jsonObject = snapshotJsonObject;
        this.timezone = timezone;
    }

    Snapshot(JSONObject snapshotJsonObject)
    {
        jsonObject = snapshotJsonObject;
    }

    /**
     * Fetches a snapshot from the camera and stores it using the current timestamp
     *
     * @param cameraId the camera's unique identifier with Evercam
     * @param notes    optional text note for this snapshot, if set to null, no text notes will
     *                 be saved with this camera.
     * @return the saved snapshot
     * @throws EvercamException if unable to save the snapshot
     */
    public static Snapshot store(String cameraId, String notes) throws EvercamException
    {
        Snapshot snapshot = null;
        if (API.hasUserKeyPair())
        {
            try
            {
                HttpResponse<JsonNode> response;
                if (notes == null)
                {
                    response = Unirest.post(URL + '/' + cameraId + "/recordings/snapshots").fields(API.userKeyPairMap()).asJson();
                }
                else
                {
                    response = Unirest.post(URL + '/' + cameraId + "/recordings/snapshots").fields(API.userKeyPairMap()).field("notes", notes).asJson();
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


//    TODO: Implement GET /cameras/{id}/recordings/snapshots.json, currently the code are out-of-date
//    /**
//     * Returns the list of all snapshots currently stored for this camera
//     *
//     * @param cameraId the unique identifier of the camera
//     * @throws EvercamException if error occurred with Evercam
//     */
//    public static ArrayList<Snapshot> getArchivedSnapshots(String cameraId) throws EvercamException
//    {
//        ArrayList<Snapshot> snapshots = new ArrayList<Snapshot>();
//        if (API.hasUserKeyPair())
//        {
//            try
//            {
//                HttpResponse<JsonNode> response = Unirest.get(URL + "/" + cameraId + "/snapshots").fields(API.userKeyPairMap()).header("accept", "application/json").asJson();
//                if (response.getCode() == CODE_OK)
//                {
//                    JSONObject snapshotsObject = response.getBody().getObject();
//                    JSONArray snapshotJsonArray = snapshotsObject.getJSONArray("snapshots");
//                    String timezone = snapshotsObject.getString("timezone");
//
//                    for (int count = 0; count < snapshotJsonArray.length(); count++)
//                    {
//                        JSONObject snapshotJsonObject = snapshotJsonArray.getJSONObject(count);
//                        snapshots.add(new Snapshot(snapshotJsonObject, timezone));
//                    }
//                }
//                else if (response.getCode() == CODE_UNAUTHORISED || response.getCode() == CODE_FORBIDDEN)
//                {
//                    throw new EvercamException(EvercamException.MSG_INVALID_USER_KEY);
//                }
//                else if (response.getCode() == CODE_SERVER_ERROR)
//                {
//                    throw new EvercamException(EvercamException.MSG_SERVER_ERROR);
//                }
//                else
//                {
//                    throw new EvercamException(response.getBody().toString());
//                }
//            } catch (UnirestException e)
//            {
//                throw new EvercamException(e);
//            } catch (JSONException e)
//            {
//                throw new EvercamException(e);
//            }
//        }
//        else
//        {
//            throw new EvercamException(EvercamException.MSG_USER_API_KEY_REQUIRED);
//        }
//        return snapshots;
//    }

    //TODO: Write test for this method. Currently snapshot doesn't work with teste server
    /**
     * Returns latest snapshot stored for this camera.
     *
     * @param cameraId the camera's unique identifier with Evercam
     * @param withData whether it should send image data
     * @throws EvercamException
     */
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
                    response = Unirest.get(URL + "/" + cameraId + "/recordings/snapshots/latest" + "?with_data=true").fields(API.userKeyPairMap()).header("accept", "application/json").asJson();
                }
                else
                {
                    response = Unirest.get(URL + "/" + cameraId + "/recordings/snapshots/latest").fields(API.userKeyPairMap()).header("accept", "application/json").asJson();
                }
                if (response.getCode() == CODE_OK)
                {
                    JSONObject snapshotsObject = response.getBody().getObject();
                    JSONArray snapshotJsonArray = snapshotsObject.getJSONArray("snapshots");
                    String timezone = snapshotsObject.getString("timezone");
                    if (snapshotJsonArray.length() != 0)
                    {
                        JSONObject snapshotJsonObject = snapshotJsonArray.getJSONObject(0);
                        snapshot = new Snapshot(snapshotJsonObject, timezone);
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

    public String getNotes() throws EvercamException
    {
        try
        {
            return jsonObject.getString("notes");
        } catch (JSONException e)
        {
            return "";
        }
    }

    public int getTimeStamp() throws EvercamException
    {
        try
        {
            return jsonObject.getInt("created_at");
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }
    }

    public String getTimeZone()
    {
        return timezone;
    }

    public String getCompleteData()
    {
        try
        {
            return jsonObject.getString("data");
        } catch (JSONException e)
        {
            return null;
        }
    }

    public String getBase64DataString()
    {
        String completeImageData = getCompleteData();
        return getBase64DataStringFrom(completeImageData);
    }

    //FIXME: Test for this method.
    public byte[] getData()
    {
        String base64Data = getBase64DataString();
        return getDataFrom(base64Data);
    }

    /**
     * Return the pure base64 image data
     *
     * @param completeDataString the full data string with 'data:image/jpeg;base64,'
     */
    protected static String getBase64DataStringFrom(String completeDataString)
    {
        if (completeDataString != null)
        {
            return completeDataString.substring(completeDataString.indexOf(",") + 1);
        }
        return null;
    }

    /**
     * Return byte data from base64 data string
     *
     * @param base64DataString the base64 data in string format
     */
    protected static byte[] getDataFrom(String base64DataString)
    {
        if (base64DataString != null)
        {
            return org.apache.commons.codec.binary.Base64.decodeBase64(base64DataString);
        }
        return null;
    }
}
