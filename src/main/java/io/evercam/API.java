package io.evercam;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
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

    private static String[] developerKeyPair = {null, null};
    private static String[] userKeyPair = {null, null};

    /**
     * Set developer app key pair.
     * Developer key pair will be send along with API requests that without user authentication.
     *
     * @param developerAppKey Evercam developer app key
     * @param developerAppID  Evercam developer app id
     */
    public static void setDeveloperKeyPair(String developerAppKey, String developerAppID)
    {
        developerKeyPair[0] = developerAppKey;
        developerKeyPair[1] = developerAppID;
    }

    /**
     * Set developer app key pair.
     * Developer key pair will be send along with API requests that require user authentication.
     *
     * @param userApiKey Evercam user API key
     * @param userApiID  Evercam user API id
     */
    public static void setUserKeyPair(String userApiKey, String userApiID)
    {
        userKeyPair[0] = userApiKey;
        userKeyPair[1] = userApiID;
    }

    /**
     * Return the developer app key pair as an array with two values.
     * The values will be null if developer key pair has not being set.
     */
    public static String[] getDeveloperKeyPair()
    {
        return developerKeyPair;
    }

    /**
     * Return the user key pair as an array with two values.
     * The values will be null if user key pair has not being set.
     */
    public static String[] getUserKeyPair()
    {
        return userKeyPair;
    }

    /**
     * Whether or not the developer app key pair has been added.
     *
     * @return true if the developer app key pair has been added, otherwise
     * return false.
     */
    public static boolean hasDeveloperKeyPair()
    {
        return (((developerKeyPair[0] != null) && (developerKeyPair[1] != null)) ? true : false);
    }

    /**
     * Whether or not the user key pair has been added.
     *
     * @return true if the user key pair has been added, otherwise return false.
     */
    public static boolean hasUserKeyPair()
    {
        return (((userKeyPair[0] != null) && (userKeyPair[1] != null)) ? true : false);
    }

    /**
     * Return the hash map of developer key and id.
     * Useful when add parameters using Unirest library
     *
     * @throws EvercamException if no developer app key pair added.
     */
    protected static Map<String, Object> developerKeyPairMap() throws EvercamException
    {
        if (hasDeveloperKeyPair())
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("api_key", getDeveloperKeyPair()[0]);
            map.put("api_id", getDeveloperKeyPair()[1]);
            return map;
        }
        else
        {
            throw new EvercamException(EvercamException.MSG_API_KEY_REQUIRED);
        }
    }

    /**
     * Return the hash map of user key and id.
     * Useful when add parameters using Unirest library
     *
     * @throws EvercamException if no user key pair added.
     */
    protected static Map<String, Object> userKeyPairMap() throws EvercamException
    {
        if (hasUserKeyPair())
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("api_key", getUserKeyPair()[0]);
            map.put("api_id", getUserKeyPair()[1]);
            return map;
        }
        else
        {
            throw new EvercamException(EvercamException.MSG_USER_API_KEY_REQUIRED);
        }
    }

    /**
     * Fetch API credentials for an authenticated user.
     *
     * @param username User name for the user to fetch credentials for
     * @param password Password for the user to fetch credentials for.
     * @return the user API credentials (key and id)
     * @throws EvercamException if developer key and id is not added
     */
    public static ApiKeyPair requestUserKeyPairFromEvercam(String username, String password) throws EvercamException
    {
        ApiKeyPair userKeyPair = null;
        if (hasDeveloperKeyPair())
        {
            try
            {
                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(URL + "/users/" + username + "/credentials?api_key=" + getDeveloperKeyPair()[0] + "&api_id=" + getDeveloperKeyPair()[1] + "&password=" + password);
                get.setHeader("Accept", "application/json");
                org.apache.http.HttpResponse response = client.execute(get);
                String result = EntityUtils.toString(response.getEntity());
                int statusCode = response.getStatusLine().getStatusCode();

                if (statusCode == EvercamObject.CODE_OK)
                {
                    JSONObject keyPairJsonObject = new JSONObject(result);
                    userKeyPair = new ApiKeyPair(keyPairJsonObject);
                }
                else
                {
                    throw new EvercamException(new JSONObject(result).getString("message"));
                }
            } catch (ClientProtocolException e)
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
