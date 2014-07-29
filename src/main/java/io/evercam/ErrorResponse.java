package io.evercam;

import org.json.JSONException;
import org.json.JSONObject;

class ErrorResponse extends EvercamObject
{
    ErrorResponse(JSONObject jsonObject)
    {
        this.jsonObject = jsonObject;
    }

    protected String getMessage() throws EvercamException
    {
        try
        {
            return jsonObject.getString("message");
        }
        catch(JSONException e)
        {
            throw new EvercamException(jsonObject.toString() + e.toString());
        }
    }
}
