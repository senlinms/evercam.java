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
    public static final String DEFAULT_MODEL_SUFFIX = "_default";

    Model(JSONObject modelJSONObject)
    {
        this.jsonObject = modelJSONObject;
    }

    /**
     * Returns model that match the unique identifier with default paging
     *
     * @param modelId unique identifier for the model
     * @throws EvercamException if developer API key and id not specified or model does not exists
     */
    public static Model getById(String modelId) throws EvercamException
    {
        if (API.hasDeveloperKeyPair())
        {
            try
            {
                HttpResponse<JsonNode> response = Unirest.get(URL + "?id=" + modelId).fields(API.developerKeyPairMap()).header("accept", "application/json").asJson();
                ModelsWithPaging modelsWithPaging = new ModelsWithPaging(response.getBody().getObject());
                ArrayList<Model> modelList = modelsWithPaging.getModelsList();
                if (modelList.size() > 0)
                {
                    return modelList.get(0);
                }
                else
                {
                    throw new EvercamException("Model with id " + modelId + " not exists");
                }
            } catch (UnirestException e)
            {
                throw new EvercamException(e);
            }
        }
        else
        {
            throw new EvercamException(EvercamException.MSG_API_KEY_REQUIRED);
        }
    }

    /**
     * Returns model that match the model name with default paging
     *
     * @param modelName name of the model
     * @throws EvercamException if developer API key and id not specified or model does not exists
     */
    public static ArrayList<Model> getAllByName(String modelName) throws EvercamException
    {
        ArrayList<Model> modelList = new ArrayList<Model>();
        if (API.hasDeveloperKeyPair())
        {
            try
            {
                HttpResponse<JsonNode> response = Unirest.get(URL + "?name=" + modelName).fields(API.developerKeyPairMap()).header("accept", "application/json").asJson();
                ModelsWithPaging modelsWithPaging = new ModelsWithPaging(response.getBody().getObject());
                modelList.addAll(modelsWithPaging.getModelsList());

                int pages = modelsWithPaging.getTotalPages();
                if (pages > 0)
                {
                    for (int index = 1; index <= pages; index++)
                    {
                        HttpResponse<JsonNode> responseLoop = Unirest.get(URL + "?name=" + modelName).field("page", index)
                                .fields(API.developerKeyPairMap()).header("accept", "application/json").asJson();
                        ModelsWithPaging modelsWithPagingLoop = new ModelsWithPaging(responseLoop.getBody().getObject());
                        modelList.addAll(modelsWithPagingLoop.getModelsList());
                    }
                }
                return modelList;
            } catch (UnirestException e)
            {
                throw new EvercamException(e);
            }
        }
        else
        {
            throw new EvercamException(EvercamException.MSG_API_KEY_REQUIRED);
        }
    }

    /**
     * Return the full list of models that associate with a specified vendor
     * Return an empty list if no model associated with the specified vendor
     *
     * @param vendorId the unique identifier of the vendor
     * @throws EvercamException if developer key and id not specified
     */
    public static ArrayList<Model> getAllByVendorId(String vendorId) throws EvercamException
    {
        ArrayList<Model> modelList = new ArrayList<Model>();
        if (API.hasDeveloperKeyPair())
        {
            ModelsWithPaging modelsWithPaging = getByVendorIdWithPaging(vendorId, 100, 0);
            modelList.addAll(modelsWithPaging.getModelsList());
            int pages = modelsWithPaging.getTotalPages();
            if (pages > 0)
            {
                for (int index = 1; index <= pages; index++)
                {
                    ModelsWithPaging modelsWithPagingLoop = getByVendorIdWithPaging(vendorId, 100, index);
                    modelList.addAll(modelsWithPagingLoop.getModelsList());
                }
            }
            return modelList;
        }
        else
        {
            throw new EvercamException(EvercamException.MSG_API_KEY_REQUIRED);
        }
    }

    public static ModelsWithPaging getByVendorIdWithPaging(String vendorId, int limit, int page) throws EvercamException
    {
        if (API.hasDeveloperKeyPair())
        {
            try
            {
                HttpResponse<JsonNode> response = Unirest.get(URL).field("vendor_id", vendorId).field("limit", limit).field("page", page).fields(API.developerKeyPairMap()).header("accept", "application/json").asJson();
                return new ModelsWithPaging(response.getBody().getObject());
            } catch (UnirestException e)
            {
                throw new EvercamException(e);
            }
        }
        else
        {
            throw new EvercamException(EvercamException.MSG_API_KEY_REQUIRED);
        }
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

    public String getVendorId() throws EvercamException
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
}
