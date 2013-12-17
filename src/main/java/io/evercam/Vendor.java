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

/**
 * Created with IntelliJ IDEA.
 * User: Liuting
 * Date: 03/12/13
 * Time: 14:24
 * To change this template use File | Settings | File Templates.
 */
public class Vendor
{
    private static String URL_VENDORS = API.URL + "vendors/";
    private final static int CODE_SUCCESS = 200;

    private JSONObject vendorJSONObject;

    private Vendor(JSONObject vendorJSONObject)
    {
        this.vendorJSONObject = vendorJSONObject;
    }

    public Vendor(String id) throws EvercamException
    {

        HttpRequest request = Unirest.get(URL_VENDORS + id);
        try
        {
            HttpResponse<JsonNode> response = request.header("accept", "application/jason").asJson();
            if (response.getCode() == CODE_SUCCESS)
            {
                try
                {
                    //here definitely only one object returned
                    vendorJSONObject = response.getBody().getObject().getJSONArray("vendors").getJSONObject(0);
                } catch (JSONException e)
                {
                    throw new EvercamException(e);
                }
            }
            else
            {
                throw new EvercamException("Unknown Vendor");
            }
        }
        catch (UnirestException e)
        {
            throw new EvercamException(e);
        }
    }

    public static ArrayList<Vendor> getAll() throws EvercamException
    {
        return getVendors(URL_VENDORS);
    }

    public static ArrayList<Vendor> getByMac(String mac) throws EvercamException
    {
       return getVendors(URL_VENDORS + mac);
    }

    public Firmware getFirmware(String name) throws EvercamException
    {
        Firmware firmware = null;
        Boolean matched = false;
        try
        {
            JSONArray firmwareJSONArray = vendorJSONObject.getJSONArray("firmwares");
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
            return vendorJSONObject.getString("id");
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }
    }

    public String getName() throws EvercamException
    {
        try
        {
            return vendorJSONObject.getString("name");
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
            JSONArray knownMacJSONArray = vendorJSONObject.getJSONArray("known_macs");
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
            JSONArray firmwareJSONArray = vendorJSONObject.getJSONArray("firmwares");
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
            HttpResponse<JsonNode> response = request.header("accept", "application/jason").asJson();
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
