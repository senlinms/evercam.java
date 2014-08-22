package io.evercam;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Model extends EvercamObject
{
    private static String URL = API.URL + "models/search";
    public static final String DEFAULT_MODEL_NAME = "Default";
    public static final String DEFAULT_MODEL_SUFFIX= "_default";

    Model(JSONObject modelJSONObject)
    {
        this.jsonObject = modelJSONObject;
    }

    public String getId()
    {
        try
        {
            return jsonObject.getString("id");
        } catch (JSONException e)
        {
            return "";
        }
    }

    public String getVendor() throws EvercamException
    {
        try
        {
            return jsonObject.getString("vendor_id");
        } catch (JSONException e)
        {
            return "";
        }
    }

    public String getName() throws EvercamException
    {
        try
        {
            return jsonObject.getString("name");
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }
    }

    public Defaults getDefaults() throws EvercamException
    {
        Defaults defaults;
        try
        {
            JSONObject defaultsJSONObject = jsonObject.getJSONObject("defaults");
            defaults = new Defaults(defaultsJSONObject);
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }
        return defaults;
    }

    protected static ArrayList<Vendor> getAll() throws EvercamException
    {
        return getModels(URL);
    }

    protected static ArrayList<Model> getByVendor(String vendorId) throws EvercamException
    {
        ArrayList<Model> modelList = new ArrayList<Model>();
        if (API.hasDeveloperKeyPair())
        {
            try
            {
                HttpResponse<JsonNode> response = Unirest.get(URL + "?vendor_id="+vendorId).fields(API.developerKeyPairMap()).header("accept", "application/json").asJson();
                if (response.getCode() == CODE_NOT_FOUND)
                {
                    throw new EvercamException("model vendor not found");
                }
                else if (response.getCode() == CODE_OK)
                {
                    JSONArray vendorsJSONArray = response.getBody().getObject().getJSONArray("models");
                    for (int vendorIndex = 0; vendorIndex < vendorsJSONArray.length(); vendorIndex++)
                    {
                        JSONObject vendorJSONObject = vendorsJSONArray.getJSONObject(vendorIndex);
                        modelList.add(new Model(vendorJSONObject));
                    }
                }
                else if (response.getCode() == CODE_UNAUTHORISED || response.getCode() == CODE_FORBIDDEN)
                {
                    throw new EvercamException(EvercamException.MSG_INVALID_USER_KEY);
                }
                else
                {
                    throw new EvercamException(response.getBody().toString());
                }
            } catch (JSONException e)
            {
                throw new EvercamException(e);
            } catch (UnirestException e)
            {
                throw new EvercamException(e);
            }
        }
        else
        {
            throw new EvercamException(EvercamException.MSG_API_KEY_REQUIRED);
        }
        return modelList;
    }

    private static ArrayList<Vendor> getModels(String url) throws EvercamException
    {
        ArrayList<Vendor> vendorList = new ArrayList<Vendor>();
        if (API.hasDeveloperKeyPair())
        {
            try
            {
                HttpResponse<JsonNode> response = Unirest.get(url).fields(API.developerKeyPairMap()).header("accept", "application/json").asJson();
                if (response.getCode() == CODE_OK)
                {
                    JSONArray vendorsJSONArray = response.getBody().getObject().getJSONArray("models");
                    for (int vendorIndex = 0; vendorIndex < vendorsJSONArray.length(); vendorIndex++)
                    {
                        JSONObject vendorJSONObject = vendorsJSONArray.getJSONObject(vendorIndex);
                        vendorList.add(new Vendor(vendorJSONObject));
                    }
                }
                else if (response.getCode() == CODE_SERVER_ERROR)
                {
                    throw new EvercamException(EvercamException.MSG_SERVER_ERROR);
                }
                else if (response.getCode() == CODE_UNAUTHORISED || response.getCode() == CODE_FORBIDDEN)
                {
                    throw new EvercamException(EvercamException.MSG_INVALID_USER_KEY);
                }
                else
                {
                    throw new EvercamException(response.getBody().toString());
                }
            } catch (JSONException e)
            {
                throw new EvercamException(e);
            } catch (UnirestException e)
            {
                throw new EvercamException(e);
            }
        }
        else
        {
            throw new EvercamException(EvercamException.MSG_API_KEY_REQUIRED);
        }
        return vendorList;
    }

    /**
     * Returns data for a particular camera model
     * @param modelId  name or unique identifier for the model
     * @throws EvercamException if missing developer API key and id
     */
    public static Model getModel(String modelId) throws EvercamException
    {
        Model model;
        if (API.hasDeveloperKeyPair())
        {
            try
            {
                HttpResponse<JsonNode> response = Unirest.get(URL + "?id=" + modelId).fields(API.developerKeyPairMap()).header("accept", "application/json").asJson();
                JSONObject modelJSONObject = response.getBody().getObject().getJSONArray("models").getJSONObject(0);
                model = new Model(modelJSONObject);
            } catch (JSONException e)
            {
                throw new EvercamException(e);
            } catch (UnirestException e)
            {
                throw new EvercamException(e);
            }
        }
        else
        {
            throw new EvercamException(EvercamException.MSG_API_KEY_REQUIRED);
        }
        return model;
    }
}
