package io.evercam;

import org.json.JSONException;
import org.json.JSONObject;

class FullUrls extends EvercamObject
{
    FullUrls(JSONObject jsonObject)
    {
        this.jsonObject = jsonObject;
    }


    public String getJpgUrl() throws EvercamException
    {
        return getUrlFromJsonObject("jpg_url");
    }

    public String getRtspUrl() throws EvercamException
    {
        return getUrlFromJsonObject("rtsp_url");
    }

    protected String getUrlFromJsonObject(String key) throws EvercamException
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
