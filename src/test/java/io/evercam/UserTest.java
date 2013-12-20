package io.evercam;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class UserTest {
    @BeforeClass
    public static void setUpClass() {
        API.URL = "http://127.0.0.1:3000/v1/";
    }

    @Test
    public void testCreateUser() throws EvercamException
    {
        Map<String, Object> userMap = new HashMap<String, Object>();
        userMap.put("forename", "Joe");
        userMap.put("lastname", "Bloggs");
        userMap.put("email", "joe.bloggs@example.org");
        userMap.put("username", "joeyb");
        userMap.put("country", "us");
    }
}
