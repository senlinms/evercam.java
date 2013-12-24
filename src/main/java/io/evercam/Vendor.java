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

    public static ArrayList<Vendor> getAll() throws EvercamException
    {
        return getVendors(URL_VENDORS);
    }

    public static ArrayList<Vendor> getByMac(String mac) throws EvercamException
    {
       return getVendors(URL_VENDORS + '/' + mac);
    }

    public Firmware getFirmware(String name) throws EvercamException
    {
        Firmware firmware = null;
        Boolean matched = false;
        try
        {
            JSONArray firmwareJSONArray = jsonObject.getJSONArray("firmwares");
            for(int arrayIndex = 0; arrayIndex <firmwareJSONArray.length(); arrayIndex++)
            {
                JSONObject firmwareJSONObject = firmwareJSONArray.getJSONObject(arrayIndex);

                if(firmwareJSONObject.getString("name").equals(name))
                {
                     matched = true;
                     firmware= new Firmware(firmwareJSONObject);
                     return firmware;
                }
            }
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }

        if(!matched)
        {
           throw new EvercamException("Unknown Firmware");
        }
        return firmware;
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
            for(int arrayIndex = 0; arrayIndex< knownMacJSONArray.length(); arrayIndex++)
            {
                knownMacs.add(arrayIndex, knownMacJSONArray.getString(arrayIndex));
            }

        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }
        return knownMacs;
    }

    public ArrayList<Firmware> getFirmwares()
    {
        ArrayList<Firmware> firmwareList = new ArrayList<Firmware>();
        try
        {
            JSONArray firmwareJSONArray = jsonObject.getJSONArray("firmwares");
            for(int arrayIndex = 0; arrayIndex <firmwareJSONArray.length(); arrayIndex++)
            {
                JSONObject firmwareJSONObject = firmwareJSONArray.getJSONObject(arrayIndex);
                firmwareList.add(new Firmware(firmwareJSONObject));

            }
        } catch (JSONException e)
        {
            return null;
        }
        return firmwareList;
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
        }
        catch (UnirestException e)
        {
            throw new EvercamException(e);
        }
        return vendorList;
    }
}
