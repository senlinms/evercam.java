package io.evercam;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Vendor extends EvercamObject
{
    private static String URL_VENDORS = API.URL + "vendors";

    private JSONObject jsonObject;

    Vendor(JSONObject vendorJSONObject)
    {
        this.jsonObject = vendorJSONObject;
    }

    public static Vendor getById(String vendorId) throws EvercamException
    {
        return Model.getByVendor(vendorId).get(0);
    }

    public static ArrayList<Vendor> getAll() throws EvercamException
    {
        return getVendors(URL_VENDORS);
    }

    public static ArrayList<Vendor> getByMac(String mac) throws EvercamException
    {
        return getVendors(URL_VENDORS + '/' + mac);
    }

    public String getId() throws EvercamException
    {
        try
        {
            return jsonObject.getString("id");
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }
    }

    public ArrayList<String> getModels() throws EvercamException
    {
        ArrayList<String> models = new ArrayList<String>();
        try
        {
            JSONArray modelsJSONArray = jsonObject.getJSONArray("models");
            for (int count = 0; count < modelsJSONArray.length(); count++)
            {
                models.add(count, modelsJSONArray.getString(count));
            }

        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }
        return models;
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

    public boolean isSupported() throws EvercamException
    {
        try
        {
            return jsonObject.getBoolean("is_supported");
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }
    }

    public ArrayList<String> getKnownMacs() throws EvercamException
    {
        ArrayList<String> knownMacs = new ArrayList<String>();
        try
        {
            JSONArray knownMacJSONArray = jsonObject.getJSONArray("known_macs");
            for (int arrayIndex = 0; arrayIndex < knownMacJSONArray.length(); arrayIndex++)
            {
                knownMacs.add(arrayIndex, knownMacJSONArray.getString(arrayIndex));
            }
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }
        return knownMacs;
    }

    public Model getModel(String modelName) throws EvercamException
    {
        return Model.getByModel(getId(), modelName);
    }

    public static ArrayList<Vendor> getSupportedVendors() throws EvercamException
    {
        return Model.getAll();
    }

    private static ArrayList<Vendor> getVendors(String url) throws EvercamException
    {
        ArrayList<Vendor> vendorList = new ArrayList<Vendor>();
        HttpRequest request = Unirest.get(url);
        try
        {
            HttpResponse<JsonNode> response = request.header("accept", "application/json").asJson();
            try
            {
                JSONArray vendorsJSONArray = response.getBody().getObject().getJSONArray("vendors");
                for (int vendorIndex = 0; vendorIndex < vendorsJSONArray.length(); vendorIndex++)
                {
                    JSONObject vendorJSONObject = vendorsJSONArray.getJSONObject(vendorIndex);
                    vendorList.add(new Vendor(vendorJSONObject));
                }
            } catch (JSONException e)
            {
                throw new EvercamException(e);
            }
        } catch (UnirestException e)
        {
            throw new EvercamException(e);
        }
        return vendorList;
    }
}
