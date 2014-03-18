package io.evercam;

import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONException;

import java.io.IOException;

public class EvercamException extends Exception
{
    protected final static String MSG_API_KEY_REQUIRED = "API key and API ID required";
    protected final static String MSG_INVALID_AUTH = "Invalid auth";
    protected final static String MSG_SERVER_ERROR = "Evercam internal server error.";

    public EvercamException(String message)
    {
        super(message);
    }

    public EvercamException(UnirestException unirestException)
    {
        super(unirestException);
    }

    public EvercamException(JSONException jsonException)
    {
        super(jsonException);
    }

    public EvercamException(IOException ioException)
    {
        super(ioException);
    }
}
