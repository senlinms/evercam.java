package io.evercam;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Model extends EvercamObject {
    private static String URL = API.URL + "models";

    Model(JSONObject modelJSONObject)
    {
        this.jsonObject = modelJSONObject;
    }

    public String getVendor() throws EvercamException
    {
        try
        {
            return jsonObject.getString("vendor");
        } catch (JSONException e)
        {
            throw new EvercamException(e);
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

    public ArrayList<String> getKnownModels() throws EvercamException
    {
        ArrayList<String> models = new ArrayList<String>();
        try
        {
            JSONArray modelsJSONArray = jsonObject.getJSONArray("known_models");
            for(int i = 0; i< modelsJSONArray.length(); i++)
            {
                models.add(i, modelsJSONArray.getString(i));
            }

        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }
        return models;
    }

    public static ArrayList<Vendor> getAll() throws EvercamException
    {
        return getModels(URL);
    }

    public static ArrayList<Vendor> getByVendor(String vendorId) throws EvercamException
    {
        return getModels(URL + '/' + vendorId);
    }

    public static ArrayList<Vendor> getModels(String url) throws EvercamException
    {
        ArrayList<Vendor> vendorList = new ArrayList<Vendor>();
        try
        {
            HttpResponse<JsonNode> response = Unirest.get(url).header("accept", "application/json").asJson();
            JSONArray vendorsJSONArray = response.getBody().getObject().getJSONArray("vendors");
            for(int vendorIndex = 0; vendorIndex < vendorsJSONArray.length(); vendorIndex++)
            {
                JSONObject vendorJSONObject =  vendorsJSONArray.getJSONObject(vendorIndex);
                vendorList.add(new Vendor(vendorJSONObject));
            }
        }
        catch (JSONException e)
        {
            throw new EvercamException(e);
        }
        catch (UnirestException e)
        {
            throw new EvercamException(e);
        }
        return vendorList;
    }

    public static Model getByModel(String vendorId, String modelId) throws EvercamException
    {
        Model model;
        try
        {
            HttpResponse<JsonNode> response = Unirest.get(URL + '/' + vendorId + '/' + modelId).header("accept", "application/json").asJson();
            JSONObject modelJSONObject = response.getBody().getObject().getJSONArray("models").getJSONObject(0);
            model = new Model(modelJSONObject);
        }
        catch (JSONException e)
        {
            throw new EvercamException(e);
        }
        catch (UnirestException e)
        {
            throw new EvercamException(e);
        }
        return model;
    }
}
