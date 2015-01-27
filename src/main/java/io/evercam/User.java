package io.evercam;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User extends EvercamObject
{

    private static String URL = API.URL + "users";


    User(JSONObject userJSONObject)
    {
        this.jsonObject = userJSONObject;
    }

    /**
     * Return two letter ISO country code of the user.
     *
     * @throws EvercamException
     */
    public String getCountry() throws EvercamException
    {
        try
        {
            return jsonObject.getString("country");
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }
    }

    /**
     * Return unique Evercam username of the user.
     *
     * @throws EvercamException
     */
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

    /**
     * Return Email address of the user.
     *
     * @throws EvercamException
     */
    public String getEmail() throws EvercamException
    {
        try
        {
            return jsonObject.getString("email");
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }
    }

    /**
     * Return last name of the user.
     *
     * @throws EvercamException
     */
    public String getLastName() throws EvercamException
    {
        try
        {
            return jsonObject.getString("lastname");
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }
    }

    /**
     * Return first name of the user.
     *
     * @throws EvercamException
     */
    public String getFirstName() throws EvercamException
    {
        try
        {
            return jsonObject.getString("firstname");
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }
    }

    /**
     * Return unique Evercam username of the user.
     *
     * @throws EvercamException
     */
    public String getUsername() throws EvercamException
    {
        try
        {
            return jsonObject.getString("username");
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        }
    }

    /**
     * Returns available information for a user by specifying user unique identifier.
     *
     * @param id unique Evercam username or Email address of the user.
     * @throws EvercamException if no user API key pair added
     */
    public User(String id) throws EvercamException
    {
        if (API.hasUserKeyPair())
        {
            try
            {
                HttpResponse<JsonNode> response = Unirest.get(URL + "/" + id).fields(API.userKeyPairMap()).header("accept", "application/json").asJson();
                if (response.getCode() == CODE_OK)
                {
                    JSONObject userJSONObject = response.getBody().getObject().getJSONArray("users").getJSONObject(0);
                    this.jsonObject = userJSONObject;
                }
                else if (response.getCode() == CODE_FORBIDDEN || response.getCode() == CODE_UNAUTHORISED)
                {
                    throw new EvercamException(EvercamException.MSG_INVALID_USER_KEY);
                }
                else if (response.getCode() == CODE_NOT_FOUND)
                {
                    throw new EvercamException(response.getBody().getObject().getString("message"));
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
            throw new EvercamException(EvercamException.MSG_USER_API_KEY_REQUIRED);
        }
    }

    /**
     * Starts the new user sign up process with Evercam
     *
     * @param userDetail user detail object with all details for the new user
     * @throws EvercamException if no developer app API key pair added
     */
    public static User create(UserDetail userDetail) throws EvercamException
    {
        User user = null;
        Map<String, Object> userMap = new HashMap<String, Object>();
        userMap.put("firstname", userDetail.getFirstname());
        userMap.put("lastname", userDetail.getLastname());
        userMap.put("email", userDetail.getEmail());
        userMap.put("username", userDetail.getUsername());
        userMap.put("country", userDetail.getCountrycode());
        userMap.put("password", userDetail.getPassword());

        try
        {
            HttpResponse<JsonNode> response = Unirest.post(URL).header("accept", "application/json").fields(userMap).asJson();
            if (response.getCode() == CODE_CREATE)
            {
                JSONObject userJSONObject = response.getBody().getObject().getJSONArray("users").getJSONObject(0);
                user = new User(userJSONObject);
            }
            else if (response.getCode() == CODE_UNAUTHORISED || response.getCode() == CODE_FORBIDDEN)
            {
                throw new EvercamException(EvercamException.MSG_INVALID_USER_KEY);
            }
            else
            {
                //The HTTP error code could be 400, 409 etc.
                ErrorResponse errorResponse = new ErrorResponse(response.getBody().getObject());
                throw new EvercamException(errorResponse.getMessage());
            }
        } catch (JSONException e)
        {
            throw new EvercamException(e);
        } catch (UnirestException e)
        {
            throw new EvercamException(e);
        }

        return user;
    }
}
