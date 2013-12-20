package io.evercam;

import org.json.JSONObject;


public class Stream {
    private static String URL = API.URL + "streams";
    private JSONObject streamJSONObject;

    private Stream(JSONObject streamJSONObject)
    {
        this.streamJSONObject = streamJSONObject;
    }
}
