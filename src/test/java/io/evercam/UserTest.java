package io.evercam;

import org.json.JSONException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class UserTest
{
    @BeforeClass
    public static void setUpClass()
    {
        API.URL = TestURL.URL;
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testCreateUser() throws EvercamException, JSONException
    {
        RandomUser randomUser = new RandomUser();

        assertEquals(randomUser.getUsername(), randomUser.getUser().getUsername());
        assertEquals(randomUser.getEmail(), randomUser.getUser().getEmail());
        ApiKeyPair apiKeyPair = API.requestUserKeyPairFromEvercam(randomUser.getUsername(), randomUser.getPassword());
        assertNotNull(apiKeyPair.getApiId());
        assertNotNull(apiKeyPair.getApiKey());
    }

    @Test
    public void testGetUserAndUserDetails() throws EvercamException
    {
        RandomUser randomUser = new RandomUser();
        ApiKeyPair apiKeyPair = API.requestUserKeyPairFromEvercam(randomUser.getUsername(), randomUser.getPassword());
        API.setUserKeyPair(apiKeyPair.getApiKey(), apiKeyPair.getApiId());
        User user = new User(randomUser.getUsername());

        assertEquals(RandomUser.FIRST_NAME, user.getFirstName());
        assertEquals(RandomUser.LAST_NAME, user.getLastName());
        assertEquals(randomUser.getEmail(), user.getEmail());
        assertEquals(randomUser.getUsername(), user.getId());
        assertEquals(randomUser.getUsername(), user.getUsername());
        assertEquals(RandomUser.COUNTRY_CODE, user.getCountry());
        API.setUserKeyPair(null, null);
    }

    @Test
    public void testGetUserAndUserDetailsByEmail() throws EvercamException
    {
        RandomUser randomUser = new RandomUser();
        ApiKeyPair apiKeyPair = API.requestUserKeyPairFromEvercam(randomUser.getEmail(), randomUser.getPassword());
        API.setUserKeyPair(apiKeyPair.getApiKey(), apiKeyPair.getApiId());
        User user = new User(randomUser.getEmail());

        assertEquals(RandomUser.FIRST_NAME, user.getFirstName());
        assertEquals(RandomUser.LAST_NAME, user.getLastName());
        assertEquals(randomUser.getEmail(), user.getEmail());
        assertEquals(randomUser.getUsername(), user.getId());
        assertEquals(randomUser.getUsername(), user.getUsername());
        assertEquals(RandomUser.COUNTRY_CODE, user.getCountry());
        API.setUserKeyPair(null, null);
    }


    @Test
    public void testMissingUserDetail() throws EvercamException
    {
        UserDetail detail = new UserDetail();
        exception.expect(EvercamException.class);
        User.create(detail);
    }

    @Test
    public void testGetCamerasWithCredentials() throws EvercamException
    {
        RandomUser randomUser = new RandomUser();
        randomUser.addRandomCamera(true);
        randomUser.addRandomCamera(false);

        ApiKeyPair apiKeyPair = API.requestUserKeyPairFromEvercam(randomUser.getUsername(), randomUser.getPassword());
        API.setUserKeyPair(apiKeyPair.getApiKey(), apiKeyPair.getApiId());

        ArrayList<Camera> allCameras = User.getCameras(randomUser.getUsername(), false, false);
        assertEquals(2, allCameras.size());

        //FIXME: Tests for get camera list including shares
        //        ArrayList<Camera> allCamerasIncludingShared = User.getCameras(randomUser.getUsername(),true);
        //        assertEquals(3, allCamerasIncludingShared.size());

        API.setUserKeyPair(null, null);
    }

    @Test
    public void testGetCamerasWithoutCredentials() throws EvercamException
    {
        RandomUser randomUser = new RandomUser();
        randomUser.addRandomCamera(true);
        randomUser.addRandomCamera(false);

        ArrayList<Camera> publicCameras = User.getCameras(randomUser.getUsername(), false, false);
        assertEquals(1, publicCameras.size());

        API.setUserKeyPair(null, null);
    }

    @AfterClass
    public static void destroyClass()
    {

    }
}
