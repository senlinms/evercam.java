package io.evercam;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Snapshot
{
    private JSONObject jsonObject;
    private String timezone = "";

    Snapshot(JSONObject snapshotJsonObject, String timezone)
    {
        jsonObject = snapshotJsonObject;
        this.timezone = timezone;
    }

    Snapshot(JSONObject snapshotJsonObject)
    {
        jsonObject = snapshotJsonObject;
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
