package io.evercam;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * User: Liuting
 * Date: 04/12/13
 * Time: 08:49
 */
public class Firmware
{
    protected JSONObject firmwareJSONObject = null;
    protected Firmware(JSONObject firmwareJSONObject)
    {
       this.firmwareJSONObject = firmwareJSONObject;
    }

    public Auth getAuth(String type) throws EvercamException
    {
        Auth auth;
        try
        {
            JSONObject authJSONObject = firmwareJSONObject.getJSONObject("auth").getJSONObject(type);
            auth = new Auth(type,authJSONObject);
        }
        catch (JSONException e)
        {
            throw new EvercamException("Unknown Auth Type");
        }
        return auth;
    }

    public String getName() throws EvercamException
    {
        try
        {
            return firmwareJSONObject.getString("name");
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }
    }
}
