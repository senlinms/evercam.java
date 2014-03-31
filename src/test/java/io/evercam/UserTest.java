package io.evercam;

import org.json.JSONException;
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
        API.setDeveloperKeyPair(LocalConstants.DEVELOPER_KEY, LocalConstants.DEVELOPER_ID);
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

        assertEquals(RandomUser.FIRST_NAME, user.getForename());
        assertEquals(RandomUser.LAST_NAME, user.getLastname());
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
    public void testGetCameras() throws EvercamException
    {
        RandomUser randomUser = new RandomUser();
        randomUser.addRandomCamera(true);
        randomUser.addRandomCamera(false);

        ArrayList<Camera> privateCameras = User.getCameras(randomUser.getUsername());
        assertEquals(1, privateCameras.size());

        ApiKeyPair apiKeyPair = API.requestUserKeyPairFromEvercam(randomUser.getUsername(), randomUser.getPassword());
        API.setUserKeyPair(apiKeyPair.getApiKey(), apiKeyPair.getApiId());

        ArrayList<Camera> allCameras = User.getCameras(randomUser.getUsername());
        assertEquals(2, allCameras.size());

        API.setUserKeyPair(null, null);
    }
}
