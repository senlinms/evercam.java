package io.evercam;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The base object for the sub containers(internal,external etc)
 * in Evercam camera model.
 */
class BaseCameraObject extends EvercamObject
{
    BaseCameraObject(JSONObject jsonObject)
    {
        this.jsonObject = jsonObject;
    }


    /**
     * @return the 'host' URL in corresponding camera object.
     */
    public String getHost()
    {
        try
        {
            return jsonObject.getString("host");
        } catch (JSONException e)
        {
            return "";
        }
    }

    /**
     * @return the 'http' object in corresponding camera object.
     */
    public EvercamHttp getHttp() throws EvercamException
    {
        JSONObject httpJsonObject = getJsonObjectByString("http");
        return new EvercamHttp(httpJsonObject);
    }

    /**
     * @return the 'rtsp' object in corresponding camera object.
     */
    public EvercamRtsp getRtsp() throws EvercamException
    {
        JSONObject rtspJsonObject = getJsonObjectByString("rtsp");
        return new EvercamRtsp(rtspJsonObject);
    }
}

class EvercamHttp extends EvercamObject
{
    EvercamHttp(JSONObject jsonObject)
    {
        this.jsonObject = jsonObject;
    }

    public int getPort()
    {
        try
        {
            return jsonObject.getInt("port");
        } catch (JSONException e)
        {
            return 0;
        }
    }

    public String getCameraUrl()
    {
        try
        {
            return jsonObject.getString("camera");
        } catch (JSONException e)
        {
            return "";
        }
    }

    protected String getJpgUrl()
    {
        try
        {
            return jsonObject.getString("jpg");
        } catch (JSONException e)
        {
            return "";
        }
    }

    public String getMjpgUrl()
    {
        try
        {
            return jsonObject.getString("mjpg");
        } catch (JSONException e)
        {
            return "";
        }
    }
}

class EvercamRtsp extends EvercamObject
{
    EvercamRtsp(JSONObject jsonObject)
    {
        this.jsonObject = jsonObject;
    }

    protected int getPort()
    {
        try
        {
            return jsonObject.getInt("port");
        } catch (JSONException e)
        {
            return 0;
        }
    }

    public String getMpegUrl()
    {
        try
        {
            return jsonObject.getString("mpeg");
        } catch (JSONException e)
        {
            return "";
        }
    }

    public String getAudioUrl()
    {
        try
        {
            return jsonObject.getString("audio");
        } catch (JSONException e)
        {
            return "";
        }
    }

    protected String getH264Url()
    {
        try
        {
            return jsonObject.getString("h264");
        } catch (JSONException e)
        {
            return "";
        }
    }
}

class Internal extends BaseCameraObject
{
    Internal(JSONObject jsonObject)
    {
        super(jsonObject);
    }
}

class External extends BaseCameraObject
{
    External(JSONObject jsonObject)
    {
        super(jsonObject);
    }
}

class DynamicDns extends BaseCameraObject
{
    DynamicDns(JSONObject jsonObject)
    {
        super(jsonObject);
    }
}

class ProxyUrl extends EvercamObject
{
    ProxyUrl(JSONObject jsonObject)
    {
        this.jsonObject = jsonObject;
    }

    public String getJpg()
    {
        return jsonObject.getString("jpg");
    }
}
