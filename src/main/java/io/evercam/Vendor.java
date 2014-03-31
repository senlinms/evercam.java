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
        ArrayList<Vendor> vendors = Model.getByVendor(vendorId);
        if(!vendors.isEmpty())
        {
        return Model.getByVendor(vendorId).get(0);
        }
        return null;
    }

    public static ArrayList<Vendor> getAll() throws EvercamException
    {
        if(API.hasDeveloperKeyPair())
        {
        return getVendors(URL_VENDORS + '/' + "?api_key=" + API.getDeveloperKeyPair()[0] + "&api_id=" + API.getDeveloperKeyPair()[1]);
        }
        else
        {
            throw new EvercamException(EvercamException.MSG_USER_API_KEY_REQUIRED);
        }
    }

    public static ArrayList<Vendor> getByMac(String mac) throws EvercamException
    {
        if(API.hasDeveloperKeyPair())
        {
        return getVendors(URL_VENDORS + '/' + mac + '/' + "?api_key=" + API.getDeveloperKeyPair()[0] + "&api_id=" + API.getDeveloperKeyPair()[1]);
        }
        else
        {
            throw new EvercamException(EvercamException.MSG_USER_API_KEY_REQUIRED);
        }
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

        if (API.hasDeveloperKeyPair())
        {
            try
            {
                HttpResponse<JsonNode> response = request.header("accept", "application/json").asJson();
                if(response.getCode() == CODE_OK)
                {
                    JSONArray vendorsJSONArray = response.getBody().getObject().getJSONArray("vendors");
                    for (int vendorIndex = 0; vendorIndex < vendorsJSONArray.length(); vendorIndex++)
                    {
                        JSONObject vendorJSONObject = vendorsJSONArray.getJSONObject(vendorIndex);
                        vendorList.add(new Vendor(vendorJSONObject));
                    }
                }
                else if(response.getCode() == CODE_FORBIDDEN || response.getCode() == CODE_UNAUTHORISED)
                {
                    throw new EvercamException(EvercamException.MSG_INVALID_DEVELOPER_KEY);
                }
                else if(response.getCode() == CODE_SERVER_ERROR)
                {
                    throw new EvercamException(EvercamException.MSG_SERVER_ERROR);
                }
                else
                {
                    throw new EvercamException(response.getBody().toString());
                }

            } catch (UnirestException e)
            {
                throw new EvercamException(e);
            } catch (JSONException e)
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
}
