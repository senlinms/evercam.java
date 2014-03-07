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
        assertFalse(API.hasKeyPair());
        API.setKeyPair(TEST_KEY, TEST_ID);
        assertTrue(API.hasKeyPair());
        assertEquals(TEST_KEY, API.getKeyPair()[0]);
        assertEquals(TEST_ID, API.getKeyPair()[1]);
    }

    @AfterClass
    public static void resetAPI()
    {
        API.setKeyPair(null, null);
        API.setAuth(null, null);
    }
}
