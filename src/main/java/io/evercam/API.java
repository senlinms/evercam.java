package io.evercam;

public abstract class API
{
    public static String VERSION = "v1";
    public static String URL = "https://api.evercam.io/" + VERSION + "/";

    private static String[] auth = {null, null};
    private static String[] keyPair = {null, null};

    public static void setAuth(String username, String password)
    {
        auth[0] = username;
        auth[1] = password;
    }

    public static void setKeyPair(String apiKey, String apiID)
    {
        keyPair[0] = apiKey;
        keyPair[1] = apiID;
    }

    public static String[] getAuth()
    {
        return auth;
    }

    public static String[] getKeyPair()
    {
        return keyPair;
    }

    public static boolean isAuth()
    {
        if (!(auth[0] == null || auth[1] == null))
        {
            return true;
        }
        return false;
    }

    public static boolean hasKeyPair()
    {
        return (((keyPair[0] != null) && (keyPair[1] != null)) ? true : false);
    }
}
