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


//    @Test
//    public void testAuth()
//    {
//        assertFalse(API.isAuth());
//        API.setAuth(TEST_USERNAME, TEST_PASSWORD);
//        String[] auth = API.getAuth();
//        assertEquals(auth[0], TEST_USERNAME);
//        assertEquals(auth[1], TEST_PASSWORD);
//        assertTrue(API.isAuth());
//    }

    @Test
    public void testDeveloperAPIKeyAndId()
    {
        assertFalse(API.hasDeveloperKeyPair());
        API.setDeveloperKeyPair(TEST_KEY, TEST_ID);
        assertTrue(API.hasDeveloperKeyPair());
        assertEquals(TEST_KEY, API.getDeveloperKeyPair()[0]);
        assertEquals(TEST_ID, API.getDeveloperKeyPair()[1]);
    }

    @Test
    public void testUserAPIKeyAndId()
    {
        assertFalse(API.hasUserKeyPair());
        API.setUserKeyPair(TEST_KEY, TEST_ID);
        assertTrue(API.hasUserKeyPair());
        assertEquals(TEST_KEY, API.getUserKeyPair()[0]);
        assertEquals(TEST_ID, API.getUserKeyPair()[1]);
    }

    @AfterClass
    public static void resetAPI()
    {
        API.setDeveloperKeyPair(null, null);
        API.setUserKeyPair(null, null);
    }
}
