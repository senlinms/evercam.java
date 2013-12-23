package io.evercam;

import org.json.JSONObject;

public class Model {
    private static String URL = API.URL + "models";
    private JSONObject modelJSONObject;

    Model(JSONObject modelJSONObject)
    {
        this.modelJSONObject = modelJSONObject;
    }
}
