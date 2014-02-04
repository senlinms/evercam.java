package io.evercam;

import org.json.JSONException;
import org.json.JSONObject;

public class Defaults extends EvercamObject
{
    Defaults(JSONObject defaultsJSONObject)
    {
        this.jsonObject = defaultsJSONObject;
    }

    public String getJpgURL() throws EvercamException
    {
        try
        {
            return jsonObject.getJSONObject("snapshots").getString("jpg");
        }
        catch (JSONException e)
        {
            throw new EvercamException(e);
        }
    }

    public Auth getAuth(String type) throws EvercamException
    {
        Auth auth;
        try
        {
            JSONObject authJSONObject = jsonObject.getJSONObject("auth").getJSONObject(type);
            auth = new Auth(type, authJSONObject);
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }
        return auth;
    }
}
