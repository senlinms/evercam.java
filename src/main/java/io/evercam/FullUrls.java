package io.evercam;

import org.json.JSONException;
import org.json.JSONObject;

public class FullUrls extends EvercamObject
{
    FullUrls(JSONObject jsonObject)
    {
        this.jsonObject = jsonObject;
    }

    public String getInternalJpgUrl() throws EvercamException
    {
        return getUrlFromJsonObject("internal_jpg_url");
    }

    public String getExternalJpgUrl() throws EvercamException
    {
        return getUrlFromJsonObject("external_jpg_url");
    }

    public String getInternalRtspUrl() throws EvercamException
    {
        return getUrlFromJsonObject("internal_rtsp_url");
    }

    public String getExternalRtspUrl() throws EvercamException
    {
        return getUrlFromJsonObject("external_rtsp_url");
    }

    public String getShortJpgUrl() throws EvercamException
    {
        return getUrlFromJsonObject("short_jpg_url");
    }

    private String getUrlFromJsonObject(String key) throws EvercamException
    {
        String url;
        try
        {
            url = jsonObject.getString(key);
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }
        if(url != "null")
        {
            return url;
        }
        else
        {
            return "";
        }
    }
}
