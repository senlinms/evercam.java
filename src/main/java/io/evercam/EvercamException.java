package io.evercam;

import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONException;

class EvercamException extends Exception
{
    protected final static String MSG_API_KEY_REQUIRED = "API key and API ID required";

    EvercamException(String message)
    {
        super(message);
    }

    EvercamException(UnirestException unirestException)
    {
        super(unirestException);
    }

    EvercamException(JSONException jsonException)
    {
        super(jsonException);
    }

}
