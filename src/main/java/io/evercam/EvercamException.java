package io.evercam;

import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONException;

public class EvercamException extends Exception
{
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

}
