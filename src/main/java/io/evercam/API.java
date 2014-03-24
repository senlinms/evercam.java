package io.evercam;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.http.utils.Base64Coder;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class API
{
    public static String VERSION = "v1";
    public static String URL = "https://api.evercam.io/" + VERSION + "/";

    private static String[] auth = {null, null};
    private static String[] developerKeyPair = {null, null};
    private static String[] userKeyPair = {null, null};

    public static void setAuth(String username, String password)
    {
        auth[0] = username;
        auth[1] = password;
    }

    public static void setKeyPair(String apiKey, String apiID)
    {
        developerKeyPair[0] = apiKey;
        developerKeyPair[1] = apiID;
    }

    public static void setUserKeyPair(String clientApiKey, String clientApiID)
    {
        userKeyPair[0] = clientApiKey;
        userKeyPair[1] = clientApiID;
    }

    public static String[] getAuth()
    {
        return auth;
    }

    public static String[] getDeveloperKeyPair()
    {
        return developerKeyPair;
    }

    public static String[] getUserKeyPair()
    {
        return developerKeyPair;
    }

    public static boolean isAuth()
    {
        if (!(auth[0] == null || auth[1] == null))
        {
            return true;
        }
        return false;
    }

    public static boolean hasDeveloperKeyPair()
    {
        return (((developerKeyPair[0] != null) && (developerKeyPair[1] != null)) ? true : false);
    }

    public static boolean hasUserKeyPair()
    {
        return (((userKeyPair[0] != null) && (userKeyPair[1] != null)) ? true : false);
    }

    protected static Map<String, Object> keyPairMap() throws EvercamException
    {
        if (hasDeveloperKeyPair())
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("app_key", getDeveloperKeyPair()[0]);
            map.put("app_id", getDeveloperKeyPair()[1]);
            return map;
        }
        else
        {
            throw new EvercamException(EvercamException.MSG_API_KEY_REQUIRED);
        }
    }

    protected static Map<String, Object> userKeyPairMap() throws EvercamException
    {
        if (hasDeveloperKeyPair())
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("app_key", getUserKeyPair()[0]);
            map.put("app_id", getUserKeyPair()[1]);
            return map;
        }
        else
        {
            throw new EvercamException(EvercamException.MSG_USER_API_KEY_REQUIRED);
        }
    }

    //FIXME: Mock test code for this method
    public static ApiKeyPair requestUserKeyPairFromEvercam(String username, String password) throws EvercamException
    {
        ApiKeyPair userKeyPair = null;
        if(hasDeveloperKeyPair())
        {
            try
            {
                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(URL + "/users/" +username + "/credentials?api_key=" +getDeveloperKeyPair()[0] + "&api_id=" + getDeveloperKeyPair()[1] + "&password="+ password);
                get.setHeader("Accept", "application/json");
                org.apache.http.HttpResponse response = client.execute(get);
                String result = EntityUtils.toString(response.getEntity());
                int statusCode = response.getStatusLine().getStatusCode();

                if(statusCode == EvercamObject.CODE_OK)
                {
                    JSONObject keyPairJsonObject = new JSONObject(result);
                    userKeyPair = new ApiKeyPair(keyPairJsonObject);
                }
                else if(statusCode == EvercamObject.CODE_UNAUTHORISED)
                {
                    throw new EvercamException("Invalid developer api key/id");
                }
                else if(statusCode == EvercamObject.CODE_FORBIDDEN)
                {
                    throw new EvercamException("Invalid password");
                }
                else if(statusCode == EvercamObject.CODE_NOT_FOUND)
                {
                    throw new EvercamException(new JSONObject(result).getString("message"));
                }
            }  catch (ClientProtocolException e)
            {
                throw new EvercamException(e);
            } catch (IOException e)
            {
                throw new EvercamException(e);
            } catch (JSONException e)
            {
                throw new EvercamException(e);
            }
        }
        else
        {
            throw new EvercamException(EvercamException.MSG_API_KEY_REQUIRED);
        }
        return userKeyPair;
    }
}
