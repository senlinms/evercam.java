package io.evercam;

import org.json.JSONException;
import org.json.JSONObject;

public class Firmware extends EvercamObject
{
    protected Firmware(JSONObject firmwareJSONObject)
    {
       this.jsonObject = firmwareJSONObject;
    }

    public Auth getAuth(String type) throws EvercamException
    {
        Auth auth;
        try
        {
            JSONObject authJSONObject = jsonObject.getJSONObject("auth").getJSONObject(type);
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
            return jsonObject.getString("name");
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }
    }
}
