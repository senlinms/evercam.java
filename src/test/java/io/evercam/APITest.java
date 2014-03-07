package io.evercam;

import org.junit.AfterClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class APITest
{

    String TEST_USERNAME = "username";
    String TEST_PASSWORD = "password";
    String TEST_KEY = "testkey";
    String TEST_ID = "testid";


    @Test
    public void testAuth()
    {
        assertFalse(API.isAuth());
        API.setAuth(TEST_USERNAME, TEST_PASSWORD);
        String[] auth = API.getAuth();
        assertEquals(auth[0], TEST_USERNAME);
        assertEquals(auth[1], TEST_PASSWORD);
        assertTrue(API.isAuth());
    }

    @Test
    public void testAPIKeyAndId()
    {
        assertFalse(API.hasKeyAndID());
        assertEquals(null, API.getKey());
        assertEquals(null, API.getID());

        API.setKey(TEST_KEY);
        assertFalse(API.hasKeyAndID());

        API.setID(TEST_ID);
        assertTrue(API.hasKeyAndID());
        assertEquals(TEST_KEY, API.getKey());
        assertEquals(TEST_ID, API.getID());
    }

    @AfterClass
    public static void resetAPI()
    {
        API.setID(null);
        API.setKey(null);
        API.setAuth(null,null);
    }
}
