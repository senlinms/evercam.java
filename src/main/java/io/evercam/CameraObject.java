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

    protected String getHost()
    {
        try
        {
            return jsonObject.getString("host");
        } catch (JSONException e)
        {
            return "";
        }
    }

    protected EvercamHttp getHttp() throws EvercamException
    {
        JSONObject httpJsonObject = getJsonObjectByString("http");
        return new EvercamHttp(httpJsonObject);
    }

    protected EvercamRtsp getRtsp() throws EvercamException
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

    protected String getCameraUrl()
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

    protected String getMjpgUrl()
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

    protected String getMpegUrl()
    {
        try
        {
            return jsonObject.getString("mpeg");
        } catch (JSONException e)
        {
            return "";
        }
    }

    protected String getAudioUrl()
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
class InternalObject extends BaseCameraObject
{
    InternalObject(JSONObject jsonObject)
    {
        super(jsonObject);
    }
}

class ExternalObject extends BaseCameraObject
{
    ExternalObject(JSONObject jsonObject)
    {
        super(jsonObject);
    }
}
