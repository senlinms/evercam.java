package io.evercam;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        ArrayList<Model> modelList = getAll(modelId, null, null);
        if (modelList.size() > 0)
        {
            return modelList.get(0);
        }
        else
        {
            throw new EvercamException("Model with id " + modelId + " not exists");
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
        return getAll(null, modelName, null);
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

    public static ModelsWithPaging getByVendorIdWithPaging(String vendorId, int limit, int page) throws EvercamException
    {
        try
        {
            HttpResponse<JsonNode> response = Unirest.get(URL).field("vendor_id", vendorId).field("limit", limit).field("page", page).header("accept", "application/json").asJson();
            if(response.getCode() == CODE_OK)
            {
                return new ModelsWithPaging(response.getBody().getObject());
            }
            else
            {
                ErrorResponse errorResponse = new ErrorResponse(response.getBody().toString());
                throw new EvercamException(errorResponse.getMessage());
            }

        } catch (UnirestException e)
        {
            throw new EvercamException(e);
        }
    }

    /**
     * Returns model that match the model name with default paging
     *
     * @param modelName name of the model
     * @throws EvercamException if developer API key and id not specified or model does not exists
     */
    /**
     * Returns model that match the given vendor id, model name, or/and vendor id.
     *
     * @param modelId   unique identifier of the model
     * @param modelName name of the model
     * @param vendorId  the unique identifier of the vendor
     * @throws EvercamException
     */
    public static ArrayList<Model> getAll(String modelId, String modelName, String vendorId) throws EvercamException
    {
        Map<String, Object> map = new HashMap<String, Object>();
        if (modelId != null && !modelId.isEmpty())
        {
            map.put("id", modelId);
        }
        if (modelName != null && !modelName.isEmpty())
        {
            map.put("name", modelName);
        }
        if (vendorId != null && !vendorId.isEmpty())
        {
            map.put("vendor_id", vendorId);
        }

        ArrayList<Model> modelList = new ArrayList<Model>();
        try
        {
            HttpResponse<JsonNode> response = Unirest.get(URL).fields(map).header("accept", "application/json").asJson();
            ModelsWithPaging modelsWithPaging = new ModelsWithPaging(response.getBody().getObject());
            modelList.addAll(modelsWithPaging.getModelsList());

            int pages = modelsWithPaging.getTotalPages();
            if (pages > 0)
            {
                for (int index = 1; index <= pages; index++)
                {
                    HttpResponse<JsonNode> responseLoop = Unirest.get(URL + "?name=" + modelName).field("page", index).header("accept", "application/json").asJson();
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

    public static Model getDefaultModelByVendorId(String vendorId) throws EvercamException
    {
        return getById(vendorId + Model.DEFAULT_MODEL_SUFFIX);
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
