package io.evercam;

import org.json.JSONException;
import org.json.JSONObject;

public class Snapshot
{
    private JSONObject jsonObject;

    Snapshot(JSONObject snapshotJsonObject)
    {
         jsonObject = snapshotJsonObject;
    }

    public String getCameraId() throws EvercamException
    {
        try
        {
            return jsonObject.getString("camera");
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }
    }

    public String getNotes() throws EvercamException
    {
        try
        {
            return jsonObject.getString("notes");
        } catch (JSONException e)
        {
            throw new EvercamException(e);
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

    public String getTimeZone() throws  EvercamException
    {
        try
        {
            return jsonObject.getString("timezone");
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }
    }
}
