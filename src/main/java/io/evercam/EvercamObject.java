package io.evercam;


import org.json.JSONException;
import org.json.JSONObject;

public abstract class EvercamObject
{

    JSONObject jsonObject;

    @Override
    public String toString()
    {
        return String.format("<%s@%s id=%s> JSON: %s", this.getClass().getName(), System.identityHashCode(this), this.getIdString(), jsonObject.toString());
    }

    private Object getIdString()
    {
        try
        {
            return jsonObject.getString("id");
        } catch (SecurityException e)
        {
            return "";
        } catch (JSONException e)
        {
            return "";
        }
    }
}
