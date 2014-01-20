package io.evercam;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * User: Liuting
 * Date: 05/12/13
 * Time: 14:56
 */
public class Auth
{
    private String type;
    private JSONObject authJSONObject = null;
    public static final String TYPE_BASIC = "basic";

    protected Auth(String type, JSONObject authJSONObject)
    {
         this.type = type;
         this.authJSONObject = authJSONObject;
    }


    public String getUsername() throws EvercamException
    {
        String username = null;
        try
        {
            username = authJSONObject.getString("username");
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }
        return username;
    }

    public String getType()
    {
        return type;
    }

    public String getPassword() throws EvercamException
    {
        String password = null;
        try
        {
            password = authJSONObject.getString("password");
        }
        catch (JSONException e)
        {
            throw new EvercamException(e);
        }
        return password;
    }

}
