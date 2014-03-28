package io.evercam;

import org.json.JSONException;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

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

//    @Test
//    public void testUserExists() throws EvercamException
//    {
//        UserDetail detail = new UserDetail();
//        detail.setFirstname("Joe");
//        detail.setLastname("Bloggs");
//        detail.setCountrycode("us");
//        detail.setEmail("joe.bloggs@example.org");
//        detail.setUsername("fail");
//        exception.expect(EvercamException.class);
//        User.create(detail);
//    }
//
//    @Test
//    public void testMissingUserDetail() throws EvercamException
//    {
//        UserDetail detail = new UserDetail();
//
//        exception.expect(EvercamException.class);
//        User.create(detail);
//
//        detail.setFirstname("Joe");
//        detail.setLastname("Bloggs");
//        detail.setCountrycode("us");
//        detail.setEmail("joe.bloggs@example.org");
//        detail.setUsername("fail");
//    }

//    @Test
//    public void testGetCameras() throws EvercamException
//    {
//        API.setAuth(null, null);
//        API.setDeveloperKeyPair("apikey", "apiid");
//        ArrayList<Camera> cameras = User.getCameras("joeyb");
//        assertEquals(1, cameras.size());
//        API.setDeveloperKeyPair(null, null);
//    }

//    @Test
//    public void testGetCamerasWithAuth() throws EvercamException
//    {
//        API.setAuth("joeyb", "12345");
//        API.setDeveloperKeyPair("apikey", "apiid");
//        ArrayList<Camera> cameras = User.getCameras("joeyb");
//        assertEquals(2, cameras.size());
//        API.setAuth(null, null);
//        API.setDeveloperKeyPair(null, null);
//    }
}
