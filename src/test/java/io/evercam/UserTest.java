package io.evercam;

import org.json.JSONException;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

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
        API.setKeyPair("apikey", "apiid");
        UserDetail detail = new UserDetail();
        detail.setFirstname("Joe");
        detail.setLastname("Bloggs");
        detail.setCountrycode("us");
        detail.setEmail("joe.bloggs@example.org");
        detail.setUsername("joeyb");
        User user = User.create(detail);
        assertEquals("Joe", user.getForename());
        assertEquals("Bloggs", user.getLastname());
        assertEquals("joe.bloggs@example.org", user.getEmail());
        assertEquals("joeyb", user.getId());
        assertEquals("joeyb", user.getUsername());
        assertEquals("us", user.getCountry());
        API.setKeyPair(null, null);
    }

    @Test
    public void testGetUser() throws EvercamException
    {
        API.setAuth("joeyb", "12345");
        API.setKeyPair("apikey", "apiid");
        User user = new User("joeyb");
        assertEquals("Joe", user.getForename());
        assertEquals("Bloggs", user.getLastname());
        assertEquals("joe.bloggs@example.org", user.getEmail());
        assertEquals("joeyb", user.getId());
        assertEquals("joeyb", user.getUsername());
        assertEquals("us", user.getCountry());
        API.setAuth(null, null);
        API.setKeyPair(null, null);
    }

    @Test
    public void testUserExists() throws EvercamException
    {
        UserDetail detail = new UserDetail();
        detail.setFirstname("Joe");
        detail.setLastname("Bloggs");
        detail.setCountrycode("us");
        detail.setEmail("joe.bloggs@example.org");
        detail.setUsername("fail");
        exception.expect(EvercamException.class);
        User.create(detail);
    }

    @Test
    public void testMissingUserDetail() throws EvercamException
    {
        UserDetail detail = new UserDetail();

        exception.expect(EvercamException.class);
        User.create(detail);

        detail.setFirstname("Joe");
        detail.setLastname("Bloggs");
        detail.setCountrycode("us");
        detail.setEmail("joe.bloggs@example.org");
        detail.setUsername("fail");
    }

    @Test
    public void testGetCameras() throws EvercamException
    {
        API.setAuth(null, null);
        API.setKeyPair("apikey", "apiid");
        ArrayList<Camera> cameras = User.getCameras("joeyb");
        assertEquals(1, cameras.size());
        API.setKeyPair(null, null);
    }

    @Test
    public void testGetCamerasWithAuth() throws EvercamException
    {
        API.setAuth("joeyb", "12345");
        API.setKeyPair("apikey", "apiid");
        ArrayList<Camera> cameras = User.getCameras("joeyb");
        assertEquals(2, cameras.size());
        API.setAuth(null, null);
        API.setKeyPair(null, null);
    }
}
