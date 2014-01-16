package io.evercam;

import org.json.JSONException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;

public class UserTest {

    @BeforeClass
    public static void setUpClass() {
        API.URL = TestURL.URL;
    }

    @Test
    public void testCreateUser() throws EvercamException, JSONException {
        Map<String, Object> userMap = new HashMap<String, Object>();
        userMap.put("forename", "Joe");
        userMap.put("lastname", "Bloggs");
        userMap.put("email", "joe.bloggs@example.org");
        userMap.put("username", "joeyb");
        userMap.put("country", "us");
        User user = User.create(userMap);
        assertEquals("Joe", user.getForename());
        assertEquals("Bloggs", user.getLastname());
        assertEquals("joe.bloggs@example.org", user.getEmail());
        assertEquals("joeyb", user.getId());
        assertEquals("us", user.getCountry());
    }

    @Test
    public void testGetCameras() throws EvercamException {
        ArrayList<Camera> cameras = User.getCameras("joeyb");
        assertEquals(1, cameras.size());
    }
}
