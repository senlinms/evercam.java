package io.evercam;

import org.junit.AfterClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class APITest {
    String DEVELOPER_TEST_KEY = "developertestkey";
    String DEVELOPER_TEST_ID = "developertestid";
    String USER_TEST_KEY = "usertestkey";
    String USER_TEST_ID = "usertestid";

    @Test
    public void testUserAPIKeyAndId() throws EvercamException {
        assertFalse(API.hasUserKeyPair());
        API.setUserKeyPair(USER_TEST_KEY, USER_TEST_ID);
        assertTrue(API.hasUserKeyPair());
        assertEquals(USER_TEST_KEY, API.getUserKeyPair()[0]);
        assertEquals(USER_TEST_ID, API.getUserKeyPair()[1]);

        assertEquals(USER_TEST_KEY, API.userKeyPairMap().get("api_key"));
        assertEquals(USER_TEST_ID, API.userKeyPairMap().get("api_id"));
    }

    @AfterClass
    public static void resetAPI() {
        API.setUserKeyPair(null, null);
    }
}
