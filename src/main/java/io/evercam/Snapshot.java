package io.evercam;

import com.mashape.unirest.http.utils.Base64Coder;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

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

    public String getTimeZone() throws EvercamException
    {
        try
        {
            return jsonObject.getString("timezone");
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }
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
        if(completeImageData != null)
        {
            return completeImageData.substring(completeImageData.indexOf(",")+1);
        }
        else
        {
            return null;
        }
    }

    //FIXME: Test for this method.
    public byte[] getData()
    {
        String base64Data = getBase64DataString();
        if(base64Data != null)
        {
            return org.apache.commons.codec.binary.Base64.decodeBase64(base64Data);
        }
        else
        {
            return null;
        }
    }
}
