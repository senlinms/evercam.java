package io.evercam;


import org.json.JSONException;
import org.json.JSONObject;

public abstract class EvercamObject
{
    static final int CODE_OK = 200;
    static final int CODE_CREATE = 201;
    static final int CODE_UNAUTHORISED = 401;
    static final int CODE_ERROR = 400;
    static final int CODE_NOT_FOUND= 404;
    static final int CODE_SERVER_ERROR = 500;
    JSONObject jsonObject;

    @Override
    public String toString()
    {
        return String.format("<%s@%s id=%s> JSON: %s", this.getClass().getName(), System.identityHashCode(this), this.getIdString(), jsonObject.toString());
    }

    private Object getIdString()
    {
        try
        {
            return jsonObject.getString("id");
        } catch (SecurityException e)
        {
            return "";
        } catch (JSONException e)
        {
            return "";
        }
    }
}
